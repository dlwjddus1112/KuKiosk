package service.main;
import java.util.ArrayList;

import entity.Product;
import repository.UserRepository;
import service.admin.AdminService;
import service.order.OrderMainMenuService;
import util.DateManager;
import util.UserSession;

import java.util.ArrayList;
import java.util.Date;
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
        UserSession.getInstance().setCurrentUser(user);
        setNowDate();
        new OrderMainMenuService(new ArrayList<Product>()).start();
    }

    private void setNowDate() {
        DateManager dm = DateManager.getInstance();
        System.out.print("오늘의 날짜를 입력해주세요(YYYYMMDD 형식으로 입력) : ");
        String dateInput = scanner.nextLine().trim();

        if (dateInput.length() != 8) {
            System.out.println("형식에 맞게 입력해주세요.");
            setNowDate();
            return;
        }

        try {
            int inputDate = Integer.parseInt(dateInput);
            int currentDate = dm.getCurrentDate();
            if (currentDate != 0 && inputDate <= currentDate) { // 이전 날짜인지 확인
                System.out.println("미래 날짜만 입력할 수 있습니다. 현재 가상 날짜: " + currentDate);
                setNowDate();
                return;
            }

            dm.setCurrentDate(inputDate);
            dm.saveDateToFile();
            System.out.println("날짜가 " + inputDate + "로 설정되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요.");
            setNowDate();
        }
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
