/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;



public class VendorOrderManagement extends javax.swing.JFrame {
    private String username;
    private String userType;
    private String userId;
    private String userAddress;
    
    
    private String pickedUpTime;

     private String updatePickedUpTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        pickedUpTime = now.format(formatter);
        return pickedUpTime;
    }
     
    public VendorOrderManagement(String username, String userType, String userId, String userAddress) {
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.userAddress = userAddress;
        initComponents();
        loadOrdersFromFile("order.txt");
        updatePickupTimes();
        updatePickedUpOrders();
    }
    
    private Map<String, String> loadOrderVendorMapping() {
        Map<String, String> mapping = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("order_vendor_mapping.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    mapping.put(parts[0], parts[1]); // orderId maps to vendorId
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading order-vendor mappings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return mapping;
    }
    
private void loadOrdersFromFile(String fileName) {
    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
    model.setRowCount(0);
    Map<String, String> orderVendorMap = loadOrderVendorMapping();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 12) continue;

            String orderId = parts[0];
            String vendorIdForOrder = orderVendorMap.get(orderId);
            
            if (vendorIdForOrder == null || !vendorIdForOrder.equals(this.userId)) {
                continue;
            }

            StringBuilder foodItems = new StringBuilder();
            StringBuilder categories = new StringBuilder();
            StringBuilder prices = new StringBuilder();
            
            // Loop through parts to find all items before order details
            for (int i = 1; i + 2 < parts.length; i += 3) {
                String foodItem = cleanFoodItem(parts[i]);
                String category = cleanCategory(parts[i + 1]);
                String price = cleanPrice(parts[i + 2]);

                System.out.println("Raw: " + parts[i] + ", " + parts[i + 1] + ", " + parts[i + 2]);
                System.out.println("Cleaned: " + foodItem + ", " + category + ", " + price);

                if (isOrderStatusOrType(foodItem) || isOrderStatusOrType(category) || price.isEmpty()) {
                    break;
                }

                if (!foodItem.isEmpty() && !category.isEmpty() && !price.isEmpty()) {
                    if (foodItems.length() > 0) {
                        foodItems.append(", ");
                        categories.append(", ");
                        prices.append(", ");
                    }
                    foodItems.append(foodItem);
                    categories.append(category);
                    prices.append(price);
                }
            }
            
            // Extract other order details
            String price = formatPrice(parts[4]);
            String tips = formatPrice(parts[5]); // Tips at index 4
            String tax = formatPrice(parts[6]); // Tax at index 5
            String totalPrice = formatPrice(parts[7]); // Total Price at index 6
            String customerName = parts[8];
            String address = parts[9];
            String orderStatus = parts[10];
            String orderType = parts[11];
            String orderDateTime = parts[12];

            ensureColumnExists(model, "Order Date Time", 12);
            ensureColumnExists(model, "Pickup Time", 13); // Add the new column for Pickup Time
            ensureColumnExists(model, "Completion Time", 14);
            
            String pickupTime = orderStatus.equals("Picked Up") ? "2025-01-10 04:00:00" : "Not Picked Up";

            // Add to table model
            model.addRow(new Object[]{
                orderId,
                foodItems.toString(),
                categories.toString(),
                prices.toString(), // This should now show "3.00, 5.00" for the example
                price,
                tips,
                tax,
                totalPrice,
                customerName,
                address,
                orderStatus,
                orderType,
                orderDateTime,
                pickupTime,
                ""
            });
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "Error loading orders: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}



    // Helper method to ensure a column exists in the model
    private void ensureColumnExists(DefaultTableModel model, String columnName, int columnIndex) {
        if (model.getColumnCount() <= columnIndex) {
            model.addColumn(columnName);
        } else if (!model.getColumnName(columnIndex).equals(columnName)) {
            model.addColumn(columnName);
        }
    }


// Helper methods
private boolean isEndOfFoodItems(String[] parts, int index) {
    return index + 3 >= parts.length || 
           isOrderStatusOrType(parts[index]) || 
           isOrderStatusOrType(parts[index + 1]) || 
           (isValidMonetaryValue(parts[index + 2]) && index + 3 < parts.length && 
            (isOrderStatusOrType(parts[index + 3]) || 
             parts[index + 3].equals("") || 
             parts[index + 3].matches(".*\\d{4}-\\d{2}-\\d{2}.*"))); // Check for date format
}

private boolean isOrderStatusOrType(String value) {
    value = value.trim();
    return value.equals("Pending") ||
           value.equals("Accepted") ||
           value.equals("PickedUp") ||
           value.equals("Dinein") ||
           value.equals("Delivery");
}

private String cleanFoodItem(String foodItem) {
    return foodItem.replaceAll("^\\d+\\.?\\d*\\s*", "") // Remove leading numbers
                   .replaceAll(",\\s*$", "") // Remove trailing comma and space
                   .trim(); // Trim whitespace
}

private String cleanCategory(String category) {
    return category.replaceAll("^\\d+\\.?\\d*\\s*", "") // Remove leading numbers
                   .replaceAll(",\\s*$", "")
                   .trim();
}

private String cleanPrice(String price) {
    // Keep commas for multiple prices but remove other non-numeric characters
    String cleaned = price.replaceAll("[^0-9.,]", "");
    return cleaned;
}

private boolean isValidMonetaryValue(String value) {
    try {
        Double.parseDouble(value.trim());
        return true;
    } catch (NumberFormatException | NullPointerException e) {
        return false;
    }
}

private String formatPrice(String price) {
    try {
        return String.format("%.2f", Double.parseDouble(price.trim()));
    } catch (NumberFormatException e) {
        return "0.00";
    }
}

    private void updateOrderFile() {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        List<String> linesToKeep = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 13) {
                    String orderId = parts[0];
                    boolean matchFound = false;

                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(orderId)) {
                            String newStatus = (String) model.getValueAt(i, 10);
                            parts[10] = newStatus; // Update status

                            // Handle Picked Up/Completed timestamps
                            if (newStatus.equals("Picked Up")) {
                                parts = ensureArrayLength(parts, 14);
                                parts[13] = (String) model.getValueAt(i, 13); // Pickup Time
                            } else if (newStatus.equals("Completed")) {
                                parts = ensureArrayLength(parts, 15);
                                parts[14] = (String) model.getValueAt(i, 14); // Completion Time
                            }

                            linesToKeep.add(String.join(";", parts));
                            matchFound = true;
                            break;
                        }
                    }
                    if (!matchFound) linesToKeep.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("order.txt"))) {
            for (String updatedLine : linesToKeep) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        updatePickedUpOrders();
        updateCompletedOrders(); // New method for completed orders
    }
    
    private void updateCompletedOrders() {
    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
    try (BufferedWriter completedBw = new BufferedWriter(new FileWriter("completed_orders.txt", true))) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ("Completed".equals(model.getValueAt(i, 10))) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) { // Include all columns
                    line.append(model.getValueAt(i, j)).append(";");
                }
                line.deleteCharAt(line.length() - 1); // Remove trailing ";"
                completedBw.write(line.toString());
                completedBw.newLine();
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error updating completed orders: " + e.getMessage(), 
            "File Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

        // Helper method to extend array length if needed
        private String[] ensureArrayLength(String[] array, int minLength) {
            return array.length >= minLength ? array : Arrays.copyOf(array, minLength);
        }
    
        private void updatePickedUpOrders() {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        try (BufferedWriter pickedUpBw = new BufferedWriter(new FileWriter("picked_up_orders.txt", true))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if ("Picked Up".equals(model.getValueAt(i, 10))) {
                    StringBuilder line = new StringBuilder(model.getValueAt(i, 0).toString()).append(";");
                    for (int j = 1; j < 13; j++) {
                        line.append(model.getValueAt(i, j)).append(";");
                    }
                    line.append(model.getValueAt(i, 12)).append(";").append(model.getValueAt(i, 13)); // Append Pickup Time
                    pickedUpBw.write(line.toString());
                    pickedUpBw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating picked up orders file: " + e.getMessage(), "File Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePickupTimes() {
        try (BufferedReader br = new BufferedReader(new FileReader("picked_up_orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 14) continue; // Check if there's pickup time

                String orderId = parts[0];
                String pickupTime = parts[13]; // Pickup Time is now the 14th field due to added semicolon

                // Update the table if the order matches
                for (int i = 0; i < orderTable.getRowCount(); i++) {
                    if (orderTable.getValueAt(i, 0).equals(orderId)) {
                        orderTable.setValueAt(pickupTime, i, 13);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // If picked_up_orders.txt does not exist, it's not an error, just ignore
        }
    }
    
    private String updateCompletionTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
     


  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        AcceptBtn = new javax.swing.JButton();
        CancelBtn = new javax.swing.JButton();
        PickedUpBtn = new javax.swing.JButton();
        dineInBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        BackBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Food", "Category", "Price", "Delivery Fee (RM5)", "Tips", "Tax", "Total Price", "Customer Name", "Address", "OrderStatus", "OrderType", "Order Date Time"
            }
        ));
        orderTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(orderTable);
        orderTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        AcceptBtn.setBackground(new java.awt.Color(51, 255, 0));
        AcceptBtn.setText("Accept Order");
        AcceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcceptBtnActionPerformed(evt);
            }
        });

        CancelBtn.setBackground(new java.awt.Color(255, 51, 51));
        CancelBtn.setText("Cancel Order");
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtnActionPerformed(evt);
            }
        });

        PickedUpBtn.setText("Picked Up");
        PickedUpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PickedUpBtnActionPerformed(evt);
            }
        });

        dineInBtn.setText("Dine in");
        dineInBtn.setPreferredSize(new java.awt.Dimension(83, 23));
        dineInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dineInBtnActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(60, 60, 60));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Vendor/street-food.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Lucida Console", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Vendor Order Management");

        BackBtn.setText("Back");
        BackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(BackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(AcceptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PickedUpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dineInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AcceptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PickedUpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dineInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AcceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcceptBtnActionPerformed
        int selectedRow = orderTable.getSelectedRow();
    
    if (selectedRow != -1) {
        String currentStatus = (String) orderTable.getValueAt(selectedRow, 10);
        
        if (currentStatus.equals("Pending")) {
            orderTable.setValueAt("Preparing", selectedRow, 10);
            updateOrderFile();
            JOptionPane.showMessageDialog(this, "Order accepted successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                "Only pending orders can be accepted.",
                "Invalid Action",
                JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this,
            "Please select an order to accept.",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_AcceptBtnActionPerformed

    private void CancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelBtnActionPerformed
   int selectedRow = orderTable.getSelectedRow();
        
        if (selectedRow != -1) {
            String currentStatus = (String) orderTable.getValueAt(selectedRow, 10);
            
            if (currentStatus.equals("Pending")) {
                orderTable.setValueAt("Cancelled", selectedRow, 10);
                updateOrderFile();
                JOptionPane.showMessageDialog(this, "Order cancelled successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Only pending orders can be cancelled.",
                    "Invalid Action",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select an order to cancel.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_CancelBtnActionPerformed

    private void BackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtnActionPerformed
     this.dispose();
     new VendorMainPage(username, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_BackBtnActionPerformed

    private void PickedUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PickedUpBtnActionPerformed
        int selectedRow = orderTable.getSelectedRow();

        if (selectedRow != -1) {
            // Check if Order ID starts with "ORDER-"
            Object orderIdObj = orderTable.getValueAt(selectedRow, 0);
            String orderId = orderIdObj.toString();
            if (orderId.startsWith("ORDER-")) {
                JOptionPane.showMessageDialog(this,
                    "Use the 'Dine In' button for orders starting with ORDER-.",
                    "Invalid Action",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object statusObj = orderTable.getValueAt(selectedRow, 10); // OrderStatus at index 10

            if (statusObj != null) {
                String currentStatus = statusObj.toString();

                if (currentStatus.equals("Runner Assigned")) {
                    orderTable.setValueAt("Picked Up", selectedRow, 10);
                    orderTable.setValueAt(updatePickedUpTime(), selectedRow, 13);
                    updateOrderFile();
                    JOptionPane.showMessageDialog(this, "Order marked as picked up successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Only orders with 'Preparing' or 'Runner Assigned' status can be picked up.",
                        "Invalid Action",
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid data for selected order.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select an order to mark as picked up.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }



    }//GEN-LAST:event_PickedUpBtnActionPerformed

    private void dineInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dineInBtnActionPerformed
        int selectedRow = orderTable.getSelectedRow();

        if (selectedRow != -1) {
            Object orderIdObj = orderTable.getValueAt(selectedRow, 0);
            Object statusObj = orderTable.getValueAt(selectedRow, 10); // Status column

            if (orderIdObj != null && statusObj != null) {
                String orderId = orderIdObj.toString();
                String currentStatus = statusObj.toString();

                // Validate Order ID starts with "ORDER-" and status is "Preparing"
                if (orderId.startsWith("ORDER-")) {
                    if (currentStatus.equals("Preparing")) {
                        // Update status to "Completed" and set completion time
                        orderTable.setValueAt("Completed", selectedRow, 10);
                        orderTable.setValueAt(updateCompletionTime(), selectedRow, 14); // Time at column 14
                        updateOrderFile();
                        JOptionPane.showMessageDialog(this, "Dine-in order marked as completed!");
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Dine-in orders can only be completed from 'Preparing' status.",
                            "Invalid Action",
                            JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "This button is only for orders starting with ORDER-.",
                        "Invalid Action",
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid data for selected order.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a dine-in order to mark as completed.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_dineInBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendorOrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendorOrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendorOrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendorOrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AcceptBtn;
    private javax.swing.JButton BackBtn;
    private javax.swing.JButton CancelBtn;
    private javax.swing.JButton PickedUpBtn;
    private javax.swing.JButton dineInBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderTable;
    // End of variables declaration//GEN-END:variables
}
