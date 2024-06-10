package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class TransferView extends JFrame {
    private JLabel title = new JLabel("Transfer Funds");
    private JLabel fromAccountLabel = new JLabel("From Account:");
    private JLabel toAccountLabel = new JLabel("To Account:");
    private JLabel amountLabel = new JLabel("Amount:");
    private JTextField fromAccountField = new JTextField(20);
    private JTextField toAccountField = new JTextField(20);
    private JTextField amountField = new JTextField(20);
    private JButton transferButton = new JButton("Transfer");
    private JButton backButton = new JButton("Back");

    public TransferView(String userId) {
        setTitle("Transfer");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(51, 102, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(fromAccountLabel, gbc);
        gbc.gridx = 1;
        add(fromAccountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(toAccountLabel, gbc);
        gbc.gridx = 1;
        add(toAccountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(amountLabel, gbc);
        gbc.gridx = 1;
        add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(transferButton, gbc);
        gbc.gridx = 1;
        add(backButton, gbc);

        transferButton.setBackground(Color.white);
        transferButton.addActionListener(e -> {
            String fromAccount = fromAccountField.getText();
            String toAccount = toAccountField.getText();
            double amount = Double.parseDouble(amountField.getText());
            transfer(userId, fromAccount, toAccount, amount);
        });

        backButton.setBackground(Color.white);
        backButton.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });

        setVisible(true);
    }

    private void transfer(String userId, String fromAccount, String toAccount, double amount) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("TRANSFER");
            output.writeUTF(userId);
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
