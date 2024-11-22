package service.order;

import entity.Product;
import service.main.MainMenuService;
import util.UserSession;

import java.util.List;

import java.util.Scanner;


//TODO : 쿠폰 조회기능 만들어야함.
public class OrderMainMenuService {
    List<Product> selectedProducts; // 리스트를 필드로 선언

    public OrderMainMenuService(List<Product> selectedProducts) { // 생성자에서 리스트 초기화
        this.selectedProducts = selectedProducts;
    }

    private final Scanner scanner = new Scanner(System.in);
    public void start(){
        printMenu();
        int choice = getUserInput();
        switch (choice){
            case 1:
                new SelectProductService().start(selectedProducts);
                break;
            case 2:
                if(!selectedProducts.isEmpty())
                    new PaymentService().start(selectedProducts);
                else{
                    System.out.println("장바구니가 비어있습니다.");
                    new OrderMainMenuService(selectedProducts).start();
                }
                break;
            case 3:
                new ShowCouponService().start(selectedProducts);
                break;
            case 4:
                UserSession.getInstance().clearSession();
                new MainMenuService().start();
                break;
            default:
                System.out.println("1~3 사이의 값만 입력해주세요");
                this.start();
                break;
        }
    }




    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       KuKiosk      ");
        System.out.println("----------------------");
        System.out.println("1) 상품 선택");
        System.out.println("2) 결제");
        System.out.println("3) 쿠폰 조회");
        System.out.println("4) 로그아웃");
        System.out.print("메뉴를 입력하세요 : ");

    }



    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
