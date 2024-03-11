import java.util.List;

public interface INumberleModel {
    public String loadAndSelectEquation(String filePath) throws Exception;
    public void handleInput(String input, List<Character> playerInput);
    public boolean isValidInput(char input, List<Character> currentInput);
    public boolean isValidEquation(String input);
    public String compareEquations(String input, String targetEq);
    public void incrementAttempts();
    public String listToString(List<Character> playerInput);
    public String arrayToString(String[] array);
}
