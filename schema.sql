-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.8.1-alpha
-- PostgreSQL version: 9.4
-- Project Site: pgmodeler.com.br
-- Model Author: ---

SET check_function_bodies = false;
-- ddl-end --


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: openolympus | type: DATABASE --
-- -- DROP DATABASE IF EXISTS openolympus;
-- CREATE DATABASE openolympus
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'en_US.UTF8'
-- 	LC_CTYPE = 'en_US.UTF8'
-- 	TABLESPACE = pg_default
-- 	OWNER = postgres
-- ;
-- -- ddl-end --
-- 

-- object: public.get_contest_end | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_contest_end(bigint) CASCADE;
CREATE FUNCTION public.get_contest_end ( _param1 bigint)
	RETURNS timestamp
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT (contests.start_time + (contests.duration * INTERVAL '1 MILLISECOND') + ( SELECT (COALESCE(max(extensions_per_user.duration), 0) * INTERVAL '1 MILLISECOND') FROM ( SELECT COALESCE (sum(time_extensions.duration), 0) as duration FROM time_extensions WHERE time_extensions.contest_id = contests.id GROUP BY time_extensions.user_id ) AS extensions_per_user ) ) FROM contests WHERE contests.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_contest_end(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.get_contest_end_for_user | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_contest_end_for_user(bigint,bigint) CASCADE;
CREATE FUNCTION public.get_contest_end_for_user ( _param1 bigint,  _param2 bigint)
	RETURNS timestamp
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT (contests.start_time + (contests.duration * INTERVAL '1 MILLISECOND') + ( SELECT (COALESCE(max(extensions_per_user.duration), 0) * INTERVAL '1 MILLISECOND') FROM ( SELECT COALESCE (sum(time_extensions.duration), 0) as duration FROM time_extensions WHERE time_extensions.contest_id = contests.id AND time_extensions.user_id = $2 GROUP BY time_extensions.user_id ) AS extensions_per_user ) ) FROM contests WHERE contests.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_contest_end_for_user(bigint,bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.get_contest_start | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_contest_start(bigint) CASCADE;
CREATE FUNCTION public.get_contest_start ( _param1 bigint)
	RETURNS timestamp
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT contests.start_time FROM contests WHERE contests.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_contest_start(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.get_contest_start_for_user | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_contest_start_for_user(bigint,bigint) CASCADE;
CREATE FUNCTION public.get_contest_start_for_user ( _param1 bigint,  _param2 bigint)
	RETURNS timestamp
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT contests.start_time FROM contests WHERE contests.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_contest_start_for_user(bigint,bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.get_solution_author | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_solution_author(bigint) CASCADE;
CREATE FUNCTION public.get_solution_author ( _param1 bigint)
	RETURNS bigint
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT user_id FROM solutions WHERE solutions.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_solution_author(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.get_solution_time_added | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.get_solution_time_added(bigint) CASCADE;
CREATE FUNCTION public.get_solution_time_added ( _param1 bigint)
	RETURNS timestamp
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 SELECT time_added FROM solutions WHERE solutions.id = $1 
$$;
-- ddl-end --
ALTER FUNCTION public.get_solution_time_added(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.maintain_contest_rank | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.maintain_contest_rank() CASCADE;
CREATE FUNCTION public.maintain_contest_rank ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 BEGIN IF (TG_OP = 'UPDATE') THEN PERFORM update_contest(NEW.id); END IF; RETURN NULL; END; 
$$;
-- ddl-end --
ALTER FUNCTION public.maintain_contest_rank() OWNER TO postgres;
-- ddl-end --

-- object: public.maintain_contest_rank_with_task | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.maintain_contest_rank_with_task() CASCADE;
CREATE FUNCTION public.maintain_contest_rank_with_task ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 BEGIN IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN PERFORM update_contest(NEW.contests_id); END IF; IF (TG_OP = 'DELETE') THEN PERFORM update_contest(OLD.contests_id); END IF; RETURN NULL; END; 
$$;
-- ddl-end --
ALTER FUNCTION public.maintain_contest_rank_with_task() OWNER TO postgres;
-- ddl-end --

-- object: public.maintain_contest_rank_with_time_extensions | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.maintain_contest_rank_with_time_extensions() CASCADE;
CREATE FUNCTION public.maintain_contest_rank_with_time_extensions ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 BEGIN IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN PERFORM update_user_in_contest(NEW.user_id, NEW.contest_id); END IF; IF (TG_OP = 'DELETE') THEN PERFORM update_user_in_contest(OLD.user_id, OLD.contest_id); END IF; RETURN NULL; END; 
$$;
-- ddl-end --
ALTER FUNCTION public.maintain_contest_rank_with_time_extensions() OWNER TO postgres;
-- ddl-end --

-- object: public.maintain_solution_score | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.maintain_solution_score() CASCADE;
CREATE FUNCTION public.maintain_solution_score ()
	RETURNS trigger
	LANGUAGE plpgsql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 BEGIN IF (TG_OP = 'DELETE') THEN PERFORM update_solution(OLD.solution_id); END IF; IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN PERFORM update_solution(NEW.solution_id); END IF; RETURN NULL; END; 
$$;
-- ddl-end --
ALTER FUNCTION public.maintain_solution_score() OWNER TO postgres;
-- ddl-end --

-- object: public.update_contest | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.update_contest(bigint) CASCADE;
CREATE FUNCTION public.update_contest ( _param1 bigint)
	RETURNS void
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 UPDATE contest_participations SET score = ( SELECT coalesce(sum(sols.score), 0) FROM( SELECT DISTINCT ON(solutions.task_id) score FROM solutions RIGHT OUTER JOIN contests_tasks ON contests_tasks.tasks_id = solutions.task_id AND contests_tasks.contests_id=contest_participations.contest_id WHERE solutions.user_id=contest_participations.user_id AND ( solutions.time_added BETWEEN (SELECT get_contest_start_for_user(contest_participations.contest_id,contest_participations.user_id)) AND (SELECT get_contest_end_for_user(contest_participations.contest_id,contest_participations.user_id)) ) ORDER BY solutions.task_id asc, solutions.time_added desc ) AS sols ) WHERE contest_participations.contest_id=$1 
$$;
-- ddl-end --
ALTER FUNCTION public.update_contest(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.update_solution | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.update_solution(bigint) CASCADE;
CREATE FUNCTION public.update_solution ( _param1 bigint)
	RETURNS void
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 UPDATE solutions SET score=(SELECT coalesce(sum(verdicts.score), 0) FROM verdicts WHERE verdicts.solution_id=solutions.id), maximum_score=(SELECT coalesce(sum(verdicts.maximum_score), 0) FROM verdicts WHERE verdicts.solution_id=solutions.id), tested=(SELECT coalesce(every(verdicts.tested), TRUE) FROM verdicts WHERE verdicts.solution_id=solutions.id) WHERE id=$1; UPDATE contest_participations SET score = ( SELECT coalesce(sum(sols.score), 0) FROM( SELECT DISTINCT ON(solutions.task_id) score FROM solutions RIGHT OUTER JOIN contests_tasks ON contests_tasks.tasks_id = solutions.task_id AND contests_tasks.contests_id=contest_participations.contest_id WHERE solutions.user_id=contest_participations.user_id AND ( solutions.time_added BETWEEN (SELECT get_contest_start_for_user(contest_participations.contest_id,contest_participations.user_id)) AND (SELECT get_contest_end_for_user(contest_participations.contest_id,contest_participations.user_id)) ) ORDER BY solutions.task_id asc, solutions.time_added desc ) AS sols ) WHERE contest_participations.user_id=get_solution_author($1) AND contest_participations.contest_id IN (SELECT contest_at_for_user(get_solution_time_added($1), get_solution_author($1))) 
$$;
-- ddl-end --
ALTER FUNCTION public.update_solution(bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.update_user_in_contest | type: FUNCTION --
-- DROP FUNCTION IF EXISTS public.update_user_in_contest(bigint,bigint) CASCADE;
CREATE FUNCTION public.update_user_in_contest ( _param1 bigint,  _param2 bigint)
	RETURNS void
	LANGUAGE sql
	VOLATILE 
	CALLED ON NULL INPUT
	SECURITY INVOKER
	COST 100
	AS $$
 UPDATE solutions SET score=(SELECT coalesce(sum(verdicts.score), 0) FROM verdicts WHERE verdicts.solution_id=solutions.id), maximum_score=(SELECT coalesce(sum(verdicts.maximum_score), 0) FROM verdicts WHERE verdicts.solution_id=solutions.id), tested=(SELECT coalesce(every(verdicts.tested), TRUE) FROM verdicts WHERE verdicts.solution_id=solutions.id) WHERE id=$1; UPDATE contest_participations SET score = ( SELECT coalesce(sum(sols.score), 0) FROM( SELECT DISTINCT ON(solutions.task_id) score FROM solutions RIGHT OUTER JOIN contests_tasks ON contests_tasks.tasks_id = solutions.task_id AND contests_tasks.contests_id=contest_participations.contest_id WHERE solutions.user_id=contest_participations.user_id AND ( solutions.time_added BETWEEN (SELECT get_contest_start_for_user(contest_participations.contest_id,contest_participations.user_id)) AND (SELECT get_contest_end_for_user(contest_participations.contest_id,contest_participations.user_id)) ) ORDER BY solutions.task_id asc, solutions.time_added desc ) AS sols ) WHERE contest_participations.user_id = $1 AND contest_participations.contest_id = $2 
$$;
-- ddl-end --
ALTER FUNCTION public.update_user_in_contest(bigint,bigint) OWNER TO postgres;
-- ddl-end --

-- object: public.acl_permission | type: TYPE --
-- DROP TYPE IF EXISTS public.acl_permission CASCADE;
CREATE TYPE public.acl_permission AS
 ENUM ('manage_acl','view','edit');
-- ddl-end --
ALTER TYPE public.acl_permission OWNER TO postgres;
-- ddl-end --

-- object: public.acl_item | type: TYPE --
-- DROP TYPE IF EXISTS public.acl_item CASCADE;
CREATE TYPE public.acl_item AS
(
  user_id bigint,
  permissions public.acl_permission[]
);
-- ddl-end --
ALTER TYPE public.acl_item OWNER TO postgres;
-- ddl-end --

-- object: public.acl_group | type: TYPE --
-- DROP TYPE IF EXISTS public.acl_group CASCADE;
CREATE TYPE public.acl_group AS
(
  owner bigint,
  authorities public.acl_item[]
);
-- ddl-end --
ALTER TYPE public.acl_group OWNER TO postgres;
-- ddl-end --

-- object: public.contest_message | type: TABLE --
-- DROP TABLE IF EXISTS public.contest_message CASCADE;
CREATE TABLE public.contest_message(
	id serial NOT NULL,
	question text,
	response text,
	user_id bigint NOT NULL,
	contest_id integer NOT NULL,
	CONSTRAINT contest_messages_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.contest_message OWNER TO postgres;
-- ddl-end --

-- object: public.contest_participation | type: TABLE --
-- DROP TABLE IF EXISTS public.contest_participation CASCADE;
CREATE TABLE public.contest_participation(
	id bigserial NOT NULL,
	score numeric(19,2),
	user_id bigint NOT NULL,
	contest_id integer NOT NULL,
	CONSTRAINT contest_participation_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.contest_participation OWNER TO postgres;
-- ddl-end --

-- object: public.contest_question | type: TABLE --
-- DROP TABLE IF EXISTS public.contest_question CASCADE;
CREATE TABLE public.contest_question(
	id serial NOT NULL,
	question text,
	response text,
	user_id bigint NOT NULL,
	contest_id integer NOT NULL,
	CONSTRAINT contest_question_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.contest_question OWNER TO postgres;
-- ddl-end --

-- object: public.persistent_logins | type: TABLE --
-- DROP TABLE IF EXISTS public.persistent_logins CASCADE;
CREATE TABLE public.persistent_logins(
	username character varying(64) NOT NULL,
	series character varying(64) NOT NULL,
	token character varying(64) NOT NULL,
	last_used timestamp NOT NULL,
	CONSTRAINT persistent_logins_pkey PRIMARY KEY (series)

);
-- ddl-end --
ALTER TABLE public.persistent_logins OWNER TO postgres;
-- ddl-end --

-- object: public.property | type: TABLE --
-- DROP TABLE IF EXISTS public.property CASCADE;
CREATE TABLE public.property(
	id bigint NOT NULL,
	property_key character varying(255),
	property_value bytea,
	CONSTRAINT property_pkey PRIMARY KEY (id),
	CONSTRAINT uk_4b6vatgj30955xsjr51yegxi9 UNIQUE (property_value),
	CONSTRAINT uk_8jytv8tu3pui7ram00b44tn4u UNIQUE (property_key)

);
-- ddl-end --
ALTER TABLE public.property OWNER TO postgres;
-- ddl-end --

-- object: public.solution | type: TABLE --
-- DROP TABLE IF EXISTS public.solution CASCADE;
CREATE TABLE public.solution(
	id bigserial NOT NULL,
	file character varying(255),
	maximum_score numeric(19,2),
	score numeric(19,2),
	tested boolean NOT NULL,
	time_added timestamp,
	user_id bigint,
	task_id integer NOT NULL,
	CONSTRAINT solution_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.solution OWNER TO postgres;
-- ddl-end --

-- object: public.time_extension | type: TABLE --
-- DROP TABLE IF EXISTS public.time_extension CASCADE;
CREATE TABLE public.time_extension(
	id bigserial NOT NULL,
	duration bigint,
	reason text,
	user_id bigint NOT NULL,
	contest_id integer NOT NULL,
	CONSTRAINT time_extension_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.time_extension OWNER TO postgres;
-- ddl-end --

-- object: public.contest | type: TABLE --
-- DROP TABLE IF EXISTS public.contest CASCADE;
CREATE TABLE public.contest(
	id serial NOT NULL,
	duration bigint,
	name character varying(255),
	show_full_tests_during_contest boolean NOT NULL,
	start_time timestamp,
	acl public.acl_group NOT NULL,
	CONSTRAINT contest_pkey PRIMARY KEY (id),
	CONSTRAINT contest_name_unique UNIQUE (name)

);
-- ddl-end --
ALTER TABLE public.contest OWNER TO postgres;
-- ddl-end --

-- object: public.task | type: TABLE --
-- DROP TABLE IF EXISTS public.task CASCADE;
CREATE TABLE public.task(
	id serial NOT NULL,
	description_file text NOT NULL,
	name character varying(255),
	task_location text NOT NULL,
	created_date timestamp,
	CONSTRAINT task_pkey PRIMARY KEY (id),
	CONSTRAINT task_name_unique UNIQUE (name)

);
-- ddl-end --
ALTER TABLE public.task OWNER TO postgres;
-- ddl-end --

-- object: public.principal_sequence | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.principal_sequence CASCADE;
CREATE SEQUENCE public.principal_sequence
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.principal_sequence OWNER TO postgres;
-- ddl-end --

-- object: public."USER" | type: TABLE --
-- DROP TABLE IF EXISTS public."USER" CASCADE;
CREATE TABLE public."USER"(
	id bigint NOT NULL DEFAULT nextval('public.principal_sequence'::regclass),
	address_city character varying(255),
	address_country character varying(255),
	address_line1 text,
	address_line2 text,
	address_state character varying(255),
	approval_email_sent boolean NOT NULL,
	birth_date timestamp,
	email_address character varying(255),
	email_confirmation_token character varying(255),
	enabled boolean NOT NULL,
	first_name_localised character varying(255),
	first_name_main character varying(255),
	landline character varying(255),
	last_name_localised character varying(255),
	last_name_main character varying(255),
	middle_name_localised character varying(255),
	middle_name_main character varying(255),
	mobile character varying(255),
	password character varying(255),
	school character varying(255),
	teacher_first_name character varying(255),
	teacher_last_name character varying(255),
	teacher_middle_name character varying(255),
	username character varying(255),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)

);
-- ddl-end --
ALTER TABLE public."USER" OWNER TO postgres;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.contest_participation DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.contest_participation ADD CONSTRAINT "USER_fk" FOREIGN KEY (user_id)
REFERENCES public."USER" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.time_extension DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.time_extension ADD CONSTRAINT "USER_fk" FOREIGN KEY (user_id)
REFERENCES public."USER" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.contest_message DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.contest_message ADD CONSTRAINT "USER_fk" FOREIGN KEY (user_id)
REFERENCES public."USER" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.solution DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.solution ADD CONSTRAINT "USER_fk" FOREIGN KEY (user_id)
REFERENCES public."USER" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: solution_by_user | type: INDEX --
-- DROP INDEX IF EXISTS public.solution_by_user CASCADE;
CREATE INDEX solution_by_user ON public.solution
	USING btree
	(
	  user_id ASC NULLS LAST
	);
-- ddl-end --

-- object: task_by_date_desc | type: INDEX --
-- DROP INDEX IF EXISTS public.task_by_date_desc CASCADE;
CREATE INDEX task_by_date_desc ON public.task
	USING btree
	(
	  created_date ASC NULLS LAST
	);
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.contest_question DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.contest_question ADD CONSTRAINT "USER_fk" FOREIGN KEY (user_id)
REFERENCES public."USER" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.verdict_status_type | type: TYPE --
-- DROP TYPE IF EXISTS public.verdict_status_type CASCADE;
CREATE TYPE public.verdict_status_type AS
 ENUM ('waiting','ok','wrong_answer','runtime_error','cpu_time_limit_exceeded','real_time_limit_exceeded','memory_limit_exceeded','disk_limit_exceeded','security_violated','internal_error');
-- ddl-end --
ALTER TYPE public.verdict_status_type OWNER TO postgres;
-- ddl-end --

-- object: public.verdict | type: TABLE --
-- DROP TABLE IF EXISTS public.verdict CASCADE;
CREATE TABLE public.verdict(
	id bigserial NOT NULL,
	score numeric(19,2),
	maximum_score numeric(19,2) NOT NULL,
	status public.verdict_status_type NOT NULL,
	solution_id bigint
);
-- ddl-end --
ALTER TABLE public.verdict OWNER TO postgres;
-- ddl-end --

-- object: solution_fk | type: CONSTRAINT --
-- ALTER TABLE public.verdict DROP CONSTRAINT IF EXISTS solution_fk CASCADE;
ALTER TABLE public.verdict ADD CONSTRAINT solution_fk FOREIGN KEY (solution_id)
REFERENCES public.solution (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.contest_tasks | type: TABLE --
-- DROP TABLE IF EXISTS public.contest_tasks CASCADE;
CREATE TABLE public.contest_tasks(
	id_contest integer,
	id_task integer,
	CONSTRAINT contest_tasks_pk PRIMARY KEY (id_contest,id_task)

);
-- ddl-end --

-- object: contest_fk | type: CONSTRAINT --
-- ALTER TABLE public.contest_tasks DROP CONSTRAINT IF EXISTS contest_fk CASCADE;
ALTER TABLE public.contest_tasks ADD CONSTRAINT contest_fk FOREIGN KEY (id_contest)
REFERENCES public.contest (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: task_fk | type: CONSTRAINT --
-- ALTER TABLE public.contest_tasks DROP CONSTRAINT IF EXISTS task_fk CASCADE;
ALTER TABLE public.contest_tasks ADD CONSTRAINT task_fk FOREIGN KEY (id_task)
REFERENCES public.task (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: contest_fk | type: CONSTRAINT --
-- ALTER TABLE public.contest_message DROP CONSTRAINT IF EXISTS contest_fk CASCADE;
ALTER TABLE public.contest_message ADD CONSTRAINT contest_fk FOREIGN KEY (contest_id)
REFERENCES public.contest (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: contest_fk | type: CONSTRAINT --
-- ALTER TABLE public.contest_question DROP CONSTRAINT IF EXISTS contest_fk CASCADE;
ALTER TABLE public.contest_question ADD CONSTRAINT contest_fk FOREIGN KEY (contest_id)
REFERENCES public.contest (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: contest_fk | type: CONSTRAINT --
-- ALTER TABLE public.contest_participation DROP CONSTRAINT IF EXISTS contest_fk CASCADE;
ALTER TABLE public.contest_participation ADD CONSTRAINT contest_fk FOREIGN KEY (contest_id)
REFERENCES public.contest (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: contest_fk | type: CONSTRAINT --
-- ALTER TABLE public.time_extension DROP CONSTRAINT IF EXISTS contest_fk CASCADE;
ALTER TABLE public.time_extension ADD CONSTRAINT contest_fk FOREIGN KEY (contest_id)
REFERENCES public.contest (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: task_fk | type: CONSTRAINT --
-- ALTER TABLE public.solution DROP CONSTRAINT IF EXISTS task_fk CASCADE;
ALTER TABLE public.solution ADD CONSTRAINT task_fk FOREIGN KEY (task_id)
REFERENCES public.task (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.group | type: TABLE --
-- DROP TABLE IF EXISTS public.group CASCADE;
CREATE TABLE public.group(
	id bigint NOT NULL DEFAULT nextval('public.principal_sequence'::regclass),
	name text NOT NULL,
	CONSTRAINT primary_key PRIMARY KEY (id),
	CONSTRAINT group_name_unique UNIQUE (name)

);
-- ddl-end --
ALTER TABLE public.group OWNER TO postgres;
-- ddl-end --

-- object: public.group_users | type: TABLE --
-- DROP TABLE IF EXISTS public.group_users CASCADE;
CREATE TABLE public.group_users(
	id_group bigint,
	"id_USER" bigint,
	CONSTRAINT group_users_pk PRIMARY KEY (id_group,"id_USER")

);
-- ddl-end --

-- object: group_fk | type: CONSTRAINT --
-- ALTER TABLE public.group_users DROP CONSTRAINT IF EXISTS group_fk CASCADE;
ALTER TABLE public.group_users ADD CONSTRAINT group_fk FOREIGN KEY (id_group)
REFERENCES public.group (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.group_users DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.group_users ADD CONSTRAINT "USER_fk" FOREIGN KEY ("id_USER")
REFERENCES public."USER" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.resource | type: TABLE --
-- DROP TABLE IF EXISTS public.resource CASCADE;
CREATE TABLE public.resource(
	id bigserial NOT NULL,
	name text NOT NULL,
	filename text NOT NULL,
	"USER_id" bigint,
	task_id integer
);
-- ddl-end --
ALTER TABLE public.resource OWNER TO postgres;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public.resource DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public.resource ADD CONSTRAINT "USER_fk" FOREIGN KEY ("USER_id")
REFERENCES public."USER" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: task_fk | type: CONSTRAINT --
-- ALTER TABLE public.resource DROP CONSTRAINT IF EXISTS task_fk CASCADE;
ALTER TABLE public.resource ADD CONSTRAINT task_fk FOREIGN KEY (task_id)
REFERENCES public.task (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --


