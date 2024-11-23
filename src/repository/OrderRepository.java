package repository;

import entity.Ingredient;
import entity.Order;
import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    static OrderRepository instance = new OrderRepository();
    public List<Order> orders = new ArrayList<>();

    public OrderRepository() {
        initOrderInfos();
    }
    public static OrderRepository getInstance() {
        if(instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    private void initOrderInfos(){
        try {
            var file = new File("OrderData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                var userId = data[0];
                var payAmount = Integer.parseInt(data[1]);
                int date = Integer.parseInt(data[2]);
                Order order = new Order(userId, payAmount, date);
                orders.add(order);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void saveIngredientInfos() {
        try {
            var file = new File("OrderData.csv");

            clearFile(file);
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var order : orders) {
                bw.write(order.convertOrderRow());
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
    public void addOrder(String userId, int payAmount, int date) {
        Order order = new Order(userId, payAmount, date);
        orders.add(order);
        saveIngredientInfos();
    }

}
