package Bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private static int accountCounter = 1000000000; // 초기 계좌 번호 시작 (10자리)
    private String accountNumber;
    private double balance;
    private List<Transaction> transactions;

    public Account() {
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    private static synchronized String generateAccountNumber() {
        return String.valueOf(accountCounter++);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount));
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
