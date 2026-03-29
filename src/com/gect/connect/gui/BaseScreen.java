package com.gect.connect.gui;

import java.awt.*;
import javax.swing.*;

/**
 * Base screen class for common UI settings.
 */
public abstract class BaseScreen extends JFrame {
    // Modern Professional Color Palette
    public static final Color PRIMARY_DARK = new Color(30, 41, 59);    // Slate 800 (Headers)
    public static final Color ACCENT_BLUE = new Color(59, 130, 246);   // Blue 500 (Primary buttons/icons)
    public static final Color ACCENT_DARK_BLUE = new Color(37, 99, 235); // Blue 600
    public static final Color BG_LIGHT = new Color(248, 250, 252);     // Slate 50 (App background)
    public static final Color CARD_WHITE = Color.WHITE;                // Cards/Bubbles
    public static final Color TEXT_MAIN = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate 500
    public static final Color DIVIDER = new Color(226, 232, 240);      // Slate 200
    public static final Color HOVER_COLOR = new Color(241, 245, 249);  // Slate 100

    // Backward compatibility aliases
    public static final Color WA_TEAL = PRIMARY_DARK;
    public static final Color WA_GREEN = ACCENT_BLUE;
    public static final Color WA_DARK_GREEN = ACCENT_DARK_BLUE;
    public static final Color WA_CHAT_BG = BG_LIGHT;
    public static final Color WA_OUTGOING = new Color(219, 234, 254);
    public static final Color WA_INCOMING = CARD_WHITE;
    public static final Color WA_GRAY = TEXT_SECONDARY;
    public static final Color WA_LIGHT_GRAY = HOVER_COLOR;

    public BaseScreen(String title) {
        setTitle("GECT Connect - " + title);
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT); 
    }

    protected void styleHeader(JPanel panel, JLabel title) {
        panel.setLayout(new BorderLayout());
        panel.setBackground(PRIMARY_DARK);
        panel.setPreferredSize(new Dimension(400, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.CENTER);
    }

    protected void addBackButton(JPanel headerPanel) {
        JButton backBtn = new JButton("<html><span style='font-size:18px;'>←</span></html>");
        backBtn.setForeground(Color.WHITE);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        
        headerPanel.add(backBtn, BorderLayout.WEST);
    }

    protected JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIVIDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(CARD_WHITE);
        return field;
    }

    protected JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIVIDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(CARD_WHITE);
        return field;
    }

    protected JButton createWAButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (primary) {
            btn.setBackground(ACCENT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        } else {
            btn.setBackground(CARD_WHITE);
            btn.setForeground(ACCENT_BLUE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(11, 24, 11, 24)
            ));
        }
        return btn;
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
