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

CREATE OR REPLACE FUNCTION maintain_solution_score() RETURNS TRIGGER
AS $maintain_solution_score$
    BEGIN
	    UPDATE solutions
		SET score=(SELECT sum(verdicts.score) FROM verdicts WHERE verdicts.solution_id=solutions.id),
			tested=(SELECT every(verdicts.tested) FROM verdicts WHERE verdicts.solution_id=solutions.id)
		WHERE id=NEW.solution_id;
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