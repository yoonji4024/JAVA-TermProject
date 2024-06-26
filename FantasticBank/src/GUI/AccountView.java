package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class AccountView extends JFrame {
	private JButton title = new JButton("View Account");
	private JScrollPane scrollpane = new JScrollPane();
	private JPanel buttonpanel = new JPanel();
	
	private JTextArea AccountTextArea = new JTextArea();
	private JButton end = new JButton("Complete");
	private JButton Back = new JButton("Back");
	private JLabel Id = new JLabel();
	
    public AccountView(String userId) {
        setTitle("Account View");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(50,10,300,30);
        title.setFont(new Font("serif",Font.BOLD,30));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);
        
        Id.setText(userId);
        Id.setBounds(20,70,80,90);
        Id.setFont(new Font("dialog",Font.BOLD,15));
        
        AccountTextArea.setFont(new Font("dialog",Font.BOLD,15));
        AccountTextArea.setBackground(Color.white);
        AccountTextArea.setEditable(false);
        
        end.setFont(new Font("dialog",Font.BOLD,15));
        end.setBackground(Color.white);
        end.addActionListener(e -> dispose());
        
        Back.setFont(new Font("dialog",Font.BOLD,15));
        Back.setBackground(Color.white);
        Back.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });
        
        buttonpanel.setBounds(80,200,250,30);
        buttonpanel.setLayout(new GridLayout(1,2));
        buttonpanel.add(Back);
        buttonpanel.add(end);
        
        scrollpane.add(AccountTextArea);
        scrollpane.setBounds(100,70,260,100);
        scrollpane.setBackground(Color.white);

        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF(userId);
            output.writeUTF("password");
            output.writeUTF("VIEW_ACCOUNTS");
            output.flush();

            String accounts = input.readUTF();
            AccountTextArea.setText(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(buttonpanel);
        add(scrollpane);
        add(Id);
        add(title);
        setVisible(true);
    }
}