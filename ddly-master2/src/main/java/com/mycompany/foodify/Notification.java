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
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */

public class Notification extends javax.swing.JFrame {

    private String userId;
    private List<String[]> notifications;

    /**
     * Creates new form Notification
     */
    public Notification(String userId) {
        this.userId = userId;
        initComponents();
        loadNotifications();
    }

    private void loadNotifications() {
        StringBuilder notificationText = new StringBuilder();
        notifications = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("notifications.txt"))) {
            String line;
            int notificationCount = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 4); 
                if (parts.length == 4 && parts[0].equals(userId)) {
                    String notificationId = parts[1];
                    String message = parts[2];
                    String timestamp = parts[3];

                    String[] dateTime = timestamp.split(" ");
                    String date = dateTime[0];
                    String time = (dateTime.length > 1) ? dateTime[1] : "";


                    notificationCount++;
                    notificationText.append("Notification ").append(notificationCount).append(":\n")
                            .append("  Message: ").append(message).append("\n")
                            .append("  Date: ").append(date).append("\n")
                            .append("  Time: ").append(time).append("\n")
                            .append("----------------------------------------\n");

                    notifications.add(new String[]{userId, notificationId, message, timestamp});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


        String text = notificationText.toString();
        if (!text.isEmpty() && text.endsWith("----------------------------------------\n")) {
            text = text.substring(0, text.length() - 41); 
        }
        jTextArea1.setText(text);
        jTextArea1.setCaretPosition(0); 
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clearBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        clearBtn.setText("Clear");
        clearBtn.setMaximumSize(new java.awt.Dimension(118, 23));
        clearBtn.setMinimumSize(new java.awt.Dimension(118, 23));
        clearBtn.setPreferredSize(new java.awt.Dimension(118, 23));
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(151, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
    int confirm = JOptionPane.showConfirmDialog(this, 
            "Clear all notifications?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("notifications.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 4 && !parts[0].equals(userId)) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error clearing notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("notifications.txt"))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            loadNotifications(); 
        }
                                  
                 
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
            java.util.logging.Logger.getLogger(Notification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Notification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Notification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Notification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
