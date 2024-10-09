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
            int choice = getUserInput();
            if(choice == 0){
                new SelectProductService().start(selectedProducts);
                return;
            }
            else if(choice <= deserts.size()){
                Product selectedDesert = deserts.get(choice - 1);
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
        System.out.println("메뉴를 선택해주세요.");

        System.out.println("0. 상품선택 화면 나가기");

        for(int i = 0 ; i< deserts.size(); i++){
            Product desert = deserts.get(i);
            System.out.println((i+1) + ". " + desert.getProductName()+"("+desert.getPrice()+"원)");
            if(desert.getCurrentQuantity() == 0){
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
