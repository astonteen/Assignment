/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import com.mycompany.foodify.CustomerDashboard;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mycompany.foodify.ViewMenuPage;


public class VendorFrame extends javax.swing.JFrame {

    private String username;
    private String userType;
    private String userId;
    private String userAddress;
    private CRUD crud;
    private Dish dish;
    private ViewMenuPage ViewMenuPage;

    public VendorFrame(String username, String userType, String userId, String userAddress) {
        initComponents();
        this.crud = new CRUD();
        this.ViewMenuPage = ViewMenuPage;
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.userAddress = userAddress;

        // set combo box value
        String[] categories = {"Choose A Category", "Japanese", "Chinese", "Western", "Beverage", "Dessert"};
        categoryCB.setModel(new javax.swing.DefaultComboBoxModel<>(categories));

        initTable();
    }
    public void updateMenu() {
        if(dish != null){
        //Save changes to the dish
        crud.updateDish(dish);
        //Refresh the menu display in ViewMenuPage
        if(ViewMenuPage !=null){
            ViewMenuPage.refreshMenu();
        }
    }
}


    public void initTable() {

        LinkedList<Dish> dishList = crud.readDish();
        // set columns name
        String[] columnNames = {"Dish Id", "Dish Name", "Category", "Price", "Rating", "Photo"};
        Object[][] data = new Object[dishList.size()][6];

        // Populate the data array from the dishList
        int row = 0;
        for (Dish dish : dishList) {
            data[row][0] = dish.getId();
            data[row][1] = dish.getName();
            data[row][2] = dish.getCategory();
            data[row][3] = dish.getPrice();
            data[row][4] = dish.getRating();
            try {
                // For the photo column, create an ImageIcon from the photo address
                data[row][5] = new ImageIcon(new ImageIcon(dish.getPhotoAddress()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                data[row][5] = null; // In case of invalid photo address
            }
            row++;
        }

        // Create a DefaultTableModel with the data and column names
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
            // Return ImageIcon class for the photo column
                if (columnIndex == 5) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }

            // Override to make all cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Cells are not editable
            }
        };

        // Create the JTable with the table model
        JTable dishTable = new JTable(tableModel);

        //setDishTable size
        dishTable.setFillsViewportHeight(true);
        dishTable.setRowHeight(50);

        // Add jtable into scrollPane
        scrollPane.setViewportView(dishTable);
        scrollPane.setVisible(true);

        revalidate();
        repaint();

        // Add MouseListener to handle row clicks
        dishTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = dishTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    // Perform action based on the clicked row
                    for (Dish d : crud.readDish()) {
                        if (d.getId() == (Integer) dishTable.getValueAt(row, 0)) {
                            idTF.setText(String.valueOf(d.getId()));
                            nameTF.setText(d.getName());
                            categoryCB.setSelectedItem(d.getCategory());
                            priceTF.setText(String.valueOf(d.getPrice()));
                            ratingTF.setText(String.valueOf(d.getRating()));
                            photoTF.setText(d.getPhotoAddress());
                            break;
                        }
                    }

                }
            }
        });
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
        editMenuPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        idTF = new javax.swing.JTextField();
        nameTF = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        priceTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ratingTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        photoTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        categoryCB = new javax.swing.JComboBox<>();
        uploadBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        backBtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        editMenuPanel.setBackground(new java.awt.Color(60, 60, 60));
        editMenuPanel.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Console", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Edit Menu");

        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Dish ID");

        jLabel3.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Dish Name");

        jLabel4.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Category");

        jLabel5.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Price");

        jLabel6.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Rating");

        jLabel7.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Photo");

        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        categoryCB.setModel(new javax.swing.DefaultComboBoxModel<>());
        categoryCB.setToolTipText("");
        categoryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryCBActionPerformed(evt);
            }
        });

        uploadBtn.setText("^");
        uploadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadBtnActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Vendor/note.png"))); // NOI18N

        javax.swing.GroupLayout editMenuPanelLayout = new javax.swing.GroupLayout(editMenuPanel);
        editMenuPanel.setLayout(editMenuPanelLayout);
        editMenuPanelLayout.setHorizontalGroup(
            editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMenuPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(editMenuPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMenuPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(editMenuPanelLayout.createSequentialGroup()
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editMenuPanelLayout.createSequentialGroup()
                                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(photoTF)
                            .addComponent(ratingTF)
                            .addComponent(priceTF)
                            .addComponent(nameTF)
                            .addComponent(idTF)
                            .addComponent(categoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
        editMenuPanelLayout.setVerticalGroup(
            editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMenuPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(editMenuPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)))
                .addGap(84, 84, 84)
                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(editMenuPanelLayout.createSequentialGroup()
                        .addComponent(idTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(categoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(priceTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ratingTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(editMenuPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(165, 165, 165)))
                .addGap(18, 18, 18)
                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(photoTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uploadBtn)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addGroup(editMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn))
                .addGap(22, 22, 22))
        );

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel9.setText("MENU LIST");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(editMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(271, 271, 271)
                        .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 881, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(63, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPane))
                    .addComponent(editMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
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

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
            if (fieldsValidation()) {
          Dish newDish = createDishFromFields();
          if (crud.createNewDish(newDish)) {
              JOptionPane.showMessageDialog(null, "Successfully created new dish.", "Success", JOptionPane.INFORMATION_MESSAGE);
          } else {
              JOptionPane.showMessageDialog(null, "Failed to create new dish because dish id already exist.", "Error", JOptionPane.ERROR_MESSAGE);
          }
          initTable();
      }

    }//GEN-LAST:event_addBtnActionPerformed

    private void uploadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();

        // Set the file chooser to only accept image files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));

        // Show the file chooser and get the result
        int result = fileChooser.showOpenDialog(this);

        // Check if the user selected a file (OK was pressed)
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file's path
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            photoTF.setText(filePath);
        }

    }//GEN-LAST:event_uploadBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (fieldsValidation()) {
            crud.updateDish(createDishFromFields());
            initTable();
            clearFields();
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        crud.deleteDish(createDishFromFields());
        initTable();
        clearFields();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        this.dispose();
        new VendorMainPage(username, userType, userId, userAddress).setVisible(true);
    }//GEN-LAST:event_backBtnActionPerformed

    private void categoryCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryCBActionPerformed

    private Dish createDishFromFields() {
        int dishID = Integer.parseInt(idTF.getText());
        String dishName = nameTF.getText();
        String category = (String) categoryCB.getSelectedItem();
        double price = Double.parseDouble(priceTF.getText());
        double rating = Double.parseDouble(ratingTF.getText());
        String photoAddress = photoTF.getText();

        return new Dish(dishID, dishName, category, price, rating, photoAddress, this.userId); // Add userId here
    }

    private boolean fieldsValidation() {
        // Validate ID
        String idText = idTF.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Dish ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!idText.matches("\\d+")) { // Check if ID contains only digits
            JOptionPane.showMessageDialog(null, "Dish ID must be an integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Name
        if (nameTF.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Dish name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Category
        if (categoryCB.getSelectedItem().equals("Please Choose A Category")) {
            JOptionPane.showMessageDialog(null, "Please select a valid category.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Price
        String priceText = priceTF.getText().trim();
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Price is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!priceText.matches("\\d+(\\.\\d+)?")) { // Check if Price is a valid double
            JOptionPane.showMessageDialog(null, "Price must be a valid number or double.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Rating
        String ratingText = ratingTF.getText().trim();
        if (ratingText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rating is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!ratingText.matches("\\d+(\\.\\d+)?")) { // Check if Rating is a valid double
            JOptionPane.showMessageDialog(null, "Rating must be a valid number or double.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (photoTF.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Photo is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // All fields are valid
        return true;
    }

    private void clearFields() {
        idTF.setText("");
        nameTF.setText("");
        categoryCB.setSelectedItem("Please Choose A Category");
        priceTF.setText("");
        ratingTF.setText("");
        photoTF.setText("");
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
            java.util.logging.Logger.getLogger(VendorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton backBtn;
    private javax.swing.JComboBox<String> categoryCB;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JPanel editMenuPanel;
    private javax.swing.JTextField idTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameTF;
    private javax.swing.JTextField photoTF;
    private javax.swing.JTextField priceTF;
    private javax.swing.JTextField ratingTF;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton updateBtn;
    private javax.swing.JButton uploadBtn;
    // End of variables declaration//GEN-END:variables
}
