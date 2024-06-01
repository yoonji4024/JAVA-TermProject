/*  private static int accountCounter = 1000000000; // 초기 계좌 번호 시작 (10자리)
    private String accountNumber;
    private String accountpw;
    private String accountowner;
    private double balance;
    private List<Transaction> transactions;
*/
package GUI;

import javax.swing.*;

import Bank.Transaction;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class CreateAccountView extends JFrame {
	private JLabel title = new JLabel("Create new account !");
	private JPanel buttonpanel = new JPanel();//create,back
	
	private JLabel Accountnum = new JLabel("Account Number");
	private JLabel Pw = new JLabel("Account Password");
	
	private JLabel AccountnumField = new JLabel("126-53-9564");
    private JPasswordField PwField = new JPasswordField(20);

    public CreateAccountView(String userId) {
        setTitle("Create Account");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(new Color(153,204,153));
        setLayout(null);
        setLocationRelativeTo(null);
        
        title.setBounds(50,10,300,30);
        title.setFont(new Font("serif",Font.BOLD,20));
        title.setBackground(new Color(153,204,153));
        title.setForeground(new Color(51,102,51));
        
        Accountnum.setBounds(50,80,150,40);
        Accountnum.setFont(new Font("dialog",Font.BOLD,15));
        
        Pw.setBounds(50,140,150,40);
        Pw.setFont(new Font("dialog",Font.BOLD,15));
        
        AccountnumField.setBounds(210,80,120,40);
        AccountnumField.setFont(new Font("dialog",Font.BOLD,15));
        AccountnumField.setOpaque(true);
        AccountnumField.setBackground(Color.white);
        
        PwField.setBounds(210,140,120,40);
        PwField.setFont(new Font("dialog",Font.BOLD,15));
        PwField.setBackground(Color.white);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(Color.white);
        createAccountButton.addActionListener(e -> createAccount(userId));

        JButton BackButton = new JButton("Back");
        BackButton.setBackground(Color.white);
        BackButton.addActionListener(e -> {
            new MainWindow(userId);
            dispose();
        });
        buttonpanel.setLayout(new GridLayout(1,2));
        buttonpanel.add(createAccountButton);
        buttonpanel.add(BackButton);
        buttonpanel.setBounds(50,210,280,40);
        
        add(title);
        add(Accountnum);
        add(Pw);
        add(AccountnumField);
        add(PwField);
        add(buttonpanel);
        
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