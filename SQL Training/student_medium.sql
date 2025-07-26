Create database studentdbs1;
use studentdbs1;
CREATE TABLE Student (
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

-- Display all columns for all students in the table
select * from student;

-- Show the name and roll number of students who scored more than 75%
select name, rollnumber,percentage from student where percentage >75;

-- List students who are older than 18 and have a percentage less than 50
select * from student where age>18 and percentage<50;

-- Display all students sorted by percentage in descending order
select * from student order by  percentage desc;

-- Count the total number of students in the table
select count(*) as total_students from student;

-- Find the average percentage of students who are younger than 20
select avg(percentage) AS avg_percentage_under_20  from student where age >20;

-- Find the student(s) who scored the highest percentage
select * from student where percentage=(select max(percentage)from student);

-- Display the number of students grouped by their age
select age,count(*) as student_count from student group by age;

-- List all students whose name starts with the letter 'A'
select* from student where name like 'A%';

--  Show names and percentages of students who scored above the average percentage
SELECT name, percentage FROM Student WHERE percentage > (SELECT AVG(percentage) FROM Student);

-- Assign grades to students based on percentage
 select name, percentage,
	case when percentage>=90 then 'A'
		when percentage >=75 then 'B'
		when percentage >=60 then 'C'
		else 'D'
	end as grade 
    from student;
    
    -- Find the second highest percentage scored by any student
    select max(percentage) as second_highest from student where percentage<(select max(percentage) from student);

-- Create a view that contains details of all students who failed (percentage less than 40)
create view  FailedStudents as  select * from student where  percentage < 40;
select * from FailedStudents;

-- Display the rank of each student based on their percentage using a window function
SELECT name, percentage,
  RANK() OVER (ORDER BY percentage DESC) AS rnk
FROM Student;

SELECT * FROM (
  SELECT name, percentage,
    RANK() OVER (ORDER BY percentage DESC) AS rnk
  FROM Student
) AS ranked_students
WHERE rnk <= 3;

