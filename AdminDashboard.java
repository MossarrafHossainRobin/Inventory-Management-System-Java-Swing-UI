package inventorymanagementapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminDashboard extends javax.swing.JFrame {

    private JPanel tablePanel; // Panel to hold the JTable
    private JTable loginTable; // JTable for displaying login details
    private DefaultTableModel tableModel; // Table model to manage data

    public AdminDashboard() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setTitle("Admin Dashboard");
        setLayout(new BorderLayout());

        // Top Title Bar
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 102, 153));
        JLabel lblWelcome = new JLabel("Admin Dashboard");
        lblWelcome.setFont(new java.awt.Font("Segoe UI Black", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        titlePanel.add(lblWelcome);
        add(titlePanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setBackground(new Color(204, 229, 255));

        JButton btnLoginDetails = createStyledButton("View Login Details", "resources/details.png");
        btnLoginDetails.addActionListener(this::showLoginDetails);
        buttonsPanel.add(btnLoginDetails);

        JButton btnOrderPanel = createStyledButton("Order Panel", "resources/order.png");
        btnOrderPanel.addActionListener(this::showCompleteOrdersPanel);
        buttonsPanel.add(btnOrderPanel);

        JButton btnLogout = createStyledButton("Logout", "resources/logout.png");
        btnLogout.addActionListener(this::logout);
        buttonsPanel.add(btnLogout);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Table Panel for displaying login details
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 245, 255));
        tablePanel.setVisible(false); // Hidden initially

        // Adding edit controls panel
        JPanel editControlsPanel = new JPanel();
        editControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        editControlsPanel.setBackground(new Color(204, 229, 255));

       

        JButton btnRemove = createStyledButton("Remove", "resources/remove.png");
        btnRemove.addActionListener(this::removeData);
        editControlsPanel.add(btnRemove);

        tablePanel.add(editControlsPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }
private void showCompleteOrdersPanel(ActionEvent evt) {
    // Database connection
    String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
    String username = "root";
    String password = "";

    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        // SQL query to join Orders and Customers tables
        String query = "SELECT o.OrderID, o.OrderDate, c.FirstName AS CustomerFirstName, c.LastName AS CustomerLastName, " +
                       "o.TotalAmount, o.Status " +
                       "FROM Orders o " +
                       "JOIN Customers c ON o.CustomerID = c.CustomerID";

        PreparedStatement pst = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = pst.executeQuery();

        // Check if the result set is empty
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "No data found for the complete orders.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;  // Exit method if no results
        }

        // Prepare data for the JTable
        String[] columnNames = {"Order ID", "Order Date", "Customer Name", "Total Amount", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for all columns
            }
        };

        // Reset cursor to the start of ResultSet to begin adding rows to the table
        rs.beforeFirst();

        while (rs.next()) {
            String orderId = rs.getString("OrderID");
            String orderDate = rs.getString("OrderDate");
            String customerName = rs.getString("CustomerFirstName") + " " + rs.getString("CustomerLastName");
            String totalAmount = rs.getString("TotalAmount");
            String status = rs.getString("Status");

            // Add each row to the table model
            tableModel.addRow(new Object[]{orderId, orderDate, customerName, totalAmount, status});
        }

        if (loginTable == null) {
            loginTable = new JTable(tableModel);
            loginTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            loginTable.setRowHeight(25);
            loginTable.setBackground(new Color(230, 245, 255));
            loginTable.setForeground(Color.BLACK);

            // Center-align cells
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < loginTable.getColumnCount(); i++) { // Apply to all columns
                loginTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            JScrollPane scrollPane = new JScrollPane(loginTable);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            loginTable.setModel(tableModel); // Update existing table model
        }

        tablePanel.setVisible(true); // Show the table panel
        revalidate();
        repaint();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setIcon(new ImageIcon(iconPath));
        button.setBackground(new Color(0, 102, 153));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(180, 50));
        return button;
    }

    private void showLoginDetails(ActionEvent evt) {
        // Database connection
        String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = """
                SELECT 
                    Login.UserId, 
                    Users.UserName, 
                    Users.Role, 
                    Login.PasswordHash 
                FROM 
                    Login 
                INNER JOIN 
                    Users 
                ON 
                    Login.UserId = Users.UserId
            """; // Query to fetch login details

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Prepare data for the JTable
            String[] columnNames = {"User ID", "Username", "Role", "Password Hash"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Disable editing for all columns
                }
            };

            while (rs.next()) {
                String userId = rs.getString("UserId");
                String userName = rs.getString("UserName");
                String role = rs.getString("Role");
                String passwordHash = rs.getString("PasswordHash");
                tableModel.addRow(new Object[]{userId, userName, role, passwordHash});
            }

            if (loginTable == null) {
                loginTable = new JTable(tableModel);
                loginTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                loginTable.setRowHeight(25);
                loginTable.setBackground(new Color(230, 245, 255));
                loginTable.setForeground(Color.BLACK);

                // Center-align cells
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < loginTable.getColumnCount(); i++) {
                    loginTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
                }

                JScrollPane scrollPane = new JScrollPane(loginTable);
                tablePanel.add(scrollPane, BorderLayout.CENTER);
            } else {
                loginTable.setModel(tableModel); // Update existing table model
            }

            tablePanel.setVisible(true); // Show the table panel
            revalidate();
            repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    

    

   private void removeData(ActionEvent evt) {
    int selectedRow = loginTable.getSelectedRow(); // Assume loginTable is used for both login and orders data
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        return;
    }

    // Identify the selected ID based on the table context
    String selectedId = loginTable.getValueAt(selectedRow, 0).toString(); // Assuming the ID is in the first column
    String url = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
    String username = "root";
    String password = "";

    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        // Check the current context - is it an order or a user?
        if (isViewingOrders()) {
            // Removing an order
            String query = "DELETE FROM Orders WHERE OrderID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, selectedId);
                pst.executeUpdate();
            }

            // Optionally delete related data (e.g., from OrderItems, if applicable)
            query = "DELETE FROM OrderItems WHERE OrderID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, selectedId);
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Order removed successfully.");
            showCompleteOrdersPanel(null);  // Refresh the orders panel after removal

        } else if (isViewingLogin()) {
            // Removing a login (user)
            String query = "DELETE FROM Login WHERE UserId = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, selectedId);
                pst.executeUpdate();
            }

            query = "DELETE FROM Users WHERE UserId = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, selectedId);
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "User removed successfully.");
            showLoginDetails(null);  // Refresh the login details panel after removal
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
} 

// Helper methods to check which table is being viewed
private boolean isViewingOrders() {
    // Return true if currently viewing the orders table
    return loginTable.getColumnCount() > 5; // Example check, adapt based on your actual table structure
}

private boolean isViewingLogin() {
    // Return true if currently viewing the login table
    return loginTable.getColumnCount() <= 5; // Example check, adapt based on your actual table structure
}

// Renderer for the "Remove" button
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setBackground(new Color(255, 0, 0)); // Red button
        setForeground(Color.WHITE); // White text
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Remove"); // Set button text
        return this;
    }
}

    private void logout(ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new Login().setVisible(true);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
