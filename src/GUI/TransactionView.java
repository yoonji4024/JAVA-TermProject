package GUI;

import Bank.Account;
import Bank.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class TransactionView extends JFrame {
    private JLabel title = new JLabel("View Transaction");
    private JComboBox<String> accountComboBox;
    private JTable transactionTable;
    private JPanel buttonPanel = new JPanel();
    private JButton completeButton = new JButton("Complete");
    private JButton backButton = new JButton("Back");

    public TransactionView(String userId) {
        setTitle("Transaction");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(51, 102, 51));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"Type", "Amount", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("dialog", Font.PLAIN, 14));
        transactionTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(new Color(153, 204, 153));

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("VIEW_ACCOUNTS");
            output.writeUTF(userId);
            output.flush();

            List<Account> accounts = (List<Account>) input.readObject();
            String[] accountNumbers = accounts.stream().map(Account::getAccountNumber).toArray(String[]::new);
            accountComboBox = new JComboBox<>(accountNumbers);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }

        accountComboBox.addActionListener(e -> viewTransactions(userId));
        centerPanel.add(accountComboBox, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        buttonPanel.setLayout(new GridLayout(1, 2));
        backButton.setBackground(Color.white);
        backButton.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });

        completeButton.setBackground(Color.white);
        completeButton.addActionListener(e -> dispose());

        buttonPanel.add(backButton);
        buttonPanel.add(completeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void viewTransactions(String userId) {
        String selectedAccount = (String) accountComboBox.getSelectedItem();
        if (selectedAccount != null) {
            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF("VIEW_TRANSACTIONS");
                output.writeUTF(userId);
                output.writeUTF(selectedAccount);
                output.flush();

                List<Transaction> transactions = (List<Transaction>) input.readObject();
                DefaultTableModel tableModel = (DefaultTableModel) transactionTable.getModel();
                tableModel.setRowCount(0); // Clear existing rows
                for (Transaction transaction : transactions) {
                    Object[] rowData = {
                            transaction.getType(),
                            transaction.getAmount(),
                            transaction.getDate()
                    };
                    tableModel.addRow(rowData);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
            }
        }
    }
}
