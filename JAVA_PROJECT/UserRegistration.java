package JAVA_PROJECT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserRegistration extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private Connection con;
    private JButton registerButton;


    public UserRegistration() {
        initializeComponents();
        setLayout();
        setupEventHandlers();
        connect();
    }
    private boolean registrationSuccessful = false;

    public void setRegistrationSuccessful(boolean registrationSuccessful) {
        this.registrationSuccessful = registrationSuccessful;
    }

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        registerButton = new JButton("Register");

        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setLayout() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(registerButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void setupEventHandlers() {
        registerButton.addActionListener(e -> registerUser());
    }

    private void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, password);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                setRegistrationSuccessful(true);
                BusBookingUI b=new BusBookingUI();
                b.setVisible(true);
                b.setUserDetails(username,email);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed!");
            }

            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus", "root", "Zaid@123");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        // Connect to the database

            SwingUtilities.invokeLater(() -> {
                UserRegistration registrationForm = new UserRegistration();
                registrationForm.setVisible(true);
            });
        }
}
