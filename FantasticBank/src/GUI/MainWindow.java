package GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
	private JButton title = new JButton("Main Window");
    private String userId;

    public MainWindow(String userId) {
        this.userId = userId;
        setTitle("Main Window");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(50,10,300,30);
        title.setFont(new Font("serif",Font.BOLD,30));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);

        JButton CreateAccountButton = new JButton("Create Account");
        CreateAccountButton.setBackground(Color.white);
        //CreateAccountButton.setForeground(new Color(51,102,51));
        CreateAccountButton.addActionListener(e -> new CreateAccountView(userId));

        JButton ViewAccountButton = new JButton("View Accounts");
        ViewAccountButton.setBackground(Color.white);
        //ViewAccountButton.setForeground(new Color(51,102,51));
        ViewAccountButton.addActionListener(e -> new AccountView(userId));

        JButton TransactionButton = new JButton("View Transactions");
        TransactionButton.setBackground(Color.white);
        //TransactionButton.setForeground(new Color(51,102,51));
        TransactionButton.addActionListener(e -> new TransactionView(userId));

        JButton DepositButton = new JButton("Deposit");
        DepositButton.setBackground(Color.white);
        //DepositButton.setForeground(new Color(51,102,51));
        DepositButton.addActionListener(e -> new DepositView(userId));

        JButton WithdrawButton = new JButton("Withdraw");
        WithdrawButton.setBackground(Color.white);
        //WithdrawButton.setForeground(new Color(51,102,51));
        WithdrawButton.addActionListener(e -> new WithdrawView(userId));

        JButton TransferButton = new JButton("Transfer");
        TransferButton.setBackground(Color.white);
        //TransferButton.setForeground(new Color(51,102,51));
        TransferButton.addActionListener(e -> new TransferView(userId));

        JButton LogoutButton = new JButton("Logout");
        LogoutButton.setBackground(Color.white);
        //LogoutButton.setForeground(new Color(51,102,51));
        LogoutButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.add(CreateAccountButton);
        panel.add(ViewAccountButton);
        panel.add(TransactionButton);
        panel.add(DepositButton);
        panel.add(WithdrawButton);
        panel.add(TransferButton);
        panel.add(LogoutButton);
        panel.setBounds(50,50,300,230);

        add(panel);
        add(title);
        setVisible(true);
    }
}
