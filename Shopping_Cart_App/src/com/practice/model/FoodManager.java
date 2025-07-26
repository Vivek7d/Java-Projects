package com.practice.model;

import java.io.*;
import java.util.*;

public class FoodManager {
    private List<FoodItem> foods;
    private final String fileName = "foods.txt";

    public FoodManager() {
        this.foods = loadFoods();
    }

    public void addFood(FoodItem food) {
        foods.add(food);
        saveFoods();
    }
    public boolean updateFood(int id, FoodItem updatedFood) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getId() == id) {
                foods.set(i, updatedFood);
                saveFoods();
                return true;
            }
        }
        return false;
    }
    public List<FoodItem> searchFoodByName(String name) {
        List<FoodItem> result = new ArrayList<>();
        for (FoodItem food : foods) {
            if (food.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(food);
            }
        }
        return result;
    }

    public boolean removeFood(int id) {
        boolean removed = foods.removeIf(f -> f.getId() == id);
        if (removed) {
            saveFoods();
        }
        return removed;
    }

    public FoodItem getFoodById(int id) {
        for (FoodItem food : foods) {
            if (food.getId() == id) {
                return food;
            }
        }
        return null;
    }

    public List<FoodItem> getAllFoods() {
        return foods;
    }

    public List<FoodItem> getFoodsByCuisineAndCategory(String cuisineType, String category) {
        List<FoodItem> result = new ArrayList<>();
        for (FoodItem food : foods) {
            if (food.getCuisineType().equalsIgnoreCase(cuisineType)
                && food.getCategory().equalsIgnoreCase(category)) {
                result.add(food);
            }
        }
        return result;
    }

    private void saveFoods() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(foods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<FoodItem> loadFoods() {
        List<FoodItem> loadedFoods = new ArrayList<>();
        File file = new File(fileName);

        // If file does not exist, add default food items
        if (!file.exists()) {
            loadedFoods.add(new FoodItem(1, "Paneer Tikka", 180, 10, "Indian", "Starter"));
            loadedFoods.add(new FoodItem(2, "Samosa", 50, 5, "Indian", "Starter"));
            loadedFoods.add(new FoodItem(3, "Butter Chicken", 250, 8, "Indian", "Main Course"));
            loadedFoods.add(new FoodItem(4, "Chole Bhature", 120, 7, "Indian", "Main Course"));
            loadedFoods.add(new FoodItem(5, "Gulab Jamun", 60, 0, "Indian", "Dessert"));
            loadedFoods.add(new FoodItem(6, "Rasgulla", 70, 5, "Indian", "Dessert"));

            loadedFoods.add(new FoodItem(7, "Bruschetta", 140, 5, "Italian", "Starter"));
            loadedFoods.add(new FoodItem(8, "Garlic Bread", 120, 3, "Italian", "Starter"));
            loadedFoods.add(new FoodItem(9, "Pasta Alfredo", 220, 8, "Italian", "Main Course"));
            loadedFoods.add(new FoodItem(10, "Margherita Pizza", 260, 10, "Italian", "Main Course"));
            loadedFoods.add(new FoodItem(11, "Tiramisu", 150, 10, "Italian", "Dessert"));
            loadedFoods.add(new FoodItem(12, "Panna Cotta", 140, 7, "Italian", "Dessert"));

            loadedFoods.add(new FoodItem(13, "Spring Rolls", 120, 5, "Chinese", "Starter"));
            loadedFoods.add(new FoodItem(14, "Manchow Soup", 100, 6, "Chinese", "Starter"));
            loadedFoods.add(new FoodItem(15, "Fried Rice", 180, 7, "Chinese", "Main Course"));
            loadedFoods.add(new FoodItem(16, "Hakka Noodles", 170, 6, "Chinese", "Main Course"));
            loadedFoods.add(new FoodItem(17, "Mango Pudding", 90, 5, "Chinese", "Dessert"));
            loadedFoods.add(new FoodItem(18, "Darsaan", 110, 4, "Chinese", "Dessert"));

            System.out.println("Default food menu loaded.");
            return loadedFoods;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);

            loadedFoods = (List<FoodItem>) ois.readObject();
            System.out.println("Food list loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading food data: " + e.getMessage());
        } finally {
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loadedFoods;
    }


}
