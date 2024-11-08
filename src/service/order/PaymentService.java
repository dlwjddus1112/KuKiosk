package service.order;

import entity.Ingredient;
import entity.Product;
import repository.IngredientRepository;
import service.main.MainMenuService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PaymentService {
    Scanner scanner = new Scanner(System.in);
    public void start(List<Product> selectedProducts){
        System.out.println("현재 선택된 상품:");
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            System.out.println((i + 1) + ". " + product.getProductName()+"("+product.getPrice()+"원)");
        }
        System.out.print("결제 하시겠습니까? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase(); // 입력을 소문자로 변
        if (input.equals("y")) {
            int money = 0;
            for (Product product : selectedProducts) {
                money += product.getPrice();
                reduceIngredient(product);
            }
            System.out.println(money+"원을 결제하였습니다. 감사합니다");
            new OrderMainMenuService(selectedProducts).start();
        }else if (input.equals("n")) {
            System.out.println("결제를 취소하였습니다. 장바구니를 비웁니다.");
            new MainMenuService().start();
        } else {
            System.out.println("잘못된 입력입니다. 'y' 또는 'n'을 입력하세요.");
            new PaymentService().start(selectedProducts);
        }


    }

    private void reduceIngredient(Product product) {
        Map<Ingredient, Integer> ingredients = product.getIngredients(); // 각 상품의 재료와 수량

        IngredientRepository ingredientRepo = IngredientRepository.getInstance();

        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int requiredQuantity = entry.getValue();

            // 재료 수량 차감
            ingredientRepo.addIngredientQuantity(ingredient.getIngredientName(), -requiredQuantity);
        }
    }


}
