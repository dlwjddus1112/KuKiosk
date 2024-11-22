package util;

import entity.Order;
import entity.User;
import repository.OrderRepository;
import repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CouponService {
    private static final OrderRepository orderRepository = OrderRepository.getInstance();
    public static void giveCoupons(int currentDate, List<User> users) {
        int currentYear = currentDate / 10000;
        int currentMonth = (currentDate / 100) % 100;
        int endMonth = currentMonth - 1;
        int endYear = currentYear;
        if (endMonth <= 0) {
            endYear -= 1;
            endMonth += 12;
        }

        int currentYYYYMM = endYear * 100 + endMonth;
        int startYear = currentYear;
        int startMonth = currentMonth - 3;

        if (startMonth <= 0) {
            startYear -= 1;
            startMonth += 12;
        }

        int startYYYYMM = startYear * 100 + startMonth;

        Map<String, Integer> userTotalAmounts = new HashMap<>();
        List<Order> orders = orderRepository.orders;

        for (Order order : orders) {
            int orderYYYYMM = order.getDate() / 100;
            if (orderYYYYMM >= startYYYYMM && orderYYYYMM <= currentYYYYMM) {
                userTotalAmounts.put(order.getUserId(),
                        userTotalAmounts.getOrDefault(order.getUserId(), 0) + order.getPayAmount());
            }
        }

        for (User user : users) {
            if (user.getLastCouponIssuedMonth() == currentYYYYMM) {
                System.out.println("이미 " + currentMonth + "월에 쿠폰을 발급받으셨습니다.");
                continue;
            }
            User currentUser = UserSession.getInstance().getCurrentUser();
            int totalAmount = userTotalAmounts.getOrDefault(user.getLoginId(), 0);
            if (totalAmount >= 30000 && totalAmount < 50000) {
                user.addCoupon("1000원 할인권", 1);
                user.addCoupon("10% 할인권", 1);
                if(user.getLoginId().equals(currentUser.getLoginId())){
                    System.out.println(user.getLoginId()+ "님, 1000원 할인권 1장, 10% 할인권 1장이 발급되었습니다.("  + currentMonth + "월 쿠폰)");
                }
            } else if (totalAmount >= 50000 && totalAmount < 100000) {
                user.addCoupon("1000원 할인권", 1);
                user.addCoupon("10% 할인권", 1);
                user.addCoupon("20% 할인권", 1);
                if(user.getLoginId().equals(currentUser.getLoginId())){
                    System.out.println(user.getLoginId()+ "님, 1000원 할인권 1장, 10% 할인권 1장, 20% 할인권 1장이 발급되었습니다.("  + currentMonth + "월 쿠폰)");
                }
            } else if (totalAmount >= 100000) {
                user.addCoupon("1000원 할인권", 1);
                user.addCoupon("2000원 할인권", 1);
                user.addCoupon("10% 할인권", 1);
                user.addCoupon("20% 할인권", 1);
                if(user.getLoginId().equals(currentUser.getLoginId())){
                    System.out.println(user.getLoginId() + "님, 1000원 할인권 1장, 2000원 할인권 1장, 10% 할인권 1장, 20% 할인권 1장이 발급되었습니다.("  + currentMonth + "월 쿠폰)");
                }
            }
            user.setLastCouponIssuedMonth(currentYYYYMM);
            UserRepository.getInstance().saveUserInfos();

        }
    }

}
