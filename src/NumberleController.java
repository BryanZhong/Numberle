import java.util.List;
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
    public void handleInput(String input, List<Character> playerInput) {
        model.handleInput(input, playerInput);
    }

    public boolean isHaveOperationSymbol(String playerInput) {
        return model.isHaveOperationSymbol(playerInput);
    }
    public boolean isHaveEqualSymbol(String playerInput) {
        return model.isHaveEqualSymbol(playerInput);
    }
    public boolean isFirstInputAndOperator(char input, List<Character> playerInput) {
        return model.isFirstInputAndOperator(input, playerInput);
    }
    public boolean isOperator(char input) {
        return model.isOperator(input);
    }
    public boolean isConsecutiveOperator(char input, List<Character> playerInput) {
        return model.isConsecutiveOperator(input, playerInput);
    }
    public String listToString(List<Character> list) {
        return model.listToString(list);
    }
    public String compareEquations(String playerEq, String targetEq) {
        return model.compareEquations(playerEq, targetEq);
    }
    public void restartGame() {
        model.restartGame();
    }
    public void setGameOver(boolean isOver) {
        model.setGameOver(isOver);
    }
    public boolean isValidEquation(String equation) {
        return model.isValidEquation(equation);
    }
    public double evaluate(String expression) {
        return model.evaluate(expression);
    }
    public void incrementAttempts() {
        model.incrementAttempts();
    }
    public void clearPlayerInput() {
        model.clearPlayerInput();
    }
    public String arrayToString(String[] arr) {
        return model.arrayToString(arr);
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }
    public String loadAndSelectEquation(String filePath) throws Exception {
        return model.loadAndSelectEquation(filePath);
    }

}
