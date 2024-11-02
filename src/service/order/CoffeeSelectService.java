package service.order;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import repository.ProductRepository;
import service.admin.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CoffeeSelectService {
    Scanner scanner = new Scanner(System.in);

    public void start(List<Product> selectedProducts){
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Ingredient> ingredients = IngredientRepository.getInstance().getIngredients();
        List<Product> coffees= new ArrayList<>();


        for(Product product : products){
            if(product.getProductType().equals("커피")){
                coffees.add(product);
            }
        }

        while(true) {
            displayCoffees(coffees,ingredients);
            var menuInput = scanner.nextLine().trim();
            validateInput(menuInput,coffees);
            int menu = Integer.parseInt(menuInput);
            if(menu == 0){
                new SelectProductService().start(selectedProducts);
            }
            else{
                Product selectedCoffee = coffees.get(menu - 1);
                if(menu <= coffees.size()){
                    if (!isSoldOut(selectedCoffee)) { // 재고가 있는 경우
                        selectedProducts.add(selectedCoffee); // 선택한 커피 추가
                        System.out.println(selectedCoffee.getProductName() + "가 추가되었습니다.");
                    } else {
                        System.out.println("[Sold Out] 이 상품은 더 이상 구매할 수 없습니다.");
                    }
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

    private void displayCoffees(List<Product> coffees, List<Ingredient> ingredients) {
        System.out.println("----------------------");
        System.out.println("0. 상품선택 화면 나가기");

        for (int i = 0; i < coffees.size(); i++) {
            Product coffee = coffees.get(i);
            boolean isSoldOut = isSoldOut(coffee);

            // 품절 상태에 따라 [Sold Out] 문구를 표시
            String soldOutText = isSoldOut ? "[Sold Out] " : "";
            System.out.println((i + 1) + ". " + soldOutText + coffee.getProductName() + " (" + coffee.getPrice() + "원)");
        }
        System.out.print("메뉴를 선택해주세요 : ");
    }


    private boolean isSoldOut(Product product) {
        Map<Ingredient, Integer> coffeeIngredients = product.getIngredients();

        for (Map.Entry<Ingredient, Integer> entry : coffeeIngredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int requiredQuantity = entry.getValue();
            int currentQuantity = IngredientRepository.getInstance().getIngredientQuantity(ingredient.getIngredientName());

            // 필요한 재료의 수량보다 현재 재고가 적으면 품절로 판단
            if (currentQuantity < requiredQuantity) {
                return true;
            }
        }
        return false; // 모든 재료의 수량이 충분하면 품절 아님
    }



}

