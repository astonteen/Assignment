/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 *
 * @author Astonteen
 */
public class MyWalletPage extends javax.swing.JFrame {
   private String loggedInUsername;
    private String userType;
    private String userId;
    private String userAddress;
    private java.util.Map<String, String> userMappings;
    private String WalletId; 


    /**
     * Creates new form MyWalletPage
     */
    public MyWalletPage (String username, String type, String userId, String userAddress) {
        this.loggedInUsername = username;
    this.userType = type;
    this.userId = userId;
    this.userAddress = userAddress;
    initComponents();
    loadWalletBalance();
    loadRecentActivity();
    this.userMappings = loadUserMappings();
    }
    

    
    private java.util.Map<String, String> loadUserMappings() {
        java.util.Map<String, String> userMap = new java.util.HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1) { // Ensure there’s a user ID and username
                    String userId = parts[0]; // User ID is the first field
                    String username = parts[1]; // Username is the second field
                    userMap.put(userId, username); // Map user ID to username
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return userMap;
    }
    
    private String getBaseId(String fullId) {
        if (fullId == null || fullId.isEmpty()) return "";
        int dashIndex = fullId.indexOf('-');
        return dashIndex == -1 ? fullId : fullId.substring(0, dashIndex);
    }

    private void writeTransaction(String senderWalletId, String recipientWalletId, double amount) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            // Get base IDs
            String senderBaseId = getBaseId(senderWalletId);
            String recipientBaseId = getBaseId(recipientWalletId);

            // Look up usernames from the mapping
            String senderUsername = userMappings.getOrDefault(senderBaseId, senderBaseId); // Default to ID if not found
            String recipientUsername = userMappings.getOrDefault(recipientBaseId, recipientBaseId); // Default to ID if not found

            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            String transactionLine = String.format("%s;%s;%.2f;%s", senderUsername, recipientUsername, amount, timestamp);
            writer.write(transactionLine);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error recording transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadWalletBalance() {
            try (BufferedReader reader = new BufferedReader(new FileReader("wallet.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[1].equals(userId)) { // Match by user ID
                    balancetxt.setText(parts[2]); // Set balance
                    this.WalletId = parts[0]; // Store full wallet ID (e.g., "0004-1736179806247-8025")
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading wallet balance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // If no wallet ID is found, set a default or show an error
        if (WalletId == null) {
            JOptionPane.showMessageDialog(this, "Wallet ID not found for current user.", "Error", JOptionPane.ERROR_MESSAGE);
            WalletId = ""; // Default empty to avoid null pointer exceptions
        }
    }
    private void loadRecentActivity() {
        DefaultTableModel model = (DefaultTableModel) RATable.getModel();
        model.setRowCount(0); // Clear existing data

        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) { // Ensure the line has the correct format
                    String senderUsername = parts[0]; // Sender is now a username
                    String recipientUsername = parts[1]; // Recipient is now a username
                    String amount = parts[2];
                    String timestamp = parts[3];

                    // Add the transaction to the table
                    model.addRow(new Object[]{senderUsername + " → " + recipientUsername, amount, timestamp});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading recent activity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        balancetxt = new javax.swing.JTextField();
        recieveBtn = new javax.swing.JButton();
        sendPageBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        RATable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        WalletIDtxt = new javax.swing.JTextField();
        AmountTxt = new javax.swing.JTextField();
        SendBtn = new javax.swing.JToggleButton();
        CancelBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        balancetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balancetxtActionPerformed(evt);
            }
        });

        recieveBtn.setText("RECIEVE");
        recieveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recieveBtnActionPerformed(evt);
            }
        });

        sendPageBtn.setText("SEND");
        sendPageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendPageBtnActionPerformed(evt);
            }
        });

        RATable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Amount"
            }
        ));
        jScrollPane1.setViewportView(RATable);

        jLabel1.setText("Recent Activity");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(balancetxt)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(sendPageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(recieveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(balancetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recieveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendPageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        WalletIDtxt.setText("Enter Recipient Wallet ID");
        WalletIDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WalletIDtxtActionPerformed(evt);
            }
        });

        AmountTxt.setText("Enter Amount");
        AmountTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AmountTxtActionPerformed(evt);
            }
        });

        SendBtn.setText("send");
        SendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendBtnActionPerformed(evt);
            }
        });

        CancelBtn.setText("cancel");
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AmountTxt)
                    .addComponent(WalletIDtxt)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(CancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(SendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(WalletIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(SendBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(152, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sendPageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendPageBtnActionPerformed
        jPanel1.setVisible(false);
        jPanel2.setVisible(true);
    }//GEN-LAST:event_sendPageBtnActionPerformed

    private void balancetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balancetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_balancetxtActionPerformed

    private void recieveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recieveBtnActionPerformed
     if (WalletId == null || WalletId.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No wallet ID available to copy.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    StringSelection stringSelection = new StringSelection(WalletId);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    try {
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(this, "Wallet ID copied to clipboard: " + WalletId, "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Failed to copy to clipboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Clipboard error: " + e.getMessage());
    }


                                              
    }//GEN-LAST:event_recieveBtnActionPerformed

    private void WalletIDtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WalletIDtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WalletIDtxtActionPerformed

    private void AmountTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AmountTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AmountTxtActionPerformed

    private void CancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelBtnActionPerformed
        jPanel2.setVisible(false);
        jPanel1.setVisible(true);
    }//GEN-LAST:event_CancelBtnActionPerformed

    private void SendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendBtnActionPerformed
    String recipientWalletId = WalletIDtxt.getText();
        String amountStr = AmountTxt.getText();
        
        

        if (recipientWalletId.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Wallet ID and Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the sender has sufficient balance
        double currentBalance = Double.parseDouble(balancetxt.getText());
        if (currentBalance < amount) {
            JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Perform the transfer (update wallet.txt)
        if (performTransfer(userId, recipientWalletId, amount)) {
            writeTransaction(userId, recipientWalletId, amount);
            JOptionPane.showMessageDialog(this, "Transfer successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadWalletBalance(); // Refresh balance
            loadRecentActivity(); // Refresh recent activity
            jPanel2.setVisible(false);
            jPanel1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private boolean performTransfer(String senderId, String recipientWalletId, double amount) {
        ArrayList<String> lines = new ArrayList<>();
        String senderWalletId = null;
        double senderBalance = -1;
        int senderIndex = -1;
        String recipientUserId = null;
        double recipientBalance = -1;
        int recipientIndex = -1;

        // Read all lines and find sender/recipient
        try (BufferedReader br = new BufferedReader(new FileReader("wallet.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String currentWalletId = parts[0];
                String currentUserId = parts[1];
                double currentBalance = Double.parseDouble(parts[2]);

                // Check if current line is the sender (by User ID)
                if (currentUserId.equals(senderId)) {
                    senderWalletId = currentWalletId;
                    senderBalance = currentBalance;
                    senderIndex = lines.size() - 1;
                }

                // Check if current line is the recipient (by Wallet ID)
                if (currentWalletId.equals(recipientWalletId)) {
                    recipientUserId = currentUserId;
                    recipientBalance = currentBalance;
                    recipientIndex = lines.size() - 1;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading wallet file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate sender and recipient
        if (senderWalletId == null) {
            JOptionPane.showMessageDialog(this, "Sender wallet not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (recipientUserId == null) {
            JOptionPane.showMessageDialog(this, "Recipient wallet not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Prevent self-transfer
        if (senderWalletId.equals(recipientWalletId)) {
            JOptionPane.showMessageDialog(this, "Cannot transfer to your own wallet.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check sender balance
        if (senderBalance < amount) {
            JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Update balances
        senderBalance -= amount;
        recipientBalance += amount;

        // Update lines
        lines.set(senderIndex, String.format("%s;%s;%.2f", senderWalletId, senderId, senderBalance));
        lines.set(recipientIndex, String.format("%s;%s;%.2f", recipientWalletId, recipientUserId, recipientBalance));

        // Write updated lines back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("wallet.txt"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating wallet: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Record notifications
        try (BufferedWriter notificationBw = new BufferedWriter(new FileWriter("notifications.txt", true))) {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

            // Sender notification
            String senderMsg = String.format("%s;NOTIF-%d;You transferred RM%.2f to %s.;%s",
                    senderId, System.currentTimeMillis(), amount, recipientWalletId, timestamp);
            notificationBw.write(senderMsg);
            notificationBw.newLine();

            // Recipient notification
            String recipientMsg = String.format("%s;NOTIF-%d;You received RM%.2f from %s.;%s",
                    recipientUserId, System.currentTimeMillis(), amount, senderId, timestamp);
            notificationBw.write(recipientMsg);
            notificationBw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Transfer successful, but failed to record notifications.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        return true;
    }//GEN-LAST:event_SendBtnActionPerformed

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
            java.util.logging.Logger.getLogger(MyWalletPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyWalletPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyWalletPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyWalletPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AmountTxt;
    private javax.swing.JToggleButton CancelBtn;
    private javax.swing.JTable RATable;
    private javax.swing.JToggleButton SendBtn;
    private javax.swing.JTextField WalletIDtxt;
    private javax.swing.JTextField balancetxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton recieveBtn;
    private javax.swing.JButton sendPageBtn;
    // End of variables declaration//GEN-END:variables
}
