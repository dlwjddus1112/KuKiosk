package entity;

import java.util.HashMap;
import java.util.Map;

public class User {
    public int id;
    public String name;
    public String loginId;
    public String password;
    public int payAmount;
    public Map<String,Integer> coupons; // <쿠폰이름, 쿠폰 수>
    public int lastCouponIssuedMonth; // 마지막으로 쿠폰을 받은 날짜

    public User(int id, String name, String loginId, String password) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.payAmount = 0;
        this.coupons = new HashMap<String,Integer>();
        this.lastCouponIssuedMonth = 0;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public String getLoginId() {
        return loginId;
    }

    public int getLastCouponIssuedMonth() {
        return lastCouponIssuedMonth;
    }

    public void setLastCouponIssuedMonth(int lastCouponIssuedMonth) {
        this.lastCouponIssuedMonth = lastCouponIssuedMonth;
    }

    public void increasePayAmount(int money) {
        this.payAmount += money;
    }

    public void addCoupon(String couponName, int count){
        this.coupons.put(couponName,this.coupons.getOrDefault(couponName,0)+count);
    }

    public String convertToCsvRow() {
        return id + "," + name + "," + loginId + "," + password + "," + payAmount + "," + lastCouponIssuedMonth;
    }


}
