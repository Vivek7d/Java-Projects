package com.practice.model.jdbc;

import com.practice.model.payment.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDao {

	public PaymentMethod addMethod(PaymentMethod method) throws SQLException {
		String sql = "INSERT INTO payment_methods (method_name, is_deleted) VALUES (?, ?)";
		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, method.getMethodName());
			pstmt.setBoolean(2, method.isDeleted());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						method.setId(generatedKeys.getInt(1));
						return method;
					}
				}
			}
		}
		return null;
	}

	public List<PaymentMethod> getAllMethods() throws SQLException {
		List<PaymentMethod> methods = new ArrayList<>();
		String sql = "SELECT * FROM payment_methods ORDER BY id";
		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				methods.add(mapRowToMethod(rs));
			}
		}
		return methods;
	}

	public boolean setMethodStatus(int methodId, boolean isDeleted) throws SQLException {
		String sql = "UPDATE payment_methods SET is_deleted = ? WHERE id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setBoolean(1, isDeleted);
			pstmt.setInt(2, methodId);

			return pstmt.executeUpdate() > 0;
		}
	}

	public boolean removeMethodById(int id) throws SQLException {
		String sql = "DELETE FROM payment_methods WHERE id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		}
	}

	public boolean existsByName(String name) throws SQLException {
		String sql = "SELECT COUNT(*) FROM payment_methods WHERE method_name = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	private PaymentMethod mapRowToMethod(ResultSet rs) throws SQLException {
		return new PaymentMethod(rs.getInt("id"), rs.getString("method_name"), rs.getBoolean("is_deleted"));
	}
}