package com.gect.connect.auth;

import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard after successful login.
 * Part of Team 1: User Registration and Authentication Module.
 */
public class Dashboard extends BaseScreen {
    private User currentUser;

    public Dashboard(User user) {
        super("Dashboard");
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 100));

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        JLabel titleLabel = new JLabel("  GECT Connect");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topHeader.add(titleLabel, BorderLayout.WEST);

        JPanel headerIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        headerIcons.setOpaque(false);
        
        JButton searchBtn = new JButton("🔍");
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> new com.gect.connect.contacts.SearchUsersScreen(currentUser).setVisible(true));

        JButton moreBtn = new JButton("⋮");
        moreBtn.setForeground(Color.WHITE);
        moreBtn.setContentAreaFilled(false);
        moreBtn.setBorderPainted(false);
        moreBtn.setFocusPainted(false);
        moreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        moreBtn.addActionListener(e -> new com.gect.connect.settings.SettingsScreen(currentUser).setVisible(true));

        headerIcons.add(searchBtn);
        headerIcons.add(moreBtn);
        topHeader.add(headerIcons, BorderLayout.EAST);

        headerPanel.add(topHeader, BorderLayout.NORTH);

        // Tab-like buttons (Interactive)
        JPanel tabPanel = new JPanel(new GridLayout(1, 3));
        tabPanel.setOpaque(false);
        
        JButton chatsTab = createTabButton("CHATS", true);
        JButton groupsTab = createTabButton("GROUPS", false);
        JButton notifyTab = createTabButton("NOTIFICATIONS", false);

        chatsTab.addActionListener(e -> new com.gect.connect.chat.ChatListScreen(currentUser).setVisible(true));
        groupsTab.addActionListener(e -> new com.gect.connect.groups.GroupsListScreen(currentUser).setVisible(true));
        notifyTab.addActionListener(e -> new com.gect.connect.notifications.NotificationsFeedScreen(currentUser).setVisible(true));

        tabPanel.add(chatsTab);
        tabPanel.add(groupsTab);
        tabPanel.add(notifyTab);
        
        headerPanel.add(tabPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Main Grid Content
        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JButton profileBtn = createMenuButton("My Profile", "👤");
        JButton contactsBtn = createMenuButton("Contacts", "👥");
        JButton chatsBtn = createMenuButton("Direct Chats", "💬");
        JButton groupsBtn = createMenuButton("Group Chats", "🏘️");
        JButton notificationsBtn = createMenuButton("Academic", "📢");
        JButton settingsBtn = createMenuButton("Settings", "⚙️");

        contentPanel.add(profileBtn);
        contentPanel.add(contactsBtn);
        contentPanel.add(chatsBtn);
        contentPanel.add(groupsBtn);
        contentPanel.add(notificationsBtn);
        contentPanel.add(settingsBtn);

        add(contentPanel, BorderLayout.CENTER);

        // Logout Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(WA_LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton logoutBtn = new JButton("LOGOUT (" + currentUser.getFullName() + ")");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.RED);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
        footerPanel.add(logoutBtn, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Action Listeners
        profileBtn.addActionListener(e -> new com.gect.connect.profile.ProfileViewScreen(currentUser).setVisible(true));
        contactsBtn.addActionListener(e -> new com.gect.connect.contacts.ContactsListScreen(currentUser).setVisible(true));
        chatsBtn.addActionListener(e -> new com.gect.connect.chat.ChatListScreen(currentUser).setVisible(true));
        groupsBtn.addActionListener(e -> new com.gect.connect.groups.GroupsListScreen(currentUser).setVisible(true));
        notificationsBtn.addActionListener(e -> new com.gect.connect.notifications.NotificationsFeedScreen(currentUser).setVisible(true));
        settingsBtn.addActionListener(e -> new com.gect.connect.settings.SettingsScreen(currentUser).setVisible(true));
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setForeground(active ? Color.WHITE : new Color(255, 255, 255, 180));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, active ? Color.WHITE : new Color(255, 255, 255, 0)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton("<html><center><span style='font-size:24px;'>" + icon + "</span><br><br>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(WA_GREEN); // Green text as requested
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WA_LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(WA_LIGHT_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }
}
