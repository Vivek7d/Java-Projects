package com.aurionpro.jdbc;

import java.sql.*;

import com.aurionpro.model.Course;

public class CourseDAO {
    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (title, instructor_id) VALUES (?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getInstructorId());
            ps.executeUpdate();
            System.out.println("Course added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void viewAllCourses() {
        String sql = "SELECT c.course_id, c.title, i.name AS instructor_name " +
                     "FROM courses c LEFT JOIN instructors i ON c.instructor_id = i.instructor_id";

        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- List of Courses ---");
            while (rs.next()) {
                int id = rs.getInt("course_id");
                String title = rs.getString("title");
                String instructor = rs.getString("instructor_name");
                System.out.println("ID: " + id + ", Title: " + title + ", Instructor: " + instructor);
            }

        } catch (SQLException e) {
            System.err.println("Error viewing courses: " + e.getMessage());
        }
    }

    public void deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Course deleted successfully.");
            } else {
                System.out.println("No course found with ID " + courseId);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
        }
    }

    public void updateCourse(int courseId, String newTitle, int newInstructorId) {
        String sql = "UPDATE courses SET title = ?, instructor_id = ? WHERE course_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newTitle);
            ps.setInt(2, newInstructorId);
            ps.setInt(3, courseId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Course updated successfully.");
            } else {
                System.out.println("No course found with ID " + courseId);
            }

        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
        }
    }
    public void getStudentsByCourseId(int courseId) {
        String sql = "SELECT s.student_id, s.name FROM students s " +
                     "JOIN enrollments e ON s.student_id = e.student_id " +
                     "WHERE e.course_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            System.out.println("Students in course ID " + courseId + ":");
            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}