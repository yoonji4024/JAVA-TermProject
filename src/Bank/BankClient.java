package Bank;

import java.io.*;
import java.net.*;

public class BankClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            // 로그인 요청
            output.writeUTF("admin1");  // 관리자 아이디
            output.writeUTF("adminpass");  // 비밀번호
            output.flush();

            String response = input.readUTF();
            if ("USER".equals(response)) {
                System.out.println("사용자로 로그인 성공");
                // 사용자 추가 요청 처리
                output.writeUTF("VIEW_ACCOUNTS");
                output.flush();

                // 서버의 응답 처리
                String accounts = input.readUTF();
                System.out.println("계좌 정보: " + accounts);
            } 
            else if ("ADMIN".equals(response)) {
                System.out.println("관리자로 로그인 성공");
                // 관리자 추가 요청 처리
                output.writeUTF("VIEW_ALL_USERS");
                output.flush();

                // 서버의 응답 처리
                String users = input.readUTF();
                System.out.println("사용자 정보: " + users);

                // 특정 사용자 세부 정보 조회
                output.writeUTF("VIEW_USER_DETAILS");
                output.writeUTF("user1");
                output.flush();

                String userDetails = input.readUTF();
                System.out.println("사용자 세부 정보: " + userDetails);
            } 
            else {
                System.out.println("로그인 실패");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}