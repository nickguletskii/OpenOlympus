
--
-- The MIT License
-- Copyright (c) 2014-2015 Nick Guletskii
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in
-- all copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
-- THE SOFTWARE.
--

CREATE OR REPLACE FUNCTION get_contest_end(in bigint) RETURNS timestamp
    AS $$
	SELECT (contests.start_time +
		(contests.duration * INTERVAL '1 MILLISECOND') +
		(
			SELECT
			(COALESCE(max(extensions_per_user.duration), 0) * INTERVAL '1 MILLISECOND')
			FROM (
				SELECT COALESCE (sum(time_extensions.duration), 0) as duration
				FROM time_extensions
				WHERE time_extensions.contest_id = contests.id
				GROUP BY time_extensions.user_id
			) AS extensions_per_user
		)
	)
	FROM contests
	WHERE contests.id = $1
    $$
    LANGUAGE SQL;
    
^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION get_contest_start(in bigint) RETURNS timestamp
    AS $$
	SELECT contests.start_time
	FROM contests
	WHERE contests.id = $1
    $$
    LANGUAGE SQL;
    
^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION get_solution_author(in bigint) RETURNS bigint
    AS $$
	SELECT user_id
	FROM solutions
	WHERE solutions.id = $1
    $$
    LANGUAGE SQL;
^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION get_solution_time_added(in bigint) RETURNS timestamp
    AS $$
	SELECT time_added
	FROM solutions
	WHERE solutions.id = $1
    $$
    LANGUAGE SQL;

^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION get_contest_end_for_user(in bigint, in bigint) RETURNS timestamp
    AS $$
	SELECT (contests.start_time +
		(contests.duration * INTERVAL '1 MILLISECOND') +
		(
			SELECT
			(COALESCE(max(extensions_per_user.duration), 0) * INTERVAL '1 MILLISECOND')
			FROM (
				SELECT COALESCE (sum(time_extensions.duration), 0) as duration
				FROM time_extensions
				WHERE time_extensions.contest_id = contests.id
				AND time_extensions.user_id = $2
				GROUP BY time_extensions.user_id
			) AS extensions_per_user
		)
	)
	FROM contests
	WHERE contests.id = $1
    $$
    LANGUAGE SQL;
    
^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION get_contest_start_for_user(in bigint, in bigint) RETURNS timestamp
    AS $$
	SELECT contests.start_time
	FROM contests
	WHERE contests.id = $1
    $$
    LANGUAGE SQL;

^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION contest_at(in timestamp) RETURNS table(f1 bigint)
    AS $$
	SELECT id
	FROM contests
	WHERE $1 BETWEEN contests.start_time AND (SELECT get_contest_end(contests.id))
    $$
    LANGUAGE SQL;

^^^ NEW STATEMENT ^^^
    
CREATE OR REPLACE FUNCTION contest_at_for_user(timestamp, bigint) RETURNS table(f1 bigint)
    AS $$
	SELECT id
	FROM contests
	WHERE $1 BETWEEN contests.start_time AND (SELECT get_contest_end_for_user(contests.id, $2))
    $$
    LANGUAGE SQL;

^^^ NEW STATEMENT ^^^

CREATE OR REPLACE FUNCTION maintain_solution_score() RETURNS TRIGGER
AS $maintain_solution_score$
	BEGIN
		UPDATE solutions
		SET score=(SELECT sum(verdicts.score) FROM verdicts WHERE verdicts.solution_id=solutions.id),
			tested=(SELECT every(verdicts.tested) FROM verdicts WHERE verdicts.solution_id=solutions.id)
		WHERE id=NEW.solution_id;
		UPDATE contest_participations
		SET score =
			(
				SELECT
				coalesce(sum(sols.score), 0)
				FROM(
					SELECT DISTINCT ON(solutions.task_id)
					score
					FROM solutions
					RIGHT OUTER JOIN
					contests_tasks
					ON contests_tasks.tasks_id = solutions.task_id AND contests_tasks.contests_id=contest_participations.contest_id
					WHERE					
						solutions.user_id=get_solution_author(NEW.solution_id)
					AND 
						(
							solutions.time_added BETWEEN
							(SELECT get_contest_start_for_user(contest_participations.contest_id,contest_participations.user_id))
							AND
							(SELECT get_contest_end_for_user(contest_participations.contest_id,contest_participations.user_id))
						)
					ORDER BY
						solutions.task_id asc,
						solutions.time_added desc
				) AS sols
			)
		WHERE 
			contest_participations.user_id=get_solution_author(NEW.solution_id)
			AND
			contest_participations.contest_id IN (SELECT contest_at_for_user(get_solution_time_added(NEW.solution_id), get_solution_author(NEW.solution_id)));
			
		RETURN NULL;
	END;
$maintain_solution_score$ LANGUAGE plpgsql;

^^^ NEW STATEMENT ^^^

DROP TRIGGER IF EXISTS update_solution_score ON verdicts;

^^^ NEW STATEMENT ^^^

CREATE TRIGGER update_solution_score
	AFTER INSERT OR UPDATE OR DELETE ON verdicts
	FOR EACH ROW
	EXECUTE PROCEDURE maintain_solution_score();
	
^^^ NEW STATEMENT ^^^

DO $$
BEGIN

IF NOT EXISTS (
		SELECT 1
	    FROM   pg_class c
	    JOIN   pg_namespace n ON n.oid = c.relnamespace
	    WHERE  c.relname = 'contests_tasks_contest_ind'
    ) THEN
	CREATE INDEX contests_tasks_contest_ind
		ON public.contests_tasks
		USING btree
		(contests_id);
END IF;

END$$;