package repository;

import entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    List<Product> products = new ArrayList<Product>();

    void addProduct(String productName, int price, int maxCount ) {
      products.add(new Product(productName, price, maxCount));
    }
}
