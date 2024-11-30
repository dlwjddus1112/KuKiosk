package service.main;
import java.util.*;

import entity.Product;
import entity.User;
import repository.UserRepository;
import service.admin.AdminService;
import service.order.OrderMainMenuService;
import util.CouponService;
import util.DateManager;
import util.UserSession;

import java.util.ArrayList;
//TODO : 날짜 형식에 맞게 입력하게 해야됨. 59일 이런거 안되게
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
        UserSession.getInstance().setCurrentUser(user); // 로그인한 사용자 정보 저장
        setNowDate();

        new OrderMainMenuService(new ArrayList<Product>()).start();
    }



    private void setNowDate() {
        DateManager dm = DateManager.getInstance();
        int currentDate = dm.getCurrentDate();
        System.out.print("오늘의 날짜를 입력해주세요(YYYYMMDD 형식으로 입력, 현재 가상 날짜 : " + currentDate + ") : ");
        String dateInput = scanner.nextLine().trim();

        if (dateInput.length() != 8) {
            System.out.println("형식에 맞게 입력해주세요.");
            setNowDate();
            return;
        }

        try {
            int inputDate = Integer.parseInt(dateInput);
            if (currentDate != 0 && inputDate < currentDate) {
                System.out.println("미래 날짜만 입력할 수 있습니다. 현재 가상 날짜: " + currentDate);
                setNowDate();
                return;
            }

            dm.setCurrentDate(inputDate);
            dm.saveDateToFile();
            checkMonthAndDay(currentDate,inputDate);// 입력 날짜와 현재 날짜를 비교하여 쿠폰 지급
            System.out.println("날짜가 " + inputDate + "로 설정되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요.");
            setNowDate();
        }
    }

    private void checkMonthAndDay(int currentDate, int inputDate) {
        int previousYear = currentDate / 10000;
        int previousMonth = (currentDate / 100) % 100;
        int currentYear = inputDate / 10000;
        int currentMonth = (inputDate / 100) % 100;

        if(currentDate == inputDate || previousMonth == currentMonth){
            System.out.println();
        }
        else{
            while(previousYear < currentYear || (previousYear == currentYear && previousMonth <= currentMonth)) {
                int monthFirstDate = previousYear * 10000 + previousMonth * 100 + 1;
                List<User> users = UserRepository.getInstance().findAllUsers();
                CouponService.giveCoupons(monthFirstDate,users);
                previousMonth++;
                if(previousMonth > 12){
                    previousYear++;
                    previousMonth = 1;
                }
            }
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
