package service.main;
import java.util.ArrayList;

import entity.Product;
import repository.UserRepository;
import service.admin.AdminService;
import service.order.OrderMainMenuService;

import java.util.ArrayList;
import java.util.Scanner;

public class LoginService {
    private final Scanner scanner = new Scanner(System.in);
    private final String adminID = "admin";
    private final String adminPWD = "1234";
    public void start() {
        printLoginMenu();

        String id = getLoginId();
        String password = getLoginPassword();

        if(id.equals(adminID) && password.equals(adminPWD)) {
            new AdminService().start();
        }
        var user = UserRepository.getInstance().findUserByLoginId(id);

        if (user == null) {
            System.out.println("등록되지 않은 아이디입니다.");
            System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        if (!user.password.equals(password)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }
        System.out.println("로그인 성공");
        new OrderMainMenuService(new ArrayList<Product>()).start();
    }

    private void printLoginMenu() {
        System.out.println("<로그인>");
        System.out.println("----------------------");
    }

    private String getLoginId() {
        System.out.print("아이디: ");
        return scanner.nextLine().trim();
    }

    private String getLoginPassword() {
        var console = System.console();
        if (console == null) {
            System.out.print("비밀번호: ");
            return scanner.nextLine().trim();
        }

        return new String(console.readPassword("비밀번호: "));
    }


}
