package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class WithdrawView extends JFrame {
    private JButton title = new JButton("Withdraw Savings");
    private JPanel buttonPanel = new JPanel();
    private JLabel accountNumLabel = new JLabel("Account Number");
    private JLabel amountLabel = new JLabel("Amount");
    private JTextField accountNumField = new JTextField(20);
    private JTextField amountField = new JTextField(20);

    public WithdrawView(String userId) {
        setTitle("Withdraw");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(30, 20, 330, 50);
        title.setFont(new Font("serif", Font.BOLD, 37));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        accountNumLabel.setBounds(30, 90, 140, 40);
        accountNumLabel.setFont(new Font("dialog", Font.BOLD, 13));

        amountLabel.setBounds(30, 150, 140, 40);
        amountLabel.setFont(new Font("dialog", Font.BOLD, 13));

        accountNumField.setBounds(170, 90, 180, 40);
        accountNumField.setFont(new Font("dialog", Font.BOLD, 15));
        accountNumField.setBackground(Color.white);

        amountField.setBounds(170, 150, 180, 40);
        amountField.setFont(new Font("dialog", Font.BOLD, 15));
        amountField.setBackground(Color.white);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(Color.white);
        withdrawButton.addActionListener(e -> {
            String accountNumber = accountNumField.getText();
            double amount = Double.parseDouble(amountField.getText());
            withdraw(userId, accountNumber, amount);
        });

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.white);
        backButton.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.setBackground(new Color(153, 204, 153));
        buttonPanel.setBounds(40, 210, 300, 40);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(backButton);

        add(title);
        add(accountNumLabel);
        add(amountLabel);
        add(accountNumField);
        add(amountField);
        add(buttonPanel);
        setVisible(true);
    }

    private void withdraw(String userId, String accountNumber, double amount) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("WITHDRAW");
            output.writeUTF(userId);
            output.writeUTF(accountNumber);
            output.writeDouble(amount);
            output.flush();

            String response = input.readUTF();
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
