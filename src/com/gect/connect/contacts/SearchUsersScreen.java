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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Search Users");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(WA_LIGHT_GRAY);
        searchField = createTextField(20);
        JButton searchBtn = createWAButton("Search", true);
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchBtn, BorderLayout.EAST);
        add(searchBarPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        add(new JScrollPane(resultsPanel), BorderLayout.CENTER);

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
            resultsPanel.add(new JLabel("No users found."));
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
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 70));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, WA_LIGHT_GRAY));

        JLabel iconLabel = new JLabel("<html><div style='background-color:#128C7E; width:40px; height:40px; border-radius:20px; color:white; text-align:center;'><br>" + user.getFullName().charAt(0) + "</div></html>");
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel detailsLabel = new JLabel(user.getDepartment() + " | " + user.getRole());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(WA_GRAY);
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        JButton addBtn = createWAButton("Add", true);
        addBtn.addActionListener(e -> {
            if (ContactManager.sendRequest(currentUser.getUserId(), user.getUserId())) {
                showInfo("Request sent to " + user.getFullName());
                addBtn.setEnabled(false);
                addBtn.setText("Sent");
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
