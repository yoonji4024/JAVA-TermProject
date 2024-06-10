package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class LoginWindow extends JFrame {
    private JButton title = new JButton("LOGIN");
    private JPanel buttonPanel = new JPanel();
    private JLabel idLabel = new JLabel("ID");
    private JLabel pwLabel = new JLabel("Password");
    private JTextField idField = new JTextField(20);
    private JPasswordField pwField = new JPasswordField(20);

    public LoginWindow() {
        setTitle("Login");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(90, 25, 200, 50);
        title.setFont(new Font("serif", Font.BOLD, 40));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        idLabel.setBounds(50, 90, 100, 40);
        idLabel.setFont(new Font("dialog", Font.BOLD, 20));

        pwLabel.setBounds(50, 150, 100, 40);
        pwLabel.setFont(new Font("dialog", Font.BOLD, 20));

        idField.setBounds(210, 90, 120, 40);
        idField.setFont(new Font("dialog", Font.BOLD, 20));
        idField.setBackground(Color.white);

        pwField.setBounds(210, 150, 120, 40);
        pwField.setFont(new Font("dialog", Font.BOLD, 20));
        pwField.setBackground(Color.white);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.white);
        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.white);
        registerButton.addActionListener(e -> {
            new RegisterWindow();
            dispose();
        });

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.setBackground(new Color(153, 204, 153));
        buttonPanel.setBounds(50, 210, 280, 40);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(title);
        add(idLabel);
        add(pwLabel);
        add(idField);
        add(pwField);
        add(buttonPanel);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = idField.getText();
            String password = new String(pwField.getPassword());

            System.out.println("Attempting login with userId=" + userId + " and password=" + password);

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF("LOGIN");
                output.writeUTF(userId);
                output.writeUTF(password);
                output.flush();

                String response = input.readUTF();
                System.out.println("Server response: " + response);

                if ("USER".equals(response)) {
                    new MainWindow(userId);
                    dispose();
                } else if ("ADMIN".equals(response)) {
                    new AdminMainWindow(userId);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login Failed");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
