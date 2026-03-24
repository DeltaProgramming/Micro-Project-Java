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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Create Account");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Full Name"), gbc);
        gbc.gridy = row++;
        nameField = createTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("College Email (@gectcr.ac.in)"), gbc);
        gbc.gridy = row++;
        emailField = createTextField(20);
        formPanel.add(emailField, gbc);
        
        gbc.gridy = row++;
        formPanel.add(new JLabel("Department"), gbc);
        gbc.gridy = row++;
        String[] departments = {"Mechanical", "Civil", "Production", "EC", "EEE", "Architecture", "CSE", "MCA", "M.Tech"};
        deptCombo = new JComboBox<>(departments);
        deptCombo.setBackground(Color.WHITE);
        formPanel.add(deptCombo, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("Role"), gbc);
        gbc.gridy = row++;
        String[] roles = {"Student", "Staff"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setBackground(Color.WHITE);
        formPanel.add(roleCombo, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("Roll No / Employee ID"), gbc);
        gbc.gridy = row++;
        rollEmpIdField = createTextField(20);
        formPanel.add(rollEmpIdField, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("Mobile Number"), gbc);
        gbc.gridy = row++;
        mobileField = createTextField(20);
        formPanel.add(mobileField, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = row++;
        passField = createPasswordField(20);
        formPanel.add(passField, gbc);

        gbc.gridy = row++;
        formPanel.add(new JLabel("Confirm Password"), gbc);
        gbc.gridy = row++;
        confirmPassField = createPasswordField(20);
        formPanel.add(confirmPassField, gbc);

        // Buttons
        gbc.gridy = row++;
        gbc.insets = new Insets(20, 20, 10, 20);
        JButton registerBtn = createWAButton("REGISTER", true);
        formPanel.add(registerBtn, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(5, 20, 20, 20);
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
