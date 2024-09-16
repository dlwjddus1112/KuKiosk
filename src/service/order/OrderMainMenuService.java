package service.order;

import java.util.Scanner;

public class OrderMainMenuService {

    Scanner scanner = new Scanner(System.in);
    public void start(){
        printMenu();
        int choice = getUserInput();
    }


    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       KuKiosk      ");
        System.out.println("----------------------");
        System.out.println("1) 상품 선택");
        System.out.println("2) 결제");
        System.out.println("3) 메뉴로 돌아가기");
        System.out.print("메뉴를 선택하세요 : ");
    }



    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
