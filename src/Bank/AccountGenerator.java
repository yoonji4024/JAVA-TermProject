package Bank;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountGenerator implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ACCOUNT_NUMBER_FILE = "accountNumber.ser";
    private AtomicInteger accountCounter;

    private static AccountGenerator instance;

    private AccountGenerator() {
        accountCounter = new AtomicInteger(1000000000);
    }

    public static synchronized AccountGenerator getInstance() {
        if (instance == null) {
            instance = loadAccountNumber();
        }
        return instance;
    }

    public synchronized String generateAccountNumber() {
        String accountNumber = String.valueOf(accountCounter.getAndIncrement());
        saveAccountNumber();
        return accountNumber;
    }

    private static void saveAccountNumber() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNT_NUMBER_FILE))) {
            oos.writeObject(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static AccountGenerator loadAccountNumber() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCOUNT_NUMBER_FILE))) {
            return (AccountGenerator) ois.readObject();
        } catch (FileNotFoundException e) {
            return new AccountGenerator();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new AccountGenerator();
        }
    }
}
