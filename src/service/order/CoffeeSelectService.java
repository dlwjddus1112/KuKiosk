package service.order;

import entity.Product;
import repository.ProductRepository;
import service.admin.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoffeeSelectService {
    Scanner scanner = new Scanner(System.in);

    public void start(List<Product> selectedProducts){
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Product> coffees= new ArrayList<>();


        for(Product product : products){
            if(product.getProductType().equals("커피")){
                coffees.add(product);
            }
        }

        while(true) {
            displayCoffees(coffees);
            var menuInput = scanner.nextLine().trim();
            validateInput(menuInput,coffees);
            int menu = Integer.parseInt(menuInput);
            if(menu == 0){
                new SelectProductService().start(selectedProducts);
            }
            else if(menu <= coffees.size()){
                Product selectedCoffee = coffees.get(menu - 1);
                if (selectedCoffee.getCurrentQuantity() > 0) { // 재고가 있는 경우
                    selectedProducts.add(selectedCoffee); // 선택한 커피 추가
                    selectedCoffee.setCurrentQuantity(selectedCoffee.getCurrentQuantity() - 1); // 재고 감소
                    System.out.println(selectedCoffee.getProductName() + "가 추가되었습니다.");
                } else {
                    System.out.println("[Sold Out] 이 상품은 더 이상 구매할 수 없습니다.");
                }
            }
        }
    }

    private void validateInput(String menuInput, List<Product> coffees) {
        try{
            int menuInt = Integer.parseInt(menuInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요. ");
            new SelectProductService().start(coffees);
        }
        int menu = Integer.parseInt(menuInput);
        if(menu > coffees.size() || menu < 0){
            if(coffees.size() == 1){
                System.out.println("1번 메뉴만 선택 가능합니다.");
                new SelectProductService().start(coffees);
            }
            else{
                System.out.println("메뉴는 1에서 " + coffees.size() + "사이로 입력해주세요.");
                new SelectProductService().start(coffees);
            }

        }
    }

    private void displayCoffees(List<Product> coffees) {
        System.out.println("----------------------");


        System.out.println("0. 상품선택 화면 나가기");

        for(int i = 0 ; i< coffees.size(); i++){
            Product coffee = coffees.get(i);
            System.out.println((i+1) + ". " + coffee.getProductName()+"("+coffee.getPrice()+"원)");
            if(coffee.getCurrentQuantity() == 0){
                System.out.print("[Sold Out]");
                System.out.println();
            }
        }
        System.out.print("메뉴를 선택해주세요 : ");

    }


}

