package service.order;

import entity.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DesertSelectService {
    Scanner scanner = new Scanner(System.in);

    public void start(List<Product> selectedProducts){
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Product> deserts= new ArrayList<>();


        for(Product product : products){
            if(product.getProductType().equals("디저트")){
                deserts.add(product);
            }
        }

        while(true) {
            displayDeserts(deserts);
            var menuInput = scanner.nextLine().trim();
            validateInput(menuInput,deserts);
            int menu = Integer.parseInt(menuInput);
            if(menu == 0){
                new SelectProductService().start(selectedProducts);
                return;
            }
            else if(menu <= deserts.size()){
                Product selectedDesert = deserts.get(menu - 1);
                if (selectedDesert.getCurrentQuantity() > 0) { // 재고가 있는 경우
                    selectedProducts.add(selectedDesert); // 선택한 커피 추가
                    selectedDesert.setCurrentQuantity(selectedDesert.getCurrentQuantity() - 1); // 재고 감소
                    System.out.println(selectedDesert.getProductName() + "가 추가되었습니다.");
                } else {
                    System.out.println("[Sold Out] 이 상품은 더 이상 구매할 수 없습니다.");
                }
            }
        }
    }

    private void displayDeserts(List<Product> deserts) {
        System.out.println("----------------------");


        System.out.println("0. 상품선택 화면 나가기");

        for(int i = 0 ; i< deserts.size(); i++){
            Product desert = deserts.get(i);
            System.out.println((i+1) + ". " + desert.getProductName()+"("+desert.getPrice()+"원)");
            if(desert.getCurrentQuantity() == 0){
                System.out.print("[Sold Out]");
                System.out.println();
            }
        }
        System.out.print("메뉴를 선택해주세요 : ");
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
}
