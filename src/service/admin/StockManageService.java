package service.admin;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import repository.ProductRepository;

import java.util.List;
import java.util.Scanner;

public class StockManageService {
    private final Scanner sc = new Scanner(System.in);
    public void start(){
        printMenu();
        List<Ingredient> ingredients = IngredientRepository.getInstance().getIngredients();
        for(int i = 0 ; i < ingredients.size() ; i++){
            Ingredient ingredient = ingredients.get(i);
            System.out.println((i+1) + "." + ingredient.getIngredientName() + " (" +ingredient.getCurrentQuantity() + " / " + ingredient.getMaxQuantity() +")" );
        }
        System.out.print("재고를 증가/감소할 재료를 선택해주세요(번호로 입력) : ");
        var menuInput = sc.nextLine().trim();
        validateInput(menuInput, ingredients);
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
        Ingredient selectedIngredient = ingredients.get(menu - 1);
        validateQuantity(selectedIngredient, quantity);
        String ingredientName = selectedIngredient.getIngredientName();

        if(quantity > 0){
            System.out.print(menu + "번 메뉴 (" + ingredientName + ")의 개수를 " + quantity + "개 증가하시겠습니까? (y/n) ");
        }
        else{
            System.out.print(menu + "번 메뉴 (" + ingredientName + ")의 개수를 " + (quantity * -1) + "개 감소하시겠습니까? (y/n) ");

        }
        String answer = sc.nextLine().trim();
        if(answer.equals("y")){
            IngredientRepository.getInstance().addIngredientQuantity(ingredientName, quantity);
            System.out.println(menu + "번 메뉴 (" + ingredientName + ")의 개수 : (" + selectedIngredient.getCurrentQuantity() + " / " + selectedIngredient.getMaxQuantity() + ")");
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

    private void validateQuantity(Ingredient selectedIngredient, int quantity) {
        if(selectedIngredient.getCurrentQuantity() + quantity > selectedIngredient.getMaxQuantity()){
            System.out.println(selectedIngredient.getIngredientName() +"의 제한 수량은 " + selectedIngredient.getMaxQuantity() + "개 입니다.");
            new AdminService().start();
        }
        if(selectedIngredient.getCurrentQuantity() + quantity < 0){
            System.out.println("재료의 수량은 0개 미만일 수 없습니다.");
            new AdminService().start();
        }
    }

    private void validateInput(String menuInput, List<Ingredient> ingredients) {
        try{
            int menuInt = Integer.parseInt(menuInput);
        } catch (NumberFormatException e) {
            System.out.println("숫자 형식으로 입력해주세요. ");
            new AdminService().start();
        }
        int menu = Integer.parseInt(menuInput);
        if(menu > ingredients.size() || menu <= 0){
            if(ingredients.size() == 1){
                System.out.println("1번 메뉴만 선택 가능합니다.");
                new AdminService().start();
            }
            else{
                System.out.println("메뉴는 1에서 " + ingredients.size() + "사이로 입력해주세요.");
                new AdminService().start();
            }

        }
    }


    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       재고 관리      ");
        System.out.println("----------------------");

    }


}
