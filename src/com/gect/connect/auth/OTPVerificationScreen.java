package com.gect.connect.auth;

import com.gect.connect.gui.BaseScreen;
import javax.swing.*;
import java.awt.*;

/**
 * OTP Verification screen for GECT Connect.
 * Part of Team 1: User Registration and Authentication Module.
 */
public class OTPVerificationScreen extends BaseScreen {
    private JTextField otpField;

    public OTPVerificationScreen() {
        super("OTP Verification");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Verify Email");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel infoLabel = new JLabel("<html><center>An OTP has been sent to your college email.<br>Please enter it below to verify.</center></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(infoLabel, gbc);

        otpField = createTextField(10);
        otpField.setHorizontalAlignment(SwingConstants.CENTER);
        otpField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridy = 1;
        contentPanel.add(otpField, gbc);

        JButton verifyBtn = createWAButton("Verify", true);
        JButton resendBtn = createWAButton("Resend OTP", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(verifyBtn);
        buttonPanel.add(resendBtn);
        
        gbc.gridy = 2;
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        verifyBtn.addActionListener(e -> {
            String otp = otpField.getText();
            if ("123456".equals(otp)) { // Simulated OTP
                showInfo("Verification successful!");
                dispose();
            } else {
                showError("Invalid OTP. Try '123456'.");
            }
        });

        resendBtn.addActionListener(e -> showInfo("A new OTP has been sent."));
    }
}
