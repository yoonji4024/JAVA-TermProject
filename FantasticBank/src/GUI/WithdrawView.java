package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class WithdrawView extends JFrame {
        private JButton title = new JButton("Withdraw Savings");
    	
    	private JPanel buttonpanel = new JPanel();//loginButton,registerButton
    	
    	private JLabel AccountNum = new JLabel("Account Number");
    	private JLabel Amount = new JLabel("Amount");
    	
    	private JTextField AccountNumField = new JTextField(20);
        private JTextField AmountField = new JTextField(20);

        public WithdrawView(String userId) {
            setTitle("Withdraw");
            setSize(400, 320);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            getContentPane().setBackground(new Color(153,204,153));
            setLayout(null);
            setLocationRelativeTo(null);
            
            title.setBounds(30,20,330,50);
            title.setFont(new Font("serif",Font.BOLD,37));
            title.setBackground(new Color(153,204,153));
            title.setForeground(new Color(51,102,51));
            title.setBorderPainted(false);
            
            AccountNum.setBounds(30,90,140,40);
            AccountNum.setFont(new Font("dialog",Font.BOLD,13));
            
            Amount.setBounds(30,150,140,40);
            Amount.setFont(new Font("dialog",Font.BOLD,13));
            
            AccountNumField.setBounds(170,90,180,40);
            AccountNumField.setFont(new Font("dialog",Font.BOLD,15));
            AccountNumField.setBackground(Color.white);
            
            AmountField.setBounds(170,150,180,40);
            AmountField.setFont(new Font("dialog",Font.BOLD,15));
            AmountField.setBackground(Color.white);
            
            JButton WithdrawButton = new JButton("Withdraw");
            WithdrawButton.setBackground(Color.white);
            WithdrawButton.addActionListener(e -> {
                String accountNumber = AccountNumField.getText();
                double amount = Double.parseDouble(AmountField.getText());
                Withdraw(userId, accountNumber, amount);
            });
            
            JButton BackButton = new JButton("Back");
            BackButton.setBackground(Color.white);
            BackButton.addActionListener(e ->{
            	new MainWindow(userId);
            	dispose();
            });
            
            
            buttonpanel.setLayout(new GridLayout(1,2));
            buttonpanel.setBackground(new Color(153,204,153));
            buttonpanel.setBounds(40,210,300,40);
            buttonpanel.add(WithdrawButton);
            buttonpanel.add(BackButton);

            add(title);
            add(AccountNum);
            add(Amount);
            add(AccountNumField);
            add(AmountField);
            add(buttonpanel);
            setVisible(true);
        }

        private void Withdraw(String userId, String accountNumber, double amount) {
            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF("password");
                output.writeUTF("WITHDRAW");
                output.writeUTF(accountNumber);
                output.writeDouble(amount);
                output.flush();

                String response = input.readUTF();
                JOptionPane.showMessageDialog(this, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

