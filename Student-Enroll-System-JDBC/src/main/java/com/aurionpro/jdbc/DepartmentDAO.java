package com.aurionpro.jdbc;

import java.sql.*;

import com.aurionpro.model.Department;

public class DepartmentDAO {

    public void addDepartment(Department dept) {
        String sql = "INSERT INTO departments (name) VALUES (?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept.getName());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Department added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding department: " + e.getMessage());
        }
    }

    public void updateDepartment(int id, String newName) {
        String sql = "UPDATE departments SET name = ? WHERE dept_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Department updated successfully.");
            } else {
                System.out.println("No department found with ID " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
        }
    }

    public void deleteDepartment(int id) {
        String sql = "DELETE FROM departments WHERE dept_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Department deleted successfully.");
            } else {
                System.out.println("No department found with ID " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
        }
    }

    public void viewAllDepartments() {
        String sql = "SELECT * FROM departments";

        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DEPARTMENTS LIST ---");
            while (rs.next()) {
                int id = rs.getInt("dept_id");
                String name = rs.getString("name");

                System.out.println("ID: " + id + ", Name: " + name);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving departments: " + e.getMessage());
        }
    }
}
