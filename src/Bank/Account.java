package Bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Exception.*;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private double balance;
    private String accountPassword;
    private List<Transaction> transactions;

    public Account(String accountPassword) {
    	AccountGenerator generator = AccountGenerator.getInstance();
        this.accountNumber = generator.generateAccountNumber();
        System.out.println("New Account Number: " + generator.generateAccountNumber());
        System.out.println("Next Account Number: " + generator.generateAccountNumber());
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.accountPassword = accountPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount));
        } else {
            throw new InsufficientFundsException("Insufficient funds for account: " + accountNumber);
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account Number: ").append(accountNumber).append("\n")
          .append("Balance: ").append(balance).append("\n")
          .append("Transactions:\n");

        for (Transaction transaction : transactions) {
            sb.append("  ").append(transaction).append("\n");
        }

        return sb.toString();
    }
}
