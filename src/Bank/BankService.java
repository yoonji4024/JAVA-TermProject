package Bank;

import Exception.*;
import java.io.*;
import java.util.*;

public class BankService {
    public static void handleRegisterRequest(String userId, String password, ObjectOutputStream output) throws IOException {
        try {
            if (!userId.isEmpty() && !password.isEmpty()) {
                User newUser = new User(userId, password);
                if (BankServer.addUser(newUser)) {
                    output.writeUTF("REGISTER_SUCCESS");
                } else {
                    output.writeUTF("REGISTER_FAIL");
                }
                output.flush();
            }
        } catch (Exception e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }

    public static void createAccount(User user, String accountPassword, ObjectOutputStream output) throws IOException {
        Account newAccount = new Account(accountPassword);
        user.addAccount(newAccount);
        System.out.println("New account created: " + newAccount.getAccountNumber());
        System.out.println("User's accounts: " + user.getAccounts());
        BankServer.saveUserData();
        output.writeUTF("Account creation successful");
        output.flush();
    }

    public static Account getAccount(User user, String accountNumber) throws AccountNotFoundException {
        for (Account account : user.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account not found: " + accountNumber);
    }

    public static Account findAccount(String accountNumber) throws AccountNotFoundException {
        for (User user : BankServer.getAllUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        throw new AccountNotFoundException("Account not found: " + accountNumber);
    }

    public static List<Account> getAccounts(User user) {
        return user.getAccounts();
    }

    public static void deposit(User user, String accountNumber, double amount) throws AccountNotFoundException {
        Account account = getAccount(user, accountNumber);
        account.deposit(amount);
        BankServer.saveUserData();
    }

    public static void withdraw(User user, String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account account = getAccount(user, accountNumber);
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            BankServer.saveUserData();
        } else {
            throw new InsufficientFundsException("Insufficient funds for account: " + accountNumber);
        }
    }

    public static void transfer(User user, String fromAccountNumber, String toAccountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account fromAccount = getAccount(user, fromAccountNumber);
        Account toAccount = findAccount(toAccountNumber);
        if (fromAccount.getBalance() >= amount) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            BankServer.saveUserData();
        } else {
            throw new InsufficientFundsException("Insufficient funds for account: " + fromAccountNumber);
        }
    }

    public static List<Transaction> getTransactions(User user, String accountNumber) throws AccountNotFoundException {
        Account account = getAccount(user, accountNumber);
        return account.getTransactions();
    }
}
