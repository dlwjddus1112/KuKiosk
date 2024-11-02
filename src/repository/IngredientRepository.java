package repository;

import entity.Ingredient;
import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {
    static IngredientRepository instance = new IngredientRepository();
    List<Ingredient> ingredients = new ArrayList<>();

    public IngredientRepository() {
        initIngredientInfos();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private void initIngredientInfos() {
        try {
            var file = new File("IngredientData.csv");
            file.createNewFile();

            if (file.length() == 0) {
                // 파일이 비어 있으면 기본 재료 목록을 추가
                String[] defaultIngredients = {
                        "물", "원두", "설탕", "우유", "빵", "시럽",
                        "생크림", "휘핑크림", "초콜릿", "얼음",
                        "버터", "과일", "요거트"
                };
                for (String name : defaultIngredients) {
                    ingredients.add(new Ingredient(name, 0));
                }
                saveIngredientInfos();
            } else {
                // 파일이 비어 있지 않으면 파일에서 재료 정보를 로드
                var fileInputStream = new FileInputStream(file);
                var br = new BufferedReader(new InputStreamReader(fileInputStream));

                String line;
                while ((line = br.readLine()) != null) {
                    var data = line.split(",");
                    var ingredient = new Ingredient(data[0],Integer.parseInt(data[2]));
                    ingredients.add(ingredient);
                }
                br.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static IngredientRepository getInstance() {
        if(instance == null) {
            return new IngredientRepository();
        }
        return instance;
    }
    private void saveIngredientInfos() {
        try {
            var file = new File("IngredientData.csv");

            clearFile(file);
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var ingredient : ingredients) {
                bw.write(ingredient.convertIngredientRow());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void clearFile(File file) {
        try {
            new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Ingredient findByIngredientName(String menuName) {
        Ingredient findIngredient = null;
        for (var ingredient : ingredients) {
            if(ingredient.getIngredientName().equals(menuName)) {
                findIngredient = ingredient;
            }
        }
        return findIngredient;
    }
    public void addIngredientQuantity(String ingredientName, int quantity) {
        Ingredient ingredient = findByIngredientName(ingredientName);
        ingredient.setCurrentQuantity(ingredient.getCurrentQuantity() + quantity);
        saveIngredientInfos();
    }
    public int getIngredientQuantity(String ingredientName) {
        Ingredient ingredient = findByIngredientName(ingredientName);
        if (ingredient != null) {
            return ingredient.getCurrentQuantity();
        } else {
            throw new IllegalArgumentException("재료를 찾을 수 없습니다: " + ingredientName);
        }
    }


}
