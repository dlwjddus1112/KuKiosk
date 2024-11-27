package service.order;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import repository.ProductRepository;
import service.main.MainMenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DesertSelectService {
    Scanner scanner = new Scanner(System.in);

    public void start(List<Product> selectedProducts){
        if(ProductRepository.getInstance().getProducts().isEmpty()){
            System.out.println("아무런 메뉴도 존재하지 않습니다. 메뉴가 등록되면 상품을 선택해주세요.");
            new MainMenuService().start();
        }
        List<Product> products = ProductRepository.getInstance().getProducts();
        List<Ingredient> ingredients = IngredientRepository.getInstance().getIngredients();
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
            Product selectedDeserts = deserts.get(menu - 1);

            if(menu <= deserts.size()){
                if (!isSoldOut(selectedDeserts)) { // 재고가 있는 경우
                    addExtraOptions(selectedDeserts);
                    selectedProducts.add(selectedDeserts); // 선택한 커피 추가
                    System.out.println(selectedDeserts.getProductName() + "가 추가되었습니다.");
                } else {
                    System.out.println("[Sold Out] 이 상품은 더 이상 구매할 수 없습니다.");
                }
            }
        }
    }

    private void displayDeserts(List<Product> deserts) {
        System.out.println("----------------------");
        if(deserts.isEmpty()){
            System.out.println("디저트 메뉴가 존재하지 않습니다.");
            new SelectProductService().start(deserts);
        }

        System.out.println("0. 상품선택 화면 나가기");

        for (int i = 0; i < deserts.size(); i++) {
            Product desert = deserts.get(i);
            boolean isSoldOut = isSoldOut(desert);

            // 품절 상태에 따라 [Sold Out] 문구를 표시
            String soldOutText = isSoldOut ? "[Sold Out] " : "";
            System.out.println((i + 1) + ". " + soldOutText + desert.getProductName() + " (" + desert.getPrice() + "원)");
        }
        System.out.print("메뉴를 선택해주세요 : ");
    }

    private void validateInput(String menuInput, List<Product> deserts) {
        try{
            int menuInt = Integer.parseInt(menuInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요. ");
            new SelectProductService().start(deserts);
        }
        int menu = Integer.parseInt(menuInput);
        if(menu > deserts.size() || menu < 0){
            if(deserts.size() == 1){
                System.out.println("1번 메뉴만 선택 가능합니다.");
                new SelectProductService().start(deserts);
            }
            else{
                System.out.println("메뉴는 1에서 " + deserts.size() + "사이로 입력해주세요.");
                new SelectProductService().start(deserts);
            }

        }
    }
    private boolean isSoldOut(Product product) {
        Map<Ingredient, Integer> desertIngredients = product.getIngredients();

        for (Map.Entry<Ingredient, Integer> entry : desertIngredients.entrySet()) {
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
    private void addExtraOptions(Product selectedCoffee) {
        List<Ingredient> extraIngredients = selectedCoffee.getExtraIngredients();
        if(extraIngredients.isEmpty()){
            System.out.println("추가할 수 있는 재료가 없습니다.");
        }
        else{
            System.out.println("---- 추가 옵션 ----");
            for (int i = 0; i < extraIngredients.size(); i++) {
                System.out.println((i+1) + ". " + extraIngredients.get(i).getIngredientName());
            }
            while(true) {
                System.out.print("재료를 추가/감소 하시겠습니까?(y/n)");
                String input = scanner.nextLine().trim();
                if (input.equals("y")) {
                    System.out.print("추가/감소할 재료를 선택해주세요 : ");
                    String extraIngredientName = scanner.nextLine().trim();
                    Ingredient foundIngredient = IngredientRepository.getInstance().findByIngredientName(extraIngredientName);
                    if (foundIngredient == null) {
                        System.out.println("존재하지 않는 재료입니다.");
                    } else if (extraIngredients.contains(foundIngredient)) {
                        System.out.print(extraIngredientName + "을 추가하시려면 y, 감소하시려면 n을 입력해주세요 : ");
                        String answer = scanner.nextLine().trim();

                       if (answer.equals("y")) {
                           System.out.print("수량을 입력해주세요 : ");
                           var quantityInput = scanner.nextLine().trim();
                           try {
                               int quantityInt = Integer.parseInt(quantityInput);
                           } catch (NumberFormatException e) {
                               System.out.println("숫자 형식으로 입력해주세요. ");
                               return;
                           }
                           int quantity = Integer.parseInt(quantityInput);
                           Map<Ingredient, Integer> addedIngredients = selectedCoffee.getAddedIngredients();

                           addedIngredients.put(foundIngredient, quantity);
                            System.out.println(extraIngredientName + " " + quantity + "개 추가되었습니다.");
                        } else if (answer.equals("n")) {
                           System.out.print("수량을 입력해주세요 : ");
                           var quantityInput = scanner.nextLine().trim();
                           try {
                               int quantityInt = Integer.parseInt(quantityInput);
                           } catch (NumberFormatException e) {
                               System.out.println("숫자 형식으로 입력해주세요. ");
                               return;
                           }
                           int quantity = Integer.parseInt(quantityInput);
                           Map<Ingredient, Integer> addedIngredients = selectedCoffee.getAddedIngredients();

                           addedIngredients.put(foundIngredient, -quantity);
                            System.out.println(extraIngredientName + " " + quantity + "개 감소되었습니다.");
                        } else {
                            System.out.println("y 또는 n만 입력해주세요.");
                        }
                    } else {
                        System.out.println("추가/감소가 불가능한 재료입니다.");
                    }
                } else if (input.equals("n")) {
                    System.out.println("재료를 추가/감소하지 않습니다.");
                    break;
                } else {
                    System.out.println("y 또는 n만 입력해주세요.");

                }
            }

        }


    }
}