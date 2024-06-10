package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class CreateAccountView extends JFrame {
    private JLabel title = new JLabel("Create new account !");
    private JPanel buttonPanel = new JPanel();
    private JLabel pwLabel = new JLabel("Account Password");
    private JPasswordField pwField = new JPasswordField(20);

    public CreateAccountView(String userId) {
        setTitle("Create Account");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);

        title.setBounds(50, 10, 300, 30);
        title.setFont(new Font("serif", Font.BOLD, 20));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setOpaque(true);

        pwLabel.setBounds(50, 110, 150, 40);
        pwLabel.setFont(new Font("dialog", Font.BOLD, 15));

        pwField.setBounds(210, 110, 120, 40);
        pwField.setFont(new Font("dialog", Font.BOLD, 15));
        pwField.setBackground(Color.white);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(Color.white);
        createAccountButton.addActionListener(e -> createAccount(userId));

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.white);
        backButton.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(createAccountButton);
        buttonPanel.add(backButton);
        buttonPanel.setBounds(50, 210, 280, 40);

        add(title);
        add(pwLabel);
        add(pwField);
        add(buttonPanel);

        setVisible(true);
    }

    private void createAccount(String userId) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("CREATE_ACCOUNT");
            output.writeUTF(userId);
            output.writeUTF(new String(pwField.getPassword()));
            output.flush();

            String response = input.readUTF();
            System.out.println("Server Response: " + response);
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }
    }
}
