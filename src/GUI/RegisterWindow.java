package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class RegisterWindow extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public RegisterWindow() {
        setTitle("Register");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userIdField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton loginButton = new JButton("Back to Login");
        loginButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("New User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF(password);
                output.writeUTF("REGISTER");
                output.flush();

                String response = input.readUTF();
                if ("REGISTER_SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Successful");
                    new LoginWindow();
                    dispose();
                } 
                else {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Failed: User ID already exists");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
