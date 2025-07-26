CREATE DATABASE ORGANIZATION;
 
USE ORGANIZATION;
 
CREATE TABLE DEPT (
DEPTNO              integer NOT NULL,
DNAME               varchar(14),
LOC                 varchar(13),
CONSTRAINT DEPT_PRIMARY_KEY PRIMARY KEY (DEPTNO));
CREATE TABLE EMP (
EMPNO               integer NOT NULL,
ENAME               varchar(10),
JOB                 varchar(9),
MGR                 integer,
HIREDATE            DATEtime,
SAL                 DECIMAL(10,2),
COMM                DECIMAL(10,2),
DEPTNO              integer NOT NULL,
CONSTRAINT EMP_FOREIGN_KEY FOREIGN KEY (DEPTNO) REFERENCES DEPT (DEPTNO),
CONSTRAINT EMP_PRIMARY_KEY PRIMARY KEY (EMPNO));
INSERT INTO DEPT VALUES (10,'ACCOUNTING','NEW YORK');
INSERT INTO DEPT VALUES (20,'RESEARCH','DALLAS');
INSERT INTO DEPT VALUES (30,'SALES','CHICAGO');
INSERT INTO DEPT VALUES (40,'OPERATIONS','BOSTON');
 
INSERT INTO EMP VALUES (7839,'KING','PRESIDENT',NULL,'1981-11-17',5000,NULL,10);
INSERT INTO EMP VALUES (7698,'BLAKE','MANAGER',7839,'1981-05-1',2850,NULL,30);
INSERT INTO EMP VALUES (7782,'CLARK','MANAGER',7839,'1981-06-9',2450,NULL,10);
INSERT INTO EMP VALUES (7566,'JONES','MANAGER',7839,'1981-4-2',2975,NULL,20);
INSERT INTO EMP VALUES (7654,'MARTIN','SALESMAN',7698,'1981-9-28',1250,1400,30);
INSERT INTO EMP VALUES (7499,'ALLEN','SALESMAN',7698,'1981-2-20',1600,300,30);
INSERT INTO EMP VALUES (7844,'TURNER','SALESMAN',7698,'1981-9-8',1500,0,30);
INSERT INTO EMP VALUES (7900,'JAMES','CLERK',7698,'1981-12-3',950,NULL,30);
INSERT INTO EMP VALUES (7521,'WARD','SALESMAN',7698,'1981-2-22',1250,500,30);
INSERT INTO EMP VALUES (7902,'FORD','ANALYST',7566,'1981-12-3',3000,NULL,20);
INSERT INTO EMP VALUES (7369,'SMITH','CLERK',7902,'1980-12-17',800,NULL,20);
INSERT INTO EMP VALUES (7788,'SCOTT','ANALYST',7566,'1982-12-09',3000,NULL,20);
INSERT INTO EMP VALUES (7876,'ADAMS','CLERK',7788,'1983-1-12',1100,NULL,20);
INSERT INTO EMP VALUES (7934,'MILLER','CLERK',7782,'1982-1-23',1300,NULL,10);
 

CREATE TABLE REGIONS
( REGION_ID INTEGER NOT NULL,
REGION_NAME VARCHAR(20),
PRIMARY KEY(REGION_ID)
);
 
CREATE TABLE COUNTRIES 
    ( COUNTRY_ID      CHAR(2)
    , COUNTRY_NAME    VARCHAR(40) 
    , REGION_ID       INTEGER 
    , PRIMARY KEY (COUNTRY_ID),
	  CONSTRAINT COUNTR_REG_FK FOREIGN KEY (REGION_ID) REFERENCES REGIONS(REGION_ID)
    ) ;
CREATE TABLE LOCATIONS
    ( LOCATION_ID    INTEGER not null
    , STREET_ADDRESS VARCHAR(40)
    , POSTAL_CODE    VARCHAR(12)
    , CITY       VARCHAR(30) NOT NULL
    , STATE_PROVINCE VARCHAR(25)
    , COUNTRY_ID     CHAR(2),
    PRIMARY KEY (LOCATION_ID),
    FOREIGN KEY (COUNTRY_ID) REFERENCES COUNTRIES(COUNTRY_ID)
    ) ;
 
INSERT INTO regions VALUES( 1, 'Europe');
INSERT INTO regions VALUES( 2, 'Americas');
INSERT INTO regions VALUES( 3, 'Asia');
INSERT INTO regions VALUES( 4, 'Middle East and Asia');
 
INSERT INTO countries VALUES( 'IT', 'Italy', 1);
INSERT INTO countries VALUES( 'JP', 'Japan', 3);
INSERT INTO countries VALUES( 'US', 'USA', 2);
INSERT INTO countries VALUES( 'CA', 'Canada', 2);
INSERT INTO countries VALUES( 'CN', 'China', 3);
INSERT INTO countries VALUES( 'IN', 'India', 3);
INSERT INTO countries VALUES( 'AU', 'Australia', 3);
INSERT INTO countries VALUES( 'ZW', 'Zimbabwe',4);
 
 
INSERT INTO countries VALUES( 'SG', 'Singapore', 3);
INSERT INTO countries VALUES( 'UK', 'United Kingdom', 1);
INSERT INTO countries VALUES( 'FR', 'France', 1);
INSERT INTO countries VALUES( 'DE', 'Germany', 1);
INSERT INTO countries VALUES( 'ZM', 'Zambia', 4);
INSERT INTO countries VALUES( 'EG', 'Egypt', 4);
INSERT INTO countries VALUES( 'BR', 'Brazil', 2);
INSERT INTO countries VALUES( 'CH', 'Switzerland', 1);
 
 
INSERT INTO countries VALUES( 'NL', 'Netherlands', 1);
INSERT INTO countries VALUES( 'MX', 'Mexico', 2);
INSERT INTO countries VALUES( 'KW', 'Kuwait', 4);
INSERT INTO countries VALUES( 'IL', 'Israel', 4);
INSERT INTO countries VALUES( 'DK', 'Denmark', 1);
INSERT INTO countries VALUES( 'HK', 'HongKong', 3);
INSERT INTO countries VALUES( 'NG', 'Nigeria', 4);
INSERT INTO countries VALUES( 'AR', 'Argentina', 2);
INSERT INTO countries VALUES( 'BE', 'Belgium', 1);
 
INSERT INTO locations VALUES( 1000, '1297 Via Cola di Rie', '00989', 'Roma', NULL, 'IT');
INSERT INTO locations VALUES( 1100, '93091 Calle della Testa', '10934', 'Venice', NULL, 'IT');
INSERT INTO locations VALUES( 1200, '2017 Shinjuku-ku', '1689', 'Tokyo', 'Tokyo Prefecture', 'JP');
INSERT INTO locations VALUES( 1300, '9450 Kamiya-cho', '6823', 'Hiroshima', NULL, 'JP');
INSERT INTO locations VALUES( 1400, '2014 Jabberwocky Rd', '26192', 'Southlake', 'Texas', 'US');
INSERT INTO locations VALUES( 1500, '2011 Interiors Blvd', '99236', 'South San Francisco', 'California', 'US');
INSERT INTO locations VALUES( 1600, '2007 Zagora St', '50090', 'South Brunswick', 'New Jersey', 'US');
INSERT INTO locations VALUES( 1700, '2004 Charade Rd', '98199', 'Seattle', 'Washington', 'US');
INSERT INTO locations VALUES( 1800, '147 Spadina Ave', 'M5V 2L7', 'Toronto', 'Ontario', 'CA');
INSERT INTO locations VALUES( 1900, '6092 Boxwood St', 'YSW 9T2', 'Whitehorse', 'Yukon', 'CA');

-- 0 Show details of all employees
SELECT * FROM EMP;

-- 1 Display all employee names in ascending order
select ename from emp order by ename asc;

-- 2 Display all employees (all columns) in department 20 and 30
select * from emp where deptno in (20,30);

-- 3 Display all the employees who are managers
select * from emp where job='manager' ;

-- 4 Display all the employees whose salary is between 2000 and 5000
select * from emp where sal>2000 && sal <5000;
select * from emp where sal between 2000 and 5000;

-- 5 Display all the employees whose commission is null
select * from emp where comm is null;

-- 6 Display emp_name, salary, commission, and CTC (calculated column)
SELECT ENAME, SAL, COMM,((SAL + IFNULL(COMM, 0))*12)AS CTC FROM EMP;

-- 7  Display hire_date, current_date, and tenure (calculated column)
select ename, hiredate, current_date() AS `current_date`,
timestampdiff(YEAR, hiredate, current_date()) AS tenure_year
from emp;

-- 8 Display all the employees whose name starts with 'S'
select * from emp where ename like 's%';

-- 9 Display unique department numbers from the employee table
select distinct deptno from emp;

-- 10 Display emp_name and job in lower case
select lower(ename) as emp_name ,lower(job) as emp_job from emp;

-- 11 Select top 3 salary earning employee
select * from emp order by sal desc limit 3;

-- 12. Select Clerks and Managers in department 10
select * from emp where deptno=10 and job in ('clerk','manager');

-- 13 Display all clerks in ascending order of the department number
select * from emp where job='clerk' order by deptno  asc;

-- 14 Display all employees in the same department as 'SCOTT'
select * from emp where deptno=(select deptno from emp where ename='scott'); 

-- 15 Employees having same designation as 'SMITH'
select * from emp where job=(select job from emp where ename='SMITH'); 

-- 16 Employees who are reporting under 'KING'
select * from emp where mgr=(select empno from emp where ename='king');

-- 17 Employees who have same salary as 'BLAKE'
select * from emp where sal=(select sal from emp where ename='blake');

-- 18 Display department-wise number of employees
select deptno, count(*) as num_employe from emp group by deptno;

-- 19 Display job-wise number of employees
select job,count(*) as num_employee from emp group by job;

-- 20 Display department-wise job-wise number of employees
select deptno,count(*) as deptcount, job, count(*) as num_employee from emp group by deptno, job;

-- 21 Display deptwise  employees greater than  3
select deptno, count(*) as num_employee from emp group by deptno having count(*) > 3;

-- 22 Display designation wise employees count greater than 3 
select job, count(*) as num_employee from emp group by job having count(*) > 3;

-- 23 Display Employee name,deptname and location
select e.ename,d.dname,d.loc from emp e join dept d on e.deptno=d.deptno;

-- 24 Show every department and the employees in it
select  d.dname, e.ename  from dept d  join emp e on d.deptno= e.deptno;

-- 25 Show only the departments that have no employees
select d.deptno, d.dname,d.loc from dept d left join  emp e on d.deptno=e.deptno where e.empno is null;

-- 26  Show each department’s employee count > 3, sorted by department name (Z→A)
select d.dname, count(e.empno) as emp_count
 from dept d 
 join emp e on d.deptno=e.deptno 
 group by d.dname 
 having count(e.empno)>3 order by d.dname desc;

-- 27  Show every employee and their manager’s name
select e.ename as employee , m.ename as manager from emp e left join  emp m on e.mgr=m.empno;

-- 28 Show employee name, department name, and manager name (alias as bossname), ordered by bossname
select e.ename as employee,d.dname as department, m.ename as Bossname
from emp e
join dept d on e.deptno=d.deptno
left join emp m on e.mgr=m.empno
order by Bossname;

-- 29 Show department name, employee name, and the employee’s manager name
select d.dname as department ,e.ename as employee, m.ename as manager
from emp e 
join dept d on e.deptno=d.deptno
left join emp m on e.mgr=m.empno;

-- 30 Show every location, its city, country and region
select l.location_id, l.city, c.country_name, r.region_name	
from locations l
join countries c on l.country_id=c.country_id
join regions r on c.region_id=r.region_id;











