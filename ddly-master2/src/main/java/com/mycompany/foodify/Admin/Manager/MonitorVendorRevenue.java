/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify.Admin.Manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class MonitorVendorRevenue extends javax.swing.JFrame {

    private Map<String, String> orderVendorMap; // OrderID -> VendorID
    private Map<String, String> vendorIdToName; // VendorID -> VendorName
    private Map<String, String> vendorNameToId; // VendorName -> VendorID
    
    
    private String username;
    private String userType;
    private String userId;
    private String userAddress;
    
    public MonitorVendorRevenue(String username, String userType, String userId, String userAddress) {
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.userAddress = userAddress;
        initComponents();
        loadMappings();
        loadVendors();
    }
    
    private void loadMappings() {
        orderVendorMap = new HashMap<>();
        vendorIdToName = new HashMap<>();
        vendorNameToId = new HashMap<>();

        // Load order-vendor mappings from order_vendor_mapping.txt
        loadOrderVendorMapping();
        
        // Load vendor names from users.txt
        loadVendorNames();
    }

    private void loadOrderVendorMapping() {
        try (BufferedReader br = new BufferedReader(new FileReader("order_vendor_mapping.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    orderVendorMap.put(parts[0], parts[1]); // OrderID -> VendorID
                }
            }
        } catch (IOException e) {
            showError("Error loading order-vendor mappings: " + e.getMessage());
        }
    }

    private void loadVendorNames() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4 && parts[3].equals("Vendor")) {
                    String vendorId = parts[0];
                    String vendorName = parts[1];
                    vendorIdToName.put(vendorId, vendorName);
                    vendorNameToId.put(vendorName, vendorId);
                }
            }
        } catch (IOException e) {
            showError("Error loading vendor names: " + e.getMessage());
        }
    }

    private void loadVendors() {
        Set<String> vendorNames = new HashSet<>(vendorNameToId.keySet());
        vendorNames.add("All Vendors"); // Add option to view all vendors

        jComboBox1.removeAllItems();
        vendorNames.forEach(jComboBox1::addItem);

        // Set all jComboBoxes to use the same model
        jComboBox2.setModel(jComboBox1.getModel());
        jComboBox3.setModel(jComboBox1.getModel());
        jComboBox4.setModel(jComboBox1.getModel());
    }

    private void loadVendorRevenue(String vendorName) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        String vendorId = vendorNameToId.get(vendorName);
        if (vendorId == null && !"All Vendors".equals(vendorName)) return;

        try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 13) continue;

                String orderId = parts[0];
                String currentVendorId = orderVendorMap.get(orderId);

                if (!"All Vendors".equals(vendorName) && !currentVendorId.equals(vendorId)) continue;

                String[] dateTime = parts[12].split(" ");
                double earned = Double.parseDouble(parts[7]);

                model.addRow(new Object[]{
                    dateTime[0],                     // Date
                    (dateTime.length > 1) ? dateTime[1] : "N/A", // Time
                    String.format("$%.2f", earned)   // Earned
                });
            }
        } catch (IOException | NumberFormatException e) {
            showError("Error loading orders: " + e.getMessage());
        }
    }
    
    private void loadWeeklyRevenue() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Week", "Start Date", "End Date", "Total Orders", "Total Revenue", "Most Sold Dish"});
        String selectedVendor = (String) jComboBox1.getSelectedItem();
        loadWeeklyOrMonthlyRevenue(model, selectedVendor, true); // true for weekly
    }

    private void loadMonthlyRevenue() {
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Month", "Total Orders", "Total Revenue", "Average Daily Revenue"});
        String selectedVendor = (String) jComboBox1.getSelectedItem();
        loadWeeklyOrMonthlyRevenue(model, selectedVendor, false); // false for monthly
    }

    private void loadWeeklyOrMonthlyRevenue(DefaultTableModel model, String vendorName, boolean isWeekly) {
        String vendorId = vendorNameToId.get(vendorName);
        if (vendorId == null && !"All Vendors".equals(vendorName)) return;

        Map<String, WeeklyData> dataMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 13) continue;

                String orderId = parts[0];
                String dishName = parts[1];
                double revenue = Double.parseDouble(parts[7]);
                LocalDate date = LocalDate.parse(parts[12].split(" ")[0], formatter);

                String currentVendorId = orderVendorMap.get(orderId);
                if (!"All Vendors".equals(vendorName) && !currentVendorId.equals(vendorId)) continue;

                String key = isWeekly ? "Week " + date.get(WeekFields.ISO.weekOfWeekBasedYear()) + ", " + date.getYear() 
                                      : date.getMonth() + " " + date.getYear();
                LocalDate start = isWeekly ? date.with(WeekFields.ISO.dayOfWeek(), 1) : date.withDayOfMonth(1);
                LocalDate end = isWeekly ? start.plusDays(6) : date.withDayOfMonth(date.lengthOfMonth());

                WeeklyData data = dataMap.computeIfAbsent(key, k -> new WeeklyData(start, end));
                data.totalOrders++;
                data.totalRevenue += revenue;
                data.dishes.merge(dishName, 1, Integer::sum);
            }
        } catch (Exception e) {
            showError("Error loading " + (isWeekly ? "weekly" : "monthly") + " data: " + e.getMessage());
        }

        dataMap.forEach((key, data) -> {
            String topDish = data.dishes.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse("N/A");
            
            if (isWeekly) {
                model.addRow(new Object[]{
                    key,
                    data.startDate.format(formatter),
                    data.endDate.format(formatter),
                    data.totalOrders,
                    String.format("$%.2f", data.totalRevenue),
                    topDish
                });
            } else {
                double avgRevenue = data.totalRevenue / data.endDate.getDayOfMonth();
                model.addRow(new Object[]{
                    key,
                    data.totalOrders,
                    String.format("$%.2f", data.totalRevenue),
                    String.format("$%.2f", avgRevenue)
                });
            }
        });
    }

    private void loadYearlyRevenue() {
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Year", "Total Orders", "Total Revenue", "Monthly Averages", "Highest Revenue Month"});

        String selectedVendor = (String) jComboBox1.getSelectedItem();
        boolean isAllVendors = "All Vendors".equals(selectedVendor);

        Map<Integer, YearlyData> yearlyData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 13) continue;

                String orderId = parts[0];
                double revenue = Double.parseDouble(parts[7]);
                LocalDate date = LocalDate.parse(parts[12].split(" ")[0], DateTimeFormatter.ISO_DATE);
                int year = date.getYear();

                String vendorId = orderVendorMap.get(orderId);
                String vendorName = vendorIdToName.get(vendorId);
                if (!isAllVendors && !selectedVendor.equals(vendorName)) continue;

                YearlyData data = yearlyData.computeIfAbsent(year, k -> new YearlyData());
                data.totalOrders++;
                data.totalRevenue += revenue;
                data.monthlyRevenue.merge(date.getMonth().toString(), revenue, Double::sum);
            }
        } catch (Exception e) {
            showError("Error loading yearly data: " + e.getMessage());
        }

        yearlyData.forEach((year, data) -> {
            double avg = data.totalRevenue / 12;
            Map.Entry<String, Double> maxMonth = data.monthlyRevenue.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElse(null);

            model.addRow(new Object[]{
                year,
                data.totalOrders,
                String.format("$%.2f", data.totalRevenue),
                String.format("$%.2f", avg),
                maxMonth != null ? maxMonth.getKey() + " ($" + String.format("%.2f", maxMonth.getValue()) + ")" : "N/A"
            });
        });
    }

    private static class WeeklyData {
        LocalDate startDate, endDate;
        int totalOrders = 0;
        double totalRevenue = 0;
        Map<String, Integer> dishes = new HashMap<>();

        public WeeklyData(LocalDate start, LocalDate end) {
            this.startDate = start;
            this.endDate = end;
        }
    }

    private static class YearlyData {
        int totalOrders = 0;
        double totalRevenue = 0;
        Map<String, Double> monthlyRevenue = new HashMap<>();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        confirmBtn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox<>();
        confirmWeeklyBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jComboBox4 = new javax.swing.JComboBox<>();
        loadMonthlyBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        loadYearlyBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Time", "Earned"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        confirmBtn.setText("CONFIRM");
        confirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtnActionPerformed(evt);
            }
        });

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(confirmBtn)))
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmBtn))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily", jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Week Number", "Start Date", "End Date", "Total Orders", "Total Revenue"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        confirmWeeklyBtn.setText("CONFIRM");
        confirmWeeklyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmWeeklyBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(confirmWeeklyBtn)))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmWeeklyBtn)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        jTabbedPane1.addTab("Weekly", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Month", "Total Orders", "Total Revenue", "Average Daily Revenue"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        loadMonthlyBtn.setText("CONFIRM");
        loadMonthlyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMonthlyBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(loadMonthlyBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadMonthlyBtn)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Monthly", jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Year", "Total Orders", "Total Revenue", "Monthly Averages", "Highest Revenue Month"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        loadYearlyBtn.setText("CONFIRM");
        loadYearlyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadYearlyBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(loadYearlyBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadYearlyBtn))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Yearly", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void confirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnActionPerformed
        String selectedVendor = (String) jComboBox1.getSelectedItem();
        if (selectedVendor == null || selectedVendor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a vendor first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadVendorRevenue(selectedVendor);
    }//GEN-LAST:event_confirmBtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void confirmWeeklyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmWeeklyBtnActionPerformed
        loadWeeklyRevenue();
    }//GEN-LAST:event_confirmWeeklyBtnActionPerformed

    private void loadMonthlyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMonthlyBtnActionPerformed
        loadMonthlyRevenue();
    }//GEN-LAST:event_loadMonthlyBtnActionPerformed

    private void loadYearlyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadYearlyBtnActionPerformed
        loadYearlyRevenue();
    }//GEN-LAST:event_loadYearlyBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.dispose();
    ManagerDashboard managerDashboard = new ManagerDashboard(username, userType, userId, userAddress);
    managerDashboard.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(MonitorVendorRevenue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MonitorVendorRevenue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MonitorVendorRevenue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MonitorVendorRevenue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmBtn;
    private javax.swing.JButton confirmWeeklyBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JButton loadMonthlyBtn;
    private javax.swing.JButton loadYearlyBtn;
    // End of variables declaration//GEN-END:variables
}
