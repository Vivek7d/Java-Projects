package com.aurionpro.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentDAO {
    public void enrollStudent(int studentId, int courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student enrolled successfully.");
            } else {
                System.out.println("Failed to enroll student.");
            }

        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
        }
    }
    public void viewAllEnrollments() {
        String sql = 
            "SELECT e.enrollment_id, s.name AS student_name, c.title AS course_title, e.enrollment_date " +
            "FROM enrollments e " +
            "JOIN students s ON e.student_id = s.student_id " +
            "JOIN courses c ON e.course_id = c.course_id " +
            "ORDER BY e.enrollment_date DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- All Enrollments ---");
            while (rs.next()) {
                int id = rs.getInt("enrollment_id");
                String student = rs.getString("student_name");
                String course = rs.getString("course_title");
                String date = rs.getString("enrollment_date");

                System.out.printf("ID: %d | Student: %s | Course: %s | Date: %s%n", id, student, course, date);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching enrollments: " + e.getMessage());
        }
    }


    // Delete an enrollment by ID
    public void deleteEnrollment(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Enrollment deleted successfully.");
            } else {
                System.out.println("No enrollment found with ID " + enrollmentId);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
        }
    }
}