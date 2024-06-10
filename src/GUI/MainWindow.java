package GUI;

import Bank.Account;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class MainWindow extends JFrame {
    private JButton title = new JButton("Main Window");
    private String userId;

    public MainWindow(String userId) {
        this.userId = userId;
        setTitle("Main Window");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(50, 10, 300, 30);
        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(Color.white);
        createAccountButton.addActionListener(e -> new CreateAccountView(userId));

        JButton viewAccountButton = new JButton("View Accounts");
        viewAccountButton.setBackground(Color.white);
        viewAccountButton.addActionListener(e -> new AccountView(userId));

        JButton transactionButton = new JButton("View Transactions");
        transactionButton.setBackground(Color.white);
        transactionButton.addActionListener(e -> {
            List<Account> accounts = fetchUserAccounts(userId);
            if (accounts != null && !accounts.isEmpty()) {
                new TransactionView(userId);
            } else {
                JOptionPane.showMessageDialog(this, "No accounts found for the user.");
            }
        });

        JButton depositButton = new JButton("Deposit");
        depositButton.setBackground(Color.white);
        depositButton.addActionListener(e -> new DepositView(userId));

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(Color.white);
        withdrawButton.addActionListener(e -> new WithdrawView(userId));

        JButton transferButton = new JButton("Transfer");
        transferButton.setBackground(Color.white);
        transferButton.addActionListener(e -> new TransferView(userId));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.white);
        logoutButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.add(createAccountButton);
        panel.add(viewAccountButton);
        panel.add(transactionButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(logoutButton);
        panel.setBounds(50, 50, 300, 230);

        add(panel);
        add(title);
        setVisible(true);
    }

    private List<Account> fetchUserAccounts(String userId) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("VIEW_ACCOUNTS");
            output.writeUTF(userId);
            output.flush();

            return (List<Account>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        new MainWindow("testUser");
    }
}
