package com.practice.model.jdbc;

import com.practice.model.delivery.DeliveryPartner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryPartnerDao {

	public DeliveryPartner addPartner(DeliveryPartner partner) throws SQLException {

		String sql = "INSERT INTO delivery_partners (name, contact_number, is_deleted) VALUES (?, ?, ?)";
		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, partner.getName());
			pstmt.setString(2, partner.getContactNumber());
			pstmt.setBoolean(3, partner.isDeleted());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						partner.setId(generatedKeys.getInt(1));
						return partner;
					}
				}
			}
		}
		return null;
	}

	public List<DeliveryPartner> getAllPartners() throws SQLException {
		List<DeliveryPartner> partners = new ArrayList<>();
		String sql = "SELECT * FROM delivery_partners ORDER BY partner_id";
		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				partners.add(mapRowToPartner(rs));
			}
		}
		return partners;
	}

	public boolean setPartnerStatus(int partnerId, boolean isDeleted) throws SQLException {
		String sql = "UPDATE delivery_partners SET is_deleted = ? WHERE partner_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setBoolean(1, isDeleted);
			pstmt.setInt(2, partnerId);

			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public boolean removePartnerById(int id) throws SQLException {
		String sql = "DELETE FROM delivery_partners WHERE partner_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public boolean existsByName(String name) throws SQLException {
		String sql = "SELECT COUNT(*) FROM delivery_partners WHERE name = ?";
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

	private DeliveryPartner mapRowToPartner(ResultSet rs) throws SQLException {
		int id = rs.getInt("partner_id");
		String name = rs.getString("name");
		String contact = rs.getString("contact_number");
		boolean isDeleted = rs.getBoolean("is_deleted");

		return new DeliveryPartner(id, name, contact, isDeleted);
	}
}