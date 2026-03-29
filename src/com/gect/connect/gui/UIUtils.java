package com.gect.connect.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UIUtils provides reusable modular UI components for GECT Connect.
 */
public class UIUtils {

    public static JPanel createSettingsRow(String title, String desc, String icon, Runnable action) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setBackground(BaseScreen.CARD_WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BaseScreen.DIVIDER),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        row.add(iconLbl, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(BaseScreen.PRIMARY_DARK);
        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLbl.setForeground(BaseScreen.TEXT_SECONDARY);
        textPanel.add(titleLbl);
        textPanel.add(descLbl);
        row.add(textPanel, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                row.setBackground(BaseScreen.HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                row.setBackground(BaseScreen.CARD_WHITE);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (action != null) action.run();
            }
        });

        return row;
    }

    public static JPanel createNotificationCard(String title, String content, String time, boolean isRead, Runnable onRead) {
        JPanel card = new JPanel(new BorderLayout(15, 8));
        card.setBackground(isRead ? BaseScreen.CARD_WHITE : new Color(239, 246, 255)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BaseScreen.DIVIDER),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Modern Read Indicator (Dot)
        if (!isRead) {
            JPanel indicator = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BaseScreen.ACCENT_BLUE);
                    g2.fillOval(0, 0, 10, 10);
                }
            };
            indicator.setOpaque(false);
            indicator.setPreferredSize(new Dimension(10, 10));
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
            wrapper.setOpaque(false);
            wrapper.add(indicator);
            card.add(wrapper, BorderLayout.WEST);
        }

        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        centerPanel.setOpaque(false);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(BaseScreen.PRIMARY_DARK);
        
        JLabel contentLbl = new JLabel("<html><body style='width: 250px'>" + content + "</body></html>");
        contentLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentLbl.setForeground(BaseScreen.TEXT_SECONDARY);
        
        centerPanel.add(titleLbl, BorderLayout.NORTH);
        centerPanel.add(contentLbl, BorderLayout.CENTER);
        card.add(centerPanel, BorderLayout.CENTER);

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLbl.setForeground(BaseScreen.TEXT_SECONDARY);
        card.add(timeLbl, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onRead != null) onRead.run();
                card.setBackground(BaseScreen.CARD_WHITE);
                card.revalidate();
                card.repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(BaseScreen.HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(isRead ? BaseScreen.CARD_WHITE : new Color(239, 246, 255));
            }
        });

        return card;
    }
}
