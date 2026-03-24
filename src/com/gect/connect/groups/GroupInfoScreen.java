package com.gect.connect.groups;

import com.gect.connect.db.GroupManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.ChatGroup;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Group info screen for GECT Connect.
 * Part of Team 5: Group Chat Module.
 */
public class GroupInfoScreen extends BaseScreen {
    private User currentUser;
    private ChatGroup group;
    private GroupsListScreen parent;
    private JPanel membersPanel;

    public GroupInfoScreen(User user, ChatGroup group, GroupsListScreen parent) {
        super("Group Info: " + group.getGroupName());
        this.currentUser = user;
        this.group = group;
        this.parent = parent;
        initUI();
        loadMembers();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Group Details");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Group Icon
        JLabel icon = new JLabel("<html><center><div style='background-color:#128C7E; width:80px; height:80px; border-radius:40px; color:white; text-align:center;'><br><br><span style='font-size:30px;'>" + group.getGroupName().charAt(0) + "</span></div></center></html>");
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(icon);
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel nameLabel = new JLabel(group.getGroupName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameLabel);

        JLabel descLabel = new JLabel(group.getDescription());
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        descLabel.setForeground(WA_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(descLabel);

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel mTitle = new JLabel("Members List");
        mTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(mTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        membersPanel.setBackground(Color.WHITE);
        contentPanel.add(new JScrollPane(membersPanel));

        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(WA_LIGHT_GRAY);
        JButton leaveBtn = createWAButton("Leave Group", false);
        leaveBtn.setForeground(Color.RED);
        JButton addBtn = createWAButton("Add Participants", true);
        
        bottomPanel.add(leaveBtn);
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        addBtn.addActionListener(e -> new AddParticipantsScreen(currentUser, group, this).setVisible(true));
        leaveBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave this group?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                GroupManager gm = new GroupManager();
                if (gm.removeParticipant(group.getGroupId(), currentUser.getUserId())) {
                    showInfo("You left the group.");
                    parent.refreshGroups();
                    dispose();
                } else {
                    showError("Failed to leave group.");
                }
            }
        });
    }

    public void loadMembers() {
        membersPanel.removeAll();
        GroupManager gm = new GroupManager();
        List<User> members = gm.getGroupMembers(group.getGroupId());
        for (User m : members) {
            membersPanel.add(createMemberRow(m));
        }
        membersPanel.revalidate();
        membersPanel.repaint();
    }

    private JPanel createMemberRow(User m) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 50));
        JLabel name = new JLabel(m.getFullName() + (m.getUserId() == currentUser.getUserId() ? " (You)" : ""));
        name.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        name.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        row.add(name, BorderLayout.CENTER);
        return row;
    }
}
