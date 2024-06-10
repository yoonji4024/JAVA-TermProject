package Bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String userId; // protected로 변경하여 Admin에서 접근 가능하도록 수정
    protected String password; // protected로 변경하여 Admin에서 접근 가능하도록 수정
    protected List<Account> accounts;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
    
    public void addAccount(Account a) {
    	accounts.add(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', password='" + password + "', accounts=" + accounts + "}";
    }
}
