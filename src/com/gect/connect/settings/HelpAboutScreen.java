package com.gect.connect.settings;

import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Help and About screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class HelpAboutScreen extends BaseScreen {
    private User currentUser;

    public HelpAboutScreen(User user) {
        super("Help & About");
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Help & Support");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel logoLabel = new JLabel("<html><center><div style='background-color:#128C7E; width:100px; height:100px; border-radius:50px; color:white; text-align:center;'><br><br><span style='font-size:30px;'>GECT</span></div></center></html>");
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        JLabel versionLabel = new JLabel("GECT Connect v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(versionLabel);

        JLabel descLabel = new JLabel("<html><center>The exclusive campus messaging app<br>for Government Engineering College Thrissur.</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(WA_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(descLabel);

        contentPanel.add(Box.createVerticalStrut(30));

        JLabel supportLabel = new JLabel("<html><center>For support, contact:<br><b>support@gectcr.ac.in</b></center></html>");
        supportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        supportLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(supportLabel);

        add(contentPanel, BorderLayout.CENTER);

        JButton closeBtn = createWAButton("Close", true);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }
}
