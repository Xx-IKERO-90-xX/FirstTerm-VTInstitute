-- Simplified SQL: only CREATE (schema/sequence/table) and INSERT statements
-- Extracted from New_Query_1763467514085.sql for use in the application

CREATE SCHEMA IF NOT EXISTS _da_vtschool_2526;

CREATE SEQUENCE enrollments_code_seq START WITH 1 INCREMENT BY 1;


CREATE SEQUENCE subjects_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

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

CREATE SEQUENCE inscriptions_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE scores_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE subjects (
    code INT PRIMARY KEY DEFAULT nextval('subjects_code_seq'),
    name VARCHAR(50) UNIQUE,
    year INT NOT NULL,
    hours INT NOT NULL
);

CREATE TABLE courses (
    code INT PRIMARY KEY DEFAULT nextval('courses_code_seq'),
    name VARCHAR(90) NOT NULL UNIQUE
);

CREATE TABLE subject_courses (
    code INT PRIMARY KEY DEFAULT nextval('subject_courses_code_seq'),
    subject_id INT NOT NULL,
    course_id INT NOT NULL,

    FOREIGN KEY (subject_id) REFERENCES subjects(code),
    FOREIGN KEY (course_id) REFERENCES courses(code)
);

CREATE TABLE students (
    idcard VARCHAR(12) PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    phone VARCHAR(12),
    email VARCHAR(100)
);

CREATE TABLE enrollments (
    code INT PRIMARY KEY DEFAULT nextval('enrollments_code_seq'),
    student VARCHAR(12) NOT NULL,
    course INT NOT NULL,
    year INT NOT NULL,
    FOREIGN KEY (student) REFERENCES students(idcard),
    FOREIGN KEY (course) REFERENCES courses(code)
);

CREATE TABLE scores (
    code INT PRIMARY KEY DEFAULT nextval('scores_code_seq'),
    enrollment_id INT NOT NULL,
    subject_id INT NOT NULL,
    score INT,

    FOREIGN KEY (enrollment_id) REFERENCES enrollments(code),
    FOREIGN KEY (subject_id) REFERENCES subjects(code),

    UNIQUE (enrollment_id, subject_id)
);

SELECT * FROM students;
-- Insert: Courses
INSERT INTO courses (name) VALUES ('Multiplatform app development');
INSERT INTO courses (name) VALUES ('Web development');


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

-- Inserts: subjects
INSERT INTO subjects (name, year, hours) VALUES ('Data Access', 2, 105);
INSERT INTO subjects (name, year, hours) VALUES ('Database Management Systems', 1, 198);
INSERT INTO subjects (name, year, hours) VALUES ('Services and Processes', 2, 83);
INSERT INTO subjects (name, year, hours) VALUES ('Technical English', 1, 99);
INSERT INTO subjects (name, year, hours) VALUES ('Development Environments', 1, 99);
INSERT INTO subjects (name, year, hours) VALUES ('Markup Languages', 1, 132);
INSERT INTO subjects (name, year, hours) VALUES ('Programming', 1, 264);
INSERT INTO subjects (name, year, hours) VALUES ('Client-side development', 2, 99);
INSERT INTO subjects (name, year, hours) VALUES ('Server-side development', 2, 168);

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
