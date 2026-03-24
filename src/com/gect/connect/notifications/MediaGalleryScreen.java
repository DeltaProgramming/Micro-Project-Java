package com.gect.connect.notifications;

import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Media gallery screen for GECT Connect.
 * Part of Team 6: Academic Notifications and Media Sharing Module.
 */
public class MediaGalleryScreen extends BaseScreen {
    private User currentUser;

    public MediaGalleryScreen(User user) {
        super("Media Gallery");
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Shared Media");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        contentPanel.setBackground(Color.WHITE);
        
        // Placeholder for shared images
        for (int i = 0; i < 12; i++) {
            JLabel placeholder = new JLabel("<html><center><div style='background-color:#eee; width:100px; height:100px; border:1px solid #ccc;'><br><br>Media " + (i+1) + "</div></center></html>");
            contentPanel.add(placeholder);
        }

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        JButton closeBtn = createWAButton("Close", false);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }
}
