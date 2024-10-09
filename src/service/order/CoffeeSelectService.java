package service.order;

import entity.Product;
import repository.ProductRepository;
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
            int choice = getUserInput();
            if(choice == 0){
                new SelectProductService().start(selectedProducts);
                return;
            }
            else if(choice <= coffees.size()){
                Product selectedCoffee = coffees.get(choice - 1);
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

    private void displayCoffees(List<Product> coffees) {
        System.out.println("----------------------");
        System.out.println("메뉴를 선택해주세요.");

        System.out.println("0. 상품선택 화면 나가기");

        for(int i = 0 ; i< coffees.size(); i++){
            Product coffee = coffees.get(i);
            System.out.println((i+1) + ". " + coffee.getProductName()+"("+coffee.getPrice()+"원)");
            if(coffee.getCurrentQuantity() == 0){
                System.out.print("[Sold Out]");
                System.out.println();
            }
        }

    }

    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

