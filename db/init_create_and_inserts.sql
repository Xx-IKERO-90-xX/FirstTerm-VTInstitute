-- Simplified SQL: only CREATE (schema/sequence/table) and INSERT statements
-- Extracted from New_Query_1763467514085.sql for use in the application

CREATE SCHEMA IF NOT EXISTS _da_vtschool_2526;

CREATE TABLE subjects (
    code integer NOT NULL,
    name character varying(50),
    year integer,
    hours integer
);

CREATE SEQUENCE subjects_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE courses (
    code integer NOT NULL,
    name character varying(90) NOT NULL
);

CREATE SEQUENCE courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE subject_courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE subject_courses (
    code integer NOT NULL DEFAULT nextval('subject_courses_code_seq'::regclass),
    subject_id integer NOT NULL,
    course_id integer NOT NULL
);

CREATE TABLE enrollments (
    student character varying(12) NOT NULL,
    course integer NOT NULL,
	year integer NOT NULL,
    code integer NOT NULL
);

CREATE SEQUENCE inscriptions_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE scores (
    enrollment_id integer NOT NULL,
    subject_id integer NOT NULL,
    score integer,
	code integer NOT NULL
);

CREATE SEQUENCE scores_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE students (
    firstname character varying(50) NOT NULL,
    lastname character varying(100) NOT NULL,
    idcard character varying(8) NOT NULL,
    phone character varying(12),
    email character varying(100)
);

-- Inserts: courses
INSERT INTO courses (name) VALUES ('Multiplatform app development');
INSERT INTO courses (name) VALUES ('Web development');

-- Inserts: enrollments
INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 1, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 2, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 2, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332006', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332111', 2, 2024);

-- Inserts: scores
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 6, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 5, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 6, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 2, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 5, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 2, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 5, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 6, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 7, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 4, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 6, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 7, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 2, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 5, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 3, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 5, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 7, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 1, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 8, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 9, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 5, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 7, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 8, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 9, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 4, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 6, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 1, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 3, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 6, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 5, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 6, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 5, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 7, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 4, 7);

-- Inserts: students
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Aitana', 'Garcia', '12332003', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Spencer', '12332004', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Smith', '12332005', '654654654', 'johnsmith@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Marcos', 'Andreu', '12332006', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Student', 'X', '12332007', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Mark', 'Ross', '12332008', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Estrella', 'Garcia', '12332002', '', 'estrella@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Robe', 'Iniesta', '12332111', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Jose', 'Garcia', '12332001', '655565566', 'jrgarcia@mail.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Ken', 'Brockman', '12332321', '123456789', 'ken@e.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Kevin', 'Smith', '12332444', '123456789', '');

-- Inserts: subjects
INSERT INTO subjects (name, year, hours) VALUES ('Data Access', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Database Management Systems', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Services and Processes', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Technical English', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Development Environments', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Markup Languages', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Programming', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Client-side development', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Server-side development', 2, NULL);

-- Inserts: subject_courses
INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (1, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (3, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (8, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (9, 2);
