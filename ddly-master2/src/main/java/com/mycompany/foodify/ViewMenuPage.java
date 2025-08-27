/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mycompany.foodify.CRUD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Astonteen
 */
public class ViewMenuPage extends javax.swing.JFrame {
    private Map<String, String> vendors = new HashMap<>();  // Map of vendorId to vendorName
    private LinkedList<Dish> allDishes = new LinkedList<>();
    private LinkedList<Dish> currentDishes = new LinkedList<>();
    private CRUD crud;
    private String loggedInUsername;
    private String userType;
    private String userId;
    private String userAddress;

    
    public ViewMenuPage(String username, String type, String userId, String userAddress) {
    this.crud = new CRUD();
    this.loggedInUsername = username;
    this.userType = type;
    this.userId = userId;
    this.userAddress = userAddress;
    initComponents();
    menuTable = new JScrollPane();
    add(menuTable);
    loadVendors();
    loadDishes();
    initTable();
    
}
    
    private void loadVendors() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 3 && "Vendor".equals(parts[3])) {
                    vendors.put(parts[0], parts[1]);  // Mapping ID to Name
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading vendors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadDishes() {
    try (BufferedReader br = new BufferedReader(new FileReader("dishes.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 7) {
                // Convert string ID to int
                int dishId = Integer.parseInt(parts[0]);
                allDishes.add(new Dish(
                    dishId, // Now using an int for the ID
                    parts[1],
                    parts[2],
                    Double.parseDouble(parts[3]),
                    Double.parseDouble(parts[4]),
                    parts[5],
                    parts[6] // Vendor ID as String
                ));
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error parsing dish ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading dishes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


private void initTable() {
    jComboBox1.removeAllItems();
    jComboBox1.addItem("All Vendors");
    vendors.values().forEach(vendorName -> jComboBox1.addItem(vendorName));

    // Set the column names
    String[] columnNames = {"Dish Id", "Dish Name", "Category", "Price", "Rating", "Photo"};
    Object[][] data = new Object[allDishes.size()][6];

    populateTableData(allDishes, data);

    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return ImageIcon.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    MenuTable.setModel(tableModel);
    MenuTable.setRowHeight(50);
    MenuTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    revalidate();
    repaint();
}

private void populateTableData(LinkedList<Dish> dishList, Object[][] data) {
    int row = 0;
    for (Dish dish : dishList) {
        data[row][0] = dish.getId();
        data[row][1] = dish.getName();
        data[row][2] = dish.getCategory();
        data[row][3] = dish.getPrice();
        data[row][4] = dish.getRating();
        try {
            data[row][5] = new ImageIcon(new ImageIcon(dish.getPhotoAddress())
                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            data[row][5] = null; // Handle invalid photo addresses
        }
        row++;
    }
}


    public void preSelectItems(List<String> dishNames) {
        for (int i = 0; i < MenuTable.getRowCount(); i++) {
            String dishName = MenuTable.getValueAt(i, 1).toString();
            if (dishNames.contains(dishName)) {
                MenuTable.addRowSelectionInterval(i, i);
            }
        }
    }
    
    public void setOrderType(String type) {
        if ("Delivery".equalsIgnoreCase(type)) {
            Deliverybtn.setSelected(true);
        } else {
            Dineinbtn.setSelected(true);
        }
    }
    
    
    public void refreshMenu(){
        initTable();
    }
    
    private double calculateTotalPrice(int[] selectedRows, boolean isDelivery) {
        double totalPrice = 0;
        for (int row : selectedRows) {
            totalPrice += Double.parseDouble(MenuTable.getValueAt(row, 3).toString());
        }
        if (isDelivery) {
            totalPrice += 5.0 * selectedRows.length; // Delivery fee calculation (assuming $5 per item)
        }
        if (Tipsbtn.isSelected()) {
            totalPrice *= 1.05; // Add 5% tip
        }
        return totalPrice;
    }

    private double calculateTax(double price) {
            // Here you can implement variable tax rates if needed
            // For now, we'll keep the 10% tax but apply it separately
            return price * 0.10;
        }
    
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Backbtn = new javax.swing.JButton();
        Dineinbtn = new javax.swing.JButton();
        Deliverybtn = new javax.swing.JButton();
        Tipsbtn = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        menuTable = new javax.swing.JScrollPane();
        MenuTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        jLabel1.setText("View Menu");

        Backbtn.setText("Back ");
        Backbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackbtnActionPerformed(evt);
            }
        });

        Dineinbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Dineinbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/dining-room.png"))); // NOI18N
        Dineinbtn.setText("Dine in");
        Dineinbtn.setIconTextGap(12);
        Dineinbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DineinbtnActionPerformed(evt);
            }
        });

        Deliverybtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Deliverybtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/take-away.png"))); // NOI18N
        Deliverybtn.setText("Delivery");
        Deliverybtn.setIconTextGap(12);
        Deliverybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeliverybtnActionPerformed(evt);
            }
        });

        Tipsbtn.setBackground(new java.awt.Color(204, 204, 204));
        Tipsbtn.setText("Tips  5%");
        Tipsbtn.setIconTextGap(10);
        Tipsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipsbtnActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Vendor" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/dish.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Deliverybtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Dineinbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Tipsbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel2)))
                        .addGap(59, 59, 59))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122)
                .addComponent(Dineinbtn)
                .addGap(18, 18, 18)
                .addComponent(Deliverybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Tipsbtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        MenuTable.setModel(new javax.swing.table.DefaultTableModel(
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
        menuTable.setViewportView(MenuTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuTable, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuTable, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BackbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackbtnActionPerformed
        this.dispose();
        new CustomerDashboard(loggedInUsername, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_BackbtnActionPerformed

    private void DineinbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DineinbtnActionPerformed
        int[] selectedRows = MenuTable.getSelectedRows();

    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(this, "Please select at least one dish to place an order.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String customerName = loggedInUsername != null ? loggedInUsername : "Unknown User";

    double subTotal = calculateTotalPrice(selectedRows, false); // Pass false for dine-in
    double tax = calculateTax(subTotal);
    double totalPrice = subTotal + tax;

    if (!deductFromWallet(totalPrice)) {
        return;
    }

    String orderId = generateUniqueOrderId();
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String orderDateTime = now.format(formatter);

    // Use currentDishes instead of crud.readDish()
    String orderDetails = formatOrderDetails(orderId, selectedRows, subTotal, 0.0, tax, totalPrice, customerName, "Dinein", " ", orderDateTime);

    try {
        writeOrderToFile(orderDetails);
        String[] vendorIds = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            int row = selectedRows[i];
            Dish dish = currentDishes.get(row);
            vendorIds[i] = dish.getUserId();
        }

        writeToOrderVendorMapping(orderId, vendorIds);
        
        JOptionPane.showMessageDialog(this, 
            "Dine-in order placed successfully. \nOrder ID: " + orderId + 
            "\nSubtotal: $" + String.format("%.2f", subTotal) +
            "\nTax: $" + String.format("%.2f", tax) +
            "\nTotal Price: $" + String.format("%.2f", totalPrice) +
            "\nOrder Time: " + orderDateTime, 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error placing Dine-in order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    private String generateUniqueOrderId() {
        File idFile = new File("order_id.txt");
        int orderId = 1;

        try {
            if (idFile.exists()) {
                List<String> lines = Files.readAllLines(idFile.toPath());
                if (!lines.isEmpty()) {
                    orderId = Integer.parseInt(lines.get(0)) + 1;
                }
            }
            Files.write(idFile.toPath(), Collections.singletonList(String.valueOf(orderId)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating Order ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return String.format("ORDER-%05d", orderId);
    
                
    }//GEN-LAST:event_DineinbtnActionPerformed

    private void DeliverybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeliverybtnActionPerformed
        int[] selectedRows = MenuTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one dish to place a delivery order.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String customerName = loggedInUsername != null ? loggedInUsername : "Unknown User";
        String customerAddress = userAddress != null ? userAddress : "Unknown Address";

        double subTotal = calculateTotalPrice(selectedRows, true); // true for delivery
        double tax = calculateTax(subTotal);
        double totalPrice = subTotal + tax;

        if (!deductFromWallet(totalPrice)) {
            return;
        }

        String orderId = generateUniqueDeliveryOrderId();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String orderDateTime = now.format(formatter);

        // Format order details using currentDishes
        String orderDetails = formatOrderDetails(orderId, selectedRows, subTotal, 0.0, tax, totalPrice, customerName, "Delivery", customerAddress, orderDateTime);

        try {
            writeOrderToFile(orderDetails);
            String[] vendorIds = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                int row = selectedRows[i];
                Dish dish = currentDishes.get(row); // Use currentDishes instead of CRUD
                vendorIds[i] = dish.getUserId();
            }

            writeToOrderVendorMapping(orderId, vendorIds);

            JOptionPane.showMessageDialog(this, 
                "Delivery order placed successfully. \nOrder ID: " + orderId +
                "\nSubtotal (including delivery): $" + String.format("%.2f", subTotal) +
                "\nTax: $" + String.format("%.2f", tax) +
                "\nTotal Price: $" + String.format("%.2f", totalPrice) +
                "\nOrder Time: " + orderDateTime, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error placing Delivery order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private String generateUniqueDeliveryOrderId() {
        File idFile = new File("delivery_order_id.txt");
        int orderId = 1;

        try {
            if (idFile.exists()) {
                List<String> lines = Files.readAllLines(idFile.toPath());
                if (!lines.isEmpty()) {
                    orderId = Integer.parseInt(lines.get(0)) + 1;
                }
            }
            Files.write(idFile.toPath(), Collections.singletonList(String.valueOf(orderId)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating Delivery Order ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return String.format("DELIVERY-%05d", orderId);
    }

    private String formatOrderDetails(String orderId, int[] selectedRows, double subTotal, double deliveryFee, double tax, double totalPrice, String customerName, String orderType, String customerAddress, String orderDateTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append(";"); // Order ID

        StringBuilder names = new StringBuilder();
        StringBuilder categories = new StringBuilder();
        StringBuilder prices = new StringBuilder();
        StringBuilder vendorIds = new StringBuilder(); // For vendor IDs

        for (int i = 0; i < selectedRows.length; i++) {
            int row = selectedRows[i];
            Dish selectedDish = currentDishes.get(row); // Use currentDishes

            names.append(selectedDish.getName());
            categories.append(selectedDish.getCategory());
            prices.append(String.format("%.2f", selectedDish.getPrice()));
            if (i > 0) {
                vendorIds.append(","); // Add comma for multiple vendors
            }
            vendorIds.append(selectedDish.getUserId()); // Append the vendor's ID from the dish

            if (i < selectedRows.length - 1) {
                names.append(", ");
                categories.append(", ");
                prices.append(", ");
            }
        }

        sb.append(names.toString()).append(";")
          .append(categories.toString()).append(";")
          .append(prices.toString()).append(";")
          .append(String.format("%.2f", subTotal)).append(";")
          .append(String.format("%.2f", tax)).append(";")
          .append(String.format("%.2f", Tipsbtn.isSelected() ? subTotal * 0.05 : 0.0)).append(";")
          .append(String.format("%.2f", totalPrice)).append(";")
          .append(customerName).append(";")
          .append(orderType.equals("Delivery") ? customerAddress : " ").append(";")
          .append("Pending").append(";")
          .append(orderType).append(";")
          .append(orderDateTime);

        return sb.toString();
    }

    private void writeOrderToFile(String orderDetails) throws IOException {
        File file = new File("order.txt");
        boolean append = file.exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
            if (append) bw.newLine();
            bw.write(orderDetails);
        }
    
    }//GEN-LAST:event_DeliverybtnActionPerformed

    private void TipsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipsbtnActionPerformed

    // Get the selected rows from the MenuTable
    int[] selectedRows = MenuTable.getSelectedRows();

    // Check if any row is selected
    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(this, "Please select at least one dish to calculate tips.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Calculate the total price of the selected items
    double totalPrice = 0;
    for (int selectedRow : selectedRows) {
        double price = Double.parseDouble(MenuTable.getValueAt(selectedRow, 3).toString());
        totalPrice += price;
    }

    // Calculate the tip amount (5% of the total price)
    double tipAmount = 0.05 * totalPrice;

    // Calculate the new total price with the tip included
    double totalWithTip = totalPrice + tipAmount;

    // Display the tip amount and the new total price
    JOptionPane.showMessageDialog(this, "Tip Amount (5%): RM" + String.format("%.2f", tipAmount) + "\n"
        + "Total Price with Tip: RM" + String.format("%.2f", totalWithTip), "Tip Information", JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_TipsbtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selectedVendorName = (String) jComboBox1.getSelectedItem();
       currentDishes = new LinkedList<>();

       if ("All Vendors".equals(selectedVendorName)) {
           currentDishes.addAll(allDishes); // Show all dishes
       } else {
           for (Dish dish : allDishes) {
               String vendorId = dish.getUserId();
               String vendorName = vendors.get(vendorId);
               if (vendorName != null && vendorName.equals(selectedVendorName)) {
                   currentDishes.add(dish);
               }
           }
       }

       Object[][] data = new Object[currentDishes.size()][6];
       populateTableData(currentDishes, data);

       String[] columnNames = {"Dish Id", "Dish Name", "Category", "Price", "Rating", "Photo"};
       DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
           @Override
           public Class<?> getColumnClass(int columnIndex) {
               if (columnIndex == 5) {
                   return ImageIcon.class;
               }
               return super.getColumnClass(columnIndex);
           }

           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };
       MenuTable.setModel(tableModel);
       MenuTable.setRowHeight(50);

    }//GEN-LAST:event_jComboBox1ActionPerformed
    
    
     private boolean deductFromWallet(double amount) {
    File walletFile = new File("wallet.txt");
    if (!walletFile.exists()) {
        JOptionPane.showMessageDialog(this, "No wallet file found.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    try {
        List<String> lines = Files.readAllLines(walletFile.toPath());
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] walletData = lines.get(i).split(";");
            if (walletData.length == 3 && walletData[1].equals(userId)) {
                double balance = Double.parseDouble(walletData[2]);
                if (balance >= amount) {
                    lines.set(i, walletData[0] + ";" + walletData[1] + ";" + String.format("%.2f",(balance - amount)));
                    found = true;
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds in wallet.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Wallet not found for this user.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Files.write(walletFile.toPath(), lines);
        return true;
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error accessing wallet: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    }
    
    private void writeToOrderVendorMapping(String orderId, String[] vendorIds) throws IOException {
    File file = new File("order_vendor_mapping.txt");
    boolean append = file.exists();

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
        for (String vendorId : vendorIds) {
            if (append) bw.newLine();
            bw.write(orderId + "," + vendorId);
            append = true; // After the first write, we always append
        }
    }
}
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
            java.util.logging.Logger.getLogger(ViewMenuPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewMenuPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewMenuPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewMenuPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Backbtn;
    private javax.swing.JButton Deliverybtn;
    private javax.swing.JButton Dineinbtn;
    private javax.swing.JTable MenuTable;
    private javax.swing.JRadioButton Tipsbtn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane menuTable;
    // End of variables declaration//GEN-END:variables
}
