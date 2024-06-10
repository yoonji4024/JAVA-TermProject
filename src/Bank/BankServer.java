package Bank;

import java.io.*;
import java.net.*;
import java.util.*;

public class BankServer {
    private static final int PORT = 12345;
    private static final String USER_DATA_FILE = "users.ser"; // 파일 확장자를 .ser로 변경
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
            System.out.println("in getUser: " + users);
            User user = users.get(userId);
            System.out.println("in getUser: " + user);
            System.out.println("getUser: Searching for userId=" + userId + ", Found: " + user);
            return user;
        }
    }

    public synchronized static Admin getAdmin(String adminId) {
        synchronized (admins) {
            Admin admin = admins.get(adminId);
            System.out.println("getAdmin: Searching for adminId=" + adminId + ", Found: " + admin);
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
                saveUserData(); // 사용자 데이터 변경 시 파일에 저장
                System.out.println("User added: " + user.getUserId());
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
                System.out.println("Default user added: user1");
                dataChanged = true;
            }
        }

        synchronized (admins) {
            if (admins.isEmpty()) {
                admins.put("admin1", new Admin("admin1", "adminpass"));
                System.out.println("Default admin added: admin1");
                dataChanged = true;
            }
        }

        if (dataChanged) {
            saveUserData(); // 초기 데이터를 설정한 후 저장
        }
    }

    protected synchronized static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            synchronized (users) {
                synchronized (admins) {
                    oos.writeObject(users);
                    oos.writeObject(admins);
                    System.out.println("User data saved.");
                    System.out.println("in save: " + users);
                    System.out.println("in save (admins): " + admins);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            System.out.println("User data file not found, starting with empty user data.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            synchronized (users) {
                synchronized (admins) {
                    users.clear();
                    admins.clear();
                    users.putAll((Map<String, User>) ois.readObject());
                    admins.putAll((Map<String, Admin>) ois.readObject());
                    System.out.println("User data loaded.");
                    System.out.println("in load: " + users);
                    System.out.println("in load (admins): " + admins);
                    printUserData(); // 추가 로그로 데이터가 제대로 로드되었는지 확인
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found, starting with empty user data.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void printUserData() {
        synchronized (users) {
            System.out.println("[Users]");
            for (String userId : users.keySet()) {
                System.out.println("UserID: " + userId + ", User: " + users.get(userId));
            }
        }
        synchronized (admins) {
            System.out.println("[Admins]");
            for (String adminId : admins.keySet()) {
                System.out.println("AdminID: " + adminId + ", Admin: " + admins.get(adminId));
            }
        }
    }
}
