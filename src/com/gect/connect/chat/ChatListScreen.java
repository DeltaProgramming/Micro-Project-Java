package com.gect.connect.chat;

import com.gect.connect.db.ContactManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Chat list screen for GECT Connect.
 * Part of Team 4: Individual Chat Module.
 */
public class ChatListScreen extends BaseScreen {
    private User currentUser;
    private JPanel listPanel;

    public ChatListScreen(User user) {
        super("Chats");
        this.currentUser = user;
        initUI();
        loadRecentChats();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Chats");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // FAB-like New Chat Button
        JButton newChatBtn = new JButton("<html><div style='background-color:#25D366; width:50px; height:50px; border-radius:25px; color:white; text-align:center;'><br><span style='font-size:20px;'>+</span></div></html>");
        newChatBtn.setContentAreaFilled(false);
        newChatBtn.setBorderPainted(false);
        newChatBtn.setFocusPainted(false);
        newChatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel fabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fabPanel.setOpaque(false);
        fabPanel.add(newChatBtn);
        
        // Use a layered pane or just south for simplicity in this mobile-ratio app
        add(fabPanel, BorderLayout.SOUTH);

        newChatBtn.addActionListener(e -> {
            // Logic to select a contact
            List<User> contacts = ContactManager.getContacts(currentUser.getUserId());
            if (contacts.isEmpty()) {
                showInfo("No contacts found. Please add contacts first.");
            } else {
                User target = (User) JOptionPane.showInputDialog(this, "Select a contact:", "New Chat",
                        JOptionPane.QUESTION_MESSAGE, null, contacts.toArray(), contacts.get(0));
                if (target != null) {
                    new ChatWindow(currentUser, target).setVisible(true);
                }
            }
        });
        add(newChatBtn, BorderLayout.SOUTH);
    }

    private void loadRecentChats() {
        listPanel.removeAll();
        // For simplicity, we'll list all contacts as recent chats
        List<User> contacts = ContactManager.getContacts(currentUser.getUserId());
        
        if (contacts.isEmpty()) {
            JLabel emptyLabel = new JLabel("No chats yet.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);
        } else {
            for (User contact : contacts) {
                listPanel.add(createChatRow(contact));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createChatRow(User contact) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 70));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, WA_LIGHT_GRAY));

        JLabel iconLabel = new JLabel("<html><div style='background-color:#128C7E; width:40px; height:40px; border-radius:20px; color:white; text-align:center;'><br>" + contact.getFullName().charAt(0) + "</div></html>");
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(contact.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lastMsgLabel = new JLabel("Click to open chat history...");
        lastMsgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastMsgLabel.setForeground(WA_GRAY);
        infoPanel.add(nameLabel);
        infoPanel.add(lastMsgLabel);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);

        // Click to open chat
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new ChatWindow(currentUser, contact).setVisible(true);
            }
        });

        return row;
    }
}
