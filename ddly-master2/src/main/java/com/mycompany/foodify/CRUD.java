package com.mycompany.foodify;

import java.io.*;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class CRUD {

    private static final String FILE_NAME = "dishes.txt"; // File name constant
    private LinkedList<Dish> dishList;

    public CRUD() {
        dishList = new LinkedList<>();
        readDishFromFile(); // Load dishes from the file on initialization
    }

    public boolean createNewDish(Dish dish) {
        for (Dish d : dishList) {
            if (d.getId() == dish.getId()) {
                // Dish with the same ID already exists
                return false;
            }
        }
        dishList.add(dish); // Add new dish to the list
        writeDishToFile(); // Persist changes to the file
        return true;
    }

    public LinkedList<Dish> readDish() {
        return dishList; // Return the list of dishes
    }

    public void updateDish(Dish dish) {
        boolean updated = false;
        for (Dish d : dishList) {
            if (d.getId() == dish.getId()) {
                // Update dish attributes
                d.setName(dish.getName());
                d.setCategory(dish.getCategory());
                d.setPrice(dish.getPrice());
                d.setRating(dish.getRating());
                d.setPhotoAddress(dish.getPhotoAddress());
                d.setUserId(dish.getUserId()); // Update userId if needed
                updated = true;
                break;
            }
        }
        if (updated) {
            writeDishToFile(); // Persist changes to the file
            JOptionPane.showMessageDialog(null, "Dish successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Dish not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteDish(Dish dish) {
        boolean deleted = dishList.removeIf(d -> d.getId() == dish.getId()); // Remove the matching dish
        if (deleted) {
            writeDishToFile(); // Persist changes to the file
            JOptionPane.showMessageDialog(null, "Dish successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Dish not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readDishFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Dish dish = new Dish(line); // Use the new constructor from Dish class
                dishList.add(dish);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid dish data format in file: " + e.getMessage());
        }
    }

    private void writeDishToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Dish dish : dishList) {
                writer.write(dish.toFileString()); // Use the method from Dish class for file-friendly output
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
}