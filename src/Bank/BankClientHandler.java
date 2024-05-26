package Bank;

import java.io.*;
import java.net.*;

public class BankClientHandler extends Thread {
    private Socket socket;

    public BankClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ) {
            String userId = input.readUTF();
            String password = input.readUTF();
            String requestType = input.readUTF();

            System.out.println("Request Type: " + requestType); // 로그 추가

            if ("REGISTER".equals(requestType)) {
                BankService.handleRegisterRequest(userId, password, output);
            } 
            else {
                User user = BankServer.getUser(userId);
                Admin admin = BankServer.getAdmin(userId);
                if (user != null && user.getPassword().equals(password)) {
                    output.writeUTF("USER");
                    output.flush();
                    handleUserRequests(user, input, output);
                } 
                else if (admin != null && admin.getPassword().equals(password)) {
                    output.writeUTF("ADMIN");
                    output.flush();
                    handleAdminRequests(admin, input, output);
                } 
                else {
                    output.writeUTF("FAIL");
                    output.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUserRequests(User user, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        try {
            while (true) {
                String request = input.readUTF();
                System.out.println("User Request: " + request); // 로그 추가
                if (request.equals("EXIT")) {
                    break;
                }
                BankService.handleRequest(user, request, input, output);
            }
        } catch (IOException e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }

    private void handleAdminRequests(Admin admin, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        try {
            while (true) {
                String request = input.readUTF();
                System.out.println("Admin Request: " + request); // 로그 추가
                if (request.equals("EXIT")) {
                    break;
                }
                BankService.handleAdminRequest(admin, request, input, output);
            }
        } catch (IOException e) {
            output.writeUTF("ERROR: " + e.getMessage());
            output.flush();
        }
    }
}
