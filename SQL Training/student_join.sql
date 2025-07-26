Create database studentdbs3;
use studentdbs3;

CREATE TABLE Student (
    studentid INT PRIMARY KEY,
    studentid INT PRIMARY KEY,
    rollnumber INT UNIQUE,
    name VARCHAR(50),
    age INT ,
    percentage DECIMAL(5,2) 
);
INSERT INTO Student (studentid, rollnumber, name, age, percentage) VALUES
(1, 101, 'Anjali Sharma', 18, 92.50),
(2, 102, 'Ravi Verma', 19, 85.00),
(3, 103, 'Priya Deshmukh', 20, 74.00),
(4, 104, 'Amit Patel', 17, 38.00),
(5, 105, 'Sneha Joshi', 21, 64.50),
(6, 106, 'Arjun Mehta', 22, 49.00),
(7, 107, 'Akshay Kumar', 18, 55.50),
(8, 108, 'Anaya Nair', 19, 88.00),
(9, 109, 'Bhavna Iyer', 20, 91.00),
(10, 110, 'Chetan Salunkhe', 18, 33.00),
(11, 111, 'Deepak Rawat', 20, 85.00),
(12, 112, 'Asha Pawar', 19, 67.00),
(13, 113, 'Ganesh Jadhav', 22, 75.00),
(14, 114, 'Aditya Jain', 21, 92.50),
(15, 115, 'Raj Thakur', 17, 40.00);

select * from student;
CREATE TABLE Profile (
   studentid INT PRIMARY KEY,
   city VARCHAR(50),
   mobileno VARCHAR(15),
   FOREIGN KEY (studentid) REFERENCES Student(studentid)
);
 
 
 
CREATE TABLE Subjects (
   subid INT PRIMARY KEY,
   studentid INT,
   subname VARCHAR(50),
   FOREIGN KEY (studentid) REFERENCES Student(studentid)
);
 
 
CREATE TABLE Course (
   courseid INT PRIMARY KEY,
   coursename VARCHAR(100)
);
 
CREATE TABLE Student_Course (
   studentid INT,
   courseid INT,
   PRIMARY KEY (studentid, courseid),
   FOREIGN KEY (studentid) REFERENCES Student(studentid),
   FOREIGN KEY (courseid) REFERENCES Course(courseid)
);
 
 
 
INSERT INTO Profile (studentid, city, mobileno) VALUES
(1, 'Pune', '9876543210'),
(2, 'Mumbai', '9823456780'),
(3, 'Nagpur', '9988776655'),
(4, 'Delhi', '9911223344'),
(5, 'Pune', '9876501234');

 
 
 
INSERT INTO Subjects VALUES
(1, 1, 'Mathematics'),
(2, 1, 'Physics'),
(3, 2, 'Biology'),
(4, 3, 'Chemistry'),
(5, 3, 'Mathematics'),
(6, 4, 'History'),
(7, 5, 'Economics');
 
 
 
INSERT INTO Course VALUES
(1, 'BSc Computer Science'),
(2, 'BSc Mathematics'),
(3, 'BA History'),
(4, 'BCom Finance');
 
INSERT INTO Student_Course VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 1),
(3, 3),
(4, 3),
(5, 4),
(2, 4);

-- List all students from ‘Pune’
SELECT s.*
FROM Student s
JOIN Profile p ON s.studentid = p.studentid
WHERE p.city = 'Pune';

--  Count how many students are in each city
SELECT p.city, COUNT(*) AS student_count
FROM Profile p
GROUP BY p.city;

--  Find students with percentage > 80
SELECT * FROM Student
WHERE percentage > 80;

-- List students who are enrolled in more than one course
SELECT s.studentid, s.name, COUNT(sc.courseid) AS course_count
FROM Student s
JOIN Student_Course sc ON s.studentid = sc.studentid
GROUP BY s.studentid, s.name
HAVING COUNT(sc.courseid) > 1;

-- Show each student's name and their subjects
SELECT s.name, sub.subname
FROM Student s
JOIN Subjects sub ON s.studentid = sub.studentid;

select * from student where studentid not in (select studentid from profile);
SELECT s.*
FROM Student s
LEFT JOIN Profile p ON s.studentid = p.studentid
WHERE p.studentid IS NULL;



