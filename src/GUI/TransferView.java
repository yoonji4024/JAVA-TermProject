package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class TransferView extends JFrame {
    public TransferView(String userId) {
        setTitle("Transfer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField fromAccountField = new JTextField(20);
        JTextField toAccountField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JButton transferButton = new JButton("Transfer");

        transferButton.addActionListener(e -> {
            String fromAccount = fromAccountField.getText();
            String toAccount = toAccountField.getText();
            double amount = Double.parseDouble(amountField.getText());
            transfer(userId, fromAccount, toAccount, amount);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JLabel("From Account:"));
        panel.add(fromAccountField);
        panel.add(new JLabel("To Account:"));
        panel.add(toAccountField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(transferButton);

        add(panel);
        setVisible(true);
    }

    private void transfer(String userId, String fromAccount, String toAccount, double amount) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("TRANSFER");
            output.writeUTF(fromAccount);
            output.writeUTF(toAccount);
            output.writeDouble(amount);
            output.flush();

            String response = input.readUTF();
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
