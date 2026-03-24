package com.gect.connect.contacts;

import com.gect.connect.db.ContactManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pending requests screen for GECT Connect.
 * Part of Team 3: Contact Management Module.
 */
public class PendingRequestsScreen extends BaseScreen {
    private User currentUser;
    private JPanel listPanel;

    public PendingRequestsScreen(User user) {
        super("Pending Requests");
        this.currentUser = user;
        initUI();
        loadRequests();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Pending Requests");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JButton closeBtn = createWAButton("Close", false);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }

    private void loadRequests() {
        listPanel.removeAll();
        List<User> requests = ContactManager.getPendingRequests(currentUser.getUserId());
        
        if (requests.isEmpty()) {
            JLabel emptyLabel = new JLabel("No pending requests.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);
        } else {
            for (User request : requests) {
                listPanel.add(createRequestRow(request));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createRequestRow(User request) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 80));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, WA_LIGHT_GRAY));

        JLabel iconLabel = new JLabel("<html><div style='background-color:#128C7E; width:40px; height:40px; border-radius:20px; color:white; text-align:center;'><br>" + request.getFullName().charAt(0) + "</div></html>");
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(request.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel detailsLabel = new JLabel(request.getDepartment() + " | " + request.getRole());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(WA_GRAY);
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        JButton acceptBtn = createWAButton("Accept", true);
        JButton rejectBtn = createWAButton("Reject", false);
        
        acceptBtn.addActionListener(e -> {
            if (ContactManager.updateContactStatus(currentUser.getUserId(), request.getUserId(), "accepted")) {
                showInfo("Contact added!");
                loadRequests();
            }
        });

        rejectBtn.addActionListener(e -> {
            if (ContactManager.updateContactStatus(currentUser.getUserId(), request.getUserId(), "rejected")) {
                showInfo("Request rejected.");
                loadRequests();
            }
        });

        actionPanel.add(acceptBtn);
        actionPanel.add(rejectBtn);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);
        row.add(actionPanel, BorderLayout.EAST);

        return row;
    }
}
