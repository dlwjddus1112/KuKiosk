package service.admin;

import entity.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoffeeManageService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Product> coffees = new ArrayList<>();
        for(Product product : products){
            if(product.getProductType().equals("커피")){
                coffees.add(product);
            }
        }
        for(int i = 0 ; i< coffees.size(); i++){
            Product coffee = coffees.get(i);
            System.out.println((i+1) + "." + coffee.getProductName() + "(" + coffee.getCurrentQuantity() + " /" + coffee.getMaxQuantity() + ")");
            if(coffee.getCurrentQuantity() == 0){
                System.out.print("[Sold Out]");
                System.out.println();
            }
        }
        System.out.print("수량 조절할 메뉴를 선택해주세요(번호로 입력) : ");
        var menuInput = sc.nextLine().trim();
        validateInput(menuInput, coffees);
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
        Product selectedCoffee = coffees.get(menu - 1);
        validateQuantity(selectedCoffee, quantity);
        String CoffeeName = selectedCoffee.getProductName();

        if(quantity > 0){
            System.out.print(menu + "번 메뉴 (" + CoffeeName + ")의 개수를 " + quantity + "개 증가하시겠습니까? (y/n) ");
        }
        else{
            System.out.print(menu + "번 메뉴 (" + CoffeeName + ")의 개수를 " + (quantity * -1) + "개 감소하시겠습니까? (y/n) ");

        }
        String answer = sc.nextLine().trim();
        if(answer.equals("y")){
            ProductRepository.getInstance().addProductQuantity(CoffeeName, quantity);
            System.out.println(menu + "번 메뉴 (" + CoffeeName + ")의 개수 : (" + selectedCoffee.getCurrentQuantity() + " / " + selectedCoffee.getMaxQuantity() + ")");
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

    private void validateQuantity(Product selectedCoffee, int quantity) {
        if(selectedCoffee.getCurrentQuantity() + quantity > selectedCoffee.getMaxQuantity()){
            System.out.println(selectedCoffee.getProductName() +"의 제한 수량은 " + selectedCoffee.getMaxQuantity() + "개 입니다.");
            new AdminService().start();
        }
        if(selectedCoffee.getCurrentQuantity() + quantity < 0){
            System.out.println("메뉴의 수량은 0개 미만일 수 없습니다.");
            new AdminService().start();
        }
    }

    private void validateInput(String menuInput, List<Product> coffees) {
        try{
            int menuInt = Integer.parseInt(menuInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요. ");
            new AdminService().start();
        }
        int menu = Integer.parseInt(menuInput);
        if(menu > coffees.size() || menu < 0){
            System.out.println("메뉴는 1에서 " + (coffees.size() + 1) + "사이로 입력해주세요.");
            new AdminService().start();
        }
    }

    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("      커피 재고 관리    ");
        System.out.println("----------------------");
    }
}
