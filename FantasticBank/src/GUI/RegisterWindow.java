package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class RegisterWindow extends JFrame {
	private JPanel buttonpanel = new JPanel();//loginButton,registerButton
	
	private JButton title = new JButton("REGISTER");
	
	private JLabel Id = new JLabel("New User ID");
	private JLabel Pw = new JLabel("New Password");
	
	private JTextField IdField = new JTextField(20);
    private JPasswordField PwField = new JPasswordField(20);

    public RegisterWindow() {
        setTitle("Register");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(70,25,250,50);
        title.setFont(new Font("serif",Font.BOLD,40));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);
        
        Id.setBounds(50,90,150,40);
        Id.setFont(new Font("dialog",Font.BOLD,20));
        
        Pw.setBounds(50,150,150,40);
        Pw.setFont(new Font("dialog",Font.BOLD,20));
        
        IdField.setBounds(210,90,120,40);
        IdField.setFont(new Font("dialog",Font.BOLD,20));
        IdField.setBackground(Color.white);
        
        PwField.setBounds(210,150,120,40);
        PwField.setFont(new Font("dialog",Font.BOLD,20));
        PwField.setBackground(Color.white);

        JButton RegisterButton = new JButton("Register");
        RegisterButton.setBackground(Color.white);
        RegisterButton.addActionListener(new RegisterButtonListener());

        JButton LoginButton = new JButton("Back to Login");
        LoginButton.setBackground(Color.white);
        LoginButton.addActionListener(e -> {
            new LoginWindow();
            dispose();
        });

        buttonpanel.setLayout(new GridLayout(1,2));
        buttonpanel.setBackground(new Color(153,204,153));
        buttonpanel.setBounds(50,210,280,40);
        buttonpanel.add(LoginButton);
        buttonpanel.add(RegisterButton);

        add(title);
        add(Id);
        add(Pw);
        add(IdField);
        add(PwField);
        add(buttonpanel);
        setVisible(true);
    }

    private class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = IdField.getText();
            String password = new String(PwField.getPassword());

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF(password);
                output.writeUTF("REGISTER");
                output.flush();

                String response = input.readUTF();
                if ("REGISTER_SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Successful");
                    new LoginWindow();
                    dispose();
                } 
                else {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration Failed: User ID already exists");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}