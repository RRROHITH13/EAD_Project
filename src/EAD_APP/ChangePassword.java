package EAD_APP;

import DB.DB;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ChangePassword extends JFrame {
    public ChangePassword() {
        setTitle("Change Password - Inventory Management System");
        setSize(600, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 167));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        JLabel headerLabel = new JLabel("Change Password");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 30));

        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JPasswordField txtNewPassword = new JPasswordField();
        txtNewPassword.setPreferredSize(new Dimension(250, 30));

        // Labels - right aligned, no stretch
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblEmail, gbc);

        gbc.gridy = 1;
        formPanel.add(lblNewPassword, gbc);

        // Fields - left aligned, stretch horizontally
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtEmail, gbc);

        gbc.gridy = 1;
        formPanel.add(txtNewPassword, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnChangePassword = new JButton("Change Password");
        JButton btnCancel = new JButton("Cancel");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Color btnColor = new Color(0, 123, 167);

        for (JButton btn : new JButton[]{btnChangePassword, btnCancel}) {
            btn.setFocusPainted(false);
            btn.setFont(buttonFont);
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(180, 35));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        buttonPanel.add(btnChangePassword);
        buttonPanel.add(btnCancel);

        // Combine form and buttons
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
        btnChangePassword.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String newPassword = new String(txtNewPassword.getPassword()).trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (resetPassword(email, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password Changed Successfully");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Password Change Failed");
            }
        });

        btnCancel.addActionListener(e -> this.dispose());
    }

    private boolean resetPassword(String email, String newPassword) {
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE email = ?")) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
        return false;
    }
}
