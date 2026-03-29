package com.gect.connect.auth;

import com.gect.connect.db.AuthManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.Staff;
import com.gect.connect.model.Student;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Registration form for GECT Connect.
 * Part of Team 1: User Registration and Authentication Module.
 */
public class RegistrationForm extends BaseScreen {
    private JTextField nameField, emailField, rollEmpIdField, mobileField;
    private JPasswordField passField, confirmPassField;
    private JComboBox<String> roleCombo, deptCombo;

    public RegistrationForm() {
        super("Registration");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Create Account");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 25, 10, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(nameLabel, gbc);
        gbc.gridy = row++;
        nameField = createTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridy = row++;
        JLabel emailLabel = new JLabel("College Email (@gectcr.ac.in)");
        emailLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(emailLabel, gbc);
        gbc.gridy = row++;
        emailField = createTextField(20);
        formPanel.add(emailField, gbc);
        
        gbc.gridy = row++;
        JLabel deptLabel = new JLabel("Department");
        deptLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(deptLabel, gbc);
        gbc.gridy = row++;
        String[] departments = {"Mechanical", "Civil", "Production", "EC", "EEE", "Architecture", "CSE", "MCA", "M.Tech"};
        deptCombo = new JComboBox<>(departments);
        deptCombo.setBackground(CARD_WHITE);
        formPanel.add(deptCombo, gbc);

        gbc.gridy = row++;
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(roleLabel, gbc);
        gbc.gridy = row++;
        String[] roles = {"Student", "Staff"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setBackground(CARD_WHITE);
        formPanel.add(roleCombo, gbc);

        gbc.gridy = row++;
        JLabel rollLabel = new JLabel("Roll No / Employee ID");
        rollLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(rollLabel, gbc);
        gbc.gridy = row++;
        rollEmpIdField = createTextField(20);
        formPanel.add(rollEmpIdField, gbc);

        gbc.gridy = row++;
        JLabel mobileLabel = new JLabel("Mobile Number");
        mobileLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(mobileLabel, gbc);
        gbc.gridy = row++;
        mobileField = createTextField(20);
        formPanel.add(mobileField, gbc);

        gbc.gridy = row++;
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(passLabel, gbc);
        gbc.gridy = row++;
        passField = createPasswordField(20);
        formPanel.add(passField, gbc);

        gbc.gridy = row++;
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(confirmLabel, gbc);
        gbc.gridy = row++;
        confirmPassField = createPasswordField(20);
        formPanel.add(confirmPassField, gbc);

        // Buttons
        gbc.gridy = row++;
        gbc.insets = new Insets(25, 25, 10, 25);
        JButton registerBtn = createWAButton("REGISTER", true);
        formPanel.add(registerBtn, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(5, 25, 30, 25);
        JButton backBtn = createWAButton("ALREADY HAVE AN ACCOUNT? LOGIN", false);
        formPanel.add(backBtn, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        registerBtn.addActionListener(e -> handleRegistration());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim().toLowerCase(); // Trim and lower case for consistency
        String rollEmpId = rollEmpIdField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        String dept = (String) deptCombo.getSelectedItem();

        // Validations
        if (name.isEmpty() || email.isEmpty() || rollEmpId.isEmpty() || password.isEmpty()) {
            showError("Please fill all mandatory fields.");
            return;
        }

        if (!email.endsWith("@gectcr.ac.in")) {
            showError("Only college email (@gectcr.ac.in) is allowed.");
            return;
        }

        if (!password.equals(confirmPass)) {
            showError("Passwords do not match!");
            return;
        }

        if (AuthManager.isEmailRegistered(email)) {
            showError("Email already registered!");
            return;
        }

        User user;
        if ("Student".equals(role)) {
            user = new Student(0, name, email, dept, rollEmpId);
        } else {
            user = new Staff(0, name, email, dept, rollEmpId);
        }
        user.setPassword(password);
        user.setMobile(mobileField.getText());

        if (AuthManager.registerUser(user)) {
            showInfo("Registration successful! Please login.");
            dispose();
            new LoginScreen().setVisible(true);
        } else {
            showError("Registration failed. Please try again.");
        }
    }
}
