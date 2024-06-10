package Bank;

import java.util.List;
import java.util.Objects;

public class Admin extends User {
    private static final long serialVersionUID = 1L;


    public Admin(String userId, String password) {
        super(userId, password);
    }

    public List<User> getAllUsers() {
        return BankServer.getAllUsers();
    }

    public User getUserDetails(String userId) {
        return BankServer.getUser(userId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(userId, admin.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "Admin{adminId='" + userId + "', password='" + password + "'}";
    }
}
