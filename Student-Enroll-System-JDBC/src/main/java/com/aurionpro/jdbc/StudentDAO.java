package com.aurionpro.jdbc;

import com.aurionpro.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(student.getName() + " added in Student successfully.");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        Student student = null;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email")
                    );
                } else {
                    System.out.println("No student found with ID " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student: " + e.getMessage());
        }

        return student;
    }

  
    public void updateStudent(int id, String name, String email) {
        String sql = "UPDATE students SET name = ?, email = ? WHERE student_id = ?"; 

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("No student found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }


   
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    
    public void viewAllStudents() {
        String sql = "SELECT * FROM students";

        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Student> studentList = new ArrayList<>();

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                studentList.add(s);
            }

            if (studentList.isEmpty()) {
                System.out.println("No students found.");
            } else {
                System.out.println("\n--- Student List ---");
                for (Student s : studentList) {
                    System.out.println("ID: " + s.getId() + ", Name: " + s.getName() + ", Email: " + s.getEmail());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
        }
    }
}
