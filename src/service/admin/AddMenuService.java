package service.admin;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import repository.ProductRepository;

import java.util.*;

public class AddMenuService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        System.out.print("추가할 메뉴의 종류를 입력하세요(커피/디저트) : ");
        var menuType = sc.nextLine().trim();
        if(!menuType.matches("^[가-힣 ]{1,5}$")){
            System.out.println("메뉴 종류가 형식에 맞지 않게 입력되었습니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }
        else if(!validMenuType(menuType)){
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

        Map<Ingredient,Integer> ingredients = new HashMap<>();
        while(true){
            System.out.print("메뉴에 필요한 재료를 추가하시겠습니까?(y/n) ");
            var answer = sc.nextLine().trim();
            if(answer.equals("n")){
                if(ingredients.isEmpty()){
                    System.out.println("재료가 0개입니다. ");
                    new AdminService().start();
                }
                break;
            }
            else if(answer.equals("y")){
                if(ingredients.size() == 10){
                    System.out.println("한 메뉴에 들어가는 재료는 최대 10개입니다.");
                    new AdminService().start();
                }
                System.out.print("재료명을 입력해주세요 : ");
                String ingredientName = sc.nextLine().trim();
                if(!validIngredientName(ingredientName)){
                    System.out.println(ingredientName+"은 재료에 없습니다.");
                    new AdminService().start();
                }
                CheckDuplicateIngredient(ingredientName, ingredients);
                System.out.print("메뉴에 필요한 재료의 개수를 입력해주세요 : ");
                var menuQuantityInput = sc.nextLine().trim();
                try{
                    int menuQuantityInt = Integer.parseInt(menuQuantityInput);
                }catch (NumberFormatException e){
                    System.out.println("숫자 형식으로 입력해주세요.");
                    new AdminService().start();
                }
                int menuQuantity = Integer.parseInt(menuQuantityInput);
                if(menuQuantity <= 0){
                    System.out.println("재료의 개수는 0보다 커야 합니다.");
                    new AdminService().start();
                }
                if(menuQuantity > 5){
                    System.out.println("한 재료가 6개 이상 들어갈 수 없습니다.");
                    new AdminService().start();
                }
                Ingredient byIngredientName = IngredientRepository.getInstance().findByIngredientName(ingredientName);
                ingredients.put(byIngredientName, menuQuantity);

            }
            else{
                System.out.println("y 또는 n만 입력해주세요.");
                new AdminService().start();
            }
        }
        Map<Ingredient,Integer> extraIngredients = new HashMap<>();
        addExtraIngredients(ingredients, extraIngredients);

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



        System.out.print("입력하신 메뉴를 추가하시겠습니까?(y/n)");
        String input = sc.nextLine().trim();
        if(input.equals("y")){
            ProductRepository.getInstance().addMenu(menuType, menuName, menuPrice,ingredients,extraIngredients);
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

    private void addExtraIngredients(Map<Ingredient, Integer> ingredients, Map<Ingredient,Integer> extraIngredients) {
        while(true){
            System.out.print("고객이 추가/제외할 수 있는 재료를 추가하시겠습니까? (y/n) : ");
            String answer = sc.nextLine().trim();
            if(answer.equals("y")){
                System.out.print("고객이 추가/제외할 수 있는 재료명을 입력해주세요 : ");
                String extraIngredientName = sc.nextLine().trim();
                Ingredient extraIngredient = IngredientRepository.getInstance().findByIngredientName(extraIngredientName);

                if(ingredients.containsKey(extraIngredient)){
                    if(extraIngredients.containsKey(extraIngredient)){
                        System.out.println("이미 추가된 재료입니다.");
                    }
                    setIngredientPrice(extraIngredients, extraIngredient, extraIngredientName);
                }
                else if(extraIngredient == null){
                    System.out.println("존재하지 않는 재료입니다.");

                }
                else{
                    System.out.println("레시피에 포함되지 않는 재료입니다.");
                }
            }
            else if(answer.equals("n")){
                break;
            }
            else{
                System.out.println("y 또는 n만 입력해주세요.");
            }

        }
    }

    private void setIngredientPrice(Map<Ingredient, Integer> extraIngredients, Ingredient extraIngredient, String extraIngredientName) {
        System.out.print("추가/제외 재료의 금액을 입력해주세요 : ");
        var priceInput = sc.nextLine().trim();
        try {
            int price = Integer.parseInt(priceInput);
            if (price <= 0) {
                System.out.println("가격은 음수가 될 수 없습니다.");
            } else {
                extraIngredients.put(extraIngredient, price);
                System.out.println(extraIngredientName + " 재료가 추가되었습니다.");
            }
        }catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요.");
        }
    }

    private void CheckDuplicateIngredient(String ingredientName, Map<Ingredient, Integer> ingredients) {
        Ingredient findIngredient = IngredientRepository.getInstance().findByIngredientName(ingredientName);
        if(ingredients.containsKey(findIngredient)){
            System.out.println("이미 추가된 재료입니다. ");
            new AdminService().start();
        }
    }

    private boolean validIngredientName(String ingredientName) {
        return IngredientRepository.getInstance().findByIngredientName(ingredientName) != null;
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
