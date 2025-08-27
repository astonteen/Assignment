/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify.Admin.Manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**Good
 *
 * @author user
 */
public class ManagerDashboard extends javax.swing.JFrame {
    
    private Map<String, String> userNameToUserId = new HashMap<>();
    private Map<String, String> complaintMap = new HashMap<>();
    private String username;
    private String userType;
    private String userId;
    private String userAddress;

    private static int lastReplyId = 0;
    


    public ManagerDashboard(String username, String userType, String userId, String userAddress) {
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        initComponents();
        populateComplaintComboBox();
        populateRunnerPerformanceTable();
        loadUserMappings(); 
    }
    
        private void loadUserMappings() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    userNameToUserId.put(parts[1].toLowerCase(), parts[0]);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + ex.getMessage());
        }
    }
    
    private synchronized String generateReplyId() {
        Set<String> existingReplyIds = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("replies.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 3 && parts[2].startsWith("Reply ID:")) {
                    String idPart = parts[2].split(":")[1].trim();
                    if (idPart.startsWith("REPLY-")) {
                        existingReplyIds.add(idPart);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading replies file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "REPLY-ERR"; // Fallback ID in case of error
        }

        String newId;
        do {
            lastReplyId++;
            newId = "REPLY-" + String.format("%03d", lastReplyId);
        } while (existingReplyIds.contains(newId));

        return newId;
    }

    private static class RunnerPerformance {
        private List<Double> ratings = new ArrayList<>();
        private double averageRating = 0.0;

        public void addRating(double rating) {
            ratings.add(rating);
        }

        public void calculateAverageRating() {
            if (!ratings.isEmpty()) {
                averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            }
        }

        public double getAverageRating() {
            return averageRating;
        }

        // New method to determine performance category
        public String getPerformance() {
            if (averageRating >= 4.0) {
                return "Excellent";
            } else if (averageRating >= 3.0) {
                return "Good";
            } else if (averageRating >= 2.0) {
                return "Fair";
            } else {
                return "Poor";
            }
        }
    }
    
    private void populateRunnerPerformanceTable() {
        Map<String, String> runnerOrderMap = readRunnerOrderMapping();
        Map<String, RunnerPerformance> runnerPerformanceMap = calculateRunnerPerformance(runnerOrderMap);
        Map<String, String> runnerNames = readRunnerNames();

        DefaultTableModel model = (DefaultTableModel) runnerPerformanceTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Map.Entry<String, RunnerPerformance> entry : runnerPerformanceMap.entrySet()) {
            String runnerId = entry.getKey();
            RunnerPerformance performance = entry.getValue();

            // Get runner name from the map of runner names
            String runnerName = runnerNames.getOrDefault(runnerId, "Unknown Runner");

            model.addRow(new Object[]{
                runnerId,
                runnerName,
                String.format("%.2f", performance.getAverageRating()),
                performance.getPerformance() // Use the performance category
            });
        }
    }

    
    private Map<String, String> readRunnerOrderMapping() {
        Map<String, String> runnerOrderMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("runner_order_mapping.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    runnerOrderMap.put(parts[0], parts[1]); // orderId, runnerId
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading runner-order mapping: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return runnerOrderMap;
    }
    
    private Map<String, String> readRunnerNames() {
        Map<String, String> runnerNames = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData.length >= 4 && userData[3].equals("Runner")) {
                    runnerNames.put(userData[0], userData[1]); // userId, userName
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return runnerNames;
    }
    
    private Map<String, RunnerPerformance> calculateRunnerPerformance(Map<String, String> runnerOrderMap) {
    Map<String, RunnerPerformance> runnerPerformanceMap = new HashMap<>();
    
    try (BufferedReader br = new BufferedReader(new FileReader("runner_review.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 4) {
                String orderId = parts[1];
                String runnerId = runnerOrderMap.get(orderId);
                if (runnerId != null) {
                    String ratingStr = parts[2];
                    double rating = Double.parseDouble(ratingStr.split(" ")[0]);
                    
                    runnerPerformanceMap.computeIfAbsent(runnerId, k -> new RunnerPerformance()).addRating(rating);
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading runner reviews: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Calculate average rating
    for (RunnerPerformance rp : runnerPerformanceMap.values()) {
        rp.calculateAverageRating();
    }
    
    return runnerPerformanceMap;
}

    

    private void populateComplaintComboBox() {
        complaintMap.clear();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Select Order ID");

        try (BufferedReader br = new BufferedReader(new FileReader("complain.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length >= 4) {
                    String orderId = parts[1].split(": ")[1];
                    String category = parts[2].split(": ")[1];
                    String complaint = parts[3].split(": ")[1];
                    complaintMap.put(orderId, category + ": \n" + complaint);
                    model.addElement(orderId);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints: " + e.getMessage());
        }
        deliveryIdcbx.setModel(model);
    }
    
    private String getCustomerUsername(String orderId) {
        try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(orderId) && parts.length > 8) {
                    return parts[8].trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading orders: " + e.getMessage());
        }
        return null;
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        runnerPerformanceTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        deliveryIdcbx = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JButton();

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Manager/vendor.png"))); // NOI18N
        jButton1.setText("Vendor Listings");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Manager/increase.png"))); // NOI18N
        jButton2.setText("Vendor Revenue Dashboard");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(51, 255, 204));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(615, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(17, 17, 17))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jTabbedPane1.addTab("Vendor ", jPanel1);

        jScrollPane2.setMaximumSize(new java.awt.Dimension(3276700, 3276700));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(50, 16));
        jScrollPane2.setName(""); // NOI18N

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        runnerPerformanceTable.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        runnerPerformanceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Runner ID", "Name", "Average Rating", "Performance"
            }
        ));
        jScrollPane3.setViewportView(runnerPerformanceTable);

        jLabel1.setFont(new java.awt.Font("STFangsong", 1, 24)); // NOI18N
        jLabel1.setText("Runner Performance");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Manager/good-feedback.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(301, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Runner ", jPanel2);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        deliveryIdcbx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliveryIdcbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Delivery ID" }));
        deliveryIdcbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deliveryIdcbxActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        sendBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sendBtn.setText("Send");
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTextField1.setText("Reply here...");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jScrollPane4.setViewportView(jTextField1);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Manager/response.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deliveryIdcbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(401, 401, 401)
                        .addComponent(sendBtn)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(deliveryIdcbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("Complaints", jPanel4);

        logoutBtn.setFont(new java.awt.Font("Segoe UI Emoji", 0, 14)); // NOI18N
        logoutBtn.setText("Logout");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutBtn)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(logoutBtn)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DeleteListings deleteListings= new DeleteListings(username, userType, userId, userAddress);
        deleteListings.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        MonitorVendorRevenue monitorVendorRevenue= new MonitorVendorRevenue(username, userType, userId, userAddress);
        monitorVendorRevenue.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void deliveryIdcbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deliveryIdcbxActionPerformed
        String selectedOrder = (String) deliveryIdcbx.getSelectedItem();
        if (selectedOrder != null && !selectedOrder.equals("Select Order ID")) {
            jTextArea1.setText(complaintMap.getOrDefault(selectedOrder, "No complaint found"));
        } else {
            jTextArea1.setText("");
        }        
    }//GEN-LAST:event_deliveryIdcbxActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
    
        String selectedOrder = (String) deliveryIdcbx.getSelectedItem();
    String reply = jTextField1.getText().trim();

    if (selectedOrder == null || selectedOrder.equals("Select Order ID")) {
        JOptionPane.showMessageDialog(this, "Please select an order first!");
        return;
    }

    if (reply.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a reply!");
        return;
    }

    try {
        // Get customer username from the selected order
        String customerUsername = getCustomerUsername(selectedOrder);
        if (customerUsername == null) {
            JOptionPane.showMessageDialog(this, "Order not found!");
            return;
        }

        // Get customer user ID from the username
        String customerUserId = userNameToUserId.get(customerUsername.toLowerCase());
        if (customerUserId == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        // Generate reply metadata
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String replyId = generateReplyId();  
        
        // Format the reply entry with customer ID
        String replyEntry = String.format("%s;OrderID: %s;CustomerID: %s;ReplyID: %s;Reply: %s\n", 
                timestamp, selectedOrder, customerUserId, replyId, reply);

        // Save to replies.txt
        try (FileWriter fw = new FileWriter("replies.txt", true)) {
            fw.write(replyEntry);
        }
        JOptionPane.showMessageDialog(this, "Reply saved successfully with ID: " + replyId);
        jTextField1.setText("");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving reply: " + e.getMessage());
    }
                                         
    }//GEN-LAST:event_sendBtnActionPerformed

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        this.dispose();
        new loginUsers().setVisible(true);
    }//GEN-LAST:event_logoutBtnActionPerformed

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
            java.util.logging.Logger.getLogger(ManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> deliveryIdcbx;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JTable runnerPerformanceTable;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
