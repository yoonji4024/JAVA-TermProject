package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class UserListView extends JFrame {
    public UserListView() {
        setTitle("User List");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea userListTextArea = new JTextArea();
        userListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userListTextArea);

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("admin1");
            output.writeUTF("adminpass");
            output.writeUTF("VIEW_ALL_USERS");
            output.flush();

            String users = input.readUTF();
            userListTextArea.setText(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(scrollPane);
        setVisible(true);
    }
}
