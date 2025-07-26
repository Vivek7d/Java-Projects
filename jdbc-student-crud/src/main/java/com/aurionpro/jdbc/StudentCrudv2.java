package com.aurionpro.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Student;

public class StudentCrudv2 {

    public static void main(String[] args) {
        try {
            int newId = insertStudent("Ravi Verma", "ravi@example.com", 82.3);
            System.out.println("Inserted ID = " + newId);

            System.out.println("\nAll Students:");
            findAll().forEach(System.out::println);

            System.out.println("\nFind by id " + newId + ":");
            System.out.println(findById(newId));

            System.out.println("\nUpdate percentage to 90.5");
            updateStudent(newId, "Ravi Verma", "ravi@example.com", 90.5);

            System.out.println("\nAfter update:");
            System.out.println(findById(newId));

            System.out.println("\nDelete student:");
            deleteStudent(newId);

            System.out.println("\nAll Students after delete:");
            findAll().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- CRUD WITH PreparedStatement ----------------

    static int insertStudent(String name, String email, double percentage) throws SQLException {
        String sql = "INSERT INTO student (name, email, percentage) VALUES (?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setDouble(3, percentage);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    static List<Student> findAll() throws SQLException {
        String sql = "SELECT * FROM student";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Student> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    static Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    static boolean updateStudent(int id, String name, String email, double percentage) throws SQLException {
        String sql = "UPDATE student SET name=?, email=?, percentage=? WHERE id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setDouble(3, percentage);
            ps.setInt(4, id);

            return ps.executeUpdate() == 1;
        }
    }

    static boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private static Student map(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getDouble("percentage")
        );
    }
}
