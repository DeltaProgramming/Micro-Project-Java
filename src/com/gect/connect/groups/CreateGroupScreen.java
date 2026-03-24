package com.gect.connect.groups;

import com.gect.connect.db.GroupManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Create group screen for GECT Connect.
 * Part of Team 5: Group Chat Module.
 */
public class CreateGroupScreen extends BaseScreen {
    private User currentUser;
    private GroupsListScreen parent;
    private JTextField nameField, descField;
    private JComboBox<String> typeCombo;

    public CreateGroupScreen(User user, GroupsListScreen parent) {
        super("Create New Group");
        this.currentUser = user;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Create Group");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelField(contentPanel, gbc, "Group Name:", nameField = createTextField(20), row++);
        addLabelField(contentPanel, gbc, "Description:", descField = createTextField(20), row++);
        
        String[] types = {"Departmental", "Interest"};
        typeCombo = new JComboBox<>(types);
        addLabelField(contentPanel, gbc, "Group Type:", typeCombo, row++);

        JButton createBtn = createWAButton("Create Group", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        createBtn.addActionListener(e -> handleCreate());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handleCreate() {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (name.isEmpty()) {
            showError("Group name is required.");
            return;
        }

        GroupManager gm = new GroupManager();
        if (gm.createGroup(name, desc, currentUser.getUserId(), type)) {
            showInfo("Group created successfully!");
            parent.refreshGroups();
            dispose();
        } else {
            showError("Failed to create group.");
        }
    }
}
