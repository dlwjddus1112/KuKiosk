package service.order;

import entity.Order;
import entity.Product;
import entity.User;
import repository.OrderRepository;
import util.DateManager;
import util.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowCouponService {
    List<Product> selectedProducts = new ArrayList<>();

    public ShowCouponService(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public void start(){
        List<Order> orders = OrderRepository.getInstance().orders;
        printMenu();
        User currentUser = UserSession.getInstance().getCurrentUser();
        int currentDate = DateManager.getInstance().getCurrentDate();
        System.out.println("현재 날짜 : " + currentDate); // ex) 20241120
        int currentYear = currentDate / 10000;
        int currentMonth = (currentDate / 100) % 100;
        int nextMonth = currentMonth + 1;

        if(nextMonth > 12){
            currentYear += 1;
            nextMonth = 1;
        }
        int nextMonthStartDate = currentYear * 10000 + nextMonth * 100 + 1; // 다음 달 1일
        showCurrentCoupons(currentUser);

        // 다음 달 1일 기준 최근 3개월 구매 금액 출력
        showLastThreeMonthsPurchases(currentUser, orders, nextMonthStartDate);

        // 다음 달 쿠폰을 받기 위해 필요한 추가 금액 계산
        calculateAdditionalPurchaseForCoupon(currentUser, orders, currentYear, currentMonth);

        new OrderMainMenuService(selectedProducts).start();

    }

    private void printMenu(){
        System.out.println("----------------------");
        System.out.println("       쿠폰 조회       ");
        System.out.println("----------------------");

    }
    private void showCurrentCoupons(User user) {
        System.out.println("현재 보유한 쿠폰:");
        if (user.coupons.isEmpty()) {
            System.out.println("  - 보유한 쿠폰이 없습니다.");
        } else {
            for (Map.Entry<String, Integer> entry : user.coupons.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + "개");
            }
        }
    }

    private void calculateAdditionalPurchaseForCoupon(User user, List<Order> orders, int currentYear, int currentMonth) {
        int startMonth = currentMonth - 2;
        int startYear = currentYear;
        if (startMonth <= 0) {
            startYear -= 1;
            startMonth += 12;
        }

        int startYYYYMM = startYear * 100 + startMonth;
        int currentYYYYMM = currentYear * 100 + currentMonth;

        int totalAmount = 0;
        for (Order order : orders) {
            int orderYYYYMM = order.getDate() / 100;
            if (order.getUserId().equals(user.loginId) && orderYYYYMM >= startYYYYMM && orderYYYYMM <= currentYYYYMM) {
                totalAmount += order.getPayAmount();
            }
        }


        if (totalAmount < 30000) {
            System.out.println("다음 달 쿠폰을 받기 위해 " + (30000 - totalAmount) + "원을 더 사용해야 합니다.");
        } else if (totalAmount < 50000) {
            System.out.println("다음 등급 쿠폰(1000원 할인권 + 10% 할인권 + 20% 할인권)을 받기 위해 " + (50000 - totalAmount) + "원을 더 사용해야 합니다.");
        } else if (totalAmount < 100000) {
            System.out.println("다음 등급 쿠폰(1000원 할인권 + 2000원 할인권 + 10% 할인권 + 20% 할인권)을 받기 위해 " + (100000 - totalAmount) + "원을 더 사용해야 합니다.");
        } else {
            System.out.println("최고 등급 쿠폰을 이미 받을 수 있습니다.");
        }
    }

    private void showLastThreeMonthsPurchases(User user, List<Order> orders, int nextMonthStartDate) {
        int nextYear = nextMonthStartDate / 10000;
        int nextMonth = (nextMonthStartDate / 100) % 100;
        int startMonth = nextMonth - 3;
        int startYear = nextYear;

        if (startMonth <= 0) {
            startYear -= 1;
            startMonth += 12;
        }

        int startYYYYMM = startYear * 100 + startMonth;
        int nextMonthYYYYMM = nextMonthStartDate / 100;

        int totalAmount = 0;
        for (Order order : orders) {
            int orderYYYYMM = order.getDate() / 100;
            if (order.getUserId().equals(user.loginId) && orderYYYYMM >= startYYYYMM && orderYYYYMM < nextMonthYYYYMM) {
                totalAmount += order.getPayAmount();
            }
        }

        System.out.println("\n다음 달 1일 기준 최근 3개월(월 기준)의 구매 금액: " + totalAmount + "원");
    }
}
