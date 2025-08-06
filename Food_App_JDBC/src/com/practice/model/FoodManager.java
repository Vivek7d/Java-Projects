package com.practice.model;

import com.practice.model.jdbc.FoodItemDao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class FoodManager {

	private final FoodItemDao foodItemDao;

	public FoodManager() {
		this.foodItemDao = new FoodItemDao();
	}

	public boolean updateFood(FoodItem foodItem) {
		try {
			return foodItemDao.updateFoodItem(foodItem);
		} catch (SQLException e) {
			System.err.println("Database error while updating item: " + e.getMessage());
			return false;
		}
	}

	public List<FoodItem> searchItems(String searchTerm) {
		try {

			return foodItemDao.searchByNameOrCuisine(searchTerm);
		} catch (SQLException e) {
			System.err.println("Database error during search: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public FoodItem getFoodById(int id) {
		try {
			return foodItemDao.getFoodItemById(id);
		} catch (SQLException e) {
			System.err.println("Database error while fetching food by ID: " + e.getMessage());
			return null;
		}
	}

	public List<FoodItem> getAllFoods() {
		try {
			return foodItemDao.getAllActiveFoodItems();
		} catch (SQLException e) {
			System.err.println("Database error while fetching menu: " + e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public FoodItem addFood(FoodItem food) {
		try {
			return foodItemDao.addFoodItem(food);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean existsByName(String name) {
		try {
			return foodItemDao.existsByName(name);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean softDeleteFood(int id) {
		try {
			return foodItemDao.softDeleteFoodItem(id);
		} catch (SQLException e) {
			System.err.println("Database error during soft delete: " + e.getMessage());
			return false;
		}
	}

	public boolean hardDeleteFood(int id) {
		try {
			return foodItemDao.hardDeleteFoodItem(id);
		} catch (SQLException e) {
			System.err.println("Database error during hard delete: " + e.getMessage());
			return false;
		}
	}

	public boolean reactivateFood(int id) {
		try {
			return foodItemDao.reactivateFoodItem(id);
		} catch (SQLException e) {
			System.err.println("Database error during reactivation: " + e.getMessage());
			return false;
		}
	}

	public List<FoodItem> getAllInactiveFoods() {
		try {
			return foodItemDao.getAllInactiveFoodItems();
		} catch (SQLException e) {
			System.err.println("Database error fetching inactive items: " + e.getMessage());
			return Collections.emptyList();
		}
	}
}