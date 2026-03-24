package com.gect.connect.groups;

import com.gect.connect.db.GroupManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.ChatGroup;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Screen showing the list of groups for the logged-in user.
 * Part of Team 5: Group Chat Module.
 */
public class GroupsListScreen extends BaseScreen {
    private User currentUser;
    private GroupManager groupManager;
    private DefaultListModel<ChatGroup> listModel;
    private JList<ChatGroup> groupList;

    public GroupsListScreen(User currentUser) {
        super("GECT Connect");
        this.currentUser = currentUser;
        this.groupManager = new GroupManager();
        this.listModel = new DefaultListModel<>();
        this.groupList = new JList<>(listModel);
        
        initUI();
        refreshGroups();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header Panel (WhatsApp Teal)
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  GECT Connect", JLabel.LEFT);
        styleHeader(headerPanel, titleLabel);
        addBackButton(headerPanel);
        
        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topActions.setOpaque(false);
        JButton refreshBtn = new JButton("↺"); // Unicode refresh
        refreshBtn.setToolTipText("Refresh Groups");
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(WA_TEAL);
        refreshBtn.setBorder(null);
        refreshBtn.addActionListener(e -> refreshGroups());
        topActions.add(refreshBtn);
        headerPanel.add(topActions, BorderLayout.EAST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // List Panel
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBackground(Color.WHITE);
        groupList.setFixedCellHeight(70);
        groupList.setCellRenderer(new GroupListRenderer());
        
        // Double-click to open chat
        groupList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    ChatGroup selected = groupList.getSelectedValue();
                    if (selected != null) new GroupChatWindow(currentUser, selected).setVisible(true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(groupList);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar (FAB-like for creating group)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton createBtn = createWAButton("ADD GROUP", true);
        createBtn.addActionListener(e -> new CreateGroupScreen(currentUser, this).setVisible(true));
        
        JButton infoBtn = createWAButton("INFO", false);
        infoBtn.addActionListener(e -> {
            ChatGroup selected = groupList.getSelectedValue();
            if (selected != null) new GroupInfoScreen(currentUser, selected, this).setVisible(true);
            else showInfo("Select a group first");
        });

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrapper.setOpaque(false);
        btnWrapper.add(infoBtn);
        btnWrapper.add(createBtn);
        
        footerPanel.add(btnWrapper, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void refreshGroups() {
        listModel.clear();
        List<ChatGroup> groups = groupManager.getGroupsForUser(currentUser.getUserId());
        for (ChatGroup g : groups) {
            listModel.addElement(g);
        }
    }

    // Custom Renderer for WhatsApp-style list items
    private class GroupListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ChatGroup group = (ChatGroup) value;
            JPanel panel = new JPanel(new BorderLayout(15, 10));
            panel.setBackground(isSelected ? new Color(240, 240, 240) : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            // Circle Icon Placeholder
            JLabel icon = new JLabel("<html><div style='background-color:#128C7E; width:45px; height:45px; border-radius:22px; color:white; text-align:center;'><br>" + group.getGroupName().charAt(0) + "</div></html>");
            panel.add(icon, BorderLayout.WEST);

            // Group Info (Name & Description)
            JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            textPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(group.getGroupName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            textPanel.add(nameLabel);

            JLabel descLabel = new JLabel(group.getDescription());
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descLabel.setForeground(WA_GRAY);
            textPanel.add(descLabel);

            panel.add(textPanel, BorderLayout.CENTER);

            // Right Info (Date/Type)
            JLabel typeLabel = new JLabel(group.getGroupType());
            typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            typeLabel.setForeground(WA_GREEN);
            panel.add(typeLabel, BorderLayout.EAST);

            return panel;
        }
    }
}
