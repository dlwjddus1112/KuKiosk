package service.admin;

import entity.Product;
import repository.ProductRepository;

import java.util.Scanner;

public class AddMenuService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        System.out.print("추가할 메뉴의 종류를 입력하세요(커피/디저트) : ");
        var menuType = sc.nextLine().trim();
        if(!validMenuType(menuType)){
            System.out.println("메뉴 종류는 커피, 디저트입니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }

        System.out.print("추가할 메뉴의 이름을 입력하세요 : ");
        var menuName = sc.nextLine().trim();
        if(!validMenuName(menuName)){
            System.out.println("메뉴 이름이 형식에 맞지 않게 입력되었습니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }
        var findProduct = ProductRepository.getInstance().findByMenuName(menuName);
        if(findProduct != null){
            System.out.println("이미 존재하는 메뉴입니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }

        System.out.print("추가할 메뉴의 가격을 입력하세요(최대 20,000원) : ");
        var menuPriceInput = sc.nextLine().trim();
        try{
            int menuPriceInt = Integer.parseInt(menuPriceInput);
        }catch (NumberFormatException e){
            System.out.println("숫자 형식으로 입력해주세요.");
            new AdminService().start();
        }
        int menuPrice = Integer.parseInt(menuPriceInput);

        if(menuPrice < 0){
            System.out.println("가격은 음수가 될 수 없습니다.");
            new AdminService().start();
        }
        else if(menuPrice > 20000){
            System.out.println("가격은 최대 20,000원입니다.");
            new AdminService().start();
        }



        System.out.print("추가할 메뉴의 제한수량을 입력하세요(최대 100개) : ");
        var quantityInput = sc.nextLine().trim();
        try{
            int quantityInt = Integer.parseInt(menuPriceInput);
        }catch (NumberFormatException e){
            System.out.println("숫자 형식으로 입력해주세요.");
            new AdminService().start();
        }
        int quantity = Integer.parseInt(quantityInput);
        if(quantity < 0){
            System.out.println("수량은 음수가 될 수 없습니다.");
            new AdminService().start();
        }
        else if(quantity > 100){
            System.out.println("최대 수량은 100개입니다.");
            new AdminService().start();
        }

        System.out.print("입력하신 메뉴를 추가하시겠습니까?(y/n)");
        String input = sc.nextLine().trim();
        if(input.equals("y")){
            ProductRepository.getInstance().addMenu(menuType, menuName, menuPrice, quantity);
            System.out.println("성공적으로 추가되었습니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }
        else if(input.equals("n")){
            System.out.println("메뉴 추가를 취소합니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }
        else{
            System.out.println("y 또는 n만 입력해주세요. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }


    }



    private boolean validMenuName(String menuName) {
        return menuName.matches("^[a-zA-Z0-9가-힣 ]{1,15}$");
    }

    private boolean validMenuType(String menuType) {
        if(menuType.equals("커피") || menuType.equals("디저트")){
            return true;
        }
        else{
            return false;
        }
    }

    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       메뉴 추가      ");
        System.out.println("----------------------");

    }

}
