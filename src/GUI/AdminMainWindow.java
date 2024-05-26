package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminMainWindow extends JFrame {
    private String adminId;

    public AdminMainWindow(String adminId) {
        this.adminId = adminId;
        setTitle("Admin Main Window");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(e -> new CreateAccountView(adminId));

        JButton viewAllUsersButton = new JButton("View All Users");
        viewAllUsersButton.addActionListener(e -> new UserListView());

        JButton viewUserDetailsButton = new JButton("View My Details");
        viewUserDetailsButton.addActionListener(e -> new UserDetailView(adminId));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(createAccountButton);
        panel.add(viewAllUsersButton);
        panel.add(viewUserDetailsButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}
