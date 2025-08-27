/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Astonteen
 */
public class CheckOrderPage extends javax.swing.JFrame {
     private CRUD crud;
    private String loggedInUsername;
    private String userType;
    private String userId;
    private String userAddress;
    /**
     * Creates new form CheckOrderPage
     */
     public CheckOrderPage() {
        initComponents();
    }
     
    public CheckOrderPage(String username, String type, String userId, String userAddress) {
    this.loggedInUsername = username;
    this.userType = type;
    this.userId = userId;
    this.userAddress = userAddress;
    initComponents();
    loadOrders();
}
    private void loadOrders() {
        String[] columnNames = {"Order ID", "Customer Name", "Food Items", "Total Price", "Order Status", "Order Type", "Order Date Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        CheckOrderTable.setModel(model);

        try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 13) continue;

                String orderId = parts[0];
                String dishName = parts[1];
                String category = parts[2];
                String price = parts[3]; // This is the dish price, not total price
                String tax = parts[4];
                String tip = parts[5];
                String totalPrice = parts[7]; // Total price is here
                String customerName = parts[8];
                String customerAddress = parts[9];
                String orderStatus = parts[10];
                String orderType = parts[11];
                String orderDateTime = parts[12];

                // Filter out completed orders
                if (orderStatus.equals("Completed")) {
                    continue;
                }

                if (!customerName.equals(loggedInUsername)) {
                    continue;
                }

                StringBuilder foodItems = new StringBuilder();
                foodItems.append(dishName).append(" (").append(category).append(", $").append(price).append(")");

                // Change status to "On the Way" if it's "Picked Up"
                String displayStatus = orderStatus.equals("Picked Up") ? "On the Way" : orderStatus;

                try {
                    double totalPriceDouble = Double.parseDouble(totalPrice);
                    model.addRow(new Object[]{
                        orderId,
                        customerName,
                        foodItems.toString(),
                        String.format("%.2f", totalPriceDouble),
                        displayStatus,
                        orderType,
                        orderDateTime
                    });
                } catch (NumberFormatException e) {
                    // Handle the case where parsing fails
                    System.err.println("Error parsing total price for order " + orderId + ": " + totalPrice);
                    model.addRow(new Object[]{
                        orderId,
                        customerName,
                        foodItems.toString(),
                        "Invalid Price",
                        displayStatus,
                        orderType,
                        orderDateTime
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading orders: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
 
    private void loadCompletedOrders() {
    String[] columnNames = {"Order ID", "Customer Name", "Food Items", "Total Price", "Order Status", "Order Type", "Order Date Time"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    CheckOrderTable.setModel(model);

    try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 14) { // Adjusted for 14 fields
                System.err.println("Skipping invalid line: " + line);
                continue;
            }

            // Extract fields based on new format
            String orderId = parts[0];
            String customerName = parts[8];    // Index 8: Customer Name
            String customerAddress = parts[9]; // Index 9: Customer Address
            String orderStatus = parts[10];    // Index 10: Order Status
            String orderType = parts[11];      // Index 11: Order Type
            String orderDateTime = parts[13];  // Index 13: Completion Timestamp

            // Skip if not the logged-in user or not "Completed"
            if (!customerName.equals(loggedInUsername) || !orderStatus.equals("Completed")) {
                continue;
            }

            // Build food items
            StringBuilder foodItems = new StringBuilder();
            for (int i = 1; i <= 3; i += 4) { // Food items start at index 1 (Dish Name)
                String dishName = parts[i];
                String category = parts[i + 1];
                String price = parts[i + 2];
                foodItems.append(dishName)
                         .append(" (")
                         .append(category)
                         .append(", $")
                         .append(price)
                         .append("), ");
            }
            if (foodItems.length() > 2) {
                foodItems.setLength(foodItems.length() - 2); // Remove trailing ", "
            }

            // Parse total price (index 7)
            double totalPrice = Double.parseDouble(parts[7]);

            model.addRow(new Object[]{
                orderId,
                customerName,
                foodItems.toString(),
                String.format("%.2f", totalPrice),
                orderStatus,
                orderType,
                orderDateTime
            });
        }
    } catch (IOException | NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Error loading orders: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
  

       

      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Backbtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CheckOrderTable = new javax.swing.JTable();
        CancelOrderbtn = new javax.swing.JButton();
        OrderHistorybtn = new javax.swing.JButton();
        Reoderbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Check Order");

        Backbtn.setText("Back");
        Backbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackbtnActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/complete.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(Backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        CheckOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(CheckOrderTable);

        CancelOrderbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/cancelled.png"))); // NOI18N
        CancelOrderbtn.setText("Cancel Order");
        CancelOrderbtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CancelOrderbtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        CancelOrderbtn.setIconTextGap(12);
        CancelOrderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelOrderbtnActionPerformed(evt);
            }
        });

        OrderHistorybtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/refresh.png"))); // NOI18N
        OrderHistorybtn.setText("Order History");
        OrderHistorybtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        OrderHistorybtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        OrderHistorybtn.setIconTextGap(12);
        OrderHistorybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderHistorybtnActionPerformed(evt);
            }
        });

        Reoderbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/reorder.png"))); // NOI18N
        Reoderbtn.setText("Reorder");
        Reoderbtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Reoderbtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Reoderbtn.setIconTextGap(12);
        Reoderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReoderbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CancelOrderbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(OrderHistorybtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Reoderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CancelOrderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(OrderHistorybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Reoderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BackbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackbtnActionPerformed
        this.dispose();
        new CustomerDashboard(loggedInUsername, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_BackbtnActionPerformed

    private void CancelOrderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelOrderbtnActionPerformed
         int selectedRow = CheckOrderTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select an order to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String selectedOrderId = CheckOrderTable.getValueAt(selectedRow, 0).toString();
    File inputFile = new File("order.txt");

    try {
        // Read all lines from the file
        List<String> lines = Files.readAllLines(inputFile.toPath());

        // Write the lines back to the file, excluding the canceled order
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            for (String line : lines) {
                if (!line.trim().isEmpty() && !line.split(";")[0].equals(selectedOrderId)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error processing order cancelation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return;
    }

    // Remove the row from the table model
    DefaultTableModel model = (DefaultTableModel) CheckOrderTable.getModel();
    model.removeRow(selectedRow);

    JOptionPane.showMessageDialog(this, "Order canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_CancelOrderbtnActionPerformed

    private void ReoderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReoderbtnActionPerformed
     this.dispose();
        new ViewMenuPage(loggedInUsername, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_ReoderbtnActionPerformed

    private void OrderHistorybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderHistorybtnActionPerformed
         loadCompletedOrders();
    }//GEN-LAST:event_OrderHistorybtnActionPerformed

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
            java.util.logging.Logger.getLogger(CheckOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CheckOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CheckOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CheckOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CheckOrderPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Backbtn;
    private javax.swing.JButton CancelOrderbtn;
    private javax.swing.JTable CheckOrderTable;
    private javax.swing.JButton OrderHistorybtn;
    private javax.swing.JButton Reoderbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
