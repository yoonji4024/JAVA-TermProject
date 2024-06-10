package GUI;

import Bank.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class AccountView extends JFrame {
    private JLabel title = new JLabel("View Account");
    private JScrollPane scrollpane = new JScrollPane();
    private JPanel buttonPanel = new JPanel();
    private JTable accountTable;
    private JButton end = new JButton("Complete");
    private JButton back = new JButton("Back");
    private JLabel id = new JLabel();

    public AccountView(String userId) {
        setTitle("Account View");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(51, 102, 51));
        add(title, BorderLayout.NORTH);

        id.setText(userId);
        id.setFont(new Font("dialog", Font.BOLD, 15));

        end.setFont(new Font("dialog", Font.BOLD, 15));
        end.setBackground(Color.white);
        end.addActionListener(e -> dispose());

        back.setFont(new Font("dialog", Font.BOLD, 15));
        back.setBackground(Color.white);
        back.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(back);
        buttonPanel.add(end);

        String[] columnNames = {"Account Number", "Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("dialog", Font.PLAIN, 14));
        accountTable.setRowHeight(30);
        scrollpane.setViewportView(accountTable);
        add(scrollpane, BorderLayout.CENTER);

        loadAccountData(userId, tableModel);

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadAccountData(String userId, DefaultTableModel tableModel) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("VIEW_ACCOUNTS");
            output.writeUTF(userId);
            output.flush();

            List<Account> accounts = (List<Account>) input.readObject();
            for (Account account : accounts) {
                Object[] rowData = {account.getAccountNumber(), account.getBalance()};
                tableModel.addRow(rowData);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
