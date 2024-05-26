package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class WithdrawView extends JFrame {
    public WithdrawView(String userId) {
        setTitle("Withdraw");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField accountNumberField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JButton withdrawButton = new JButton("Withdraw");

        withdrawButton.addActionListener(e -> {
            String accountNumber = accountNumberField.getText();
            double amount = Double.parseDouble(amountField.getText());
            withdraw(userId, accountNumber, amount);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Account Number:"));
        panel.add(accountNumberField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(withdrawButton);

        add(panel);
        setVisible(true);
    }

    private void withdraw(String userId, String accountNumber, double amount) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("WITHDRAW");
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
