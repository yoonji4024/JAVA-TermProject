package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class CreateAccountView extends JFrame {

    public CreateAccountView(String userId) {
        setTitle("Create Account");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton createAccountButton = new JButton("Create Account");

        createAccountButton.addActionListener(e -> createAccount(userId));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.add(createAccountButton);

        add(panel);
        setVisible(true);
    }

    private void createAccount(String userId) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");  // 사용자 비밀번호는 실제로는 보안이 중요합니다.
            output.writeUTF("CREATE_ACCOUNT");
            output.flush();

            String response = input.readUTF();
            System.out.println("Server Response: " + response); // 로그 추가
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage());
        }
    }
}
