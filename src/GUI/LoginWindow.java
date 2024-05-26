package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class LoginWindow extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public LoginWindow() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userIdField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegisterWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF(password);
                output.writeUTF("LOGIN");
                output.flush();

                String response = input.readUTF();
                if ("USER".equals(response)) {
                    new MainWindow(userId);
                    dispose();
                } 
                else if ("ADMIN".equals(response)) {
                    new AdminMainWindow(userId);
                    dispose();
                } 
                else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login Failed");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
