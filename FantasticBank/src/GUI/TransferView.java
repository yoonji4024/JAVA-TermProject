package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class TransferView extends JFrame {
        private JButton title = new JButton("Transfer Account");
    	
    	private JPanel buttonpanel = new JPanel();//loginButton,registerButton
    	
    	private JLabel FromAccount = new JLabel("From Account");
    	private JLabel ToAccount = new JLabel("To Account");
    	private JLabel Amount = new JLabel("Amount");
    	
    	private JTextField FromAccountField = new JTextField(20);
    	private JTextField ToAccountField = new JTextField(20);
        private JTextField AmountField = new JTextField(20);

        public TransferView(String userId) {
            setTitle("Transfer");
            setSize(400, 360);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            getContentPane().setBackground(new Color(153,204,153));
            setLayout(null);
            setLocationRelativeTo(null);
            
            title.setBounds(30,20,330,50);
            title.setFont(new Font("serif",Font.BOLD,37));
            title.setBackground(new Color(153,204,153));
            title.setForeground(new Color(51,102,51));
            title.setBorderPainted(false);
            
            FromAccount.setBounds(30,90,140,40);
            FromAccount.setFont(new Font("dialog",Font.BOLD,13));
            
            ToAccount.setBounds(30,140,140,40);
            ToAccount.setFont(new Font("dialog",Font.BOLD,13));
            
            Amount.setBounds(30,190,140,40);
            Amount.setFont(new Font("dialog",Font.BOLD,13));
            
            FromAccountField.setBounds(170,90,180,40);
            FromAccountField.setFont(new Font("dialog",Font.BOLD,15));
            FromAccountField.setBackground(Color.white);
            
            ToAccountField.setBounds(170,140,180,40);
            ToAccountField.setFont(new Font("dialog",Font.BOLD,15));
            ToAccountField.setBackground(Color.white);
            
            AmountField.setBounds(170,190,180,40);
            AmountField.setFont(new Font("dialog",Font.BOLD,15));
            AmountField.setBackground(Color.white);
            
            JButton TransferButton = new JButton("Transfer");
            TransferButton.setBackground(Color.white);
            TransferButton.addActionListener(e -> {
                String fromAccount = FromAccountField.getText();
                String toAccount = ToAccountField.getText();
                double amount = Double.parseDouble(AmountField.getText());
                transfer(userId, fromAccount, toAccount, amount);
            });
            
            JButton BackButton = new JButton("Back");
            BackButton.setBackground(Color.white);
            BackButton.addActionListener(e ->{
            	new MainWindow(userId);
            	dispose();
            });
            
            
            buttonpanel.setLayout(new GridLayout(1,2));
            buttonpanel.setBackground(new Color(153,204,153));
            buttonpanel.setBounds(40,250,300,40);
            buttonpanel.add(TransferButton);
            buttonpanel.add(BackButton);

            add(title);
            add(FromAccount);
            add(ToAccount);
            add(Amount);
            add(FromAccountField);
            add(ToAccountField);
            add(AmountField);
            add(buttonpanel);
            setVisible(true);
        }

        private void transfer(String userId, String fromAccount, String toAccount, double amount) {
            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF(userId);
                output.writeUTF("password");
                output.writeUTF("TRANSFER");
                output.writeUTF(fromAccount);
                output.writeUTF(toAccount);
                output.writeDouble(amount);
                output.flush();

                String response = input.readUTF();
                JOptionPane.showMessageDialog(this, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
