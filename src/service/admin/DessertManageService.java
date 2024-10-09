package service.admin;

import entity.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DessertManageService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Product> desserts = new ArrayList<>();
        for(Product product : products){
            if(product.getProductType().equals("디저트")){
                desserts.add(product);
            }
        }
        for(int i = 0 ; i< desserts.size(); i++){
            Product dessert = desserts.get(i);
            System.out.println((i+1) + "." + dessert.getProductName() + "(" + dessert.getCurrentQuantity() + " /" + dessert.getMaxQuantity() + ")");
            if(dessert.getCurrentQuantity() == 0){
                System.out.print("[Sold Out]");
                System.out.println();
            }
        }
        System.out.print("수량 조절할 메뉴를 선택해주세요(번호로 입력) : ");
        var menuInput = sc.nextLine().trim();
        validateInput(menuInput, desserts);
        int menu = Integer.parseInt(menuInput);
        System.out.print("수량 입력(감소인 경우에는 음수 입력) : ");
        var quantityInput = sc.nextLine().trim();
        try{
            int quantityInt = Integer.parseInt(quantityInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요.");
            new AdminService().start();
        }
        int quantity = Integer.parseInt(quantityInput);
        Product selectedDessert = desserts.get(menu - 1);
        validateQuantity(selectedDessert, quantity);
        String DessertName = selectedDessert.getProductName();


        System.out.print(menu + "번 메뉴 (" + DessertName + ")의 개수를 " + quantity + "개 증가하시겠습니까? (y/n) ");
        String answer = sc.nextLine().trim();
        if(answer.equals("y")){
            ProductRepository.getInstance().addProductQuantity(DessertName, quantity);
            System.out.println(menu + "번 메뉴 (" + DessertName + ")의 개수 : (" + selectedDessert.getCurrentQuantity() + " / " + selectedDessert.getMaxQuantity() + ")");
            new AdminService().start();
        }
        else if(answer.equals("n")){
            System.out.println("취소되었습니다.");
            new AdminService().start();
        }
        else{
            System.out.println("알맞지 않은 입력입니다.");
            new AdminService().start();
        }




    }

    private void validateQuantity(Product selectedDessert, int quantity) {
        if(selectedDessert.getCurrentQuantity() + quantity > selectedDessert.getMaxQuantity()){
            System.out.println(selectedDessert.getProductName() +"의 제한 수량은 " + selectedDessert.getMaxQuantity() + "개 입니다.");
            new AdminService().start();
        }
        if(selectedDessert.getCurrentQuantity() + quantity < 0){
            System.out.println("메뉴의 수량은 0개 미만일 수 없습니다.");
            new AdminService().start();
        }
    }

    private void validateInput(String menuInput, List<Product> desserts) {
        try{
            int menuInt = Integer.parseInt(menuInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요. ");
            new AdminService().start();
        }
        int menu = Integer.parseInt(menuInput);
        if(menu > desserts.size()  || menu < 0){
            if(desserts.size() == 1){
                System.out.println("1번 메뉴만 선택 가능합니다.");
                new AdminService().start();
            }
            else{
                System.out.println("메뉴는 1에서 " + desserts.size() + "사이로 입력해주세요.");
                new AdminService().start();
            }
        }
    }

    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("      디저트 재고 관리    ");
        System.out.println("----------------------");
    }
}
