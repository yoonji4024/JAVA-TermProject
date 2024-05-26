package Bank;

import java.util.List;

public class Admin extends User {
    public Admin(String userId, String password) {
        super(userId, password);
    }

    public List<User> getAllUsers() {
        return BankServer.getAllUsers();
    }

    public User getUserDetails(String userId) {
        return BankServer.getUser(userId);
    }
}
