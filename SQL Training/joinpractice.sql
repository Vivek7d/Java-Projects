CREATE DATABASE practice1;
 
USE practice1;

CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT,
    city VARCHAR(50)
);
 
INSERT INTO Students (student_id, name, age, city) VALUES
(1, 'Rohan', 22, 'Mumbai'),
(2, 'Priya', 21, 'Delhi'),
(3, 'Aman', 23, 'Bangalore'),
(4, 'Sneha', 22, 'Mumbai');



CREATE TABLE Instructors (
    instructor_id INT PRIMARY KEY,
    instructor_name VARCHAR(50) NOT NULL
);
 
INSERT INTO Instructors (instructor_id, instructor_name) VALUES
(1, 'Mr. Sharma'),
(2, 'Ms. Kapoor'),
(3, 'Mr. Khan');

CREATE TABLE Courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    instructor_id INT,
    FOREIGN KEY (instructor_id) REFERENCES Instructors(instructor_id)
);
 
INSERT INTO Courses (course_id, course_name, instructor_id) VALUES
(101, 'Java Programming', 1),
(102, 'MySQL Basics', 2),
(103, 'Spring Boot', 1),
(104, 'Angular', 3);

CREATE TABLE Enrollments (
    enrollment_id INT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);
 
INSERT INTO Enrollments (enrollment_id, student_id, course_id, enrollment_date) VALUES
(1, 1, 101, '2025-07-01'),
(2, 1, 102, '2025-07-02'),
(3, 2, 101, '2025-07-03'),
(4, 3, 103, '2025-07-05'),
(5, 4, 104, '2025-07-06');

select * from Students;
select * from Instructors;
select * from Courses;
select * from Enrollments;

-- 1 Show student names and the courses they are enrolled in.
select s.name, c.course_name from students s inner join enrollments e on s.student_id=e.student_id 
inner join courses c on c.course_id=e.course_id;

-- 2. List all students along with their instructor’s name for each enrolled course 
select s.name, c.course_name,i.instructor_name from students s inner join enrollments e on s.student_id=e.student_id 
inner join courses c on c.course_id=e.course_id
inner join instructors i on i.instructor_id=c.instructor_id;

-- 3. Find students who are enrolled in “Java Programming”.
select s.name, c.course_name from students s inner join enrollments e on e.student_id=s.student_id
inner join courses c on c.course_id=e.course_id where course_name="Java Programming";

-- 4.List all students from “Mumbai” who are enrolled in any course.
select s.name, s.city , c.course_name from students s inner join enrollments e on e.student_id=s.student_id
inner join courses c on c.course_id=e.course_id where s.city="Mumbai";

-- 5. Show students who enrolled after “2025-07-02” with their course name. 
select s.name, c.course_name ,e.enrollment_date from students s inner join enrollments e on s.student_id=e.student_id
inner join courses c on c.course_id= e.course_id 
where e.enrollment_date > '2025-07-02';

-- 6. Show all students and their enrolled courses (including students with no enrollments
select s.name, c.course_name from students s left join enrollments e on s.student_id=e.student_id
left join courses c on e.course_id=c.course_id;

-- 7. Show all courses and their enrolled students including courses with no students enrolled
select c.course_name, s.name from courses c left join enrollments e on c.course_id=e.course_id
left join students s on e.student_id= s.student_id;

--  A View to show student names, course names, and instructor names
CREATE VIEW StudentCourseInstructorView AS
SELECT 
    s.name AS student_name,
    c.course_name,
    i.instructor_name
FROM Students s
JOIN Enrollments e ON s.student_id = e.student_id
JOIN Courses c ON c.course_id = e.course_id
JOIN Instructors i ON i.instructor_id = c.instructor_id;

SELECT * FROM StudentCourseInstructorView;

-- A function to count how many students are enrolled in a given course
DELIMITER $$

CREATE FUNCTION get_enrollment_count(courseId INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE count_students INT;
    
    SELECT COUNT(*) INTO count_students
    FROM Enrollments
    WHERE course_id = courseId;
    
    RETURN count_students;
END $$
DELIMITER ;
SELECT get_enrollment_count(101); 



