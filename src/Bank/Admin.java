package Bank;
import java.util.*;

public class Admin extends User {
    public Admin(String userId, String password) {
        super(userId, password);
    }

    // 모든 사용자를 반환하는 메서드
    public List<User> getAllUsers() {
        return BankServer.getAllUsers();
    }

    // 특정 사용자의 세부 정보를 반환하는 메서드
    public User getUserDetails(String userId) {
        return BankServer.getUser(userId);
    }
}