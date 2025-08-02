package inventorymanagementapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminLoginForm extends javax.swing.JFrame {

    private JLabel lblTitle;
    private JLabel lblAdminUsername;
    private JLabel lblAdminPassword;
    private JTextField txtAdminUsername;
    private JPasswordField txtAdminPassword;
    private JButton btnLogin;
    private JButton btnClose;

    public AdminLoginForm() {
        initComponents();
    }

    private void initComponents() {
        // Set frame properties
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        getContentPane().setLayout(new BorderLayout());

        // Background panel with gradient effect
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(102, 153, 255);
                Color color2 = new Color(255, 204, 204);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Title Label
        lblTitle = new JLabel("Admin Login");
        lblTitle.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 300, 40));

        // Username Label and Field
        lblAdminUsername = new JLabel("Username:");
        lblAdminUsername.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAdminUsername.setForeground(Color.BLACK);
        backgroundPanel.add(lblAdminUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 100, 30));

        txtAdminUsername = new JTextField();
        txtAdminUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAdminUsername.setBorder(BorderFactory.createLineBorder(new Color(102, 102, 255), 2));
        backgroundPanel.add(txtAdminUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 250, 30));

        // Password Label and Field
        lblAdminPassword = new JLabel("Password:");
        lblAdminPassword.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAdminPassword.setForeground(Color.BLACK);
        backgroundPanel.add(lblAdminPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 100, 30));

        txtAdminPassword = new JPasswordField();
        txtAdminPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAdminPassword.setBorder(BorderFactory.createLineBorder(new Color(102, 102, 255), 2));
        backgroundPanel.add(txtAdminPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 250, 30));

        // Login Button
        btnLogin = createStyledButton("Login");
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.addActionListener(this::btnLoginActionPerformed);
        backgroundPanel.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 100, 40));

        // Close Button
        btnClose = createStyledButton("Close");
        btnClose.setBackground(new Color(255, 51, 51));
        btnClose.addActionListener(evt -> dispose());
        backgroundPanel.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 100, 40));

        // Add background panel to the frame
        getContentPane().add(backgroundPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void btnLoginActionPerformed(ActionEvent evt) {
        String adminUsername = txtAdminUsername.getText();
        String adminPassword = new String(txtAdminPassword.getPassword());

        // Database connection and admin login validation
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");
            String sql = "SELECT * FROM admin WHERE Username = ? AND Password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, adminUsername);
            pst.setString(2, adminPassword);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                this.dispose();
                new AdminDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new AdminLoginForm().setVisible(true));
    }
}
