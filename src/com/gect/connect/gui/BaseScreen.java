package com.gect.connect.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Base screen class for common UI settings.
 */
public abstract class BaseScreen extends JFrame {
    // WhatsApp-inspired Color Palette
    public static final Color WA_TEAL = new Color(7, 94, 84);       // Header background
    public static final Color WA_GREEN = new Color(37, 211, 102);    // Vibrant Accents (Primary Buttons)
    public static final Color WA_DARK_GREEN = new Color(18, 140, 126); // Darker accents
    public static final Color WA_CHAT_BG = new Color(236, 229, 221); // Chat background
    public static final Color WA_OUTGOING = new Color(220, 248, 198); // My message bubbles
    public static final Color WA_INCOMING = Color.WHITE;              // Other's message bubbles
    public static final Color WA_GRAY = new Color(110, 110, 110);    // Secondary text
    public static final Color WA_LIGHT_GRAY = new Color(240, 240, 240); // List background

    public BaseScreen(String title) {
        setTitle("GECT Connect - " + title);
        setSize(400, 650); // More standard mobile-app ratio
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE); // Default to white for cleaner look
    }

    protected void styleHeader(JPanel panel, JLabel title) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBackground(WA_TEAL);
        panel.setPreferredSize(new Dimension(400, 60));
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }

    protected void addBackButton(JPanel headerPanel) {
        JButton backBtn = new JButton("←");
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        
        // If header uses BorderLayout, add to WEST. If FlowLayout, add at start.
        if (headerPanel.getLayout() instanceof BorderLayout) {
            headerPanel.add(backBtn, BorderLayout.WEST);
        } else {
            headerPanel.add(backBtn, 0); // Add at index 0
        }
    }

    protected JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    protected JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    protected JButton createWAButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setForeground(WA_GREEN); // Set text color to GREEN as requested
        
        if (primary) {
            btn.setBackground(WA_TEAL); // Strong visible color for primary actions
            btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WA_TEAL, 1),
                BorderFactory.createEmptyBorder(11, 24, 11, 24)
            ));
        }
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
