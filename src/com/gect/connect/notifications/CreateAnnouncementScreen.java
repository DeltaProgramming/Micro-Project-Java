package com.gect.connect.notifications;

import com.gect.connect.db.NotificationManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Create announcement screen for faculty in GECT Connect.
 * Part of Team 6: Academic Notifications and Media Sharing Module.
 */
public class CreateAnnouncementScreen extends BaseScreen {
    private User currentUser;
    private NotificationsFeedScreen parent;
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> deptCombo;
    private File attachedFile;

    public CreateAnnouncementScreen(User user, NotificationsFeedScreen parent) {
        super("New Announcement");
        this.currentUser = user;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Post Announcement");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelField(contentPanel, gbc, "Title:", titleField = createTextField(20), row++);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        contentPanel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 1;
        contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentPanel.add(new JScrollPane(contentArea), gbc);
        row++;

        String[] departments = {"All", "Mechanical", "Civil", "Production", "EC", "EEE", "Architecture", "CSE", "MCA", "M.Tech"};
        deptCombo = new JComboBox<>(departments);
        addLabelField(contentPanel, gbc, "Target Dept:", deptCombo, row++);

        JButton attachBtn = new JButton("📎 Attach File");
        attachBtn.setForeground(WA_GREEN); // Green text
        attachBtn.setBackground(Color.WHITE);
        attachBtn.setFocusPainted(false);
        JLabel fileLabel = new JLabel("No file attached");
        attachBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                attachedFile = fc.getSelectedFile();
                fileLabel.setText(attachedFile.getName());
            }
        });
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        contentPanel.add(attachBtn, gbc);
        gbc.gridx = 1;
        contentPanel.add(fileLabel, gbc);
        row++;

        JButton postBtn = createWAButton("Post Announcement", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(postBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        // Listeners
        postBtn.addActionListener(e -> handlePost());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handlePost() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        String dept = (String) deptCombo.getSelectedItem();
        if ("All".equals(dept)) dept = null;

        if (title.isEmpty() || content.isEmpty()) {
            showError("Title and content are required.");
            return;
        }

        String filePath = attachedFile != null ? attachedFile.getAbsolutePath() : null;
        if (NotificationManager.postNotification(title, content, currentUser.getUserId(), dept, filePath)) {
            showInfo("Announcement posted successfully!");
            parent.loadNotifications();
            dispose();
        } else {
            showError("Failed to post announcement.");
        }
    }
}
