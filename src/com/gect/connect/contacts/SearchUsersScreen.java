package com.gect.connect.contacts;

import com.gect.connect.db.ContactManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Search users screen for GECT Connect.
 * Part of Team 3: Contact Management Module.
 */
public class SearchUsersScreen extends BaseScreen {
    private User currentUser;
    private JTextField searchField;
    private JPanel resultsPanel;

    public SearchUsersScreen(User user) {
        super("Search Users");
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Search Users");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setBackground(BG_LIGHT);
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        searchField = createTextField(20);
        searchField.setToolTipText("Search by name or email...");
        
        JButton searchBtn = createWAButton("SEARCH", true);
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchBtn, BorderLayout.EAST);
        
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(headerPanel, BorderLayout.NORTH);
        topWrapper.add(searchBarPanel, BorderLayout.SOUTH);
        add(topWrapper, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(BG_LIGHT);
        
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        resultsPanel.removeAll();
        List<User> results = ContactManager.searchUsers(query);
        
        if (results.isEmpty()) {
            JLabel emptyLabel = new JLabel("No users found.");
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(Box.createVerticalStrut(50));
            resultsPanel.add(emptyLabel);
        } else {
            for (User user : results) {
                if (user.getUserId() != currentUser.getUserId()) {
                    resultsPanel.add(createResultRow(user));
                }
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createResultRow(User user) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(CARD_WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel iconLabel = new JLabel("<html><div style='background-color:#3b82f6; width:45px; height:45px; border-radius:22px; color:white; text-align:center;'><br>" + user.getFullName().charAt(0) + "</div></html>");

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(TEXT_MAIN);
        JLabel detailsLabel = new JLabel(user.getDepartment() + " | " + user.getRole());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsLabel.setForeground(TEXT_SECONDARY);
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        JButton addBtn = createWAButton("ADD", true);
        addBtn.setPreferredSize(new Dimension(80, 35));
        addBtn.addActionListener(e -> {
            if (ContactManager.sendRequest(currentUser.getUserId(), user.getUserId())) {
                showInfo("Request sent to " + user.getFullName());
                addBtn.setEnabled(false);
                addBtn.setText("SENT");
                addBtn.setBackground(TEXT_SECONDARY);
            } else {
                showError("Request failed. Already contacts or pending?");
            }
        });

        row.add(iconLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);
        row.add(addBtn, BorderLayout.EAST);

        return row;
    }
}
