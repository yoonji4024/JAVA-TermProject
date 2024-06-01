package GUI;
//background(153,204,153),strong(51,102,51)
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class LoginWindow extends JFrame {
	private JButton title = new JButton("LOGIN");
	
	private JPanel buttonpanel = new JPanel();//loginButton,registerButton
	
	private JLabel Id = new JLabel("ID");
	private JLabel Pw = new JLabel("Password");
	
	private JTextField IdField = new JTextField(20);
    private JPasswordField PwField = new JPasswordField(20);

    public LoginWindow() {
        setTitle("Login");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(90,25,200,50);
        title.setFont(new Font("serif",Font.BOLD,40));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        title.setBorderPainted(false);
        
        Id.setBounds(50,90,100,40);
        Id.setFont(new Font("dialog",Font.BOLD,20));
        
        Pw.setBounds(50,150,100,40);
        Pw.setFont(new Font("dialog",Font.BOLD,20));
        
        IdField.setBounds(210,90,120,40);
        IdField.setFont(new Font("dialog",Font.BOLD,20));
        IdField.setBackground(Color.white);
        
        PwField.setBounds(210,150,120,40);
        PwField.setFont(new Font("dialog",Font.BOLD,20));
        PwField.setBackground(Color.white);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.white);
        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.white);
        registerButton.addActionListener(e -> {
            new RegisterWindow();
            dispose();
        });
        
        buttonpanel.setLayout(new GridLayout(1,2));
        buttonpanel.setBackground(new Color(153,204,153));
        buttonpanel.setBounds(50,210,280,40);
        buttonpanel.add(loginButton);
        buttonpanel.add(registerButton);

        add(title);
        add(Id);
        add(Pw);
        add(IdField);
        add(PwField);
        add(buttonpanel);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userId = IdField.getText();
            String password = new String(PwField.getPassword());

            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF(password);
                output.writeUTF("LOGIN");
                output.flush();

                String response = input.readUTF();
                if ("USER".equals(response)) {
                    new MainWindow(userId);
                    dispose();
                } 
                else if ("ADMIN".equals(response)) {
                    new AdminMainWindow(userId);
                    dispose();
                } 
                else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login Failed");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}