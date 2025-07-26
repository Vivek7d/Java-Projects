package com.practice.model;

import java.io.*;
import java.util.*;

public class FoodManager {
    private List<FoodItem> foods;
    private List<FoodItem> inactiveFoods;
    private final String activeFile = "foods.txt";
    private final String inactiveFile = "inactive_foods.txt";

    public FoodManager() {
        this.foods = loadFoods(activeFile);
        this.inactiveFoods = loadFoods(inactiveFile);
    }

    public List<FoodItem> getFoods() {
        return foods;
    }

    public List<FoodItem> getInactiveFoods() {
        return inactiveFoods;
    }

    public List<FoodItem> getAllFoods() {
        return new ArrayList<>(foods);
    }

    public List<FoodItem> getAllInactiveFoods() {
        return new ArrayList<>(inactiveFoods);
    }

    
    public boolean addFood(FoodItem food) {
        for (FoodItem item : foods) {
            if (item.getId() == food.getId()) return false; // duplicate
        }
        food.setDeleted(false);
        foods.add(food);
        saveFoods(foods, activeFile);
        return true;
    }

    
    public boolean removeFood(int id) {
        Iterator<FoodItem> iterator = foods.iterator();
        while (iterator.hasNext()) {
            FoodItem item = iterator.next();
            if (item.getId() == id) {
                item.setDeleted(true);
                iterator.remove();
                inactiveFoods.add(item);
                saveFoods(foods, activeFile);
                saveFoods(inactiveFoods, inactiveFile);
                return true;
            }
        }
        return false;
    }

   
    public boolean reactivateFood(int id) {
        Iterator<FoodItem> iterator = inactiveFoods.iterator();
        while (iterator.hasNext()) {
            FoodItem item = iterator.next();
            if (item.getId() == id) {
                item.setDeleted(false);
                iterator.remove();
                foods.add(item);
                saveFoods(foods, activeFile);
                saveFoods(inactiveFoods, inactiveFile);
                return true;
            }
        }
        return false;
    }

   
    public boolean hardDeleteFood(int id) {
        Iterator<FoodItem> iterator = foods.iterator();
        while (iterator.hasNext()) {
            FoodItem item = iterator.next();
            if (item.getId() == id) {
                iterator.remove();
                saveFoods(foods, activeFile);
                System.out.println("✅ Permanently deleted from active: " + item.getName());
                return true;
            }
        }

        Iterator<FoodItem> inactiveIterator = inactiveFoods.iterator();
        while (inactiveIterator.hasNext()) {
            FoodItem item = inactiveIterator.next();
            if (item.getId() == id) {
                inactiveIterator.remove();
                saveFoods(inactiveFoods, inactiveFile);
                System.out.println("✅ Permanently deleted from inactive: " + item.getName());
                return true;
            }
        }

        System.out.println("❌ Food item not found.");
        return false;
    }

    
    private void saveFoods(List<FoodItem> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private List<FoodItem> loadFoods(String filename) {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<FoodItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    public void saveAll() {
        saveFoods(foods, activeFile);
        saveFoods(inactiveFoods, inactiveFile);
    }

    
    public void loadDefaultFoods() {
        if (!foods.isEmpty()) return;

        List<FoodItem> defaults = Arrays.asList(
            new FoodItem(1, "Paneer Tikka", 180, 10, "Indian", "Starter"),
            new FoodItem(2, "Veg Biryani", 220, 15, "Indian", "Main Course"),
            new FoodItem(3, "Chicken Wings", 250, 8, "Continental", "Starter"),
            new FoodItem(4, "Margherita Pizza", 300, 12, "Italian", "Main Course"),
            new FoodItem(5, "Gulab Jamun", 90, 20, "Indian", "Dessert"),
            new FoodItem(6, "Cheesecake", 150, 6, "Continental", "Dessert"),
            new FoodItem(7, "Sushi Roll", 400, 5, "Japanese", "Main Course"),
            new FoodItem(8, "Spring Rolls", 120, 18, "Chinese", "Starter"),
            new FoodItem(9, "Tandoori Roti", 20, 30, "Indian", "Bread"),
            new FoodItem(10, "Butter Chicken", 260, 10, "Indian", "Main Course")
        );

        for (FoodItem item : defaults) {
            item.setDeleted(false);
        }

        foods.addAll(defaults);
        saveFoods(foods, activeFile);
    }
    public boolean existsById(int id) {
        return foods.stream().anyMatch(f -> f.getId() == id)
                || inactiveFoods.stream().anyMatch(f -> f.getId() == id);
    }

    public boolean existsByName(String name) {
        return foods.stream().anyMatch(f -> f.getName().equalsIgnoreCase(name))
                || inactiveFoods.stream().anyMatch(f -> f.getName().equalsIgnoreCase(name));
    }
    public boolean updateFood(int id, FoodItem updatedFood) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getId() == id) {
                foods.set(i, updatedFood);
                saveAll();
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

    public FoodItem getFoodById(int id) {
        for (FoodItem food : foods) {
            if (food.getId() == id) {
                return food;
            }
        }
        return null;
    }
}
