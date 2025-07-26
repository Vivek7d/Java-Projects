package com.aurionpro.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Student;

public class StudentCrudv1 {

	public static void main(String[] args) {
		try {
//			int newId = insertStudent("Ravi Verma", "ravi@example.com", 82.3);
//			System.out.println("Inserted ID = " + newId);

//			System.out.println("\nAll Students:");
//			findAll().forEach(System.out::println);
//
//			System.out.println("\nFind by id " + 3 + ":");
//			System.out.println(findById(3));

//			 System.out.println("\nUpdate percentage to 90.5");
//			 updateStudent(3, "Vivek Dhalkari", "vivek@example.com", 90.11);
//
//			 System.out.println("\nAfter update:");
//			 System.out.println(findById(3));

			System.out.println("\nDelete student:");
			deleteStudent(5);

			System.out.println("\nAll Students after delete:");
			findAll().forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int insertStudent(String name, String email, double percentage) throws SQLException {
		String sql = "INSERT INTO student (name, email, percentage) VALUES (?, ?, ?)";
		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.setDouble(3, percentage);

			pstmt.executeUpdate();

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1;
	}

	static List<Student> findAll() throws SQLException {
		String sql = "SELECT * FROM student";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Student> list = new ArrayList<>();

		try {
			con = DbUtil.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				Student student = new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
						rs.getDouble("percentage"));
				list.add(student);
			}
		} finally {
			rs.close();
			st.close();
			con.close();
		}

		return list;
	}

	static Student findById(int id) throws SQLException {
		String sql = "SELECT * FROM student WHERE id=" + id;
		try (Connection con = DbUtil.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			return rs.next() ? map(rs) : null;
		}
	}

	private static Student map(ResultSet rs) throws SQLException {
		return new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getDouble("percentage"));
	}

	static boolean updateStudent(int id, String name, String email, double percentage) throws SQLException {
		String sql = String.format("UPDATE student SET name='%s', email='%s', percentage=%s WHERE id=%d", escape(name),
				escape(email), percentage, id);
		try (Connection con = DbUtil.getConnection(); Statement st = con.createStatement()) {
			return st.executeUpdate(sql) == 1;
		}
	}

	static boolean deleteStudent(int id) throws SQLException {
		String sql = "DELETE FROM student WHERE id=" + id;
		try (Connection con = DbUtil.getConnection(); Statement st = con.createStatement()) {
			return st.executeUpdate(sql) == 1;
		}
	}

	private static String escape(String s) {
		return s.replace("'", "''");
	}
}
