package EAD_APP;

import DB.DB;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage extends JFrame {
    public RegisterPage() {
        setTitle("Register - Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 167));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        JLabel headerLabel = new JLabel("Register New Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("New Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField txtUsername = new JTextField(20);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("New Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JPasswordField txtPassword = new JPasswordField(20);

        // Add components to form panel
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Color btnColor = new Color(0, 123, 167);

        for (JButton btn : new JButton[]{btnRegister, btnCancel}) {
            btn.setFocusPainted(false);
            btn.setFont(buttonFont);
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(160, 35));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        // Center panel combining form and buttons
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Button actions
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(username, email, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed");
            }
        });

        btnCancel.addActionListener(e -> this.dispose());
    }

    private boolean registerUser(String username, String email, String password) {
        String hashedPassword = PasswordUtil.hashPassword(password);
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
        return false;
    }
}
