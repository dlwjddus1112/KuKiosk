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

import java.util.*;


public class PaymentService {
    Scanner scanner = new Scanner(System.in);
    public void start(List<Product> selectedProducts){
        User currentUser = UserSession.getInstance().getCurrentUser();
        UserRepository userRepository = UserRepository.getInstance();
        System.out.println("현재 선택된 상품:");
        int totalPrice = 0;
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            System.out.println((i + 1) + ". " + product.getProductName()+"("+product.getPrice()+"원)");
            totalPrice += product.getPrice();
        }
        System.out.println("현재 장바구니 총액 : " + totalPrice);

        int discountPrice = 0;
        Map<String, Integer> coupons = currentUser.getCoupons();
        if(coupons.isEmpty()){
            System.out.println("보유한 쿠폰이 없습니다.");
        }
        else{
            discountPrice = getDiscountPrice(selectedProducts, discountPrice, totalPrice, currentUser, userRepository);
        }


        if(discountPrice >= totalPrice){
            totalPrice = 0;
        }
        else{
            totalPrice = totalPrice - discountPrice;
        }

        System.out.println("결제 예정 금액 : " + totalPrice + "원");
        System.out.print("결제 하시겠습니까? (y/n): ");
        String input = scanner.nextLine().trim();
        if (input.equals("y")) {
            // 삭제할 제품들을 따로 저장
            List<Product> productsToRemove = new ArrayList<>();
            for (Product product : selectedProducts) {
                reduceIngredient(product);
                productsToRemove.add(product); // 나중에 삭제하기 위해 저장
            }
            selectedProducts.removeAll(productsToRemove);
            System.out.println(totalPrice+"원을 결제하였습니다. 감사합니다");

            String id = currentUser.getLoginId();
            int currentDate = DateManager.getInstance().getCurrentDate();
            OrderRepository.getInstance().addOrder(id, totalPrice, currentDate);

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

    private int getDiscountPrice(List<Product> selectedProducts, int discountPrice, int totalPrice, User currentUser, UserRepository userRepository) {
        Map<String, Integer> coupons = currentUser.getCoupons();
        while(true){
            System.out.print("쿠폰을 사용하시겠습니까? (y/n) ");
            if(discountPrice >= totalPrice){
                System.out.println("쿠폰 금액이 상품 금액보다 큽니다. 더 이상 쿠폰을 사용할 수 없습니다.");
                break;
            }
            String couponInput = scanner.nextLine().trim();
            if(couponInput.equals("y")){
                new ShowCouponService().showCurrentCoupons(currentUser);
                System.out.print("사용할 쿠폰의 이름을 입력하세요 : ");

                String couponName = scanner.nextLine().trim();
                if(coupons.containsKey(couponName)){
                    switch(couponName){
                        case "10% 할인권" :
                            discountPrice += (int) (totalPrice * 0.1);
                            currentUser.UseCoupon(couponName);
                            userRepository.saveUserInfos();
                            break;
                        case "20% 할인권" :
                            discountPrice += (int) (totalPrice * 0.2);
                            currentUser.UseCoupon(couponName);
                            userRepository.saveUserInfos();
                            break;
                        case "1000원 할인권" :
                            discountPrice += 1000;
                            currentUser.UseCoupon(couponName);
                            userRepository.saveUserInfos();
                            break;
                        case "2000원 할인권" :
                            discountPrice += 2000;
                            currentUser.UseCoupon(couponName);
                            userRepository.saveUserInfos();
                            break;
                    }
                }
                else{
                    System.out.println("존재하지 않는 쿠폰입니다.");
                }
            }
            else if(couponInput.equals("n")){
                break;
            }
            else{
                System.out.println("y 또는 n만 입력해주세요.");
            }
        }
        return discountPrice;
    }

    private void reduceIngredient(Product product) {
        Map<Ingredient, Integer> ingredients = product.getIngredients();// 각 상품의 재료와 수량
        Map<Ingredient, Integer> addedIngredients = product.getAddedIngredients();
        IngredientRepository ingredientRepo = IngredientRepository.getInstance();

        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            adjustIngredientQuantity(entry.getKey(), entry.getValue(), ingredientRepo);
        }

        // 추가/삭제된 재료 차감
        for (Map.Entry<Ingredient, Integer> entry : addedIngredients.entrySet()) {
            adjustIngredientQuantity(entry.getKey(), entry.getValue(), ingredientRepo);
        }
        addedIngredients.clear();
    }
    private void adjustIngredientQuantity(Ingredient ingredient, int quantity, IngredientRepository ingredientRepo) {
        int currentQuantity = ingredientRepo.getIngredientQuantity(ingredient.getIngredientName());

        if (quantity > 0 && currentQuantity < quantity) {
            System.out.println("재고가 부족합니다: " + ingredient.getIngredientName());
            new MainMenuService().start();
        }

        // 재료 차감 (양수는 감소, 음수는 증가)
        ingredientRepo.addIngredientQuantity(ingredient.getIngredientName(), -quantity);
    }



}
