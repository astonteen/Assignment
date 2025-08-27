/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.foodify;

import com.mycompany.foodify.Admin.Manager.loginUsers;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author xianc
 */
public class RunnerHome extends javax.swing.JFrame {
    
    private String username;
    private String userType;
    private String userId;
    private String userAddress;

        private String getCurrentTime() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
        
    public RunnerHome(String username, String userType, String userId, String userAddress) {
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.userAddress = userAddress;
        initComponents();
        tab1.setBackground(Color.white);
        
        //load image
        ImageIcon img = new ImageIcon(getClass().getResource("/delivery.png"));

        Image scaledImage = img.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH); //Width: 200px, Height: 200px
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
        this.setLocationRelativeTo(null); //to center the gui form
        logo.setText(""); //remove the text
        logo.setIcon(scaledIcon); // set the image
        
        loadOrderData();
        loadmyOrderTable();
        loadOrderDetails();
        loadtaskHistory();
        loadrunnerReviews();
        loadDailyEarningsTable();
        loadMonthlyEarningsTable();
        loadYearlyEarningsTable();
    }
    
    private void writeRunnerOrderMapping(String orderId, String runnerId) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("runner_order_mapping.txt", true))) {
            bw.write(orderId + "," + runnerId);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error writing runner-order mapping: " + e.getMessage(),
                "File Write Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isRunnerAssigned(String orderId, String runnerId) {
        try (BufferedReader br = new BufferedReader(new FileReader("runner_order_mapping.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(orderId) && parts[1].equals(runnerId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading runner-order mapping: " + e.getMessage(),
                "File Read Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

private void loadOrderData() {
    String filePath = "order.txt";
    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming data is semicolon-separated
            
            

            // Check if the line starts with "DELIVERY-" and has enough elements
            if (data.length >= 10 && data[0].startsWith("DELIVERY-")) {
                String deliveryId = data[0];
                String foodItems = data[1];
                String totalPrice = data[7];
                String orderStatus = data[10]; // Adjust this index if needed
            
            if (orderStatus.equals("Completed")) {
                    continue;
                }
            
            if (orderStatus.equals("Picked Up")) {
                    continue;
                }

                // Ensure the table model has the correct number of columns
                if (model.getColumnCount() == 4) {
                    model.addRow(new Object[]{deliveryId, foodItems, totalPrice, orderStatus});
                } else {
                    System.err.println("Table model does not have the correct number of columns.");
                }
            } else {
                System.err.println("Invalid line or not a delivery order: " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void updateOrderFile() {
    DefaultTableModel orderModel = (DefaultTableModel) orderTable.getModel();
    DefaultTableModel myOrderModel = (DefaultTableModel) myOrderTable.getModel();
    try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");

            boolean updated = false;
            // Check orderTable for "Preparing" to "Runner Assigned"
            for (int row = 0; row < orderModel.getRowCount(); row++) {
                String tableOrderId = orderModel.getValueAt(row, 0).toString();
                String tableOrderStatus = orderModel.getValueAt(row, 3).toString();

                if (data[0].equals(tableOrderId) && data[10].equals("Preparing") && tableOrderStatus.equals("Runner Assigned")) {
                    data[10] = "Runner Assigned"; // Update to Runner Assigned
                    updated = true;
                    break;
                }
            }

            // Check myOrderTable for "Runner Assigned" to "Completed"
            if (!updated) {
                for (int row = 0; row < myOrderModel.getRowCount(); row++) {
                    String tableOrderId = myOrderModel.getValueAt(row, 0).toString();
                    String tableOrderStatus = myOrderModel.getValueAt(row, 2).toString();

                    if (data[0].equals(tableOrderId) && data[10].equals("Picked Up") && tableOrderStatus.equals("Completed")) {
                        data[10] = "Completed"; // Update to Completed
                        updated = true;
                        break;
                    }
                }
            }

            lines.add(String.join(";", data));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("order.txt"))) {
            for (String updatedLine : lines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "Error updating file: " + e.getMessage(),
            "File Update Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

private void loadmyOrderTable() {
    String filePath = "order.txt";
    DefaultTableModel model = (DefaultTableModel) myOrderTable.getModel();
    model.setRowCount(0); // Clear existing rows

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming data is semicolon-separated

            // Check if the line starts with "DELIVERY-" and has enough elements
            if (data.length >= 10 && data[0].startsWith("DELIVERY-")) {
                String deliveryId = data[0];
                String totalPrice = data[7];
                String orderStatus = data[10];

                if (orderStatus.equals("Completed")) {
                    continue;
                }

                // Check if this order is assigned to the current runner
                if (isRunnerAssigned(deliveryId, userId)) {
                    model.addRow(new Object[]{deliveryId, totalPrice, orderStatus});
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


private void loadOrderDetails() {
    String filePath = "order.txt";
    DefaultTableModel model = (DefaultTableModel) orderDetails.getModel();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming data is semicolon-separated

            // Check if the line starts with "DELIVERY-" and has enough elements
            if (data.length >= 10 && data[0].startsWith("DELIVERY-")) {
                String customerName = data[8];
                String orderId = data[0];
                String customerAddress = data[9];
                String dishName = data[1];
                String price = data[3];
                String tips = data[5];
                String tax = data[6];
                String delivery_fee = "5.00";
                
                
                // Ensure the table model has the correct number of columns
                if (model.getColumnCount() == 8) {
                    model.addRow(new Object[]{customerName, orderId, customerAddress, dishName, price, tips, tax, delivery_fee});
                } else {
                    System.err.println("Table model does not have the correct number of columns.");
                }
            } else {
                System.err.println("Invalid line or not a delivery order: " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void loadtaskHistory() {
    String filePath = "completed_orders.txt";
    DefaultTableModel model = (DefaultTableModel) taskHistory.getModel();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming data is semicolon-separated

            // Check if the line starts with "DELIVERY-" and has enough elements
            if (data.length >= 10 && data[0].startsWith("DELIVERY-")) {
                String deliveryId = data[0];
                String customerName = data[8];
                String totalPrice = data[7];
                String dishName = data[1];
                String orderStatus = data[10];
                String completedTime = data[13];

                if (orderStatus.equals("Picked Up")) {
                    continue;
                }
                
                if (model.getColumnCount() == 6) {
                    model.addRow(new Object[]{deliveryId, customerName, totalPrice, dishName, orderStatus, completedTime});
                } else {
                    System.err.println("Table model does not have the correct number of columns.");
                }
            } else {
                System.err.println("Invalid line or not a delivery order: " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void updateOrderFileToCompleted(String orderId) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data[0].equals(orderId) && data.length > 9) {
                data[10] = "Completed"; // Update the status to Completed
            }
            lines.add(String.join(";", data));
        }
    } catch (IOException e) {
        e.printStackTrace();
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("order.txt"))) {
        for (String updatedLine : lines) {
            bw.write(updatedLine);
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void saveCompletedTime(String orderId, String completedTime) {
    // Find the order in order.txt
    String existingOrder = null;
    try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data[0].equals(orderId)) {
                existingOrder = line;
                break;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "Error reading order.txt: " + e.getMessage(),
            "File Read Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (existingOrder == null) {
        JOptionPane.showMessageDialog(this,
            "Order not found in order.txt",
            "Order Not Found",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Split and modify the existing order data to include completed time
    String[] existingData = existingOrder.split(";");
    String[] newData = new String[existingData.length + 1]; // Allocate space for completed time
    System.arraycopy(existingData, 0, newData, 0, existingData.length);
    newData[newData.length - 1] = completedTime;

    // Write the modified data to completed_orders.txt
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("completed_orders.txt", true))) {
        bw.write(String.join(";", newData));
        bw.newLine();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "Error writing to completed_orders.txt: " + e.getMessage(),
            "File Write Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

private void loadrunnerReviews() {
    String filePath = "runner_review.txt";
    DefaultTableModel model = (DefaultTableModel) runnerReview.getModel();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming data is semicolon-separated

            // Check if the line starts with "DELIVERY-" and has enough elements
            if (data.length >= 4 && data[1].startsWith("DELIVERY-")) {
                String timestamp = data[0];
                String deliveryId = data[1];
                String rating = data[2];
                String comment = data[3];

                // Ensure the table model has the correct number of columns
                if (model.getColumnCount() == 4) {
                    model.addRow(new Object[]{timestamp, deliveryId, rating, comment});
                } else {
                    System.err.println("Table model does not have the correct number of columns. Expected 4, but got " + model.getColumnCount());
                }
            } else {
                System.err.println("Invalid line or not a delivery order: " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}




////finance dashboard
private void loadDailyEarningsTable() {
    String filePath = "completed_orders.txt";
    DefaultTableModel model = (DefaultTableModel) dailyEarnings.getModel();
    model.setRowCount(0); // Clear existing rows

    String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    double totalTips = 0.0;
    double totalDeliveryFees = 0.0;
    double totalEarnings = 0.0; // Initialize totalEarnings

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming semicolon-separated values

            if (data.length >= 14) {
                String orderDateTime = data[12].trim(); // Correct index for order date and time
                String[] dateParts = orderDateTime.split(" "); // Split date and time
                if (dateParts.length > 0) {
                    String orderDate = dateParts[0]; // Extract the date part

                    if (orderDate.equals(todayDate)) {
                        String orderId = data[0]; // Order ID
                        String date = orderDateTime; // Use the full date-time string
                        try {
                            double tips = Double.parseDouble(data[6]); // Correct index for tips
                            double deliveryFee = 5.00; // Default delivery fee
                            double earnings = tips + deliveryFee; // Calculate earnings

                            // Add data to the table with formatted values
                            model.addRow(new Object[]{orderId, date, String.format("%.2f", tips), String.format("%.2f", deliveryFee), String.format("%.2f", earnings)});

                            // Update totals
                            totalTips += tips;
                            totalDeliveryFees += deliveryFee;
                            totalEarnings += earnings; // Update total earnings
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing data for line: " + line);
                        }
                    }
                } else {
                    System.err.println("Could not split date and time for line: " + line);
                }
            }
        }

        // Add a summary row with formatted values
        model.addRow(new Object[]{"TOTAL", "", String.format("%.2f", totalTips), String.format("%.2f", totalDeliveryFees), "<html><b>" + String.format("%.2f", totalEarnings) + "</b></html>"});

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error reading file: " + e.getMessage(),
            "File Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

private void loadMonthlyEarningsTable() {
    String filePath = "completed_orders.txt";
    String[] columnNames = {"Year", "Month", "Total Tips", "Total Delivery Fees", "Total Earnings"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    monthlyEarnings.setModel(model);
    model.setRowCount(0); // Clear existing rows

    Map<String, double[]> monthlyTotals = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 14) { // Ensure there are enough fields
                String orderDateTime = data[12].trim(); // Correct index for order date and time
                String[] dateParts = orderDateTime.split(" ");
                if (dateParts.length > 0) {
                    String orderDate = dateParts[0]; // Only the date part
                    if (orderDate.length() >= 7) {
                        String monthYear = orderDate.substring(0, 7); // Extract month as yyyy-MM

                        try {
                            double tips = Double.parseDouble(data[6]); // Correct index for tips
                            double deliveryFee = 5.00; // Default delivery fee
                            double earnings = tips + deliveryFee;

                            monthlyTotals.putIfAbsent(monthYear, new double[3]);
                            double[] totals = monthlyTotals.get(monthYear);
                            totals[0] += tips; // Total Tips
                            totals[1] += deliveryFee; // Total Delivery Fees
                            totals[2] += earnings; // Total Earnings
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing data for line: " + line);
                        }
                    } else {
                        System.err.println("Invalid date format: " + orderDate);
                    }
                } else {
                    System.err.println("Could not split date and time for line: " + line);
                }
            }
        }

        // Add monthly totals to the table with Month in column 1
        for (Map.Entry<String, double[]> entry : monthlyTotals.entrySet()) {
            String monthYear = entry.getKey();
            String[] parts = monthYear.split("-");
            if (parts.length == 2) {
                String year = parts[0];
                String monthNum = parts[1];
                
                String monthName = getMonthName(Integer.parseInt(monthNum));
                
                double[] totals = entry.getValue();
                model.addRow(new Object[]{
                    year,                  
                    monthName,             
                    String.format("%.2f", totals[0]), 
                    String.format("%.2f", totals[1]), 
                    "<html><b>" + String.format("%.2f", totals[2]) + "</b></html>"
                });
            } else {
                System.err.println("Invalid month-year format: " + monthYear);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error reading file: " + e.getMessage(),
            "File Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

private String getMonthName(int month) {
    String[] months = {"January", "February", "March", "April", "May", "June", "July", 
                       "August", "September", "October", "November", "December"};
    if (month >= 1 && month <= 12) {
        return months[month - 1];
    } else {
        return "Unknown"; // Handle invalid month numbers
    }
}

private void loadYearlyEarningsTable() {
    String filePath = "completed_orders.txt";
    String[] columnNames = {"Year", "Total Tips", "Total Delivery Fees", "Total Earnings"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    yearlyEarnings.setModel(model);
    model.setRowCount(0); // Clear existing rows

    Map<String, double[]> yearlyTotals = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); // Assuming semicolon-separated values

            if (data.length >= 14) { // Ensure there are enough fields
                String orderDateTime = data[12].trim(); // Correct index for order date and time
                String[] dateParts = orderDateTime.split(" "); // Split date and time
                if (dateParts.length > 0) {
                    String orderDate = dateParts[0]; // Extract the date part
                    if (orderDate.length() >= 4) {
                        String year = orderDate.substring(0, 4); // Extract the year (yyyy)

                        try {
                            double tips = Double.parseDouble(data[6]); // Correct index for tips
                            double deliveryFee = 5.00; // Default delivery fee
                            double earnings = tips + deliveryFee; // Calculate earnings

                            // Update yearly totals
                            yearlyTotals.putIfAbsent(year, new double[3]);
                            double[] totals = yearlyTotals.get(year);
                            totals[0] += tips;
                            totals[1] += deliveryFee;
                            totals[2] += earnings;
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing data for line: " + line);
                        }
                    } else {
                        System.err.println("Invalid date format: " + orderDate);
                    }
                } else {
                    System.err.println("Could not split date and time for line: " + line);
                }
            }
        }

        // Add yearly totals to the table
        for (Map.Entry<String, double[]> entry : yearlyTotals.entrySet()) {
            String year = entry.getKey();
            double[] totals = entry.getValue();
            model.addRow(new Object[]{
                year,
                String.format("%.2f", totals[0]),
                String.format("%.2f", totals[1]),
                "<html><b>" + String.format("%.2f", totals[2]) + "</b></html>"
            });
        }

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error reading file: " + e.getMessage(),
            "File Error",
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

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        tab1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tab2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tab3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tab4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tab5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jp1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        Assignme = new javax.swing.JButton();
        declineBtn = new javax.swing.JButton();
        refreshOrder = new javax.swing.JButton();
        jp2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        MarkAsDelivered = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        orderDetails = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        myOrderTable = new javax.swing.JTable();
        reloadBtn = new javax.swing.JButton();
        jp3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        taskHistory = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        refreshTask = new javax.swing.JButton();
        jp4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        runnerReview = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jp5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dailyEarnings = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        monthlyEarnings = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        yearlyEarnings = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        refreshEarnings = new javax.swing.JButton();

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTable4);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(jTable6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(239, 480));

        logo.setText("logo");

        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel1.setText("New Task");

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(71, 71, 71))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab2MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel2.setText("My Task");

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(74, 74, 74))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab3MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel3.setText("Task History");

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(62, 62, 62))
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab4MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel4.setText("Review");

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(80, 80, 80))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab5MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel6.setText("Revenue Dashboard");

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab5Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(21, 21, 21))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Runner/logout.png"))); // NOI18N
        jButton1.setText("Log Out");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tab5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tab4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tab3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tab2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tab1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jButton1)))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(logo)
                .addGap(36, 36, 36)
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(33, 33, 33))
        );

        jPanel2.setLayout(new javax.swing.OverlayLayout(jPanel2));

        jp1.setBackground(new java.awt.Color(255, 255, 255));
        jp1.setPreferredSize(new java.awt.Dimension(676, 500));

        jLabel5.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel5.setText("New Order");

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Food Items", "Total Price", "Order Status"
            }
        ));
        orderTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(orderTable);
        orderTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        Assignme.setText("Assign Me");
        Assignme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AssignmeActionPerformed(evt);
            }
        });

        declineBtn.setText("Decline");
        declineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineBtnActionPerformed(evt);
            }
        });

        refreshOrder.setText("Refresh Order");
        refreshOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp1Layout = new javax.swing.GroupLayout(jp1);
        jp1.setLayout(jp1Layout);
        jp1Layout.setHorizontalGroup(
            jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp1Layout.createSequentialGroup()
                .addGroup(jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp1Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 746, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jp1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refreshOrder)
                                .addGap(41, 41, 41))))
                    .addGroup(jp1Layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(Assignme, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(declineBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp1Layout.setVerticalGroup(
            jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp1Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(refreshOrder))
                .addGap(57, 57, 57)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Assignme)
                    .addComponent(declineBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jp1);

        jp2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel7.setText("MY ORDER");

        MarkAsDelivered.setText("Mark As Delivered");
        MarkAsDelivered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MarkAsDeliveredActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel8.setText("Order Details");

        orderDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Your Order From", "Order No.", "Delivery Address", "Food", "Price", "Tips", "Tax Charges", "Delivery Charges"
            }
        ));
        jScrollPane5.setViewportView(orderDetails);

        jButton4.setText("Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        myOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Total Price", "Order Status"
            }
        ));
        myOrderTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(myOrderTable);

        reloadBtn.setText("Refresh Order");
        reloadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp2Layout = new javax.swing.GroupLayout(jp2);
        jp2.setLayout(jp2Layout);
        jp2Layout.setHorizontalGroup(
            jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp2Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MarkAsDelivered)
                    .addGroup(jp2Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGap(18, 18, 18))
            .addGroup(jp2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(reloadBtn)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(25, 25, 25))
        );
        jp2Layout.setVerticalGroup(
            jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4)
                        .addComponent(reloadBtn)))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(MarkAsDelivered)
                .addGap(8, 8, 8)
                .addComponent(jLabel8)
                .addGap(25, 25, 25)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jp2);

        jp3.setBackground(new java.awt.Color(255, 255, 255));
        jp3.setPreferredSize(new java.awt.Dimension(485, 0));

        jLabel9.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel9.setText("TASK HISTORY");

        taskHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Customer Name", "Total Price", "Food Item", "Status", "Completed Time"
            }
        ));
        jScrollPane8.setViewportView(taskHistory);

        jButton3.setText("Back");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        refreshTask.setText("Refresh Task");
        refreshTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshTaskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp3Layout = new javax.swing.GroupLayout(jp3);
        jp3.setLayout(jp3Layout);
        jp3Layout.setHorizontalGroup(
            jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp3Layout.createSequentialGroup()
                .addGroup(jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jp3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshTask)
                        .addGap(34, 34, 34)
                        .addComponent(jButton3))
                    .addGroup(jp3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp3Layout.setVerticalGroup(
            jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton3)
                    .addComponent(refreshTask))
                .addGap(54, 54, 54)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jp3);

        jp4.setBackground(new java.awt.Color(255, 255, 255));
        jp4.setPreferredSize(new java.awt.Dimension(485, 0));

        jLabel10.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel10.setText("CUSTOMER REVIEW");

        runnerReview.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Review Date", "Rating", "Review", "Comment"
            }
        ));
        jScrollPane9.setViewportView(runnerReview);

        jButton6.setText("Back");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp4Layout = new javax.swing.GroupLayout(jp4);
        jp4.setLayout(jp4Layout);
        jp4Layout.setHorizontalGroup(
            jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addGroup(jp4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 568, Short.MAX_VALUE)
                        .addComponent(jButton6)))
                .addGap(30, 30, 30))
        );
        jp4Layout.setVerticalGroup(
            jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp4Layout.createSequentialGroup()
                .addGroup(jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jButton6))
                    .addGroup(jp4Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel10)))
                .addGap(55, 55, 55)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jp4);

        jp5.setBackground(new java.awt.Color(255, 255, 255));
        jp5.setPreferredSize(new java.awt.Dimension(485, 495));

        jLabel11.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel11.setText("FINANCIAL DASHBOARD");

        jTabbedPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jTabbedPane1ComponentMoved(evt);
            }
        });

        dailyEarnings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Delivery ID", "Date and Time", "Tips", "Delivery Fee", "Earnings"
            }
        ));
        jScrollPane2.setViewportView(dailyEarnings);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily", jPanel3);

        monthlyEarnings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Month", "Total Tips", "Total Delivery Fees", "Total Earnings"
            }
        ));
        jScrollPane7.setViewportView(monthlyEarnings);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Monthly", jPanel4);

        yearlyEarnings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Year", "Total Tips", "Total Delivery Fees", "Total Earnings"
            }
        ));
        jScrollPane11.setViewportView(yearlyEarnings);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Yearly", jPanel5);

        jButton7.setText("Back");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        refreshEarnings.setText("Refresh");
        refreshEarnings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshEarningsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp5Layout = new javax.swing.GroupLayout(jp5);
        jp5.setLayout(jp5Layout);
        jp5Layout.setHorizontalGroup(
            jp5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(112, 112, 112)
                .addComponent(refreshEarnings)
                .addGap(34, 34, 34)
                .addComponent(jButton7)
                .addGap(33, 33, 33))
        );
        jp5Layout.setVerticalGroup(
            jp5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jp5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jLabel11)
                    .addComponent(refreshEarnings))
                .addGap(22, 22, 22)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jPanel2.add(jp5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        jp1.setVisible(true);
        jp2.setVisible(false);
        jp3.setVisible(false);
        jp4.setVisible(false);
        jp5.setVisible(false);
        tab1.setBackground(Color.white);
        tab2.setBackground(new Color(214,217,223,255));
        tab3.setBackground(new Color(214,217,223,255));
        tab4.setBackground(new Color(214,217,223,255));
        tab5.setBackground(new Color(214,217,223,255));
    }//GEN-LAST:event_tab1MouseClicked

    private void tab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2MouseClicked
        jp1.setVisible(false);
        jp2.setVisible(true);
        jp3.setVisible(false);
        jp4.setVisible(false);
        jp5.setVisible(false);
        tab2.setBackground(Color.white);
        tab1.setBackground(new Color(214,217,223,255));
        tab3.setBackground(new Color(214,217,223,255));
        tab4.setBackground(new Color(214,217,223,255));
        tab5.setBackground(new Color(214,217,223,255));
    }//GEN-LAST:event_tab2MouseClicked

    private void tab3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab3MouseClicked
        jp1.setVisible(false);
        jp2.setVisible(false);
        jp3.setVisible(true);
        jp4.setVisible(false);
        jp5.setVisible(false);
        tab3.setBackground(Color.white);
        tab2.setBackground(new Color(214,217,223,255));
        tab1.setBackground(new Color(214,217,223,255));
        tab4.setBackground(new Color(214,217,223,255));
        tab5.setBackground(new Color(214,217,223,255));
    }//GEN-LAST:event_tab3MouseClicked

    private void tab4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab4MouseClicked
        jp1.setVisible(false);
        jp2.setVisible(false);
        jp3.setVisible(false);
        jp4.setVisible(true);
        jp5.setVisible(false);
        tab4.setBackground(Color.white);
        tab2.setBackground(new Color(214,217,223,255));
        tab3.setBackground(new Color(214,217,223,255));
        tab1.setBackground(new Color(214,217,223,255));
        tab5.setBackground(new Color(214,217,223,255));
    }//GEN-LAST:event_tab4MouseClicked

    private void tab5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab5MouseClicked
        jp1.setVisible(false);
        jp2.setVisible(false);
        jp3.setVisible(false);
        jp4.setVisible(false);
        jp5.setVisible(true);
        tab5.setBackground(Color.white);
        tab2.setBackground(new Color(214,217,223,255));
        tab3.setBackground(new Color(214,217,223,255));
        tab4.setBackground(new Color(214,217,223,255));
        tab1.setBackground(new Color(214,217,223,255));
    }//GEN-LAST:event_tab5MouseClicked

    private void AssignmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AssignmeActionPerformed
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an order to assign.",
                "No Order Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderStatus = orderTable.getValueAt(selectedRow, 3).toString();
        if (orderStatus.equals("Preparing")) {
            String orderId = orderTable.getValueAt(selectedRow, 0).toString();

            // Assign the current runner to the order
            writeRunnerOrderMapping(orderId, userId);

            // Update the UI and files
            orderTable.setValueAt("Runner Assigned", selectedRow, 3);
            updateOrderFile();

            JOptionPane.showMessageDialog(this,
                "Runner assigned successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Runner cannot be assigned. Order is already in progress or completed.",
                "Assignment Error",
                JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_AssignmeActionPerformed

    private void MarkAsDeliveredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MarkAsDeliveredActionPerformed
int selectedRow = myOrderTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select an order to mark as delivered.",
            "No Order Selected",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    String orderStatus = myOrderTable.getValueAt(selectedRow, 2).toString();
    if (orderStatus.equals("Picked Up")) {
        myOrderTable.setValueAt("Completed", selectedRow, 2);
        
        // Update order.txt with "Completed" status
        updateOrderFileToCompleted(myOrderTable.getValueAt(selectedRow, 0).toString());
        
        // Get current time
        String completedTime = getCurrentTime();
        
        // Add the completed time to the task history table
        DefaultTableModel taskModel = (DefaultTableModel) taskHistory.getModel();
        for (int i = 0; i < taskModel.getRowCount(); i++) {
            String orderId = taskModel.getValueAt(i, 0).toString();
            if (orderId.equals(myOrderTable.getValueAt(selectedRow, 0).toString())) {
                taskModel.setValueAt(completedTime, i, taskModel.getColumnCount() - 1); // Assuming 'Completed Time' is the last column
                break;
            }
        }
        
        
        
        JOptionPane.showMessageDialog(this,
            "Order marked as delivered successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Save completed time to a new file
        saveCompletedTime(myOrderTable.getValueAt(selectedRow, 0).toString(), completedTime);
    } else {
        JOptionPane.showMessageDialog(this,
            "Order cannot be marked as delivered. It is either not assigned or already completed.",
            "Assignment Error",
            JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_MarkAsDeliveredActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    jp1.setVisible(true);
    jp2.setVisible(false);
    jp3.setVisible(false);
    jp4.setVisible(false);
    jp5.setVisible(false);
    
    tab1.setBackground(Color.white);
    tab2.setBackground(new Color(204, 204, 255));
    tab3.setBackground(new Color(204, 204, 255));
    tab4.setBackground(new Color(204, 204, 255));
    tab5.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    jp1.setVisible(true);
    jp2.setVisible(false);
    jp3.setVisible(false);
    jp4.setVisible(false);
    jp5.setVisible(false);
    
    tab1.setBackground(Color.white);
    tab2.setBackground(new Color(204, 204, 255));
    tab3.setBackground(new Color(204, 204, 255));
    tab4.setBackground(new Color(204, 204, 255));
    tab5.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void declineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineBtnActionPerformed
        int selectedRow = orderTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select an order to decline.",
            "No Order Selected",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    String orderStatus = orderTable.getValueAt(selectedRow, 3).toString();
    if (orderStatus.equals("Runner Assigned")) {
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to decline this order? It will be available for other runners.",
            "Confirm Decline",
            JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            orderTable.setValueAt("Preparing", selectedRow, 3); // Revert status to "Preparing"
            updateOrderFile();
            JOptionPane.showMessageDialog(this,
                "Order declined successfully! It is now available for reassignment.",
                "Order Declined",
                JOptionPane.INFORMATION_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this,
            "Only orders with 'Runner Assigned' status can be declined.",
            "Invalid Action",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_declineBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    jp1.setVisible(true);
    jp2.setVisible(false);
    jp3.setVisible(false);
    jp4.setVisible(false);
    jp5.setVisible(false);
    
    tab1.setBackground(Color.white);
    tab2.setBackground(new Color(204, 204, 255));
    tab3.setBackground(new Color(204, 204, 255));
    tab4.setBackground(new Color(204, 204, 255));
    tab5.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTabbedPane1ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1ComponentMoved

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jp1.setVisible(true);
    jp2.setVisible(false);
    jp3.setVisible(false);
    jp4.setVisible(false);
    jp5.setVisible(false);
    
    tab1.setBackground(Color.white);
    tab2.setBackground(new Color(204, 204, 255));
    tab3.setBackground(new Color(204, 204, 255));
    tab4.setBackground(new Color(204, 204, 255));
    tab5.setBackground(new Color(204, 204, 255));
    }//GEN-LAST:event_jButton7ActionPerformed

    private void reloadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadBtnActionPerformed
    DefaultTableModel model = (DefaultTableModel) myOrderTable.getModel();
    model.setRowCount(0); // Clear the current rows
    loadmyOrderTable(); // Reload data from the file
    }//GEN-LAST:event_reloadBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new loginUsers().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void refreshOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshOrderActionPerformed
    DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
    model.setRowCount(0); // Clear the current rows
    loadOrderData(); // Reload data from the file
    }//GEN-LAST:event_refreshOrderActionPerformed

    private void refreshTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshTaskActionPerformed
    DefaultTableModel model = (DefaultTableModel) taskHistory.getModel();
    model.setRowCount(0); // Clear the current rows
    loadtaskHistory(); // Reload data from the file
    }//GEN-LAST:event_refreshTaskActionPerformed

    private void refreshEarningsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshEarningsActionPerformed
    loadDailyEarningsTable(); // Refresh daily earnings table
    loadMonthlyEarningsTable(); // Refresh monthly earnings table
    loadYearlyEarningsTable(); // Refresh yearly earnings table
    }//GEN-LAST:event_refreshEarningsActionPerformed

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
            java.util.logging.Logger.getLogger(RunnerHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunnerHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunnerHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunnerHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Assignme;
    private javax.swing.JButton MarkAsDelivered;
    private javax.swing.JTable dailyEarnings;
    private javax.swing.JButton declineBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable6;
    private javax.swing.JPanel jp1;
    private javax.swing.JPanel jp2;
    private javax.swing.JPanel jp3;
    private javax.swing.JPanel jp4;
    private javax.swing.JPanel jp5;
    private javax.swing.JLabel logo;
    private javax.swing.JTable monthlyEarnings;
    private javax.swing.JTable myOrderTable;
    private javax.swing.JTable orderDetails;
    private javax.swing.JTable orderTable;
    private javax.swing.JButton refreshEarnings;
    private javax.swing.JButton refreshOrder;
    private javax.swing.JButton refreshTask;
    private javax.swing.JButton reloadBtn;
    private javax.swing.JTable runnerReview;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JTable taskHistory;
    private javax.swing.JTable yearlyEarnings;
    // End of variables declaration//GEN-END:variables
}
