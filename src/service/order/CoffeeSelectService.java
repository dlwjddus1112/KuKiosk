package service.order;

import entity.Ingredient;
import entity.Product;
import entity.User;
import repository.IngredientRepository;
import repository.ProductRepository;
import service.admin.AdminService;
import service.main.MainMenuService;
import util.UserSession;

import java.util.*;

public class CoffeeSelectService {
    Scanner scanner = new Scanner(System.in);

    public void start(List<Product> selectedProducts){
        User currentUser = UserSession.getInstance().getCurrentUser();
        if(ProductRepository.getInstance().getProducts().isEmpty()){
            System.out.println("아무런 메뉴도 존재하지 않습니다. 메뉴가 등록되면 상품을 선택해주세요.");
            new MainMenuService().start();
        }
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
                        int optionPrice = addExtraOptions(selectedCoffee);
                        currentUser.setExtraOptionPrice(optionPrice);
                        selectedProducts.add(selectedCoffee);
                        System.out.println(selectedCoffee.getProductName() + "가 추가되었습니다.");
                    } else {
                        System.out.println("[Sold Out] 이 상품은 더 이상 구매할 수 없습니다.");
                    }
                }
            }

        }
    }

    private int addExtraOptions(Product selectedCoffee) {
        Map<Ingredient, Integer> extraIngredients = selectedCoffee.getExtraIngredients();
        int extraOptionPrice = 0;
        if(extraIngredients.isEmpty()){
            System.out.println("추가할 수 있는 재료가 없습니다.");
        }
        else{
            System.out.println("---- 추가 옵션 ----");
            int k = 1;
            for (Ingredient extraIngredient : extraIngredients.keySet()) {
                System.out.println(k + ". " + extraIngredient.getIngredientName() + "(" + extraIngredients.get(extraIngredient) + "원)");
                k++;
            }

            while(true){
                System.out.print("재료를 추가/감소 하시겠습니까?(y/n)");
                String input = scanner.nextLine().trim();
                if(input.equals("y")){
                    System.out.print("추가/감소할 재료를 선택해주세요 : ");
                    String extraIngredientName = scanner.nextLine().trim();
                    Ingredient foundIngredient = IngredientRepository.getInstance().findByIngredientName(extraIngredientName);
                    if(foundIngredient == null){
                        System.out.println("존재하지 않는 재료입니다.");
                    }
                    else if(extraIngredients.containsKey(foundIngredient)){
                        System.out.print(extraIngredientName+"을 추가하시려면 y, 감소하시려면 n을 입력해주세요(토핑을 원치 않으시면 그 외의 입력을 입력해주세요) : ");
                        String answer = scanner.nextLine().trim();


                        if(answer.equals("y")){
                            System.out.print("수량을 입력해주세요 : ");
                            var quantityInput = scanner.nextLine().trim();
                            try{
                                int quantityInt = Integer.parseInt(quantityInput);
                                if (quantityInt <= 0) {
                                    System.out.println("수량은 1 이상이어야 합니다.");
                                    continue;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("숫자 형식으로 입력해주세요. ");
                                continue;
                            }
                            int quantity = Integer.parseInt(quantityInput);
                            int ingredientPrice = extraIngredients.get(foundIngredient) * quantity;
                            extraOptionPrice += ingredientPrice;
                            Map<Ingredient, Integer> addedIngredients = selectedCoffee.getAddedIngredients();
                            addedIngredients.put(foundIngredient, quantity);
                            System.out.println(extraIngredientName + " " + quantity + "개 추가되었습니다.");
                        }
                        else if(answer.equals("n")){
                            System.out.print("수량을 입력해주세요 : ");
                            var quantityInput = scanner.nextLine().trim();
                            try{
                                int quantityInt = Integer.parseInt(quantityInput);
                                if (quantityInt <= 0) {
                                    System.out.println("수량은 1 이상이어야 합니다.");
                                    continue;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("숫자 형식으로 입력해주세요. ");
                                continue;
                            }
                            Map<Ingredient, Integer> recipe = selectedCoffee.getIngredients();
                            int quantity = Integer.parseInt(quantityInput);
                            if(recipe.containsKey(foundIngredient)){
                                if(recipe.get(foundIngredient) - quantity < 0 ){
                                    System.out.println(selectedCoffee.getProductName()+ "은 기본적으로 "
                                            + foundIngredient.getIngredientName() + "이 "
                                            + recipe.get(foundIngredient)
                                            + "개 들어갑니다. 음수가 될 수 없습니다.");
                                }
                                else{
                                    Map<Ingredient, Integer> addedIngredients = selectedCoffee.getAddedIngredients();
                                    addedIngredients.put(foundIngredient, -quantity);
                                    System.out.println(extraIngredientName + " " + quantity + "개 감소되었습니다.");
                                }
                            }

                        }
                        else{
                            System.out.println("y 또는 n만 입력해주세요.");
                        }
                    }
                    else{
                        System.out.println("추가/감소가 불가능한 재료입니다.");
                    }
                }
                else if(input.equals("n")){
                    System.out.println("재료를 추가/감소하지 않습니다.");
                    break;
                }
                else{
                    System.out.println("y 또는 n만 입력해주세요.");

                }
            }



        }
        return extraOptionPrice;

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
        if(coffees.isEmpty()){
            System.out.println("커피 메뉴가 존재하지 않습니다.");
            new SelectProductService().start(coffees);
        }
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

