package service.admin;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class DeleteIngredientService {
    private final Scanner sc = new Scanner(System.in);
    public void start() {
        printMenu();
        System.out.print("삭제할 재료의 이름을 입력해주세요 : ");
        var ingredientName = sc.nextLine().trim();
        if(!validIngredientName(ingredientName)){
            System.out.println("재료 이름이 형식에 맞지 않게 입력되었습니다.");
            new AdminService().start();
        }
        Ingredient foundIngredient = IngredientRepository.getInstance().findByIngredientName(ingredientName);
        if (foundIngredient == null) {
            System.out.println("존재하지 않는 재료입니다.");
            new AdminService().start();
        }
        System.out.print(ingredientName + "을 삭제하시겠습니까?(y/n)");
        String input = sc.nextLine().trim();
        if(input.equals("y")){
            boolean isRecipe = false;
            List<Product> products = ProductRepository.getInstance().getProducts();
            for (Product product : products) {
                Map<Ingredient, Integer> ingredients = product.getIngredients();
                if (ingredients.containsKey(foundIngredient)) {
                    isRecipe = true;
                    break;
                }
            }
            if(!isRecipe){
                IngredientRepository.getInstance().deleteIngredient(ingredientName);
                System.out.println(ingredientName +"가 삭제되었습니다.");
            }
            else{
                System.out.println("레시피에 존재하는 재료는 삭제할 수 없습니다.");
            }

            new AdminService().start();
        }
        else if(input.equals("n")){
            System.out.println(ingredientName + " 삭제가 취소되었습니다.");
            new AdminService().start();
        }
        else{
            System.out.println("y 또는 n만 입력해주세요.");
            this.start();
        }


    }

    private boolean validIngredientName(String ingredientName) {
        return ingredientName.matches("^[가-힣 ]{1,5}$");
    }

    private void printMenu(){

        System.out.println("----------------------");
        System.out.println("       재료 삭제     ");
        System.out.println("----------------------");


    }
}
