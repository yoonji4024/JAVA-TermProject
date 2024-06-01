package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminMainWindow extends JFrame {
	private JButton title = new JButton("Admin Main Service");
    private String userId;

    public AdminMainWindow(String adminId) {
        this.userId = userId;
        setTitle("Admin Main Window");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(35,10,320,30);
        title.setFont(new Font("serif",Font.BOLD,30));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);

        JButton CreateAccountButton = new JButton("Create Account");
        CreateAccountButton.setBackground(Color.white);
        //CreateAccountButton.setForeground(new Color(51,102,51));
        CreateAccountButton.addActionListener(e -> {
        	new CreateAccountView(adminId);
	        dispose();
        });

        JButton ViewUserButton = new JButton("View All Users");
        ViewUserButton.setBackground(Color.white);
        //ViewAccountButton.setForeground(new Color(51,102,51));
        ViewUserButton.addActionListener(e -> {
        	new UserListView(adminId);
        	dispose();
        });

        JButton ViewMyButton = new JButton("View My Details");
        ViewMyButton.setBackground(Color.white);
        //TransactionButton.setForeground(new Color(51,102,51));
        ViewMyButton.addActionListener(e -> {
        	new UserDetailView(adminId);
        	dispose();
        });
        
        JButton LogoutButton = new JButton("Logout");
        LogoutButton.setBackground(Color.white);
        //LogoutButton.setForeground(new Color(51,102,51));
        LogoutButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(CreateAccountButton);
        panel.add(ViewUserButton);
        panel.add(ViewMyButton);
        panel.add(LogoutButton);
        panel.setBounds(45,60,300,180);

        add(panel);
        add(title);
        setVisible(true);
    }
}