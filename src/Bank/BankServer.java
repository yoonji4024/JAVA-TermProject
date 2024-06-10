package Bank;

import java.io.*;
import java.net.*;
import java.util.*;

public class BankServer {
    private static final int PORT = 12345;
    private static final String USER_DATA_FILE = "users.ser";
    private static final Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Admin> admins = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        loadUserData();
        initializeData();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Bank server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nClient connected: " + socket.getInetAddress());
                new BankClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static User getUser(String userId) {
        synchronized (users) {
            User user = users.get(userId);
            return user;
        }
    }

    public synchronized static Admin getAdmin(String adminId) {
        synchronized (admins) {
            Admin admin = admins.get(adminId);
            return admin;
        }
    }

    public synchronized static List<User> getAllUsers() {
        synchronized (users) {
            return new ArrayList<>(users.values());
        }
    }

    public synchronized static boolean addUser(User user) {
        synchronized (users) {
            if (!users.containsKey(user.getUserId())) {
                users.put(user.getUserId(), user);
                saveUserData();
                return true;
            }
            return false;
        }
    }

    private static void initializeData() {
        boolean dataChanged = false;

        synchronized (users) {
            if (users.isEmpty()) {
                users.put("user1", new User("user1", "password1"));
                dataChanged = true;
            }
        }

        synchronized (admins) {
            if (admins.isEmpty()) {
                admins.put("admin1", new Admin("admin1", "adminpass"));
                dataChanged = true;
            }
        }

        if (dataChanged) {
            saveUserData();
        }
    }

    protected synchronized static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            synchronized (users) {
                synchronized (admins) {
                    oos.writeObject(users);
                    oos.writeObject(admins);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            synchronized (users) {
                synchronized (admins) {
                    users.clear();
                    admins.clear();
                    users.putAll((Map<String, User>) ois.readObject());
                    admins.putAll((Map<String, Admin>) ois.readObject());
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
