package com.practice.model.jdbc;

import com.practice.model.FoodItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDao {

	public List<FoodItem> searchByNameOrCuisine(String searchTerm) throws SQLException {
		List<FoodItem> foodList = new ArrayList<>();

		String sql = "SELECT * FROM food_items WHERE (name LIKE ? OR cuisine_type LIKE ?) AND is_deleted = false ORDER BY cuisine_type, name";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String queryParam = "%" + searchTerm + "%";
			pstmt.setString(1, queryParam);
			pstmt.setString(2, queryParam);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					foodList.add(mapRowToFoodItem(rs));
				}
			}
		}
		return foodList;
	}

	public FoodItem getFoodItemById(int id) throws SQLException {
		String sql = "SELECT * FROM food_items WHERE food_id = ? AND is_deleted = false";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapRowToFoodItem(rs);
				}
			}
		}
		return null;
	}

	public boolean updateFoodItem(FoodItem foodItem) throws SQLException {
		String sql = "UPDATE food_items SET name = ?, price = ?, discount = ?, cuisine_type = ?, category = ? WHERE food_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, foodItem.getName());
			pstmt.setDouble(2, foodItem.getPrice());
			pstmt.setDouble(3, foodItem.getDiscount());
			pstmt.setString(4, foodItem.getCuisineType());
			pstmt.setString(5, foodItem.getCategory());
			pstmt.setInt(6, foodItem.getId());

			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public List<FoodItem> getAllActiveFoodItems() throws SQLException {
		List<FoodItem> foodList = new ArrayList<>();

		String sql = "SELECT * FROM food_items WHERE is_deleted = false ORDER BY food_id";
		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				foodList.add(mapRowToFoodItem(rs));
			}
		}
		return foodList;
	}

	private FoodItem mapRowToFoodItem(ResultSet rs) throws SQLException {
		int id = rs.getInt("food_id");
		String name = rs.getString("name");
		double price = rs.getDouble("price");
		double discount = rs.getDouble("discount");
		String cuisineType = rs.getString("cuisine_type");
		String category = rs.getString("category");

		FoodItem foodItem = new FoodItem(name, price, discount, cuisineType, category);
		foodItem.setId(id);
		foodItem.setDeleted(rs.getBoolean("is_deleted"));
		return foodItem;
	}

	public FoodItem addFoodItem(FoodItem foodItem) throws SQLException {
		String sql = "INSERT INTO food_items (name, price, discount, cuisine_type, category) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, foodItem.getName());
			pstmt.setDouble(2, foodItem.getPrice());
			pstmt.setDouble(3, foodItem.getDiscount());
			pstmt.setString(4, foodItem.getCuisineType());
			pstmt.setString(5, foodItem.getCategory());
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						foodItem.setId(generatedKeys.getInt(1));
						return foodItem;
					}
				}
			}
		}
		return null;
	}

	public boolean existsByName(String name) throws SQLException {
		String sql = "SELECT COUNT(*) FROM food_items WHERE name = ? AND is_deleted = false";
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

	public boolean softDeleteFoodItem(int id) throws SQLException {
		String sql = "UPDATE food_items SET is_deleted = true WHERE food_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public boolean hardDeleteFoodItem(int id) throws SQLException {
		String sql = "DELETE FROM food_items WHERE food_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public boolean reactivateFoodItem(int id) throws SQLException {
		String sql = "UPDATE food_items SET is_deleted = false WHERE food_id = ?";
		try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}

	public List<FoodItem> getAllInactiveFoodItems() throws SQLException {
		List<FoodItem> foodList = new ArrayList<>();
		String sql = "SELECT * FROM food_items WHERE is_deleted = true ORDER BY food_id";
		try (Connection conn = DbUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				foodList.add(mapRowToFoodItem(rs));
			}
		}
		return foodList;
	}

}