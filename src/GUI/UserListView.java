package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import Bank.User;

public class UserListView extends JFrame {
    private JButton title = new JButton("User List");
    private JScrollPane scrollPane = new JScrollPane();
    private JPanel buttonPanel = new JPanel();
    private JTextArea userListArea = new JTextArea();
    private JButton end = new JButton("Complete");
    private JButton back = new JButton("Back");
    private JLabel adminIdLabel = new JLabel();

    public UserListView(String userId) {
        setTitle("User List");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(50, 15, 300, 35);
        title.setFont(new Font("serif", Font.BOLD, 35));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        adminIdLabel.setText(userId);
        adminIdLabel.setBounds(20, 70, 80, 90);
        adminIdLabel.setFont(new Font("dialog", Font.BOLD, 15));

        userListArea.setFont(new Font("dialog", Font.BOLD, 15));
        userListArea.setBackground(Color.white);
        userListArea.setEditable(false);

        end.setFont(new Font("dialog", Font.BOLD, 15));
        end.setBackground(Color.white);
        end.addActionListener(e -> dispose());

        back.setFont(new Font("dialog", Font.BOLD, 15));
        back.setBackground(Color.white);
        back.addActionListener(e -> {
            new AdminMainWindow(userId);
            dispose();
        });

        buttonPanel.setBounds(80, 190, 250, 40);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(back);
        buttonPanel.add(end);

        scrollPane.setViewportView(userListArea);
        scrollPane.setBounds(20, 70, 360, 100);
        scrollPane.setBackground(Color.white);

        loadUserList(userId);

        add(buttonPanel);
        add(scrollPane);
        add(adminIdLabel);
        add(title);
        setVisible(true);
    }

    private void loadUserList(String userId) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("VIEW_ALL_USERS");
            output.writeUTF(userId);
            output.flush();

            List<User> users = (List<User>) input.readObject();
            StringBuilder userListText = new StringBuilder();
            for (User user : users) {
                userListText.append(user.getUserId()).append("\n");
            }
            userListArea.setText(userListText.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
