package repository;

import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                var product = new Product(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]),Integer.parseInt(data[4]));
                products.add(product);
            }
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

    public void addMenu(String productType, String productName, int price, int quantity ) {
        Product product = new Product(productType, productName, price, quantity,quantity);

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

    public void addProductQuantity(String productName, int quantity) {
        Product menu = findByMenuName(productName);
        menu.setCurrentQuantity(menu.getCurrentQuantity() + quantity);
        saveProductInfos();
    }

}
