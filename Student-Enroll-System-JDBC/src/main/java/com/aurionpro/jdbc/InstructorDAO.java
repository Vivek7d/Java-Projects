package com.aurionpro.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aurionpro.model.Instructor;

public class InstructorDAO {
	 public void addInstructorToDepartment(Instructor instructor) {
	        String sql = "INSERT INTO instructors (name, dept_id) VALUES (?, ?)";

	        try (Connection conn = DbUtil.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setString(1, instructor.getName());
	            ps.setInt(2, instructor.getDeptId());

	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Instructor added successfully.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error adding instructor: " + e.getMessage());
	        }
	    }

	public void updateInstructor(int id, String name, int deptId) {
		String sql = "UPDATE instructors SET name = ?, dept_id = ? WHERE instructor_id = ?";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, name);
			ps.setInt(2, deptId);
			ps.setInt(3, id);

			int rows = ps.executeUpdate();
			if (rows > 0) {
				System.out.println("Instructor updated successfully.");
			} else {
				System.out.println("No instructor found with ID " + id);
			}
		} catch (SQLException e) {
			System.err.println("Error updating instructor: " + e.getMessage());
		}
	}

	public void deleteInstructor(int id) {
		String sql = "DELETE FROM instructors WHERE instructor_id = ?";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);

			int rows = ps.executeUpdate();
			if (rows > 0) {
				System.out.println("Instructor deleted successfully.");
			} else {
				System.out.println("No instructor found with ID " + id);
			}
		} catch (SQLException e) {
			System.err.println("Error deleting instructor: " + e.getMessage());
		}
	}

	public void viewAllInstructors() {
		String sql = "SELECT i.instructor_id, i.name, d.name AS department_name "
				+ "FROM instructors i LEFT JOIN departments d ON i.dept_id = d.dept_id";

		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			System.out.println("\n--- All Instructors ---");
			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("instructor_id") + ", Name: " + rs.getString("name")
						+ ", Department: " + rs.getString("department_name"));
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving instructors: " + e.getMessage());
		}
	}
}