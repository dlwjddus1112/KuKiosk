package entity;

public class Product {
    private String productType;
    private String productName;
    private int price;
    private int currentQuantity;
    private int maxQuantity;


    public Product(String productType, String productName, int price,int currentQuantity, int maxCount) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.currentQuantity = currentQuantity;
        this.maxQuantity = maxCount;
    }


    public String convertProductRow(){
        return productType + "," + productName + "," + price + "," + currentQuantity + "," +  maxQuantity;
    }

    public String getProductType() {
        return productType;
    }

    public int getCurrentQuantity(){
        return currentQuantity;
    }
    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
}
