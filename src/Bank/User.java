package Bank;
import java.util.*;

public class User { // 사용자 class
	private String userID;
	private String password;
	private List<Account> accounts;
	
	public User(String userID, String password) {
		this.userID = userID;
		this.password = password;
		this.accounts = new ArrayList<>();
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
}
