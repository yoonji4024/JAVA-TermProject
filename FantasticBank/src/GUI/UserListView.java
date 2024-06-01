package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class UserListView extends JFrame {
	private JButton title = new JButton("User List");
	private JScrollPane scrollpane = new JScrollPane();
	private JPanel buttonpanel = new JPanel();
	
	private JTextArea UserListArea = new JTextArea();
	private JButton end = new JButton("Complete");
	private JButton Back = new JButton("Back");
	private JLabel AdminId = new JLabel();
	
    public UserListView(String userId) {
        setTitle("User List");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(50,15,300,35);
        title.setFont(new Font("serif",Font.BOLD,35));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);
        
        AdminId.setText(userId);
        AdminId.setBounds(20,70,80,90);
        AdminId.setFont(new Font("dialog",Font.BOLD,15));
        
        UserListArea.setFont(new Font("dialog",Font.BOLD,15));
        UserListArea.setBackground(Color.white);
        UserListArea.setEditable(false);
        
        end.setFont(new Font("dialog",Font.BOLD,15));
        end.setBackground(Color.white);
        end.addActionListener(e -> dispose());
        
        Back.setFont(new Font("dialog",Font.BOLD,15));
        Back.setBackground(Color.white);
        Back.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });
        
        buttonpanel.setBounds(80,190,250,40);
        buttonpanel.setLayout(new GridLayout(1,2));
        buttonpanel.add(Back);
        buttonpanel.add(end);
        
        scrollpane.add(UserListArea);
        scrollpane.setBounds(100,70,260,100);
        scrollpane.setBackground(Color.white);

        try (Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

               output.writeUTF("admin1");
               output.writeUTF("adminpass");
               output.writeUTF("VIEW_ALL_USERS");
               output.flush();

               String users = input.readUTF();
               UserListArea.setText(users);
        }catch (IOException e) {
        	e.printStackTrace();
        }

        add(buttonpanel);
        add(scrollpane);
        add(AdminId);
        add(title);
        setVisible(true);
    }
}