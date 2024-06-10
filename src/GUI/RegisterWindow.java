package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class RegisterWindow extends JFrame {
    private JPanel buttonPanel = new JPanel();
    private JButton title = new JButton("REGISTER");
    private JLabel idLabel = new JLabel("New User ID");
    private JLabel pwLabel = new JLabel("New Password");
    private JTextField idField = new JTextField(20);
    private JPasswordField pwField = new JPasswordField(20);

    public RegisterWindow() {
        setTitle("Register");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(70, 25, 250, 50);
        title.setFont(new Font("serif", Font.BOLD, 40));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        idLabel.setBounds(50, 90, 150, 40);
        idLabel.setFont(new Font("dialog", Font.BOLD, 20));

        pwLabel.setBounds(50, 150, 150, 40);
        pwLabel.setFont(new Font("dialog", Font.BOLD, 20));

        idField.setBounds(210, 90, 120, 40);
        idField.setFont(new Font("dialog", Font.BOLD, 20));
        idField.setBackground(Color.white);

        pwField.setBounds(210, 150, 120, 40);
        pwField.setFont(new Font("dialog", Font.BOLD, 20));
        pwField.setBackground(Color.white);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.white);
        registerButton.addActionListener(new RegisterButtonListener());

        JButton loginButton = new JButton("Back to Login");
        loginButton.setBackground(Color.white);
        loginButton.addActionListener(e -> {
            new LoginWindow();
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

    private class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = idField.getText();
            String password = new String(pwField.getPassword());

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF("REGISTER");
                output.writeUTF(userId);
                output.writeUTF(password);
                output.flush();

                String response = input.readUTF();
                if ("REGISTER_SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Successful");
                    new LoginWindow();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Failed: User ID already exists");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
