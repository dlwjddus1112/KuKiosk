package entity;

import java.util.HashMap;
import java.util.Map;

public class User {
    public int id;
    public String name;
    public String loginId;
    public String password;
    public Map<String,Integer> coupons; // <쿠폰이름, 쿠폰 수>
    public int lastCouponIssuedMonth; // 마지막으로 쿠폰을 받은 날짜

    public User(int id, String name, String loginId, String password) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.coupons = new HashMap<String,Integer>();
        this.lastCouponIssuedMonth = 0;
    }

    public User(int id, String name, String loginId, String password, int lastCouponIssuedMonth) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.coupons = new HashMap<String,Integer>();
        this.lastCouponIssuedMonth = lastCouponIssuedMonth;
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

    public void addCoupon(String couponName, int count){
        this.coupons.put(couponName,this.coupons.getOrDefault(couponName,0)+count);
    }

    public String convertToCsvRow() {
        StringBuilder couponData = new StringBuilder();
        for (Map.Entry<String, Integer> entry : coupons.entrySet()) {
            if (!couponData.isEmpty()) {
                couponData.append(";"); // 쿠폰 사이를 세미콜론으로 구분
            }
            couponData.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return id + "," + name + "," + loginId + "," + password + "," + lastCouponIssuedMonth + "," + couponData;
    }



}
