package inventorymanagementapp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Tables extends javax.swing.JFrame {

    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel titleLabel;
    private JPanel tablePanel; // Panel to display category data
    private JTable categoryTable; // JTable to display category data
    private DefaultTableModel categoryTableModel;

    public Tables() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Exit on close behavior
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());

        // Add colorful background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(135, 206, 250); // Light blue
                Color color2 = new Color(255, 182, 193); // Light pink
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Title Label
        titleLabel = new JLabel("USER DASHBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(false);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Side Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Single column
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        menuPanel.setBackground(new Color(0, 102, 204)); // Light blue for menu panel

        // Add buttons to the side menu
        jButton2 = createStyledButton("Category", "/images/category.png");
        menuPanel.add(jButton2);

        jButton3 = createStyledButton("Product", "/images/product.png");
        menuPanel.add(jButton3);

        jButton4 = createStyledButton("Customer", "/images/customers.png");
        menuPanel.add(jButton4);

        jButton5 = createStyledButton("Order", "/images/Orders.png");
        menuPanel.add(jButton5);

        jButton6 = createStyledButton("Payments", null);
        menuPanel.add(jButton6);

        jButton9 = createStyledButton("Order Details", "/images/View-orders.png");
        menuPanel.add(jButton9);

        jButton8 = createStyledButton("Logout", "/images/Exit.png");
        menuPanel.add(jButton8);

        // Add action listeners to remaining buttons
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                openCategory(evt);
            }
        });
        jButton3.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        openProduct(evt);
    }
});
        
          jButton4.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            openCustomer(evt);
        }
    });
          jButton5.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        openOrders(evt); // Call the method to display the Orders table
    }
});
          jButton6.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        openPayments(evt); // Call the method to display the Payments table
    }
});
          jButton9.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        openOrderDetails(evt);
    }
});

        // Add action listener for Logout button
        jButton8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logout(evt); // Calls the logout method
            }
        });
        
        

        // Add menu panel to the left side
        backgroundPanel.add(menuPanel, BorderLayout.WEST);

        // Panel to display data (right side of the screen)
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(255, 255, 255));
        tablePanel.setVisible(false); // Initially hidden
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        // Add background panel to the frame
        add(backgroundPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(51, 153, 255)); // Blue shade
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
        button.setOpaque(true);

        if (iconPath != null) {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        }
        return button;
    }

    private void openCategory(ActionEvent evt) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
        String username = "root";
        String password = "";

        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT CategoryID, CategoryName, Description, CreatedAt FROM categories";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create table model
        String[] columnNames = {"Category ID", "Category Name", "Description", "Created At"};
        categoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in the last row
                return row == getRowCount() - 1;
            }
        };

        while (rs.next()) {
            int categoryId = rs.getInt("CategoryID");
            String categoryName = rs.getString("CategoryName");
            String description = rs.getString("Description");
            String createdAt = rs.getString("CreatedAt");
            categoryTableModel.addRow(new Object[]{categoryId, categoryName, description, createdAt});
        }

        // Add a blank row for new entries
        categoryTableModel.addRow(new Object[]{null, null, null, null});

        // Create and display JTable for category data
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(categoryTable);

        // Clear tablePanel and add new components
        tablePanel.removeAll();
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add a save button below the table
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCategoryData();
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the table panel
        tablePanel.revalidate();
        tablePanel.repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void saveCategoryData() {
        try {
            String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
           String username = "root";
        String password = "";


            Connection conn = DriverManager.getConnection(url, username, password);

            int lastRow = categoryTableModel.getRowCount() - 1;
            String categoryName = (String) categoryTableModel.getValueAt(lastRow, 1);
            String description = (String) categoryTableModel.getValueAt(lastRow, 2);
            String createdAt = (String) categoryTableModel.getValueAt(lastRow, 3);

            if (categoryName != null && description != null && createdAt != null) {
                String query = "INSERT INTO categories (CategoryName, Description, CreatedAt) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, categoryName);
                pst.setString(2, description);
                pst.setString(3, createdAt);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category added successfully!");

                // Add a new blank row for further entries
                categoryTableModel.addRow(new Object[]{null, null, null, null});
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openProduct(ActionEvent evt) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
        String username = "root";
        String password = "";

        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT ProductID, ProductName, Description, QuantityInStock, Price, CategoryID FROM products";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create table model for products
        String[] columnNames = {"Product ID", "Product Name", "Description", "Quantity In Stock", "Price", "Category ID"};
        DefaultTableModel productTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in the last row
                return row == getRowCount() - 1;
            }
        };

        while (rs.next()) {
            int productId = rs.getInt("ProductID");
            String productName = rs.getString("ProductName");
            String description = rs.getString("Description");
            int quantityInStock = rs.getInt("QuantityInStock");
            double price = rs.getDouble("Price");
            int categoryId = rs.getInt("CategoryID");
            productTableModel.addRow(new Object[]{productId, productName, description, quantityInStock, price, categoryId});
        }

        // Add a blank row for new entries
        productTableModel.addRow(new Object[]{null, null, null, null, null, null});

        // Create and display JTable for product data
        JTable productTable = new JTable(productTableModel);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(productTable);

        // Clear tablePanel and add new components
        tablePanel.removeAll();
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add a save button below the table
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProductData(productTableModel);
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the table panel
        tablePanel.revalidate();
        tablePanel.repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void saveProductData(DefaultTableModel productTableModel) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
       String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);

        int lastRow = productTableModel.getRowCount() - 1;
        String productName = (String) productTableModel.getValueAt(lastRow, 1);
        String description = (String) productTableModel.getValueAt(lastRow, 2);
        Object quantityObject = productTableModel.getValueAt(lastRow, 3);
        Object priceObject = productTableModel.getValueAt(lastRow, 4);
        Object categoryObject = productTableModel.getValueAt(lastRow, 5);

        if (productName != null && description != null && quantityObject != null && priceObject != null && categoryObject != null) {
            int quantityInStock = Integer.parseInt(quantityObject.toString());
            double price = Double.parseDouble(priceObject.toString());
            int categoryId = Integer.parseInt(categoryObject.toString());

            String query = "INSERT INTO products (ProductName, Description, QuantityInStock, Price, CategoryID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, productName);
            pst.setString(2, description);
            pst.setInt(3, quantityInStock);
            pst.setDouble(4, price);
            pst.setInt(5, categoryId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product added successfully!");

            // Refresh the table: add a new blank row
            productTableModel.addRow(new Object[]{null, null, null, null, null, null});
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid data entered. Ensure numeric fields are correct.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void openCustomer(ActionEvent evt) {
    // Toggle visibility of panels (show customer table, hide others)
    tablePanel.removeAll(); // Clear the existing table content

    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
      String username = "root";
        String password = "";

        
        // Establish a connection
        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT CustomerID, FirstName, LastName, Email, PhoneNumber, Address, CreatedAt FROM customers";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create the column names for the customer table (no Actions column)
        String[] columnNames = {"Customer ID", "First Name", "Last Name", "Email", "Phone Number", "Address", "Created At"};
        DefaultTableModel customerTableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data from the database
        while (rs.next()) {
            int customerId = rs.getInt("CustomerID");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String email = rs.getString("Email");
            String phoneNumber = rs.getString("PhoneNumber");
            String address = rs.getString("Address");
            String createdAt = rs.getString("CreatedAt");
            customerTableModel.addRow(new Object[]{customerId, firstName, lastName, email, phoneNumber, address, createdAt});
        }

        // Add a blank row for new customer entry
        customerTableModel.addRow(new Object[]{null, null, null, null, null, null, null});

        // Create and display the JTable
        JTable customerTable = new JTable(customerTableModel);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add Save Button below the table for adding new customer
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCustomerData(customerTableModel);
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the customer panel
        revalidate();
        repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void saveCustomerData(DefaultTableModel customerTableModel) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
        String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);

        int lastRow = customerTableModel.getRowCount() - 1;
        String firstName = (String) customerTableModel.getValueAt(lastRow, 1);
        String lastName = (String) customerTableModel.getValueAt(lastRow, 2);
        String email = (String) customerTableModel.getValueAt(lastRow, 3);
        String phoneNumber = (String) customerTableModel.getValueAt(lastRow, 4);
        String address = (String) customerTableModel.getValueAt(lastRow, 5);
        String createdAt = (String) customerTableModel.getValueAt(lastRow, 6);

        if (firstName != null && lastName != null && email != null && phoneNumber != null && address != null && createdAt != null) {
            String query = "INSERT INTO customers (FirstName, LastName, Email, PhoneNumber, Address, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, email);
            pst.setString(4, phoneNumber);
            pst.setString(5, address);
            pst.setString(6, createdAt);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer added successfully!");

            // Refresh blank row for further entries
            customerTableModel.addRow(new Object[]{null, null, null, null, null, null, null});
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void openOrders(ActionEvent evt) {
    // Toggle visibility of panels (show orders table, hide others)
    tablePanel.removeAll(); // Clear the existing table content

    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
    String username = "root";
        String password = "";


        // Establish a connection
        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT OrderID, CustomerID, OrderDate, TotalAmount, Status FROM orders";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create the column names for the orders table (without EmployeeID)
        String[] columnNames = {"Order ID", "Customer ID", "Order Date", "Total Amount", "Status"};
        DefaultTableModel ordersTableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with data from the database
        while (rs.next()) {
            int orderId = rs.getInt("OrderID");
            int customerId = rs.getInt("CustomerID");
            String orderDate = rs.getString("OrderDate");
            double totalAmount = rs.getDouble("TotalAmount");
            String status = rs.getString("Status");
            ordersTableModel.addRow(new Object[]{orderId, customerId, orderDate, totalAmount, status});
        }

        // Add a blank row for new order entry
        ordersTableModel.addRow(new Object[]{null, null, null, null, null});

        // Create and display the JTable
        JTable ordersTable = new JTable(ordersTableModel);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ordersTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add Save Button below the table for adding new orders
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrderData(ordersTableModel);
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the orders panel
        revalidate();
        repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void saveOrderData(DefaultTableModel ordersTableModel) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
       String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);

        int lastRow = ordersTableModel.getRowCount() - 1;
        Object orderId = ordersTableModel.getValueAt(lastRow, 0); // OrderID is auto-generated; ignore for new entry
        int customerId = Integer.parseInt(ordersTableModel.getValueAt(lastRow, 1).toString());
        String orderDate = (String) ordersTableModel.getValueAt(lastRow, 2);
        double totalAmount = Double.parseDouble(ordersTableModel.getValueAt(lastRow, 3).toString());
        String status = (String) ordersTableModel.getValueAt(lastRow, 4);

        // Check if required fields are filled
        if (customerId != 0 && orderDate != null && !orderDate.isEmpty() && totalAmount > 0 && status != null && !status.isEmpty()) {
            String query = "INSERT INTO orders (CustomerID, OrderDate, TotalAmount, Status) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, customerId);
            pst.setString(2, orderDate);
            pst.setDouble(3, totalAmount);
            pst.setString(4, status);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order added successfully!");

            // Refresh blank row for further entries
            ordersTableModel.addRow(new Object[]{null, null, null, null, null});
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid number format: " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
}


private void openPayments(ActionEvent evt) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
        String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT PaymentID, OrderID, PaymentMethodID, PaymentDate, Amount FROM payments";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create table model for Payments
        String[] columnNames = {"Payment ID", "Order ID", "Payment Method ID", "Payment Date", "Amount"};
        DefaultTableModel paymentsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in the last row
                return row == getRowCount() - 1;
            }
        };

        // Populate the table model with data from the database
        while (rs.next()) {
            int paymentId = rs.getInt("PaymentID");
            int orderId = rs.getInt("OrderID");
            int paymentMethodId = rs.getInt("PaymentMethodID");
            String paymentDate = rs.getString("PaymentDate");
            double amount = rs.getDouble("Amount");
            paymentsTableModel.addRow(new Object[]{paymentId, orderId, paymentMethodId, paymentDate, amount});
        }

        // Add a blank row for new entries
        paymentsTableModel.addRow(new Object[]{null, null, null, null, null});

        // Create and display JTable for Payments data
        JTable paymentsTable = new JTable(paymentsTableModel);
        paymentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paymentsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        tablePanel.removeAll(); // Clear previous content
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add a save button below the table
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePaymentsData(paymentsTableModel);
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the table panel
        revalidate();
        repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void savePaymentsData(DefaultTableModel paymentsTableModel) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
       String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);

        int lastRow = paymentsTableModel.getRowCount() - 1;
        Object orderIdObj = paymentsTableModel.getValueAt(lastRow, 1);
        Object paymentMethodIdObj = paymentsTableModel.getValueAt(lastRow, 2);
        Object paymentDateObj = paymentsTableModel.getValueAt(lastRow, 3);
        Object amountObj = paymentsTableModel.getValueAt(lastRow, 4);

        // Validation: Ensure no null or invalid data in the last row
        if (orderIdObj == null || paymentMethodIdObj == null || paymentDateObj == null || amountObj == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = Integer.parseInt(orderIdObj.toString());
        int paymentMethodId = Integer.parseInt(paymentMethodIdObj.toString());
        String paymentDate = paymentDateObj.toString();
        double amount = Double.parseDouble(amountObj.toString());

        // Prepare SQL query to insert data into the database
        String query = "INSERT INTO payments (OrderID, PaymentMethodID, PaymentDate, Amount) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, orderId);
        pst.setInt(2, paymentMethodId);
        pst.setString(3, paymentDate);
        pst.setDouble(4, amount);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Payment added successfully!");

        // Refresh blank row for further entries
        paymentsTableModel.addRow(new Object[]{null, null, null, null, null});
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid data format: " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
}
private void openOrderDetails(ActionEvent evt) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
       String username = "root";
        String password = "";

        Connection conn = DriverManager.getConnection(url, username, password);
        String query = "SELECT OrderDetailID, OrderID, ProductID, Quantity, UnitPrice FROM orderdetails";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Create table model for OrderDetails (without TotalPrice column)
        String[] columnNames = {"OrderDetailID", "OrderID", "ProductID", "Quantity", "UnitPrice"};
        DefaultTableModel orderDetailsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in the last row
                return row == getRowCount() - 1;
            }
        };

        // Populate the table model with data from the database
        while (rs.next()) {
            int orderDetailId = rs.getInt("OrderDetailID");
            int orderId = rs.getInt("OrderID");
            int productId = rs.getInt("ProductID");
            int quantity = rs.getInt("Quantity");
            double unitPrice = rs.getDouble("UnitPrice");
            orderDetailsTableModel.addRow(new Object[]{orderDetailId, orderId, productId, quantity, unitPrice});
        }

        // Add a blank row for new entries
        orderDetailsTableModel.addRow(new Object[]{null, null, null, null, null});

        // Create and display JTable for OrderDetails data
        JTable orderDetailsTable = new JTable(orderDetailsTableModel);
        orderDetailsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orderDetailsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(orderDetailsTable);
        tablePanel.removeAll(); // Clear previous content
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add a save button below the table
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrderDetailsData(orderDetailsTableModel);
            }
        });
        tablePanel.add(saveButton, BorderLayout.SOUTH);

        tablePanel.setVisible(true); // Show the table panel
        revalidate();
        repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void saveOrderDetailsData(DefaultTableModel orderDetailsTableModel) {
    try {
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
      String username = "root";
        String password = "";


        Connection conn = DriverManager.getConnection(url, username, password);

        int lastRow = orderDetailsTableModel.getRowCount() - 1;
        Object orderIdObj = orderDetailsTableModel.getValueAt(lastRow, 1);
        Object productIdObj = orderDetailsTableModel.getValueAt(lastRow, 2);
        Object quantityObj = orderDetailsTableModel.getValueAt(lastRow, 3);
        Object unitPriceObj = orderDetailsTableModel.getValueAt(lastRow, 4);

        // Validation: Ensure no null or invalid data in the last row
        if (orderIdObj == null || productIdObj == null || quantityObj == null || unitPriceObj == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields in the last row.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = Integer.parseInt(orderIdObj.toString());
        int productId = Integer.parseInt(productIdObj.toString());
        int quantity = Integer.parseInt(quantityObj.toString());
        double unitPrice = Double.parseDouble(unitPriceObj.toString());

        // Prepare SQL query to insert data into the database
        String query = "INSERT INTO orderdetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, orderId);
        pst.setInt(2, productId);
        pst.setInt(3, quantity);
        pst.setDouble(4, unitPrice);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Order details added successfully!");

        // Refresh blank row for further entries
        orderDetailsTableModel.addRow(new Object[]{null, null, null, null, null});
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid data format: " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
}



    private void logout(ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current window
            new Login().setVisible(true); // Open the Login screen
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Tables().setVisible(true));
    }
}
