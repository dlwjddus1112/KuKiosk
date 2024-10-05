package service.admin;

import java.util.Scanner;

public class StockManageService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        int choice = getUserInput();
        switch(choice){
            case 0 :
                new AdminService().start();
                break;
            case 1 :
                new CoffeeManageService().start();
                break;
            case 2 :
                new DessertManageService().start();
                break;
            default:
                System.out.println("0~2 사이의 값만 입력해주세요");
                this.start();
        }
    }


    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       재고 관리      ");
        System.out.println("----------------------");
        System.out.println("0) 메뉴화면 나가기");
        System.out.println("1) 커피 ");
        System.out.println("2) 디저트 ");
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
