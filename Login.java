package inventorymanagementapp;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends javax.swing.JFrame {

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;  // Create Account button
    private javax.swing.JButton jButton4;  // Admin Login button
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration

    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();  // Initialize the Create Account button
        jButton4 = new javax.swing.JButton();  // Initialize the Admin Login button
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel1.setText("Welcome");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 100, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setText("Email");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 150, 60, -1));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        getContentPane().add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 180, 306, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel3.setText("Password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 220, -1, -1));

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 260, 306, -1));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login.png"))); // Ensure the image path is correct
        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 310, 306, -1));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // Ensure the image path is correct
        jButton2.setText("Close");
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0); // Close the application
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 360, 306, -1));

        // Create Account Button
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Create Account");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCreateUserForm(evt); // Open the CreateUserForm
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 400, 306, -1));

        // Admin Login Button
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("Admin Login");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openAdminLoginForm(evt); // Open the AdminLoginForm
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 440, 306, -1));

        jLabel4.setBackground(new java.awt.Color(10, 10, 10));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login-background.PNG"))); // Ensure the image path is correct
        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 2000, 765));

        pack();
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        // Database connection and login validation
        try {
            // Update with your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            // Adjusted query: The email is in the Username column and password in PasswordHash
            String sql = "SELECT * FROM users WHERE Username = ? AND PasswordHash = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Successful login
                JOptionPane.showMessageDialog(this, "Login successful!");

                // Retrieve user data from the result set
                int userID = rs.getInt("UserID");  // Assuming the UserID column is an integer
                String username = rs.getString("UserName");
                String passwordHash = rs.getString("PasswordHash");

                // Insert the login information into the login table
                String insertSql = "INSERT INTO login (UserID, UserName, PasswordHash) VALUES (?, ?, ?)";
                PreparedStatement insertPst = conn.prepareStatement(insertSql);
                insertPst.setInt(1, userID);
                insertPst.setString(2, username);
                insertPst.setString(3, passwordHash);

                int rowsAffected = insertPst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Login information stored successfully.");
                } else {
                    System.out.println("Failed to store login information.");
                }

                // Redirect to the Tables JFrame
                this.dispose(); // Close the Login form
                new Tables().setVisible(true); // Open the Tables form
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password. Try again.");
            }
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void openCreateUserForm(java.awt.event.ActionEvent evt) {
        // Open the Create User Account form
        CreateUserForm createUserForm = new CreateUserForm();
        createUserForm.setVisible(true);
    }

    private void openAdminLoginForm(java.awt.event.ActionEvent evt) {
        // Open the Admin Login form
        AdminLoginForm adminLoginForm = new AdminLoginForm();
        adminLoginForm.setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
