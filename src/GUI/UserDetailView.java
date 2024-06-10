package GUI;

import Bank.Account;
import Bank.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class UserDetailView extends JFrame {
    private JLabel title = new JLabel("View User Detail");
    private JComboBox<String> userComboBox;
    private JTable accountTable;
    private JTextArea transactionTextArea = new JTextArea();
    private JPanel buttonPanel = new JPanel();
    private JButton completeButton = new JButton("Complete");
    private JButton backButton = new JButton("Back");

    public UserDetailView(String adminId) {
        setTitle("User Detail View");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(51, 102, 51));
        add(title, BorderLayout.NORTH);

        transactionTextArea.setFont(new Font("dialog", Font.BOLD, 15));
        transactionTextArea.setBackground(Color.white);
        transactionTextArea.setEditable(false);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(new Color(153, 204, 153));

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("VIEW_ALL_USERS");
            output.writeUTF(adminId);
            output.flush();

            List<User> users = (List<User>) input.readObject();
            String[] userIds = users.stream().map(User::getUserId).toArray(String[]::new);
            userComboBox = new JComboBox<>(userIds);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }

        userComboBox.addActionListener(e -> viewUserDetails(adminId));
        centerPanel.add(userComboBox, BorderLayout.NORTH);

        String[] columnNames = {"Account Number", "Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("dialog", Font.PLAIN, 14));
        accountTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(accountTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JScrollPane transactionScrollPane = new JScrollPane(transactionTextArea);
        centerPanel.add(transactionScrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        buttonPanel.setLayout(new GridLayout(1, 2));
        backButton.setBackground(Color.white);
        backButton.addActionListener(e -> {
            new AdminMainWindow(adminId);
            dispose();
        });

        completeButton.setBackground(Color.white);
        completeButton.addActionListener(e -> dispose());

        buttonPanel.add(backButton);
        buttonPanel.add(completeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void viewUserDetails(String adminId) {
        String selectedUser = (String) userComboBox.getSelectedItem();
        if (selectedUser != null) {
            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF("VIEW_USER_DETAILS");
                output.writeUTF(adminId);
                output.writeUTF(selectedUser);
                output.flush();

                User user = (User) input.readObject();
                List<Account> accounts = user.getAccounts();

                DefaultTableModel tableModel = (DefaultTableModel) accountTable.getModel();
                tableModel.setRowCount(0); // Clear existing rows
                StringBuilder transactionsBuilder = new StringBuilder();
                for (Account account : accounts) {
                    Object[] rowData = {account.getAccountNumber(), account.getBalance()};
                    tableModel.addRow(rowData);
                    transactionsBuilder.append("Account: ").append(account.getAccountNumber()).append("\n");
                    transactionsBuilder.append("Transactions:\n");
                    account.getTransactions().forEach(transaction -> transactionsBuilder.append(transaction).append("\n"));
                    transactionsBuilder.append("\n");
                }
                transactionTextArea.setText(transactionsBuilder.toString());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
            }
        }
    }
}
