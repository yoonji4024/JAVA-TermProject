package Bank;

import Exception.*;
import java.io.*;
import java.util.*;

public class BankService {

    public static void handleRequest(User user, String request, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        try {
            switch (request) {
                case "VIEW_ACCOUNTS":
                    output.writeUTF(user.getAccounts().toString());
                    output.flush();
                    break;
                    
                case "CREATE_ACCOUNT":
                    createAccount(user);
                    output.writeUTF("Account creation successful");
                    output.flush();
                    break;
                    
                case "DEPOSIT":
                    String accountNumber = input.readUTF();
                    double amount = input.readDouble();
                    deposit(user, accountNumber, amount);
                    output.writeUTF("Deposit successful");
                    output.flush();
                    break;
                    
                case "WITHDRAW":
                    accountNumber = input.readUTF();
                    amount = input.readDouble();
                    withdraw(user, accountNumber, amount);
                    output.writeUTF("Withdrawal successful");
                    output.flush();
                    break;
                    
                case "TRANSFER":
                    String fromAccountNumber = input.readUTF();
                    String toAccountNumber = input.readUTF();
                    amount = input.readDouble();
                    transfer(user, fromAccountNumber, toAccountNumber, amount);
                    output.writeUTF("Transfer successful");
                    output.flush();
                    break;
                    
                case "VIEW_TRANSACTIONS":
                    accountNumber = input.readUTF();
                    List<Transaction> transactions = getTransactions(user, accountNumber);
                    output.writeUTF(transactions.toString());
                    output.flush();
                    break;
                // 기타 사용자 요청 처리
            }
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }

    public static void handleAdminRequest(Admin admin, String request, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        try {
            switch (request) {
                case "VIEW_ALL_USERS":
                    List<User> users = admin.getAllUsers();
                    output.writeUTF(users.toString());
                    output.flush();
                    break;
                case "VIEW_USER_DETAILS":
                    String userId = input.readUTF();
                    User user = admin.getUserDetails(userId);
                    if (user != null) {
                        output.writeUTF(user.toString());
                    } 
                    else {
                        output.writeUTF("User not found");
                    }
                    output.flush();
                    break;
            }
        } catch (Exception e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }

    public static void handleRegisterRequest(String userId, String password, ObjectOutputStream output) throws IOException {
        try {
            User newUser = new User(userId, password);
            if (BankServer.addUser(newUser)) {
                output.writeUTF("REGISTER_SUCCESS");
            } 
            else {
                output.writeUTF("REGISTER_FAIL");
            }
            output.flush();
        } catch (Exception e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }

    private static Account getAccount(User user, String accountNumber) throws AccountNotFoundException {
        for (Account account : user.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account not found: " + accountNumber);
    }

    private static void createAccount(User user) {
        Account newAccount = new Account();
        user.addAccount(newAccount);
        System.out.println("New account created: " + newAccount.getAccountNumber()); // 로그 추가
        BankServer.saveUserData(); // 계좌 생성 후 사용자 데이터 저장
    }

    private static void deposit(User user, String accountNumber, double amount) throws AccountNotFoundException {
        Account account = getAccount(user, accountNumber);
        account.deposit(amount);
        BankServer.saveUserData(); // 입금 후 사용자 데이터 저장
    }

    private static void withdraw(User user, String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account account = getAccount(user, accountNumber);
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            BankServer.saveUserData(); // 출금 후 사용자 데이터 저장
        } 
        else {
            throw new InsufficientFundsException("Insufficient funds for account: " + accountNumber);
        }
    }

    private static void transfer(User user, String fromAccountNumber, String toAccountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account fromAccount = getAccount(user, fromAccountNumber);
        Account toAccount = getAccount(user, toAccountNumber);
        if (fromAccount.getBalance() >= amount) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            BankServer.saveUserData(); // 이체 후 사용자 데이터 저장
        } 
        else {
            throw new InsufficientFundsException("Insufficient funds for account: " + fromAccountNumber);
        }
    }

    private static List<Transaction> getTransactions(User user, String accountNumber) throws AccountNotFoundException {
        Account account = getAccount(user, accountNumber);
        return account.getTransactions();
    }
}
