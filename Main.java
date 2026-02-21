package com.citybookshop;

import com.citybookshop.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fallback to default
        }

        // Launch on Event Dispatch Thread (EDT) - Swing best practice
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
