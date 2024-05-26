package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class AccountView extends JFrame {
    public AccountView(String userId) {
        setTitle("Account View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea accountTextArea = new JTextArea();
        accountTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(accountTextArea);

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("VIEW_ACCOUNTS");
            output.flush();

            String accounts = input.readUTF();
            accountTextArea.setText(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(scrollPane);
        setVisible(true);
    }
}
