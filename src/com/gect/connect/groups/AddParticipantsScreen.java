package com.gect.connect.groups;

import com.gect.connect.db.ContactManager;
import com.gect.connect.db.GroupManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.ChatGroup;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Add participants screen for GECT Connect.
 * Part of Team 5: Group Chat Module.
 */
public class AddParticipantsScreen extends BaseScreen {
    private User currentUser;
    private ChatGroup group;
    private GroupInfoScreen parent;
    private JPanel listPanel;

    public AddParticipantsScreen(User user, ChatGroup group, GroupInfoScreen parent) {
        super("Add Participants");
        this.currentUser = user;
        this.group = group;
        this.parent = parent;
        initUI();
        loadContacts();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Add to Group");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JButton closeBtn = createWAButton("Done", true);
        closeBtn.addActionListener(e -> {
            parent.loadMembers();
            dispose();
        });
        add(closeBtn, BorderLayout.SOUTH);
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
        JLabel detailsLabel = new JLabel(contact.getDepartment() + " | " + contact.getRole());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(WA_GRAY);
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        JButton addBtn = createWAButton("Add", true);
        addBtn.addActionListener(e -> {
            GroupManager gm = new GroupManager();
            if (gm.addParticipant(group.getGroupId(), contact.getUserId(), "Member")) {
                showInfo("Added " + contact.getFullName());
                addBtn.setEnabled(false);
                addBtn.setText("Added");
            } else {
                showError("Already a member or failed.");
            }
        });

        row.add(iconLabel, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);
        row.add(addBtn, BorderLayout.EAST);

        return row;
    }
}
