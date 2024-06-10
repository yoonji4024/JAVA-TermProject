package Bank;

import java.io.*;
import java.net.*;
import java.util.List;

import Exception.AccountNotFoundException;
import Exception.InsufficientFundsException;

public class BankClientHandler extends Thread {
    private Socket socket;

    public BankClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ) {
            String requestType = input.readUTF();
            String userId = input.readUTF();

            System.out.println("Request Type: " + requestType);
            System.out.println("User ID: " + userId);

            if ("REGISTER".equals(requestType)) {
                String password = input.readUTF();
                System.out.println("Password: " + password);
                BankService.handleRegisterRequest(userId, password, output);
            } else {
                BankServer.loadUserData();
                User user = BankServer.getUser(userId);
                Admin admin = BankServer.getAdmin(userId);
                synchronized(this) {
                    if ("LOGIN".equals(requestType)) {
                        String password = input.readUTF();
                        System.out.println("Password: " + password);

                        if (user != null && user.getPassword().equals(password)) {
                            output.writeUTF("USER");
                            output.flush();
                        } else if (admin != null && admin.getPassword().equals(password)) {
                            output.writeUTF("ADMIN");
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("CREATE_ACCOUNT".equals(requestType)) {
                        if (user != null) {
                            String accountPassword = input.readUTF();
                            System.out.println("Password: " + accountPassword);
                            BankService.createAccount(user, accountPassword, output);
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("VIEW_ACCOUNTS".equals(requestType)) {
                        if (user != null) {
                            List<Account> accounts = user.getAccounts();
                            output.writeObject(accounts);
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("DEPOSIT".equals(requestType)) {
                        if (user != null) {
                            try {
                                String accountNumber = input.readUTF();
                                double amount = input.readDouble();
                                BankService.deposit(user, accountNumber, amount);
                                output.writeUTF("Deposit successful");
                            } catch (AccountNotFoundException e) {
                                output.writeUTF("ERROR: " + e.getMessage());
                            }
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("WITHDRAW".equals(requestType)) {
                        if (user != null) {
                            try {
                                String accountNumber = input.readUTF();
                                double amount = input.readDouble();
                                BankService.withdraw(user, accountNumber, amount);
                                output.writeUTF("Withdrawal successful");
                            } catch (AccountNotFoundException | InsufficientFundsException e) {
                                output.writeUTF("ERROR: " + e.getMessage());
                            }
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("TRANSFER".equals(requestType)) {
                        if (user != null) {
                            try {
                                String fromAccountNumber = input.readUTF();
                                String toAccountNumber = input.readUTF();
                                double amount = input.readDouble();
                                BankService.transfer(user, fromAccountNumber, toAccountNumber, amount);
                                output.writeUTF("Transfer successful");
                            } catch (AccountNotFoundException | InsufficientFundsException e) {
                                output.writeUTF("ERROR: " + e.getMessage());
                            }
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("VIEW_TRANSACTIONS".equals(requestType)) {
                        if (user != null) {
                            try {
                                String accountNumber = input.readUTF();
                                List<Transaction> transactions = BankService.getTransactions(user, accountNumber);
                                output.writeObject(transactions);
                                output.flush();
                            } catch (AccountNotFoundException e) {
                                output.writeUTF("ERROR: " + e.getMessage());
                                output.flush();
                            }
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("VIEW_ALL_USERS".equals(requestType)) {
                        if (admin != null) {
                            List<User> users = BankServer.getAllUsers();
                            output.writeObject(users);
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else if ("VIEW_USER_DETAILS".equals(requestType)) {
                        if (admin != null) {
                            String targetUserId = input.readUTF();
                            User targetUser = admin.getUserDetails(targetUserId);
                            if (targetUser != null) {
                                output.writeObject(targetUser);
                            } else {
                                output.writeUTF("User not found");
                            }
                            output.flush();
                        } else {
                            output.writeUTF("FAIL");
                            output.flush();
                        }
                    } else {
                        output.writeUTF("Unknown request");
                        output.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
