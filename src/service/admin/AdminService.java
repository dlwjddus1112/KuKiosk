package service.admin;

import service.main.MainMenuService;

import java.util.Scanner;

public class AdminService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printAdminMenu();
        int choice = getUserInput();
        switch(choice){
            case 1:
                new StockManageService().start();
                break;
            case 2:
                new AddMenuService().start();
                break;
            case 3:
                new DeleteMenuService().start();
                break;
            case 4:
                new MainMenuService().start();
                break;
            default:
                System.out.println("1~4 사이의 값만 입력해주세요.");
                this.start();
        }
    }

    private void printAdminMenu(){
        System.out.println("----------------------");
        System.out.println("       관리자 메뉴      ");
        System.out.println("----------------------");
        System.out.println("1) 재고 관리 ");
        System.out.println("2) 메뉴 추가");
        System.out.println("3) 메뉴 삭제");
        System.out.println("4) 로그아웃 ");
        System.out.print("메뉴를 입력하세요: ");
    }

    private int getUserInput() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;

        }
    }
}
