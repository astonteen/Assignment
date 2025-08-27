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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Astonteen
 */
public class FeedbackPage extends javax.swing.JFrame {

    private String loggedInUsername;
    private String userType;
    private String userId;
    private String userAddress;
    private Set<String> reviewedOrderIds = new HashSet<>();
    private ButtonGroup ratingGroup;


    
    
    public FeedbackPage(String username, String Type, String userId, String userAddress) {
    this.loggedInUsername = username;
    this.userType = Type;
    this.userId = userId;
    this.userAddress = userAddress;
        initComponents();
        populateOrderIdComboBox();
       loadAndDisplayReviews();
        populateComplaintOrderIdComboBox();
        populateComplaintDeliveryIdComboBox();
        
        //runner review
        initRunnerRatingGroup();
        populateDeliveryIdComboBox();
        
         // Initialize and group radio buttons
        ratingGroup = new ButtonGroup();
        ratingGroup.add(Radiobtn1);
        ratingGroup.add(Radiobtn2);
        ratingGroup.add(Radiobtn3);
        ratingGroup.add(Radiobtn4);
        ratingGroup.add(Radiobtn5);
    }
    
    private List<String> readReviewsFromFile() {
    List<String> reviews = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("review.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            reviews.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error reading reviews: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return reviews;
}
    private List<String> shuffleReviews(List<String> reviews) {
    Collections.shuffle(reviews);
    return reviews;
}
    private void displayReviewsInJList(List<String> reviews) {
    DefaultListModel<String> listModel = new DefaultListModel<>();
    for (String review : reviews) {
        listModel.addElement(review);
    }
    ReviewList.setModel(listModel);
}
    private void loadAndDisplayReviews() {
    List<String> reviews = readReviewsFromFile();
    reviews = shuffleReviews(reviews);
    displayReviewsInJList(reviews);
}




    private void populateOrderIdComboBox() {
    try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) { // File changed
        String line;
        boolean hasOrders = false;
        OrderIdCbx.removeAllItems();
        OrderIdCbx.addItem("Select Order ID");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 12) { // Adjusted for completed_orders.txt structure
                String orderId = parts[0];
                String customerName = parts[8]; // Index changed to 8
                String orderType = parts[11]; // Index changed to 11
                if (customerName.trim().equalsIgnoreCase(loggedInUsername.trim())) {
                    if (orderType.trim().equalsIgnoreCase("Delivery") || orderType.trim().equalsIgnoreCase("Dinein")) {
                        OrderIdCbx.addItem(orderId);
                        hasOrders = true;
                    }
                }
            }
        }
        if (!hasOrders) {
            OrderIdCbx.addItem("No orders available");
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading order IDs: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
  
   private void saveReview(String idType, String id, String reviewText) {
    try {
        File file = new File("review.txt");
        boolean append = file.exists();

        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
            if (append) {
                bw.newLine();
            }
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            bw.write(timestamp + " - " + idType + ": " + id + " - Review: " + reviewText);
        }

        JOptionPane.showMessageDialog(this, "Review saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Debugging output
        System.out.println("Review saved: " + idType + " = " + id + ", Review = " + reviewText);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving review: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Debugging: Print stack trace
    }
}
    private void populateComplaintOrderIdComboBox() {
    try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) { // File changed
        String line;
        boolean hasOrders = false;
        SelectOrderIdCbx.removeAllItems();
        SelectOrderIdCbx.addItem("Select Order ID");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 12) { // Adjusted for completed_orders.txt structure
                String orderId = parts[0];
                String customerName = parts[8]; // Index changed to 8
                String orderType = parts[11]; // Index changed to 11
                if (customerName.trim().equalsIgnoreCase(loggedInUsername.trim())) {
                    if (orderType.trim().equalsIgnoreCase("Delivery") || orderType.trim().equalsIgnoreCase("Dinein")) {
                        SelectOrderIdCbx.addItem(orderId);
                        hasOrders = true;
                    }
                }
            }
        }
        if (!hasOrders) {
            SelectOrderIdCbx.addItem("No orders available");
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading order IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void populateComplaintDeliveryIdComboBox() {
    try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) { // File changed
        String line;
        boolean hasDeliveryOrders = false;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 15) { // Adjusted for completed_orders.txt structure
                String orderId = parts[0];
                String customerName = parts[8]; // Index changed to 8
                String orderType = parts[11]; // Index changed to 11
                if (orderType.trim().equalsIgnoreCase("Delivery") && customerName.trim().equalsIgnoreCase(loggedInUsername.trim())) {
                    hasDeliveryOrders = true;
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading delivery order IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


//////runner review

// Declare the button group at class level
private ButtonGroup runnerRatingGroup;

private void initRunnerRatingGroup() {
    // Initialize button group
    runnerRatingGroup = new ButtonGroup();
    
    // Add radio buttons to the group
    runnerRatingGroup.add(RunnerRadiobtn1);
    runnerRatingGroup.add(RunnerRadiobtn2);
    runnerRatingGroup.add(RunnerRadiobtn3);
    runnerRatingGroup.add(RunnerRadiobtn4);
    runnerRatingGroup.add(RunnerRadiobtn5);
}

private void populateDeliveryIdComboBox() {
    try (BufferedReader br = new BufferedReader(new FileReader("completed_orders.txt"))) { // File changed
        String line;
        boolean hasDeliveries = false;
        RunnerIdCbx.removeAllItems();
        RunnerIdCbx.addItem("Select Delivery ID");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 12) { // Adjusted for completed_orders.txt structure
                String deliveryId = parts[0];
                String orderType = parts[11]; // Index changed to 11
                if (orderType.trim().equalsIgnoreCase("Delivery")) {
                    RunnerIdCbx.addItem(deliveryId);
                    hasDeliveries = true;
                }
            }
        }
        if (!hasDeliveries) {
            RunnerIdCbx.addItem("No deliveries available");
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading delivery IDs: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
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

        jLabel9 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        OrderIdCbx = new javax.swing.JComboBox<>();
        Submitbtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ReviewList = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        ReviewText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Radiobtn1 = new javax.swing.JRadioButton();
        Radiobtn2 = new javax.swing.JRadioButton();
        Radiobtn3 = new javax.swing.JRadioButton();
        Radiobtn5 = new javax.swing.JRadioButton();
        Radiobtn4 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        SelectOrderIdCbx = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        ComplaintCategorycbx = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ComplaintText = new javax.swing.JTextArea();
        ComplaintSubmitBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RunnerIdCbx = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        RunnerReviewText = new javax.swing.JTextField();
        RunnerRadiobtn1 = new javax.swing.JRadioButton();
        RunnerRadiobtn2 = new javax.swing.JRadioButton();
        RunnerRadiobtn3 = new javax.swing.JRadioButton();
        RunnerRadiobtn4 = new javax.swing.JRadioButton();
        RunnerRadiobtn5 = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        RunnerReviewSubmit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Backbtn = new javax.swing.JButton();

        jLabel9.setText("jLabel9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        OrderIdCbx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        OrderIdCbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Order ID" }));
        OrderIdCbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderIdCbxActionPerformed(evt);
            }
        });

        Submitbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Submitbtn.setForeground(new java.awt.Color(51, 153, 255));
        Submitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/send.png"))); // NOI18N
        Submitbtn.setText("Submit");
        Submitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitbtnActionPerformed(evt);
            }
        });

        ReviewList.setBackground(new java.awt.Color(255, 255, 204));
        ReviewList.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jScrollPane2.setViewportView(ReviewList);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Recently Reviews");

        ReviewText.setText("Kindly leave a feedback for us!");
        ReviewText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReviewTextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel8.setText("Rate and write review below:");

        Radiobtn1.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        Radiobtn1.setText("1 Star");
        Radiobtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radiobtn1ActionPerformed(evt);
            }
        });

        Radiobtn2.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        Radiobtn2.setText("2 Star");
        Radiobtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radiobtn2ActionPerformed(evt);
            }
        });

        Radiobtn3.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        Radiobtn3.setText("3 Star");

        Radiobtn5.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        Radiobtn5.setText("5 Star");

        Radiobtn4.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        Radiobtn4.setText("4 Star");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(ReviewText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Submitbtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Radiobtn1)
                                .addGap(18, 18, 18)
                                .addComponent(Radiobtn2)
                                .addGap(18, 18, 18)
                                .addComponent(Radiobtn3)
                                .addGap(18, 18, 18)
                                .addComponent(Radiobtn4)
                                .addGap(18, 18, 18)
                                .addComponent(Radiobtn5))
                            .addComponent(OrderIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 330, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(305, 305, 305))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(OrderIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(Radiobtn1)
                    .addComponent(Radiobtn2)
                    .addComponent(Radiobtn3)
                    .addComponent(Radiobtn4)
                    .addComponent(Radiobtn5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ReviewText, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Submitbtn))
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jTabbedPane1.addTab("Order Review", jPanel2);

        SelectOrderIdCbx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SelectOrderIdCbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Order ID" }));
        SelectOrderIdCbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectOrderIdCbxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Order Reference:");

        ComplaintCategorycbx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ComplaintCategorycbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bad Service", "Food Issues", "Enviroment Issues" }));
        ComplaintCategorycbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComplaintCategorycbxActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Complaint Category:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Complaint Description");

        ComplaintText.setColumns(20);
        ComplaintText.setRows(5);
        ComplaintText.setText("\n\n\nKindly describe your issues in detail. We will reply you as soon as possible.");
        ComplaintText.setToolTipText("");
        ComplaintText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ComplaintText.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        ComplaintText.setFocusTraversalPolicyProvider(true);
        ComplaintText.setInheritsPopupMenu(true);
        jScrollPane3.setViewportView(ComplaintText);

        ComplaintSubmitBtn.setBackground(new java.awt.Color(255, 204, 204));
        ComplaintSubmitBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ComplaintSubmitBtn.setForeground(new java.awt.Color(255, 255, 255));
        ComplaintSubmitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/submission.png"))); // NOI18N
        ComplaintSubmitBtn.setText("Submit");
        ComplaintSubmitBtn.setIconTextGap(10);
        ComplaintSubmitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComplaintSubmitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SelectOrderIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(ComplaintCategorycbx, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))
                        .addGap(22, 22, 22))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ComplaintSubmitBtn)
                .addGap(325, 325, 325))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComplaintCategorycbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(SelectOrderIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(ComplaintSubmitBtn)
                .addGap(27, 27, 27))
        );

        jTabbedPane1.addTab("Complaint", jPanel3);

        RunnerIdCbx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        RunnerIdCbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Delivery ID" }));
        RunnerIdCbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunnerIdCbxActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel10.setText("Write your runner review below:");

        RunnerReviewText.setText("Kindly leave a feedback for us!");
        RunnerReviewText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        RunnerReviewText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunnerReviewTextActionPerformed(evt);
            }
        });

        RunnerRadiobtn1.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        RunnerRadiobtn1.setText("1 Star");

        RunnerRadiobtn2.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        RunnerRadiobtn2.setText("2 Star");
        RunnerRadiobtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunnerRadiobtn2ActionPerformed(evt);
            }
        });

        RunnerRadiobtn3.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        RunnerRadiobtn3.setText("3 Star");

        RunnerRadiobtn4.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        RunnerRadiobtn4.setText("4 Star");

        RunnerRadiobtn5.setFont(new java.awt.Font("STKaiti", 0, 18)); // NOI18N
        RunnerRadiobtn5.setText("5 Star");

        jLabel11.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel11.setText("Ratings:");

        RunnerReviewSubmit.setBackground(new java.awt.Color(255, 255, 204));
        RunnerReviewSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RunnerReviewSubmit.setForeground(new java.awt.Color(255, 102, 0));
        RunnerReviewSubmit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/send2.png"))); // NOI18N
        RunnerReviewSubmit.setText("Submit");
        RunnerReviewSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunnerReviewSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RunnerReviewSubmit))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RunnerReviewText)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(RunnerRadiobtn1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(RunnerRadiobtn2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(RunnerRadiobtn3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(RunnerRadiobtn4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(RunnerRadiobtn5))
                                    .addComponent(RunnerIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(0, 282, Short.MAX_VALUE)))))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(RunnerIdCbx, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(RunnerRadiobtn1)
                    .addComponent(RunnerRadiobtn2)
                    .addComponent(RunnerRadiobtn3)
                    .addComponent(RunnerRadiobtn4)
                    .addComponent(RunnerRadiobtn5))
                .addGap(31, 31, 31)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RunnerReviewText, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(RunnerReviewSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Runner Review", jPanel1);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Feedback");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Customer/feedback2.png"))); // NOI18N

        Backbtn.setText("Back");
        Backbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel13))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(Backbtn))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SubmitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitbtnActionPerformed
   String review = ReviewText.getText().trim();
String selectedOrderId = (String) OrderIdCbx.getSelectedItem(); // Get the selected order ID

// Check if review is empty
if (review.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter a review first!");
    return;
}

// Check if an order ID is selected
if (selectedOrderId == null || selectedOrderId.isEmpty() || selectedOrderId.equals("No orders available") || selectedOrderId.equals("Select Order ID")) {
    JOptionPane.showMessageDialog(this, "Please select an order ID first!");
    return;
}

// Determine the type of ID and save the review accordingly
String idType = "Order ID";
String id = selectedOrderId;
reviewedOrderIds.add(selectedOrderId); // Add this order ID to the reviewed set

// Get selected rating
String rating = "";
if (Radiobtn1.isSelected()) {
    rating = "1 Star";
} else if (Radiobtn2.isSelected()) {
    rating = "2 Stars";
} else if (Radiobtn3.isSelected()) {
    rating = "3 Stars";
} else if (Radiobtn4.isSelected()) {
    rating = "4 Stars";
} else if (Radiobtn5.isSelected()) {
    rating = "5 Stars";
}

if (rating.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please select a star rating!");
    return;
}

String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
// Write the review to review.txt
try (BufferedWriter writer = new BufferedWriter(new FileWriter("review.txt", true))) {
    writer.write(timestamp + " - " + idType + ": " + id + " - Rating: " + rating + " - Review: " + review);
    writer.newLine();
    JOptionPane.showMessageDialog(this, "Review submitted successfully!");
    ReviewText.setText("");
    OrderIdCbx.setSelectedIndex(0);
    // Reset rating buttons
    Radiobtn1.setSelected(false);
    Radiobtn2.setSelected(false);
    Radiobtn3.setSelected(false);
    Radiobtn4.setSelected(false);
    Radiobtn5.setSelected(false);
} catch (IOException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
}
    }//GEN-LAST:event_SubmitbtnActionPerformed

    private void ComplaintCategorycbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComplaintCategorycbxActionPerformed
       // Example: Update ComplaintText with selected category
    String selectedCategory = (String) ComplaintCategorycbx.getSelectedItem();
    if (selectedCategory != null && !selectedCategory.equals("Select Category")) {
        ComplaintText.setText("");
    } else {
        ComplaintText.setText("");
    }
    }//GEN-LAST:event_ComplaintCategorycbxActionPerformed

    private void BackbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackbtnActionPerformed
    this.dispose();
    CustomerDashboard customerDashboard = new CustomerDashboard(loggedInUsername, userType, userId, userAddress);
    customerDashboard.setVisible(true);
    }//GEN-LAST:event_BackbtnActionPerformed

    private void ReviewTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReviewTextActionPerformed
       String text = ReviewText.getText().trim();
       if(!text.isEmpty()){
           SubmitbtnActionPerformed(evt);
       }
    
    }//GEN-LAST:event_ReviewTextActionPerformed

    private void OrderIdCbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderIdCbxActionPerformed
                                            
    String selectedOrderId = (String) OrderIdCbx.getSelectedItem();

    if (selectedOrderId != null && !selectedOrderId.equals("No orders available") && !selectedOrderId.equals("Select Order ID")) {
        if (reviewedOrderIds.contains(selectedOrderId)) {
            JOptionPane.showMessageDialog(this, "This order ID has already been reviewed.");
            ReviewText.setText(""); // Clear the text field
        } else {
            ReviewText.setText("");
        }
    } else {
        ReviewText.setText(""); // Clear the text field
    }

    System.out.println("Selected Order ID: " + selectedOrderId);


    }//GEN-LAST:event_OrderIdCbxActionPerformed

    private void Radiobtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radiobtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Radiobtn1ActionPerformed

    private void ComplaintSubmitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComplaintSubmitBtnActionPerformed
                                                        
    String complaint = ComplaintText.getText().trim();
    String selectedOrderId = (String) SelectOrderIdCbx.getSelectedItem();
    String selectedCategory = (String) ComplaintCategorycbx.getSelectedItem();

    if (complaint.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a complaint first!");
        return;
    }

    if (selectedCategory == null || selectedCategory.isEmpty() || selectedCategory.equals("Select Category")) {
        JOptionPane.showMessageDialog(this, "Please select a complaint category!");
        return;
    }

    String idType;
    String id;
    if (selectedOrderId != null && !selectedOrderId.isEmpty() && !selectedOrderId.equals("No orders available") && !selectedOrderId.equals("Select Order ID")) {
        idType = "Order ID";
        id = selectedOrderId;
    } else {
        JOptionPane.showMessageDialog(this, "Invalid selection. Please select a valid order ID.");
        return;
    }

    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("complain.txt", true))) {
        writer.write(timestamp + " - " + idType + ": " + id + " - Category: " + selectedCategory + " - Complaint: " + complaint);
        writer.newLine();
        JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
        ComplaintText.setText("");
        SelectOrderIdCbx.setSelectedIndex(0);
        ComplaintCategorycbx.setSelectedIndex(0);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
    }

    }//GEN-LAST:event_ComplaintSubmitBtnActionPerformed

    private void SelectOrderIdCbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectOrderIdCbxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectOrderIdCbxActionPerformed

    private void RunnerRadiobtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunnerRadiobtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RunnerRadiobtn2ActionPerformed

    private void RunnerIdCbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunnerIdCbxActionPerformed
         String selectedDeliveryId = (String) RunnerIdCbx.getSelectedItem();

    if (selectedDeliveryId != null && !selectedDeliveryId.equals("No deliveries available") && !selectedDeliveryId.equals("Select Delivery ID")) {
        if (reviewedOrderIds.contains(selectedDeliveryId)) {
            JOptionPane.showMessageDialog(this, "This delivery ID has already been reviewed.");
        } else {
            ReviewText.setText("Delivery ID: " + selectedDeliveryId);
        }
    } else {
        ReviewText.setText("");  // Clear the text field if no valid selection
    }

    System.out.println("Selected Delivery ID: " + selectedDeliveryId);
    }//GEN-LAST:event_RunnerIdCbxActionPerformed

    private void Radiobtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radiobtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Radiobtn2ActionPerformed

    private void RunnerReviewSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunnerReviewSubmitActionPerformed
       String review = RunnerReviewText.getText().trim();
    String selectedOrderId = (String) RunnerIdCbx.getSelectedItem(); // Get the selected order ID

    // Debug statements to check the values
    System.out.println("Review: " + review);
    System.out.println("Selected Order ID: " + selectedOrderId);

    // Check if review is empty
    if (review.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a review first!");
        return;
    }

    // Check if an order ID is selected
    if (selectedOrderId == null || selectedOrderId.isEmpty() || 
        selectedOrderId.equals("No orders available") || selectedOrderId.equals("Select Order ID")) {
        JOptionPane.showMessageDialog(this, "Please select an order ID first!");
        return;
    }

    // Determine the type of ID and save the review accordingly
    String idType = "Order ID";
    String id = selectedOrderId;
    reviewedOrderIds.add(selectedOrderId); // Add this order ID to the reviewed set

    // Get selected rating
    String rating = "";
    if (RunnerRadiobtn1.isSelected()) {
        rating = "1 Star";
    } else if (RunnerRadiobtn2.isSelected()) {
        rating = "2 Stars";
    } else if (RunnerRadiobtn3.isSelected()) {
        rating = "3 Stars";
    } else if (RunnerRadiobtn4.isSelected()) {
        rating = "4 Stars";
    } else if (RunnerRadiobtn5.isSelected()) {
        rating = "5 Stars";
    }

    if (rating.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a star rating!");
        return;
    }

    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    // Write the review to review.txt in the new format
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("runner_review.txt", true))) {
        writer.write(timestamp + ";" + id + ";" + rating + ";" + review);
        writer.newLine();
        JOptionPane.showMessageDialog(this, "Review submitted successfully!");
        ReviewText.setText("");
        OrderIdCbx.setSelectedIndex(0);
        // Reset rating buttons
        Radiobtn1.setSelected(false);
        Radiobtn2.setSelected(false);
        Radiobtn3.setSelected(false);
        Radiobtn4.setSelected(false);
        Radiobtn5.setSelected(false);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
    }
    }//GEN-LAST:event_RunnerReviewSubmitActionPerformed

    private void RunnerReviewTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunnerReviewTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RunnerReviewTextActionPerformed

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
            java.util.logging.Logger.getLogger(FeedbackPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FeedbackPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FeedbackPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FeedbackPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JComboBox<String> ComplaintCategorycbx;
    private javax.swing.JButton ComplaintSubmitBtn;
    private javax.swing.JTextArea ComplaintText;
    private javax.swing.JComboBox<String> OrderIdCbx;
    private javax.swing.JRadioButton Radiobtn1;
    private javax.swing.JRadioButton Radiobtn2;
    private javax.swing.JRadioButton Radiobtn3;
    private javax.swing.JRadioButton Radiobtn4;
    private javax.swing.JRadioButton Radiobtn5;
    private javax.swing.JList<String> ReviewList;
    private javax.swing.JTextField ReviewText;
    private javax.swing.JComboBox<String> RunnerIdCbx;
    private javax.swing.JRadioButton RunnerRadiobtn1;
    private javax.swing.JRadioButton RunnerRadiobtn2;
    private javax.swing.JRadioButton RunnerRadiobtn3;
    private javax.swing.JRadioButton RunnerRadiobtn4;
    private javax.swing.JRadioButton RunnerRadiobtn5;
    private javax.swing.JButton RunnerReviewSubmit;
    private javax.swing.JTextField RunnerReviewText;
    private javax.swing.JComboBox<String> SelectOrderIdCbx;
    private javax.swing.JButton Submitbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
