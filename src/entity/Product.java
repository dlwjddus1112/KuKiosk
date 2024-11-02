package entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private String productType;
    private String productName;
    private int price;
    private Map<Ingredient, Integer> ingredients;


    public Product(String productType, String productName, int price,Map<Ingredient, Integer> ingredients) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.ingredients = ingredients;
    }

    public Product(String productType, String productName, int price) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.ingredients = new HashMap<Ingredient, Integer>();
    }



    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public String convertProductRow(){
        StringBuilder str = new StringBuilder(productType + "," + productName + "," + price + ",");
        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            str.append(entry.getKey().getIngredientName()).append(":").append(entry.getValue()).append(";");
        }
        return str.toString();
    }


    public String getProductType() {
        return productType;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }


}
