package com.gect.connect.profile;

import com.gect.connect.db.ProfileManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Change profile picture screen for GECT Connect.
 * Part of Team 2: User Profile Management Module.
 */
public class ChangeProfilePictureScreen extends BaseScreen {
    private User user;
    private JLabel previewLabel;
    private File selectedFile;

    public ChangeProfilePictureScreen(User user) {
        super("Change Profile Picture");
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Change Picture");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        previewLabel = new JLabel("<html><center><div style='background-color:#eee; width:200px; height:200px; border: 1px solid #ccc;'><br><br><br>Preview Area</div></center></html>");
        previewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(previewLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        JButton chooseBtn = createWAButton("Choose File", false);
        JButton saveBtn = createWAButton("Save Picture", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        chooseBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(chooseBtn);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(saveBtn);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(cancelBtn);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        chooseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                previewLabel.setText("<html><center>File Selected:<br>" + selectedFile.getName() + "</center></html>");
            }
        });

        saveBtn.addActionListener(e -> {
            if (selectedFile != null) {
                if (ProfileManager.updateProfilePicture(user.getUserId(), selectedFile.getAbsolutePath())) {
                    user.setProfilePicPath(selectedFile.getAbsolutePath());
                    showInfo("Profile picture updated!");
                    dispose();
                } else {
                    showError("Failed to update picture.");
                }
            } else {
                showError("Please choose a file first.");
            }
        });

        cancelBtn.addActionListener(e -> dispose());
    }
}
