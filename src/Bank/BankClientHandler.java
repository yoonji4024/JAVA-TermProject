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
            // 로그인 및 요청 처리 로직
            String userId = input.readUTF();
            String password = input.readUTF();

            // 사용자 인증
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUserRequests(User user, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        while (true) {
            String request = input.readUTF();
            if (request.equals("EXIT")) {
                break;
            }
            BankService.handleRequest(user, request, input, output);
        }
    }

    private void handleAdminRequests(Admin admin, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        while (true) {
            String request = input.readUTF();
            if (request.equals("EXIT")) {
                break;
            }
            BankService.handleAdminRequest(admin, request, input, output);
        }
    }
}