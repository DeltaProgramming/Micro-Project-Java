package com.gect.connect.main;

import com.gect.connect.auth.LoginScreen;
import javax.swing.*;

/**
 * Main class to launch the GECT Connect Application.
 */
public class Main {
    public static void main(String[] args) {
        // Use modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the Login Screen
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}
