package service.order;

import entity.Ingredient;
import entity.Product;
import entity.User;
import repository.IngredientRepository;
import repository.OrderRepository;
import repository.UserRepository;
import service.main.MainMenuService;
import util.DateManager;
import util.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


//TODO : 결제할 때 쿠폰 사용할 수 있게 해야함
public class PaymentService {
    Scanner scanner = new Scanner(System.in);
    public void start(List<Product> selectedProducts){
        System.out.println("현재 선택된 상품:");
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            System.out.println((i + 1) + ". " + product.getProductName()+"("+product.getPrice()+"원)");
        }
        System.out.print("결제 하시겠습니까? (y/n): ");
        String input = scanner.nextLine().trim();
        if (input.equals("y")) {
            int money = 0;
            // 삭제할 제품들을 따로 저장
            List<Product> productsToRemove = new ArrayList<>();
            for (Product product : selectedProducts) {
                money += product.getPrice();
                reduceIngredient(product);
                productsToRemove.add(product); // 나중에 삭제하기 위해 저장
            }
            selectedProducts.removeAll(productsToRemove);
            System.out.println(money+"원을 결제하였습니다. 감사합니다");
            User currentUser = UserSession.getInstance().getCurrentUser();

            String id = currentUser.getLoginId();
            int currentDate = DateManager.getInstance().getCurrentDate();
            OrderRepository.getInstance().addOrder(id, money, currentDate);

            new OrderMainMenuService(selectedProducts).start();
        }else if (input.equals("n")) {
            System.out.println("결제를 취소하였습니다. 장바구니를 비웁니다.");
            List<Product> productsToRemove = new ArrayList<>();
            for (Product product : selectedProducts) {
                productsToRemove.add(product); // 나중에 삭제하기 위해 저장
            }
            selectedProducts.removeAll(productsToRemove);
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

            // 현재 재고 확인
            int currentQuantity = ingredientRepo.getIngredientQuantity(ingredient.getIngredientName());
            if (currentQuantity < requiredQuantity) {
                System.out.println("재고가 부족합니다.");
                new MainMenuService().start();
            }

            // 재료 수량 차감
            ingredientRepo.addIngredientQuantity(ingredient.getIngredientName(), -requiredQuantity);
        }
    }



}
