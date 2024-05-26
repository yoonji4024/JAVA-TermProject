package Bank;

import javax.swing.*;
import GUI.LoginWindow;

public class BankClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}
