CREATE DATABASE studentdb11;
USE studentdb11;

CREATE TABLE Student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    percentage DECIMAL(5,2)
);

INSERT INTO Student (first_name, last_name, email, percentage)
VALUES 
('Vivek', 'Dhalkari', 'vivek@example.com', 90.50),
('Riya', 'Sharma', 'h2.sharma@example.com', 92.25),
('Aman', 'Verma', 'ama.verma@example.com', 88.75);
-- View inserted data
SELECT * FROM Student;
-- Add the dob column
ALTER TABLE Student ADD dob DATE NULL;

-- Update dob for id = 2
UPDATE Student SET dob = '2002-08-15' WHERE id = 1;

-- View updated data
SELECT * FROM Student;

select count(*) as totalCount from Student;
select * from Student where percentage > 90;


UPDATE Student SET dob = '2003-08-15' WHERE id = 2;

UPDATE Student SET dob = '2002-01-15' WHERE id = 3;

ALTER TABLE Student ADD age int NULL;
UPDATE Student SET age = 23 WHERE id = 1;

UPDATE Student SET age = 20 WHERE id = 2;

UPDATE Student SET age = 19 WHERE id = 3;

select * from Student where age >= 20;

ALTER TABLE Student DROP COLUMN age;

SELECT *
FROM Student
WHERE TIMESTAMPDIFF(YEAR, dob, CURDATE()) > 22;

ALTER TABLE Student ADD COLUMN studentAge INT;
SET SQL_SAFE_UPDATES = 0;
UPDATE Student SET studentAge = TIMESTAMPDIFF(YEAR, dob, CURDATE());

SELECT * FROM Student WHERE TIMESTAMPDIFF(YEAR, dob, CURDATE()) > 22;

SELECT * FROM Student WHERE TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 22 AND 44;

ALTER TABLE student ADD COLUMN record_date DATE NOT NULL DEFAULT (CURDATE());

ALTER TABLE student
  ADD CONSTRAINT chk_age_10_30
    CHECK (
      dob BETWEEN
        DATE_SUB(record_date, INTERVAL 30 YEAR)   -- ≤ 30 yrs old
        AND
        DATE_SUB(record_date, INTERVAL 10 YEAR)   -- ≥ 10 yrs old
    );





