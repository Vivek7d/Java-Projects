CREATE DATABASE indexings;
USE indexings;

-- Department Table
CREATE TABLE departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL
);

-- Students Table
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

-- Courses Table
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100),
    credits INT
);

-- Enrollments Table
CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

-- Departments
INSERT INTO departments (dept_name) VALUES
('Computer Science'), ('Electrical'), ('Mechanical');

-- Students
INSERT INTO students (name, age, dept_id) VALUES
('Rohan', 21, 1),
('Priya', 22, 2),
('Aman', 23, 1),
('Sneha', 20, 3);
select * from students;
-- Courses
INSERT INTO courses (course_name, credits) VALUES
('Data Structures', 4),
('Circuits', 3),
('Thermodynamics', 4),
('Operating Systems', 3);

-- Enrollments
INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES
(1, 1, '2025-07-10'),
(1, 4, '2025-07-12'),
(2, 2, '2025-07-11'),
(3, 1, '2025-07-14'),
(4, 3, '2025-07-13');



-- Indexing is like creating a "quick search" feature in your database.
-- Instead of scanning the entire table, MySQL uses the index to  jump directly to the relevant rows 
-- It's similar to an index in a book — it helps the database find rows faster.

-- Creates an index named idx_enroll_date on the enrollment_date column in the enrollments table.
CREATE INDEX idx_enroll_date ON enrollments(enrollment_date);

SELECT * FROM enrollments
WHERE enrollment_date = '2025-07-12';

EXPLAIN SELECT * 
FROM enrollments 
WHERE enrollment_date = '2025-07-12';

-- Composite Index (Multi-column)
CREATE INDEX idx_student_course_composite ON enrollments(student_id, course_id);
SHOW INDEX FROM enrollments;

EXPLAIN SELECT * 
FROM enrollments 
WHERE student_id = 1 AND course_id = 2;

EXPLAIN SELECT * 
FROM enrollments 
WHERE course_id = 2 AND student_id = 1;













