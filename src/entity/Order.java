package entity;

public class Order {
    private final String userId;
    private final int payAmount;
    private final int date;

    public Order(String userId, int payAmount, int date) {
        this.userId = userId;
        this.payAmount = payAmount;
        this.date = date;
    }

    public String convertOrderRow(){
        return userId + "," + payAmount + "," + date;
    }

    public String getUserId() {
        return userId;
    }
}
