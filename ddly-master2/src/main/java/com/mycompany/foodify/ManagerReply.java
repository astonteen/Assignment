/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class ManagerReply extends javax.swing.JFrame {

    private String currentUser; // This should be set to the user's ID when creating the instance
    private Map<String, String> orderUserMap = new HashMap<>();
    private Map<String, String> userNameToUserId = new HashMap<>();

    /**
     * Creates new form AdminReply
     */
    public ManagerReply(String user) {
        this.currentUser = user;
        initComponents();
        loadUserMappings();
        loadOrderUserMapping();
        loadReplies();
    }

    private void loadUserMappings() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    userNameToUserId.put(parts[1].toLowerCase(), parts[0]); // Map username to user ID
                }
            }
        } catch (IOException ex) {
            jTextArea1.setText("Error loading user data: " + ex.getMessage());
        }
    }

    private void loadOrderUserMapping() {
        try (BufferedReader br = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 8) {
                    String userName = parts[8].toLowerCase(); // Assuming username is at index 8
                    if (userNameToUserId.containsKey(userName)) {
                        orderUserMap.put(parts[0], userNameToUserId.get(userName)); // Map order ID to user ID
                    }
                }
            }
        } catch (IOException ex) {
            jTextArea1.setText("Error loading order data: " + ex.getMessage());
        }
    }

    private void loadReplies() {
        try (BufferedReader br = new BufferedReader(new FileReader("replies.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String customerId = "";
                String orderId = "";
                String reply = "";
                String timestamp = parts[0].trim();

                // Extract fields from parts
                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length < 2) continue;
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "OrderID":
                            orderId = value;
                            break;
                        case "CustomerID":
                            customerId = value;
                            break;
                        case "Reply":
                            reply = value;
                            break;
                    }
                }

                // Check if the reply is intended for the current user (customer)
                if (customerId.equals(currentUser)) {
                    sb.append("Timestamp: ").append(timestamp).append("\n")
                      .append("Order ID: ").append(orderId).append("\n")
                      .append("Reply: ").append(reply).append("\n\n");
                }
            }
            if (sb.length() == 0) {
                jTextArea1.setText("No replies available for your orders.");
            } else {
                jTextArea1.setText(sb.toString());
            }
        } catch (IOException ex) {
            jTextArea1.setText("Error loading replies: " + ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        clearBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
            .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clearBtn))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Are you sure you want to delete all your replies?", 
        "Confirm Clear", 
        JOptionPane.YES_NO_OPTION
    );
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    List<String> filteredLines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("replies.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            boolean isCurrentUserReply = false;
            String[] parts = line.split(";");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length < 2) continue;
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                if (key.equals("CustomerID") && value.equals(currentUser)) {
                    isCurrentUserReply = true;
                    break;
                }
            }
            if (!isCurrentUserReply) {
                filteredLines.add(line); // Keep replies not for the current user
            }
        }
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error reading replies: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Write filtered lines back to the file
    try (FileWriter fw = new FileWriter("replies.txt")) {
        for (String line : filteredLines) {
            fw.write(line + "\n");
        }
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error writing replies: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Refresh the displayed replies
    loadReplies();
    JOptionPane.showMessageDialog(this, "All replies have been cleared.");

    }//GEN-LAST:event_clearBtnActionPerformed

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
            java.util.logging.Logger.getLogger(ManagerReply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerReply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerReply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerReply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
