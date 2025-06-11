package EAD_APP;

import DB.DB;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {
    public LoginPage() {
        // Set up the main frame
        setTitle("Login - Inventory Management System"); // Window title
        setSize(600, 400); // Initial window size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit application on close
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(true); // Allow the window to be resized

        // Main container panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light grey background

        // -------- Header Section --------
        JPanel headerPanel = new JPanel(); // Panel for header
        headerPanel.setBackground(new Color(0, 123, 167)); // Set background color
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70)); // Header height

        JLabel headerLabel = new JLabel("Inventory Management System"); // Header title
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Font styling
        headerLabel.setForeground(Color.WHITE); // White text color
        headerPanel.add(headerLabel); // Add label to header

        // -------- Form Section (Center) --------
        JPanel formPanel = new JPanel(new GridBagLayout()); // Use GridBag for better alignment
        formPanel.setBackground(Color.WHITE); // Background color
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Padding

        GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout control
        gbc.insets = new Insets(12, 12, 12, 12); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components stretch horizontally

        // Username Label and Field
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField txtUsername = new JTextField(20); // Wider text field

        // Password Label and Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JPasswordField txtPassword = new JPasswordField(20);

        // Add Username components "GridBagConstraints" gbc
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        // Add Password components
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // -------- Button Section (Bottom of form) --------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Buttons centered with spacing
        buttonPanel.setBackground(Color.WHITE); // Match background with form

        // Create and style buttons
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnForgotPassword = new JButton("Forgot Password");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14); // Common font for all buttons
        Color btnColor = new Color(0, 123, 167); // Common blue color

        // Apply styling to all buttons
        for (JButton btn : new JButton[]{btnLogin, btnRegister, btnForgotPassword}) {
            btn.setFocusPainted(false); // Remove focus border
            btn.setFont(buttonFont); // Set font
            btn.setBackground(btnColor); // Background color
            btn.setForeground(Color.WHITE); // Text color
            btn.setPreferredSize(new Dimension(160, 35)); // Set button size
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        }

        // Add buttons to the button panel
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnForgotPassword);

        // -------- Center Panel combines form and button panel --------
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add border around form
        centerPanel.add(formPanel, BorderLayout.CENTER); // Add form
        centerPanel.add(buttonPanel, BorderLayout.SOUTH); // Add buttons

        // Add header and center to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to the frame and make visible
        add(mainPanel);
        setVisible(true);

        // -------- Action Listeners --------

        // Login button action
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            // Validate input
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Authenticate user
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                new Home().setVisible(true); // Open Home window
                this.dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });

        // Open Register Page on click
        btnRegister.addActionListener(e -> new RegisterPage().setVisible(true));

        // Open Forgot Password Page on click
        btnForgotPassword.addActionListener(e -> new ChangePassword().setVisible(true));
    }

    // -------- User Authentication Logic --------
    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            stmt.setString(1, username); // Set username parameter
            ResultSet rs = stmt.executeQuery(); // Execute query

            if (rs.next()) {
                String hashedPassword = rs.getString("password"); // Get hashed password
                return PasswordUtil.checkPassword(password, hashedPassword); // Compare with input
            }
        } catch (SQLException ex) {
            // Show error if database fails
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
        return false; // Default to false on error or mismatch
    }

    // -------- Main Method to Run the UI --------
   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new); // Start GUI on Event Dispatch Thread
    }*/ // test the page
}
