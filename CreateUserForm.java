package inventorymanagementapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CreateUserForm extends JFrame {

    private JTextField txtUserID, txtUsername, txtRole;
    private JPasswordField txtPassword;
    private JButton btnSubmit;
    private JLabel lblBackground;

    public CreateUserForm() {
        setTitle("Create User Account");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Create a colorful background using gradient
        lblBackground = new JLabel();
        lblBackground.setBounds(0, 0, 400, 450); // Adjust size according to the frame
        lblBackground.setOpaque(true);
        lblBackground.setBackground(new Color(0, 123, 255)); // Blue background for the panel
        add(lblBackground);

        // Add title label with bold font
        JLabel lblTitle = new JLabel("Create New User");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBounds(120, 20, 200, 30);
        lblBackground.add(lblTitle);

        // Add fields for UserID, Username, Password, Role
        JLabel lblUserID = new JLabel("UserID  :");
        lblUserID.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUserID.setForeground(Color.WHITE);
        lblUserID.setBounds(50, 70, 100, 30);
        lblBackground.add(lblUserID);

        txtUserID = new JTextField();
        txtUserID.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtUserID.setBounds(150, 70, 200, 30);
        txtUserID.setBackground(new Color(255, 255, 255));
        txtUserID.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255))); // Border color
        lblBackground.add(txtUserID);

        JLabel lblUsername = new JLabel("Username  :");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(50, 120, 120, 30);
        lblBackground.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtUsername.setBounds(150, 120, 200, 30);
        txtUsername.setBackground(new Color(255, 255, 255));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255))); // Border color
        lblBackground.add(txtUsername);

        JLabel lblPassword = new JLabel("Password  :");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(50, 170, 100, 30);
        lblBackground.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtPassword.setBounds(150, 170, 200, 30);
        txtPassword.setBackground(new Color(255, 255, 255));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255))); // Border color
        lblBackground.add(txtPassword);

        JLabel lblRole = new JLabel("Role  :");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRole.setForeground(Color.WHITE);
        lblRole.setBounds(50, 220, 100, 30);
        lblBackground.add(lblRole);

        txtRole = new JTextField();
        txtRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtRole.setBounds(150, 220, 200, 30);
        txtRole.setBackground(new Color(255, 255, 255));
        txtRole.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255))); // Border color
        lblBackground.add(txtRole);

        // Submit button with modern design
        btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 15));  // Same font as previous design
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(new Color(0, 123, 255));  // Button background color (blue)
        btnSubmit.setBounds(150, 280, 100, 40);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorder(BorderFactory.createLineBorder(new Color(8, 123, 255))); // Border color
        lblBackground.add(btnSubmit);

        // Action listener for Submit button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = txtUserID.getText();
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String role = txtRole.getText();
                String createdAt = new java.util.Date().toString(); // Set the current date as CreatedAt

                // Database connection and insertion logic
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");
                    // SQL query to insert a new user into the database
                    String sql = "INSERT INTO users (UserID, Username, PasswordHash, Role) VALUES (?, ?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, userID);
                    pst.setString(2, username);
                    pst.setString(3, password);  // In real applications, make sure to hash the password
                    pst.setString(4, role);

                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "User account created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create user.");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        // Run the form
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreateUserForm().setVisible(true);
            }
        });
    }
}
