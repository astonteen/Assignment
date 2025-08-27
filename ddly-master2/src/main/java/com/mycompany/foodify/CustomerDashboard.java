/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;


import com.mycompany.foodify.Admin.Manager.loginUsers;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Astonteen
 */
public class CustomerDashboard extends javax.swing.JFrame {
    
    private String username;
    private String userType;
    private String userId;
    private String userAddress;
    
    public CustomerDashboard(String username, String userType, String userId, String userAddress) {
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.userAddress = userAddress;
        initComponents();

    }
    
        public List<String> getNotificationsForUser(String userId) {
        List<String> notifications = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("notifications.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 3); // Split into userId, message, timestamp
                if (parts.length >= 3 && parts[0].equals(userId)) {
                    notifications.add(parts[1] + " (" + parts[2] + ")");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return notifications;
    }

     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SidePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ViewMenuBtn = new javax.swing.JButton();
        CheckOrderBtn = new javax.swing.JButton();
        FeedbackBtn = new javax.swing.JButton();
        NotificationBtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        LogoutBtn = new javax.swing.JButton();
        MyWalletbtn = new javax.swing.JToggleButton();
        HistoryBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        SidePanel.setBackground(new java.awt.Color(255, 255, 255));
        SidePanel.setToolTipText("");
        SidePanel.setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Foodify.png"))); // NOI18N
        SidePanel.add(jLabel2);
        jLabel2.setBounds(20, 110, 253, 210);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        ViewMenuBtn.setFont(new java.awt.Font("Papyrus", 1, 14)); // NOI18N
        ViewMenuBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/menu.png"))); // NOI18N
        ViewMenuBtn.setText("View Menu");
        ViewMenuBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewMenuBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ViewMenuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewMenuBtnActionPerformed(evt);
            }
        });

        CheckOrderBtn.setFont(new java.awt.Font("Papyrus", 1, 14)); // NOI18N
        CheckOrderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/order.png"))); // NOI18N
        CheckOrderBtn.setText("Check Order");
        CheckOrderBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CheckOrderBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        CheckOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckOrderBtnActionPerformed(evt);
            }
        });

        FeedbackBtn.setFont(new java.awt.Font("Papyrus", 1, 14)); // NOI18N
        FeedbackBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/feedback.png"))); // NOI18N
        FeedbackBtn.setText("Feedback");
        FeedbackBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FeedbackBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        FeedbackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FeedbackBtnActionPerformed(evt);
            }
        });

        NotificationBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/notification.png"))); // NOI18N
        NotificationBtn.setText("Notifcation");
        NotificationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NotificationBtnActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/chat.png"))); // NOI18N
        jButton1.setText("Manager Reply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/user.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        LogoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/logout (1).png"))); // NOI18N
        LogoutBtn.setText("Logout");
        LogoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutBtnActionPerformed(evt);
            }
        });

        MyWalletbtn.setFont(new java.awt.Font("Papyrus", 1, 14)); // NOI18N
        MyWalletbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/digital-wallet.png"))); // NOI18N
        MyWalletbtn.setText("My Wallet");
        MyWalletbtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MyWalletbtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MyWalletbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyWalletbtnActionPerformed(evt);
            }
        });

        HistoryBtn.setFont(new java.awt.Font("Papyrus", 1, 14)); // NOI18N
        HistoryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/order-fulfillment.png"))); // NOI18N
        HistoryBtn.setText("Transaction History");
        HistoryBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        HistoryBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        HistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LogoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(NotificationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(HistoryBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(MyWalletbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FeedbackBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CheckOrderBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ViewMenuBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(74, 74, 74))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LogoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NotificationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(ViewMenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CheckOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FeedbackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MyWalletbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HistoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ViewMenuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewMenuBtnActionPerformed
        ViewMenuPage viewMenuPage = new ViewMenuPage(username, userType, userId, userAddress);
        viewMenuPage.setVisible(true);
        this.dispose();
        System.out.println("Dashboard User ID: " + userId);
    }//GEN-LAST:event_ViewMenuBtnActionPerformed

    private void CheckOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckOrderBtnActionPerformed
       this.dispose();
        new CheckOrderPage(username, userType, userId, userAddress).setVisible(true);
         
    }//GEN-LAST:event_CheckOrderBtnActionPerformed

    private void HistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryBtnActionPerformed
        this.dispose();
        new TransactionHistory(username, userType, userId, userAddress).setVisible(true);
        
    }//GEN-LAST:event_HistoryBtnActionPerformed

    private void FeedbackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FeedbackBtnActionPerformed
  this.dispose();
  new FeedbackPage(username, userType, userId, userAddress).setVisible(true);
   
    }//GEN-LAST:event_FeedbackBtnActionPerformed

    private void LogoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutBtnActionPerformed
         // Create an instance of the loginUsers frame
    loginUsers login = new loginUsers();
    
    // Dispose of the current FeedbackPage frame
    this.dispose();
    
    // Set the loginUsers frame to visible
    login.setVisible(true);
    }//GEN-LAST:event_LogoutBtnActionPerformed

    private void MyWalletbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyWalletbtnActionPerformed
        new MyWalletPage(username, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_MyWalletbtnActionPerformed

    private void NotificationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NotificationBtnActionPerformed
        new Notification(userId).setVisible(true);
    }//GEN-LAST:event_NotificationBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new ManagerReply(userId).setVisible(true);
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
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CheckOrderBtn;
    private javax.swing.JButton FeedbackBtn;
    private javax.swing.JButton HistoryBtn;
    private javax.swing.JButton LogoutBtn;
    private javax.swing.JToggleButton MyWalletbtn;
    private javax.swing.JButton NotificationBtn;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JButton ViewMenuBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
