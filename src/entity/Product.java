package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private String productType;
    private String productName;
    private int price;
    private Map<Ingredient, Integer> ingredients;
    private Map<Ingredient, Integer> addedIngredients; // 사용자가 추가/감소한 재료 일시적으로 담는 Map
    private Map<Ingredient, Integer> extraIngredients;


    public Product(String productType, String productName, int price,Map<Ingredient, Integer> ingredients, Map<Ingredient,Integer> extraIngredients) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.ingredients = ingredients;
        this.extraIngredients = extraIngredients;
        this.addedIngredients = new HashMap<>();
    }
    public Product(String productType, String productName, int price,Map<Ingredient, Integer> ingredients) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.ingredients = ingredients;
        this.extraIngredients = new HashMap<>();
        this.addedIngredients = new HashMap<>();
    }

    public Product(String productType, String productName, int price) {
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.ingredients = new HashMap<Ingredient, Integer>();
        this.extraIngredients = new HashMap<>();
        this.addedIngredients = new HashMap<>();
    }
    public void addIngredient(Ingredient ingredient, int quantity) {
        this.ingredients.put(ingredient, quantity);
    }

    public Map<Ingredient, Integer> getAddedIngredients() {
        return addedIngredients;
    }

    public Map<Ingredient, Integer> getExtraIngredients() {
        return extraIngredients;
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public String convertProductRow() {
        StringBuilder str = new StringBuilder(productType + "," + productName + "," + price + ",");
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            str.append(entry.getKey().getIngredientName()).append(":").append(entry.getValue()).append(";");
        }
        str.append(",");
        int count = 0;
        for (Map.Entry<Ingredient, Integer> entry : extraIngredients.entrySet()) {
            if (count > 0) {
                str.append(";");
            }
            str.append(entry.getKey().getIngredientName()).append(":").append(entry.getValue());
            count++;
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
