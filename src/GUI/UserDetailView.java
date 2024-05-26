package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class UserDetailView extends JFrame {
    public UserDetailView(String userId) {
        setTitle("User Detail View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea userDetailTextArea = new JTextArea();
        userDetailTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userDetailTextArea);

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("admin1");
            output.writeUTF("adminpass");
            output.writeUTF("VIEW_USER_DETAILS");
            output.writeUTF(userId);
            output.flush();

            String userDetails = input.readUTF();
            userDetailTextArea.setText(userDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(scrollPane);
        setVisible(true);
    }
}
