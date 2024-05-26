package Bank;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BankClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static String loggedInUserId;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            login(scanner);
        } else if (choice == 2) {
            register(scanner);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("아이디: ");
        String userId = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            // 로그인 요청
            output.writeUTF(userId);
            output.writeUTF(password);
            output.writeUTF("LOGIN");
            output.flush();

            String response = input.readUTF();
            if ("USER".equals(response)) {
                loggedInUserId = userId;  // 로그인한 사용자 아이디 저장
                System.out.println(loggedInUserId + "로 로그인 성공");
                // 사용자 추가 요청 처리
                output.writeUTF("VIEW_ACCOUNTS");
                output.flush();

                // 서버의 응답 처리
                String accounts = input.readUTF();
                System.out.println("계좌 정보: " + accounts);
            } else if ("ADMIN".equals(response)) {
                loggedInUserId = userId;  // 로그인한 관리자 아이디 저장
                System.out.println(loggedInUserId + "로 로그인 성공");
                // 관리자 추가 요청 처리
                output.writeUTF("VIEW_ALL_USERS");
                output.flush();

                // 서버의 응답 처리
                String users = input.readUTF();
                System.out.println("사용자 정보: " + users);

                // 로그인한 관리자 세부 정보 조회
                output.writeUTF("VIEW_USER_DETAILS");
                output.writeUTF(loggedInUserId);  // 로그인한 관리자 아이디 사용
                output.flush();

                String userDetails = input.readUTF();
                System.out.println("관리자 세부 정보: " + userDetails);
            } else {
                System.out.println("로그인 실패");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("새 아이디: ");
        String userId = scanner.nextLine();
        System.out.print("새 비밀번호: ");
        String password = scanner.nextLine();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            // 회원가입 요청
            output.writeUTF(userId);
            output.writeUTF(password);
            output.writeUTF("REGISTER");
            output.flush();

            String response = input.readUTF();
            if ("REGISTER_SUCCESS".equals(response)) {
                System.out.println("회원가입 성공");
            } else {
                System.out.println("회원가입 실패: 이미 존재하는 아이디");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
