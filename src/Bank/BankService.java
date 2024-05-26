package Bank;

import java.io.*;
import java.util.*;

public class BankService {
    public static void handleRequest(User user, String request, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        switch (request) {
            case "VIEW_ACCOUNTS":
                output.writeUTF(user.getAccounts().toString());
                output.flush();
                break;
                
            case "DEPOSIT":
                String accountNumber = input.readUTF();
                double amount = input.readDouble();
                Account account = getAccount(user, accountNumber);
                if (account != null) {
                    account.deposit(amount);
                    output.writeUTF("Deposit successful");
                } 
                else {
                    output.writeUTF("Account not found");
                }
                output.flush();
                break;
                
            case "WITHDRAW":
                accountNumber = input.readUTF();
                amount = input.readDouble();
                account = getAccount(user, accountNumber);
                if (account != null) {
                    if (account.getBalance() >= amount) {
                        account.withdraw(amount);
                        output.writeUTF("Withdrawal successful");
                    } 
                    else {
                        output.writeUTF("Insufficient funds");
                    }
                } 
                else {
                    output.writeUTF("Account not found");
                }
                output.flush();
                break;
                
            case "TRANSFER":
                String fromAccountNumber = input.readUTF();
                String toAccountNumber = input.readUTF();
                amount = input.readDouble();
                Account fromAccount = getAccount(user, fromAccountNumber);
                Account toAccount = getAccount(user, toAccountNumber);
                if (fromAccount != null && toAccount != null) {
                    if (fromAccount.getBalance() >= amount) {
                        fromAccount.withdraw(amount);
                        toAccount.deposit(amount);
                        output.writeUTF("Transfer successful");
                    } 
                    else {
                        output.writeUTF("Insufficient funds");
                    }
                } 
                else {
                    output.writeUTF("Account not found");
                }
                output.flush();
                break;
        }
    }

    public static void handleAdminRequest(Admin admin, String request, ObjectInputStream input, ObjectOutputStream output) throws IOException {
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
    }

    private static Account getAccount(User user, String accountNumber) {
        for (Account account : user.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
