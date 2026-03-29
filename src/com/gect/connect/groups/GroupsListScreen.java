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

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Groups");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        
        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topActions.setOpaque(false);
        JButton refreshBtn = new JButton("<html><span style='font-size:18px;'>↺</span></html>");
        refreshBtn.setToolTipText("Refresh Groups");
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshGroups());
        topActions.add(refreshBtn);
        headerPanel.add(topActions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // List Panel
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBackground(BG_LIGHT);
        groupList.setFixedCellHeight(80);
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

        // Bottom Action Bar
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(CARD_WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DIVIDER));

        JButton createBtn = createWAButton("ADD GROUP", true);
        createBtn.addActionListener(e -> new CreateGroupScreen(currentUser, this).setVisible(true));
        
        JButton infoBtn = createWAButton("INFO", false);
        infoBtn.addActionListener(e -> {
            ChatGroup selected = groupList.getSelectedValue();
            if (selected != null) new GroupInfoScreen(currentUser, selected, this).setVisible(true);
            else showInfo("Select a group first");
        });

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
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

    // Custom Renderer for modern list items
    private class GroupListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ChatGroup group = (ChatGroup) value;
            JPanel panel = new JPanel(new BorderLayout(15, 10));
            panel.setBackground(isSelected ? HOVER_COLOR : CARD_WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
            ));

            // Circle Icon Placeholder
            JLabel icon = new JLabel("<html><div style='background-color:#3b82f6; width:45px; height:45px; border-radius:22px; color:white; text-align:center;'><br>" + group.getGroupName().charAt(0) + "</div></html>");
            panel.add(icon, BorderLayout.WEST);

            // Group Info (Name & Description)
            JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            textPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(group.getGroupName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLabel.setForeground(TEXT_MAIN);
            textPanel.add(nameLabel);

            JLabel descLabel = new JLabel(group.getDescription());
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setForeground(TEXT_SECONDARY);
            textPanel.add(descLabel);

            panel.add(textPanel, BorderLayout.CENTER);

            // Right Info (Type)
            JLabel typeLabel = new JLabel(group.getGroupType());
            typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            typeLabel.setForeground(ACCENT_BLUE);
            panel.add(typeLabel, BorderLayout.EAST);

            return panel;
        }
    }
}
