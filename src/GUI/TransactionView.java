package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class TransactionView extends JFrame {
    public TransactionView(String userId) {
        setTitle("Transaction View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea transactionTextArea = new JTextArea();
        transactionTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionTextArea);

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("VIEW_TRANSACTIONS");
            output.flush();

            String transactions = input.readUTF();
            transactionTextArea.setText(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(scrollPane);
        setVisible(true);
    }
}
