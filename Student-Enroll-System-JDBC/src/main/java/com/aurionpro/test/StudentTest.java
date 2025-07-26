package com.aurionpro.test;

import java.util.Scanner;

import com.aurionpro.jdbc.*;
import com.aurionpro.model.*;

public class StudentTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		StudentDAO studentDAO = new StudentDAO();
		DepartmentDAO departmentDAO = new DepartmentDAO();
		InstructorDAO instructorDAO = new InstructorDAO();
		CourseDAO courseDAO = new CourseDAO();
		EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

		while (true) {
			System.out.println("\n--- MAIN MENU ---");
			System.out.println("1. Manage Students");
			System.out.println("2. Manage Department");
			System.out.println("3. Manage Instructor");
			System.out.println("4. Manage Courses");
			System.out.println("5. Manage Enrollments");
			System.out.println("6. Find Students from Course");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine(); 

			switch (choice) {
			case 1:
				manageStudents(sc, studentDAO);
				break;

			case 2:
				manageDepartments(sc, departmentDAO);
				break;

			case 3:
				manageInstructors(sc, instructorDAO);
				break;

			case 4:
				manageCourses(sc, courseDAO);
				break;

			case 5:
				manageEnrollment(sc, enrollmentDAO);
				break;

			case 6:
				System.out.print("Enter Course ID: ");
				int cId = sc.nextInt();
				courseDAO.getStudentsByCourseId(cId);
				break;

			case 7:
				System.out.println("Exiting...");
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice. Try again.");
				break;
			}
		}
	}

	private static void manageStudents(Scanner sc, StudentDAO studentDAO) {
		while (true) {
			System.out.println("\n--- STUDENT MENU ---");
			System.out.println("1. Add Student");
			System.out.println("2. Update Student");
			System.out.println("3. Delete Student");
			System.out.println("4. View All Students");
			System.out.println("5. Get Student by Id");
			System.out.println("6. Back to Main Menu");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine(); 

			switch (choice) {
			case 1:
				System.out.print("Enter Student Name: ");
				String name = sc.nextLine();
				System.out.print("Enter Student Email: ");
				String email = sc.nextLine();
				studentDAO.addStudent(new Student(name, email));
				break;

			case 2:
				studentDAO.viewAllStudents();
				System.out.print("Enter Student ID to Update: ");
				int updateId = sc.nextInt();
				sc.nextLine();
				System.out.print("Enter New Name: ");
				String newName = sc.nextLine();
				System.out.print("Enter New Email: ");
				String newEmail = sc.nextLine();
				studentDAO.updateStudent(updateId, newName, newEmail);
				break;

			case 3:
				studentDAO.viewAllStudents();
				System.out.print("Enter Student ID to Delete: ");
				int deleteId = sc.nextInt();
				studentDAO.deleteStudent(deleteId);
				break;

			case 4:
				studentDAO.viewAllStudents();
				break;

			case 5:
				System.out.print("Enter Student ID : ");
				int id = sc.nextInt();
				Student student =studentDAO.getStudentById(id);
				if (student != null) {
			        System.out.println("\n--- Student Details ---");
			        System.out.println("ID: " + student.getId());
			        System.out.println("Name: " + student.getName());
			        System.out.println("Email: " + student.getEmail());
			    }
				return;
			case 6:
				return;

			default:
				System.out.println("Invalid choice. Try again.");
				break;
			}
		}
	}

	private static void manageDepartments(Scanner sc, DepartmentDAO departmentDAO) {
		while (true) {
			System.out.println("\n--- DEPARTMENT MENU ---");
			System.out.println("1. Add Department");
			System.out.println("2. Update Department");
			System.out.println("3. Delete Department");
			System.out.println("4. View All Departments");
			System.out.println("5. Back to Main Menu");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				System.out.print("Enter Department Name: ");
				String name = sc.nextLine();
				departmentDAO.addDepartment(new Department(name));
				break;

			case 2:
				departmentDAO.viewAllDepartments();
				System.out.print("Enter Department ID to Update: ");
				int updateId = sc.nextInt();
				sc.nextLine();
				System.out.print("Enter New Department Name: ");
				String newName = sc.nextLine();
				departmentDAO.updateDepartment(updateId, newName);
				break;

			case 3:
				departmentDAO.viewAllDepartments();
				System.out.print("Enter Department ID to Delete: ");
				int deleteId = sc.nextInt();
				departmentDAO.deleteDepartment(deleteId);
				break;

			case 4:
				departmentDAO.viewAllDepartments();
				break;

			case 5:
				return;

			default:
				System.out.println("Invalid choice. Try again.");
				break;
			}
		}
	}

	private static void manageInstructors(Scanner sc, InstructorDAO instructorDAO) {
		while (true) {
			System.out.println("\n--- INSTRUCTOR MENU ---");
			System.out.println("1. Add Instructor to Department");
			System.out.println("2. Update Instructor");
			System.out.println("3. Delete Instructor");
			System.out.println("4. View All Instructors");
			System.out.println("5. Back to Main Menu");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				System.out.print("Enter Instructor Name: ");
				String name = sc.nextLine();

				System.out.print("Enter Department ID: ");
				int deptId = sc.nextInt();

				Instructor instructor = new Instructor(name, deptId);
				instructorDAO.addInstructorToDepartment(instructor);
				break;

			case 2:
				instructorDAO.viewAllInstructors();
				System.out.print("Enter Instructor ID to Update: ");
				int updateId = sc.nextInt();
				sc.nextLine();

				System.out.print("Enter New Name: ");
				String newName = sc.nextLine();

				System.out.print("Enter New Department ID: ");
				int newDeptId = sc.nextInt();

				instructorDAO.updateInstructor(updateId, newName, newDeptId);
				break;

			case 3:
				instructorDAO.viewAllInstructors();
				System.out.print("Enter Instructor ID to Delete: ");
				int deleteId = sc.nextInt();
				instructorDAO.deleteInstructor(deleteId);
				break;

			case 4:
				instructorDAO.viewAllInstructors();
				break;

			case 5:
				return;

			default:
				System.out.println("Invalid choice. Try again.");
				break;
			}
		}
	}

	private static void manageCourses(Scanner sc, CourseDAO courseDAO) {
		while (true) {
			System.out.println("\n--- COURSE MENU ---");
			System.out.println("1. Add Course");
			System.out.println("2. View All Courses");
			System.out.println("3. Delete Course");
			System.out.println("4. Update Course");
			System.out.println("5. Get Students by Course ID");
			System.out.println("6. Exit");

			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				System.out.print("Enter Course Title: ");
				String title = sc.nextLine();
				System.out.print("Enter Instructor ID: ");
				int instructorId = sc.nextInt();
				courseDAO.addCourse(new Course(title, instructorId));
				break;

			case 2:
				courseDAO.viewAllCourses();
				System.out.print("Enter Course ID to Update: ");
				int updateId = sc.nextInt();
				sc.nextLine();
				System.out.print("Enter New Course Title: ");
				String newTitle = sc.nextLine();
				System.out.print("Enter New Instructor ID: ");
				int newInstructorId = sc.nextInt();
				courseDAO.updateCourse(updateId, newTitle, newInstructorId);
				break;

			case 3:
				courseDAO.viewAllCourses();
				System.out.print("Enter Course ID to Delete: ");
				int deleteId = sc.nextInt();
				courseDAO.deleteCourse(deleteId);
				break;

			case 4:
				courseDAO.viewAllCourses();
				break;
			case 5:
				System.out.print("Enter Course ID to view enrolled students: ");
				int courseId = sc.nextInt();
				courseDAO.getStudentsByCourseId(courseId);
				break;

			case 6:
				return;

			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}

	public static void manageEnrollment(Scanner scanner, EnrollmentDAO enrollmentDAO) {

		while (true) {
			System.out.println("\n--- Manage Enrollments ---");
			System.out.println("1. Enroll Student to Course");
			System.out.println("2. View All Enrollments");
			System.out.println("3. Delete Enrollment");
			System.out.println("4. Back to Main Menu");
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				System.out.print("Enter Student ID: ");
				int studentId = scanner.nextInt();
				System.out.print("Enter Course ID: ");
				int courseId = scanner.nextInt();
				enrollmentDAO.enrollStudent(studentId, courseId);
				break;

			case 2:
				enrollmentDAO.viewAllEnrollments();
				break;

			case 3:
				System.out.print("Enter Enrollment ID to delete: ");
				int enrollmentId = scanner.nextInt();
				enrollmentDAO.deleteEnrollment(enrollmentId);
				break;

			case 4:
				return;

			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}

}
