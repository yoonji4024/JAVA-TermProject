package Bank;

import java.io.*;
import java.net.*;
import java.util.*;

public class BankServer {
    private static final int PORT = 12345;
    private static final Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Admin> admins = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        initializeData();
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

    public static User getUser(String userId) {
        synchronized (users) {
            return users.get(userId);
        }
    }

    public static Admin getAdmin(String adminId) {
        synchronized (admins) {
            return admins.get(adminId);
        }
    }

    public static List<User> getAllUsers() {
        synchronized (users) {
            return new ArrayList<>(users.values());
        }
    }

    public static boolean addUser(User user) {
        synchronized (users) {
            if (!users.containsKey(user.getUserId())) {
                users.put(user.getUserId(), user);
                return true;
            }
            return false;
        }
    }

    private static void initializeData() {
        users.put("user1", new User("user1", "password1"));
        admins.put("admin1", new Admin("admin1", "adminpass"));
    }
}
