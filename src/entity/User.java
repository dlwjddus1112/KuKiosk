package entity;

public class User {
    public int id;
    public String name;
    public String loginId;
    public String password;
    public int payAmount;

    public User(int id, String name, String loginId, String password) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.payAmount = 0;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public String getLoginId() {
        return loginId;
    }



    public void increasePayAmount(int money) {
        this.payAmount += money;
    }

    public String convertToCsvRow() {
        return id + "," + name + "," + loginId + "," + password + "," + payAmount;
    }

}
