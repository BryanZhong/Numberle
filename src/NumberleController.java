import java.util.Scanner;

public class NumberleController {
    private NumberleModel model;
    private NumberleView view;
    private Scanner scanner;

    public NumberleController(NumberleModel model, NumberleView view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        view.initGUI();
    }
}
