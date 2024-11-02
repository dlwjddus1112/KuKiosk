package repository;

import entity.Ingredient;
import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    static ProductRepository instance = new ProductRepository();
    List<Product> products = new ArrayList<>();


    public ProductRepository() {
        initProductInfos();
    }

    public List<Product> getProducts() {
        return products;
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }
    private void initProductInfos() {
        try {
            var file = new File("ProductData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                var productType = data[0];
                var productName = data[1];
                int price = Integer.parseInt(data[2]);

                // 새로운 Product 객체 생성
                Product product = new Product(productType, productName, price);

                // 재료 정보가 있는 경우 처리
                if (data.length > 3) {
                    String[] ingredientsData = data[3].split(";");
                    for (String ingredientData : ingredientsData) {
                        String[] ingredientInfo = ingredientData.split(":");
                        String ingredientName = ingredientInfo[0];
                        int quantity = Integer.parseInt(ingredientInfo[1]);

                        // IngredientRepository에서 재료를 찾고 Product에 추가
                        Ingredient ingredient = IngredientRepository.getInstance().findByIngredientName(ingredientName);
                        if (ingredient != null) {
                            product.getIngredients().put(ingredient,quantity);
                        }
                    }
                }
                // Product 리스트에 추가
                products.add(product);
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void saveProductInfos() {
        try {
            var file = new File("ProductData.csv");

            clearFile(file);
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var product : products) {
                bw.write(product.convertProductRow());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMenu(String productType, String productName, int price, Map<Ingredient,Integer> ingredients) {
        Product product = new Product(productType, productName, price, ingredients);

        products.add(product);
        saveProductInfos();
    }

    public void deleteMenu(String productName){
        var product = findByMenuName(productName);
        if(product != null) {
            products.remove(product);
        }
        saveProductInfos();
    }


    private void clearFile(File file) {
        try {
            new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Product findByMenuName(String menuName) {
        Product findProduct = null;
        for (var product : products) {
            if(product.getProductName().equals(menuName)) {
                findProduct = product;
            }
        }
        return findProduct;
    }



}
