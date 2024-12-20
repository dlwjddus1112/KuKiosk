package repository;

import entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository {
    static UserRepository instance;
    HashMap<Integer, User> userInfos = new HashMap<>();

    private UserRepository() {
        initUserInfos();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public List<User> findAllUsers(){
        return new ArrayList<>(userInfos.values());
    }

    public User findUserByLoginId(String loginId) {
        return userInfos.values().stream()
                .filter(user -> user.loginId.equals(loginId))
                .findFirst().orElse(null);
    }

    public void addUser(String name, String loginId, String password) {
        var maxId = userInfos.keySet().stream()
                .max(Integer::compare)
                .orElse(0);
        var user = new User(maxId + 1, name, loginId, password);
        userInfos.put(user.id, user);
        saveUserInfos();
    }

    public boolean isDuplicateLoginId(String loginId) {
        return userInfos.values().stream()
                .anyMatch(user -> user.loginId.equals(loginId));
    }

    private void initUserInfos() {
        try {
            var file = new File("UserData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String loginId = data[2];
                String password = data[3];
                int totalPayAmount = Integer.parseInt(data[4]);
                int lastCouponMonth = Integer.parseInt(data[5]);
                var user = new User(id, name, loginId, password,totalPayAmount,lastCouponMonth);
                userInfos.put(user.id, user);
                if(data.length > 6){
                    String[] couponEntries = data[6].split(";"); // 쿠폰은 세미콜론으로 구분
                    for (String couponEntry : couponEntries) {
                        String[] couponParts = couponEntry.split(":");
                        String couponName = couponParts[0];
                        int count = Integer.parseInt(couponParts[1]);
                        user.addCoupon(couponName, count);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUserInfos() {
        try {
            var file = new File("UserData.csv");

            clearFile(file);
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var user : userInfos.values()) {
                bw.write(user.convertToCsvRow());
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


}
