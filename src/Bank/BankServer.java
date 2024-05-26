package Bank;

import java.io.*;
import java.net.*;
import java.util.*;

public class BankServer {
    private static final int PORT = 12345;
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Admin> admins = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Bank server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                new BankClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized User getUser(String userId) {
        return users.get(userId);
    }

    public static synchronized Admin getAdmin(String adminId) {
        return admins.get(adminId);
    }

    public static synchronized List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    static {
        users.put("user1", new User("user1", "password1"));
        admins.put("admin1", new Admin("admin1", "adminpass"));
    }
}
