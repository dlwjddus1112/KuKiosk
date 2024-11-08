package entity;

public class User {
    public int id;
    public String name;
    public String loginId;
    public String password;

    public User(int id, String name, String loginId, String password) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public String convertToCsvRow() {
        return id + "," + name + "," + loginId + "," + password;
    }

}
