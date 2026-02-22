package com.citybookshop.ui;

import com.citybookshop.model.Manager;
import com.citybookshop.model.User;
import com.citybookshop.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginFrame() {
        this.userService = new UserService();
        initUI();
    }

    private void initUI() {
        setTitle("City Bookshop - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 85, 136));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("City Bookshop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 220, 255));

        JPanel headerInner = new JPanel(new GridLayout(2, 1));
        headerInner.setBackground(new Color(34, 85, 136));
        headerInner.add(titleLabel);
        headerInner.add(subtitleLabel);
        headerPanel.add(headerInner);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(34, 85, 136));
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 13));
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this::handleLogin);
        formPanel.add(loginBtn, gbc);

        // Status label
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        // Default credentials hint
        JPanel hintPanel = new JPanel();
        hintPanel.setBackground(new Color(245, 245, 245));
        hintPanel.add(new JLabel("<html><small>Default Manager: admin / admin123</small></html>"));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(hintPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key triggers login
        passwordField.addActionListener(this::handleLogin);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password.");
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            statusLabel.setText("");
            this.dispose();
            // Polymorphism: open different UI based on user type
            if (user instanceof Manager) {
                new ManagerDashboard(user, userService).setVisible(true);
            } else {
                new CashierDashboard(user).setVisible(true);
            }
        } else {
            statusLabel.setText("Invalid username or password.");
            passwordField.setText("");
        }
    }
}
