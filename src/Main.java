import service.main.MainMenuService;
import util.DateManager;

public class Main {
    public static void main(String[] args) {
        DateManager.getInstance().loadDateFromFile();
        new MainMenuService().start();
    }
}