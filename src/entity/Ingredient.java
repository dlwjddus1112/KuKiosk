package entity;

public class Ingredient {
    private final String ingredientName;
    private final int maxQuantity = 100;
    private int currentQuantity;

//재료 : 물, 원두, 설탕, 우유, 빵, 시럽, 생크림, 휘핑크림, 초콜릿, 얼음, 버터, 과일, 요거트,

    public Ingredient(String ingredientName, int currentQuantity) {
        this.ingredientName = ingredientName;
        this.currentQuantity = currentQuantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }
    public String convertIngredientRow(){
        return ingredientName +  "," + maxQuantity + "," +  currentQuantity;
    }
    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

}
