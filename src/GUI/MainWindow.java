package GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private String userId;

    public MainWindow(String userId) {
        this.userId = userId;
        setTitle("Main Window");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(e -> new CreateAccountView(userId));

        JButton viewAccountButton = new JButton("View Accounts");
        viewAccountButton.addActionListener(e -> new AccountView(userId));

        JButton transactionButton = new JButton("View Transactions");
        transactionButton.addActionListener(e -> new TransactionView(userId));

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> new DepositView(userId));

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> new WithdrawView(userId));

        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(e -> new TransferView(userId));

        JButton logoutButton = new JButton("Logout");
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

        add(panel);
        setVisible(true);
    }
}
