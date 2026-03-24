package com.gect.connect.contacts;

import com.gect.connect.db.ContactManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Contacts list screen for GECT Connect.
 * Part of Team 3: Contact Management Module.
 */
public class ContactsListScreen extends BaseScreen {
    private User currentUser;
    private JPanel listPanel;

    public ContactsListScreen(User user) {
        super("Contacts");
        this.currentUser = user;
        initUI();
        loadContacts();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Contacts");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(WA_LIGHT_GRAY);
        JButton searchBtn = createWAButton("Search New Contacts", true);
        JButton requestsBtn = createWAButton("Pending Requests", false);
        
        bottomPanel.add(searchBtn);
        bottomPanel.add(requestsBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        searchBtn.addActionListener(e -> new SearchUsersScreen(currentUser).setVisible(true));
        requestsBtn.addActionListener(e -> new PendingRequestsScreen(currentUser).setVisible(true));
    }

    private void loadContacts() {
        listPanel.removeAll();
        List<User> contacts = ContactManager.getContacts(currentUser.getUserId());
        
        if (contacts.isEmpty()) {
            JLabel emptyLabel = new JLabel("No contacts found.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);
        } else {
            for (User contact : contacts) {
                listPanel.add(createContactRow(contact));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createContactRow(User contact) {
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
        JLabel deptLabel = new JLabel(contact.getDepartment() + " | " + contact.getRole());
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deptLabel.setForeground(WA_GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(deptLabel);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);

        // Options button
        JButton optionsBtn = new JButton("⋮");
        optionsBtn.setBorderPainted(false);
        optionsBtn.setContentAreaFilled(false);
        row.add(optionsBtn, BorderLayout.EAST);

        return row;
    }
}
