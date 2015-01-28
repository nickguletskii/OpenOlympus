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
		IF (TG_OP = 'DELETE' OR TG_OP = 'UPDATE') THEN
			UPDATE solutions
			SET score=score-OLD.score
			WHERE id=OLD.solution_id;
		END IF;
	
		IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
			UPDATE solutions
			SET score=score+NEW.score
			WHERE id=NEW.solution_id;
        END IF;
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