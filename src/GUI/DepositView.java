package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class DepositView extends JFrame {
    public DepositView(String userId) {
        setTitle("Deposit");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField accountNumberField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JButton depositButton = new JButton("Deposit");

        depositButton.addActionListener(e -> {
            String accountNumber = accountNumberField.getText();
            double amount = Double.parseDouble(amountField.getText());
            deposit(userId, accountNumber, amount);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Account Number:"));
        panel.add(accountNumberField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(depositButton);

        add(panel);
        setVisible(true);
    }

    private void deposit(String userId, String accountNumber, double amount) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("DEPOSIT");
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
