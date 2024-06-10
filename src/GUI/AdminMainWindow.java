package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminMainWindow extends JFrame {
    private JButton title = new JButton("Admin Main Service");
    private String userId;

    public AdminMainWindow(String adminId) {
        this.userId = adminId;
        setTitle("Admin Main Window");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(153, 204, 153));
        setLayout(null);
        setLocationRelativeTo(null);

        title.setBounds(35, 10, 320, 30);
        title.setFont(new Font("serif", Font.BOLD, 30));
        title.setBackground(new Color(153, 204, 153));
        title.setForeground(new Color(51, 102, 51));
        title.setBorderPainted(false);

        JButton viewUserButton = new JButton("View All Users");
        viewUserButton.setBackground(Color.white);
        viewUserButton.addActionListener(e -> {
            new UserListView(adminId);
            dispose();
        });

        JButton viewMyButton = new JButton("View User Details");
        viewMyButton.setBackground(Color.white);
        viewMyButton.addActionListener(e -> {
            new UserDetailView(adminId);
            dispose();
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.white);
        logoutButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(viewUserButton);
        panel.add(viewMyButton);
        panel.add(logoutButton);
        panel.setBounds(45, 60, 300, 180);

        add(panel);
        add(title);
        setVisible(true);
    }
}
