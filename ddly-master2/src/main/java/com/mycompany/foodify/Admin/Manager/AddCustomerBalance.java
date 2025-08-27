/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify.Admin.Manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
public class AddCustomerBalance extends javax.swing.JFrame {
    
    private static int lastNotificationId = 0;
    
    private synchronized String generateNotificationId() {
        Set<String> existingIds = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("notifications.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    existingIds.add(parts[1]); // Assuming the ID is the second element
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading notifications file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "NOTIF-ERR"; // Fallback ID in case of error
        }

        int lastId = 0;
        for (String id : existingIds) {
            if (id.startsWith("NOTIF-")) {
                try {
                    int num = Integer.parseInt(id.replace("NOTIF-", ""));
                    lastId = Math.max(lastId, num);
                } catch (NumberFormatException e) {
                    continue; // Skip invalid IDs
                }
            }
        }

        String newId = "NOTIF-" + String.format("%03d", lastId + 1);
        return newId;
    }

    private void loadDataIntoTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing rows if any

        // Map to store usernames from users.txt, keyed by user ID
        Map<String, String> userMap = new HashMap<>();

        // First, load usernames from users.txt
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData.length == 8) { // Check if the line has all fields
                    userMap.put(userData[0], userData[1]); // userID -> username
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading users file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if we can't read user data
        }

        // Now, load wallet data and include usernames
        try (BufferedReader br = new BufferedReader(new FileReader("wallet.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] walletData = line.split(";");

                if (walletData.length == 3) {
                    String walletId = walletData[0];
                    String userId = walletData[1];
                    String balance = walletData[2];

                    // Get the username, default to "Unknown" if not found
                    String username = userMap.getOrDefault(userId, "Unknown");

                    // Add row to table with Wallet ID, Username, User ID, and Balance
                    model.addRow(new Object[]{walletId, userId, username, balance});
                } else {
                    System.err.println("Malformed wallet data: " + line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading wallet file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public AddCustomerBalance() {
        initComponents();
        loadDataIntoTable();
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        addBalanceBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("SimHei", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("UserID");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SimHei", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Add Balance");

        addBalanceBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addBalanceBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Admin/add-wallet.png"))); // NOI18N
        addBalanceBtn.setText("   Add");
        addBalanceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBalanceBtnActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "WalletID", "UserID", "Username", "Balance"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("REGISTER");

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("UPDATE");

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 204, 204));
        jLabel13.setText("DELETE");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(12, 190, 104));
        jLabel10.setText("CUSTOMER CREDIT");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addBalanceBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(addBalanceBtn)))
                .addGap(0, 167, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void addBalanceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBalanceBtnActionPerformed
        String userId = jTextField1.getText().trim();
        String amountToAddStr = jTextField2.getText().trim();

        if (userId.isEmpty() || amountToAddStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both UserID and the amount to add.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amountToAdd;
        try {
            amountToAdd = Double.parseDouble(amountToAddStr);
            if (amountToAdd <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount to add to the balance.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean userFound = false;
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("wallet.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] walletData = line.split(";");
                if (walletData.length == 3 && walletData[1].equals(userId)) {
                    userFound = true;
                    double currentBalance = Double.parseDouble(walletData[2]);
                    double newBalance = currentBalance + amountToAdd;

                    updatedLines.add(walletData[0] + ";" + walletData[1] + ";" + String.format("%.2f", newBalance));
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading wallet file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!userFound) {
            JOptionPane.showMessageDialog(this, "User with ID " + userId + " not found in the wallet file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("wallet.txt"))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Balance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDataIntoTable();

            // Write notification to notifications.txt without status
            try (BufferedWriter notificationBw = new BufferedWriter(new FileWriter("notifications.txt", true))) {
                String notificationId = generateNotificationId();
                String notificationMessage = String.format("Admin has added RM%.2f to your balance.", amountToAdd);
                String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                String notificationEntry = userId + ";" + notificationId + ";" + notificationMessage + ";" + timestamp;
                notificationBw.write(notificationEntry);
                notificationBw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Balance updated, but failed to record notification: " + ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to wallet file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        jTextField1.setText("");
        jTextField2.setText("");
        loadDataIntoTable();
    
    

    }//GEN-LAST:event_addBalanceBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        this.dispose();
        new Home().setVisible(true);
    }//GEN-LAST:event_backBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AddCustomerBalance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddCustomerBalance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddCustomerBalance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddCustomerBalance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddCustomerBalance().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBalanceBtn;
    private javax.swing.JButton backBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
