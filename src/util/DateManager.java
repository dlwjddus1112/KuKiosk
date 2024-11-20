package util;

import java.io.*;

public class DateManager {
    private static DateManager instance;
    private int currentDate;
    private DateManager() {}

    public static DateManager getInstance() {
        if(instance == null) {
            instance = new DateManager();
        }
        return instance;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate = currentDate;
    }
    public void saveDateToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("currentDate.txt"))) {
            writer.write(String.valueOf(currentDate));
        } catch (IOException e) {
            System.out.println("날짜 저장 중 오류 발생: " + e.getMessage());
        }
    }

    public void loadDateFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("currentDate.txt"))) {
            String dateString = reader.readLine();
            if (dateString != null) {
                currentDate = Integer.parseInt(dateString);
            }
        } catch (IOException | NumberFormatException e) {

        }
    }
}


