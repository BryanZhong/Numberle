import java.util.List;


public interface INumberleModel {
    String loadAndSelectEquation(String filePath) throws Exception;
    void handleInput(String input, List<Character> playerInput);
    boolean isHaveOperationSymbol(String playerInput);
    boolean isHaveEqualSymbol(String playerInput);
    boolean isFirstInputAndOperator(char input, List<Character> playerInput);
    boolean isOperator(char input);
    boolean isConsecutiveOperator(char input, List<Character> playerInput);
    String listToString(List<Character> list);
    String compareEquations(String playerEq, String targetEq);
    void setGameOver(boolean isOver);
    boolean isValidEquation(String equation);
    double evaluate(String expression);
    void incrementAttempts();
    boolean isGameOver();
    void clearPlayerInput();
    String arrayToString(String[] arr);
    void restartGame();
}
