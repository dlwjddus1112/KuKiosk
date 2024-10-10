package service.order;
import java.util.Scanner;

import entity.Product;
import java.util.ArrayList;
import java.util.List;
public class SelectProductService {
    Scanner scanner = new Scanner(System.in);
    List<Product> selectedProducts = new ArrayList<>();

    public void start(List<Product> selectedProducts){
        printMenu();
        int choice = getUserInput();
        switch (choice){
            case 0:
                new OrderMainMenuService(selectedProducts).start();
                break;
            case 1:
                new CoffeeSelectService().start(selectedProducts);
                break;
            case 2:
                new DesertSelectService().start(selectedProducts);
                break;
            default:
                System.out.println("0~2 사이의 값만 입력해주세요");
                this.start(selectedProducts);
                break;


        }
    }
    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       상품 선택       ");
        System.out.println("----------------------");
        System.out.println("0. 상품 선택 화면 나가기");
        System.out.println("1. 커피");
        System.out.println("2. 디저트");
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
