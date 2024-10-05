package service.admin;

import entity.Product;
import repository.ProductRepository;

import java.util.Scanner;

public class DeleteMenuService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        System.out.print("삭제할 메뉴 이름을 입력해주세요 : ");
        var menuName = sc.nextLine().trim();
        if(!validMenuName(menuName)){
            System.out.println("메뉴 이름이 형식에 맞지 않게 입력되었습니다.");
            new AdminService().start();
        }

        Product findProduct = ProductRepository.getInstance().findByMenuName(menuName);
        if(findProduct == null){
            System.out.println("존재하지 않는 메뉴입니다.");
            new AdminService().start();
        }
        System.out.print(menuName + "을 삭제하시겠습니까? (y/n): ");
        String input = sc.nextLine().trim();
        if(input.equals("y")){
            ProductRepository.getInstance().deleteMenu(menuName);
            System.out.println(menuName + " 삭제되었습니다.");
            new AdminService().start();
        }
        else if(input.equals("n")){
            System.out.println(menuName + " 삭제가 취소되었습니다.");
            new AdminService().start();
        }
        else{
            System.out.println("y 또는 n만 입력해주세요.");
            this.start();
        }

    }

    private boolean validMenuName(String menuName) {
        return menuName.matches("^[a-zA-Z0-9가-힣 ]{1,15}$");
    }

    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       메뉴 삭제      ");
        System.out.println("----------------------");

    }
}
