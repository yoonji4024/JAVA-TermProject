package Bank;

import java.io.*;
import java.net.*;
import java.util.*;

public class BankServer {
    private static final int PORT = 12345;
    private static final String USER_DATA_FILE = "users.dat";
    private static Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, Admin> admins = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        loadUserData();
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
                saveUserData(); // 사용자 데이터 변경 시 파일에 저장
                System.out.println("User added: " + user.getUserId());
                return true;
            }
            return false;
        }
    }

    private static void initializeData() {
        if (users.isEmpty()) {
            users.put("user1", new User("user1", "password1"));
        }
        if (admins.isEmpty()) {
            admins.put("admin1", new Admin("admin1", "adminpass"));
        }
        saveUserData(); // 초기 데이터를 설정한 후 저장
    }

    protected static void saveUserData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUserData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (Map<String, User>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found, starting with empty user data.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
