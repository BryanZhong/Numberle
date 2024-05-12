import javax.swing.*;
import java.io.IOException;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NumberleModel extends Observable implements INumberleModel {
    public static final int MAX_LENGTH = 7;
    private static final List<Character> VALID_CHARS =
            Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '=');
    public static Set<Character> incorrectValues = new HashSet<>();
    public static String targetEquation="1+1+1=3";
    public List<Character> playerInput = new ArrayList<>();
    protected static int attempts = 0;

    public static boolean gameIsOver = false;
    static final int rows = 6;
    static final int cols = 7;
    static String[][] matrix = new String[rows][cols];
    static CharacterFeedback[][] feedbackMatrix = new CharacterFeedback[6][7];
    static int currentRow = 0;
    static int currentCol = 0;


    ArrayList<String> CORRECT = new ArrayList<>();
    ArrayList<String> INCORRECT = new ArrayList<>();
    ArrayList<String> WRONG_POSITION = new ArrayList<>();
    private boolean showEquationFlag = false;
    private boolean showErrorMessageFlag = false;
    private boolean randomEquationFlag = false;
    private String errorMessage;

    public NumberleModel() {
        super();
        //初始化incorrectValues，在游戏开始前清空，以处理多次游戏运行的情况
        incorrectValues.clear();
        attempts = 0;
        gameIsOver = false;
        CORRECT.clear();
        INCORRECT.clear();
        WRONG_POSITION.clear();
        feedbackMatrix = new CharacterFeedback[6][7]; // 重置反馈矩阵
        currentRow = 0; // 重置当前行
        clearPlayerInput();
    }

    /**
     * Gets the number of rows in the game matrix.
     *
     * @return the number of rows in the matrix
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the game matrix.
     *
     * @return the number of columns in the matrix
     */
    public int getCols() {
        return cols;
    }

    /**
     * Retrieves the current game matrix.
     *
     * @return the current state of the game matrix
     */
    public String[][] getMatrix() {
        return matrix;
    }

    /**
     * Sets the game matrix to a new matrix.
     * <p>Precondition: The new matrix must have the same dimensions as the existing matrix.</p>
     * <p>Postcondition: The matrix will be set to the specified value.</p>
     *
     * @param matrix the new matrix to set
     */
    public void setMatrix(String[][] matrix) {
        assert matrix.length == getRows() && matrix[0].length == getCols() : "Precondition failed: matrix dimensions do not match.";
        NumberleModel.matrix = matrix;
        assert getMatrix() == matrix : "Postcondition failed: matrix not set correctly.";
    }

    /**
     * Gets the current row index in the game matrix.
     *
     * @return the index of the current row
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * Sets the current row index in the game matrix.
     * <p>Precondition: currentRow must be within the range of existing rows in the matrix.</p>
     * <p>Postcondition: The currentRow index will be set to the specified value.</p>
     *
     * @param currentRow the new row index to set
     * @throws IllegalArgumentException if the specified index is out of bounds
     */
    public void setCurrentRow(int currentRow) {
        assert currentRow >= 0 && currentRow < getRows() : "Precondition failed: currentRow index out of bounds.";
        NumberleModel.currentRow = currentRow;
        assert getCurrentRow() == currentRow : "Postcondition failed: currentRow not set correctly.";
    }

    /**
     * Gets the current column index in the game matrix.
     *
     * @return the index of the current column
     */
    public int getCurrentCol() {
        return currentCol;
    }

    /**
     * Sets the current column index in the game matrix.
     * <p>Precondition: currentCol must be within the range of existing columns in the matrix.</p>
     * <p>Postcondition: The currentCol index will be set to the specified value.</p>
     *
     * @param currentCol the new column index to set
     */
    protected void setCurrentCol(int currentCol) {
        assert currentCol >= 0 && currentCol < getCols() : "Precondition failed: currentCol index out of bounds.";
        NumberleModel.currentCol = currentCol;
        assert getCurrentCol() == currentCol : "Postcondition failed: currentCol not set correctly.";
    }

    /**
     * Retrieves the list of characters that are correctly positioned and correct.
     *
     * @return a list of characters correctly guessed
     */
    public ArrayList<String> getCORRECT() {
        return CORRECT;
    }

    /**
     * Retrieves the list of characters that are guessed correctly but positioned incorrectly.
     *
     * @return a list of characters guessed but misplaced
     */
    public ArrayList<String> getINCORRECT() {
        return INCORRECT;
    }

    /**
     * Retrieves the list of characters that are incorrect.
     *
     * @return a list of characters that are not in the target equation
     */
    public ArrayList<String> getWRONG_POSITION() {
        return WRONG_POSITION;
    }

    /**
     * Sets the list of characters that are correctly positioned and correct.
     * <p>Precondition: CORRECT list must not be null.</p>
     *<p>Postcondition: The CORRECT list will be set to the specified value.</p>
     *
     * @param CORRECT the new list of correct characters
     */
    public void setCORRECT(ArrayList<String> CORRECT) {
        assert CORRECT != null : "Precondition failed: CORRECT list cannot be null.";
        this.CORRECT = CORRECT;
        assert getCORRECT() == CORRECT : "Postcondition failed: CORRECT list not set correctly.";
    }

    /**
     * Sets the list of characters that are guessed correctly but positioned incorrectly.
     *
     * @param INCORRECT the new list of misplaced characters
     */
    public void setINCORRECT(ArrayList<String> INCORRECT) {
        this.INCORRECT = INCORRECT;
    }

    /**
     * Sets the list of characters that are incorrect.
     * <p>Precondition: WRONG_POSITION list must not be null.</p>
     * <p>Postcondition: The WRONG_POSITION list will be set to the specified value.</p>
     *
     * @param WRONG_POSITION the new list of incorrect characters
     */
    public void setWRONG_POSITION(ArrayList<String> WRONG_POSITION) {
        assert WRONG_POSITION != null : "Precondition failed: WRONG_POSITION list cannot be null.";
        this.WRONG_POSITION = WRONG_POSITION;
        assert getWRONG_POSITION() == WRONG_POSITION : "Postcondition failed: WRONG_POSITION list not set correctly.";
    }

    /**
     * Loads and selects an equation from a file, ensuring the file path is not null.
     * <p>Precondition: filePath must not be null.</p>
     * <p>Postcondition: Returns a valid equation string.</p>
     *
     * @param filePath the path to the file containing equations
     * @return the selected equation
     * @throws IOException if an error occurs while reading the file
     */
    public String loadAndSelectEquation(String filePath) throws IOException {
        assert filePath != null : "Precondition failed: filePath cannot be null.";
        List<String> equations = Files.readAllLines(Paths.get(filePath));
        assert !equations.isEmpty() : "Postcondition failed: equations list is empty.";
        String equation = equations.get(new Random().nextInt(equations.size()));
        setChanged();
        notifyObservers(equation);
        assert equation != null && !equation.isEmpty() : "Postcondition failed: loaded equation is null or empty.";
        return equation;
    }
    /**
     * Handles user input by adding or removing characters from the player's input list based on specific rules.
     * This method also handles 'delete' operations where the last character is removed if 'd' or 'D' is input.
     * <p>Precondition: The input must be non-null and should either be "d" (for delete) or a single character that is valid.</p>
     * <p>Postcondition: If valid, the input modifies the playerInput list. If invalid, an error message is set.</p>
     *
     * @param input the input character to handle
     * @param playerInput the list of characters representing the player's current game input
     */
    /**
     * Handles user input by adding or removing characters from the player's input.
     *
     * @param input the input to handle
     * @param playerInput the list of characters representing the player's input
     */
    @Override
    public void handleInput(String input, List<Character> playerInput) {
        boolean stateChanged = false;
        if ("d".equalsIgnoreCase(input) && !playerInput.isEmpty()) {
            playerInput.remove(playerInput.size() - 1);
            stateChanged = true;
        } else if (input.length() == 1 && VALID_CHARS.contains(input.charAt(0))) {
            if (isFirstInputAndOperator(input.charAt(0), playerInput) || isConsecutiveOperator(input.charAt(0), playerInput)) {
                // 如果是无效输入，不修改状态，因此也不更新观察者
                errorMessage="The first input cannot be a symbol and the symbols are not connected to each other.";
            } else {
                playerInput.add(input.charAt(0));
                stateChanged = true;
            }
        } else {
            errorMessage="Invalid Input!";
        }

        if (stateChanged) {
            setChanged(); // 标记此 Observable 对象为已改变的对象
            notifyObservers(); // 通知所有的观察者
        }
    }


    /**
     * Checks if the input contains an operation symbol.
     * <p>Precondition: playerInput must not be null.</p>
     *
     * @param playerInput the input to check
     * @return true if the input contains an operation symbol, false otherwise
     */
    @Override
    public boolean isHaveOperationSymbol(String playerInput) {
        assert playerInput != null : "Precondition failed: playerInput is null.";
        List<String> operationSymbols = Arrays.asList("+", "-", "*", "/");
        for (int i = 0; i < playerInput.length(); i++) {
            // Convert each character to String because operationSymbols is a list of String
            String currentCharAsString = String.valueOf(playerInput.charAt(i));
            if (operationSymbols.contains(currentCharAsString)) {
                return true; // Found an operation symbol in the input
            }
        }
        errorMessage="Missing Operator!";
        return false; // No operation symbol found
    }

    /**
     * Checks if the input contains an equal symbol.
     * <p>Precondition: playerInput must not be null.</p>
     *
     * @param playerInput the input to check
     * @return true if the input contains an equal symbol, false otherwise
     */
    @Override
    public boolean isHaveEqualSymbol(String playerInput) {
        assert playerInput != null : "Precondition failed: playerInput is null.";
        if(playerInput.contains("=")==true){
            return true;
        }
        else{
            errorMessage="Missing Equal Sign!";
        }
        return false;
    }
    /**
     * Checks if the input is the first character and is an operator.
     * <p>Precondition: playerInput must not be null.</p>
     *
     * @param input the input to check
     * @param playerInput the list of characters representing the player's input
     * @return true if the input is the first character and is an operator, false otherwise
     */
    @Override
    public boolean isFirstInputAndOperator(char input, List<Character> playerInput) {
        assert input != '\u0000' : "Precondition failed: playerInput is null.";
        // 检查是否为第一个输入且为运算符
        if (playerInput.isEmpty() && isOperator(input)==true) {
            errorMessage="First input cannot be an operator!";
            return true;
        }
        return false;
    }
    /**
     * Gets the error message.
     *
     * @return the error message
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Checks if the input is an operator.
     *
     * @param input the input to check
     * @return true if the input is an operator, false otherwise
     */
    @Override
    public boolean isOperator(char input) {
        // 检查字符是否为运算符
        return input == '+' || input == '-' || input == '*' || input == '/' || input == '=';
    }
    /**
     * Checks if the input is a consecutive operator.
     * <p>Precondition: playerInput must not be null.</p>
     *
     * @param input the input to check
     * @param playerInput the list of characters representing the player's input
     * @return true if the input is a consecutive operator, false otherwise
     */
    @Override
    public boolean isConsecutiveOperator(char input, List<Character> playerInput) {
        assert input != '\u0000' : "Precondition failed: playerInput is null.";
        // 检查最后一个输入是否为运算符，以及当前输入是否也为运算符
        if (!playerInput.isEmpty() && isOperator(input)) {
            char lastInput = playerInput.get(playerInput.size() - 1);
            return isOperator(lastInput);
        }
        return false;
    }
    /**
     * Converts a list of characters to a string.
     *
     * @param list the list of characters to convert
     * @return the string representation of the list
     */
    @Override
    public String listToString(List<Character> list) {
        assert list != null : "Precondition failed: list is null.";
        StringBuilder sb = new StringBuilder();
        for (Character ch : list) {
            sb.append(ch);
        }
        return sb.toString();
    }
    /**
     * Compares the player's equation to the target equation.
     *
     * @param playerEq the player's equation to compare
     * @param targetEq the target equation to compare against
     * @return the result of the comparison
     */
    @Override
    public String compareEquations(String playerEq, String targetEq) {
        assert playerEq != null && targetEq != null : "Precondition failed: playerEq or targetEq is null.";
        StringBuilder feedback = new StringBuilder();
        boolean isAllCorrect = true; // Assume the player's input is completely correct
        for (int i = 0; i < playerEq.length(); i++) {
            char pChar = playerEq.charAt(i);
            char tChar = targetEq.charAt(i);

            if (pChar == tChar) {
                feedback.append(pChar).append(" - correct position and value ");
                feedbackMatrix[currentRow][i] = CharacterFeedback.CORRECT;
                if (!CORRECT.contains(String.valueOf(pChar))) {
                    CORRECT.add(String.valueOf(pChar));
                }
            } else {
                isAllCorrect = false; // There are errors
                if (targetEq.indexOf(pChar) != -1) {
                    feedback.append(pChar).append(" - correct content, wrong position ");
                    feedbackMatrix[currentRow][i] = CharacterFeedback.WRONG_POSITION;
                    if (!WRONG_POSITION.contains(String.valueOf(pChar))) {
                        WRONG_POSITION.add(String.valueOf(pChar));
                    }
                } else {
                    feedback.append(pChar).append(" - completely incorrect ");
                    feedbackMatrix[currentRow][i] = CharacterFeedback.INCORRECT;
                    if (!INCORRECT.contains(String.valueOf(pChar))) {
                        INCORRECT.add(String.valueOf(pChar));
                    }
                    incorrectValues.add(pChar); // Add the completely incorrect value to the Set
                }
            }
        }
        currentRow++;
        System.out.println(feedback); // Print feedback comparison

        if (!incorrectValues.isEmpty()) {
            System.out.println("Completely incorrect values: " + incorrectValues.toString());
            System.out.println("Correct content, wrong position values: " + WRONG_POSITION.toString());
            System.out.println("Correct position and value: " + CORRECT.toString());
        } else {
            System.out.println("No completely incorrect values.");
        }
        if (isAllCorrect) {
            System.out.println("Congratulations, you guessed right! Game won.");
            attempts++;
            setChanged(); // Mark the state as changed
            notifyObservers("Win"); // Notify observers of state change
            setGameOver(true);
            return "Win";
        } else if (attempts < 5) {
            // Print the number of guesses information here
            System.out.println("This is guess number " + (attempts + 1) + ", you have " + (6 - attempts - 1) + " chances left.");
            setChanged(); // Mark the state as changed
            notifyObservers("Continue"); // Notify observers of state change
            setGameOver(false);
            attempts++;
            return "Continue";
        } else {
            System.out.println("Unfortunately, you did not guess correctly within the allowed attempts. Game over.");
            attempts++;
            setChanged(); // Mark the state as changed
            notifyObservers("Lose"); // Notify observers of state change
            setGameOver(true);
            return "Lose";
        }
    }

    /**
     * Sets the game over state.
     *
     * @param isOver the new game over state
     * <p>Precondition: None</p>
     * <p>Postcondition: Updates the game state to over and notifies observers.</p>
     */
    @Override
    public void setGameOver(boolean isOver) {
        gameIsOver = isOver;
        setChanged();
        notifyObservers("GameOver");
    }


    /**
     * Verifies the player's input.
     * <p>Precondition: The equation must not be null and should contain an '=' character.</p>
     * <p>Postcondition: Returns true if the equation is mathematically valid, false otherwise.</p>
     *
     * @param equation the input to verify
     * @return true if the input is valid, false otherwise
     */
    @Override
    public boolean isValidEquation(String equation) {
        assert equation != null && equation.contains("=") : "Precondition failed: equation is null or does not contain '='";

        String[] parts = equation.split("=");
        if (parts.length != 2) {
            errorMessage = "Invalid Equation!";
            setChanged();
            notifyObservers("InvalidEquation");
            return false;
        }
        try {
            double leftValue = evaluate(parts[0]);
            double rightValue = evaluate(parts[1]);
            boolean isValid = Math.abs(leftValue - rightValue) < 0.0001;
            assert isValid : "Postcondition failed: The left and right parts of the equation do not match";

            setChanged();
            notifyObservers("ValidEquation");
            return isValid;
        } catch (Exception e) {
            setChanged();
            notifyObservers("InvalidEquation");
            return false;
        }
    }

    /**
     * Evaluates a mathematical expression.
     * <p>Precondition: The expression must not be null and should be a valid mathematical expression.</p>
     * <p>Postcondition: Returns the calculated result of the expression.</p>
     *
     * @param expression the expression to evaluate
     * @return the result of the evaluation
     */
    @Override
    public double evaluate(String expression) {
        assert expression != null : "Precondition failed: expression is null";

        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double result = parseExpression();
                assert pos == expression.length() : "Postcondition failed: Did not reach the end of the expression";
                return result;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    /**
     * Updates the user log with the input.
     * <p>Precondition: The input must not be null and should be a valid equation.</p>
     */
    public void deleteLastCharacter() {
        assert currentRow >= 0 && currentCol >= 0 : "Precondition failed: currentRow or currentCol is negative.";
        if (currentCol > 0) {
            matrix[currentRow][--currentCol] = ""; // Delete the last character of the current row
        } else if (currentRow == 0) {
            // If the current column is 0, and not in the first row, and Enter has not been pressed to move to this row, do not perform deletion
            errorMessage="Cannot delete previous row!";
        }
    }
    /**
     * Sets the random equation flag.
     * <p>Precondition: randomEquationFlag must be a boolean value.</p>
     *
     * @param randomEquationFlag the new random equation flag
     */
    @Override
    public void setRandomEquationFlag(boolean randomEquationFlag) {
        assert randomEquationFlag == true || randomEquationFlag == false : "Precondition failed: randomEquationFlag must be a boolean value.";
        this.randomEquationFlag = randomEquationFlag;
        if (randomEquationFlag == true) {
            try {
                targetEquation = loadAndSelectEquation("equations.txt");
                System.out.println("Random equation loaded: " + targetEquation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setDefaultEquation();
            System.out.println("Default equation set: " + targetEquation);
        }
    }
    /**
     * Sets the show equation flag.
     * <p>Precondition: showEquationFlag must be a boolean value.</p>
     *
     * @param showEquationFlag the new show equation flag
     */
    @Override
    public void setShowEquationFlag(boolean showEquationFlag) {
        assert showEquationFlag == true || showEquationFlag == false : "Precondition failed: showEquationFlag must be a boolean value.";
        this.showEquationFlag = showEquationFlag;
    }
    /**
     * Sets the show error message flag.
     * <p>Precondition: showErrorMessageFlag must be a boolean value.</p>
     *
     * @param showErrorMessageFlag the new show error message flag
     */
    @Override
    public void setShowErrorMessageFlag(boolean showErrorMessageFlag) {
        assert showErrorMessageFlag == true || showErrorMessageFlag == false : "Precondition failed: showErrorMessageFlag must be a boolean value.";
        this.showErrorMessageFlag = showErrorMessageFlag;
    }
    /**
     * Sets the random equation flag.
     */
    @Override
    public boolean getShowEquationFlag() {
        return showEquationFlag;
    }
    /**
     * Gets the show equation flag.
     */
    @Override
    public boolean getShowErrorMessageFlag() {
        return showErrorMessageFlag;
    }
    /**
     * Gets the random equation flag.
     */
    @Override
    public boolean getRandomEquationFlag() {
        return randomEquationFlag;
    }
    /**
     * sets the error message.
     * <p>Precondition: errorMessage must not be null.</p>
     */
    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Increments the number of attempts.
     */
    @Override
    public void incrementAttempts() {
        attempts++;
    }

    /**
     * Checks if the game is over.
     * <p>Precondition: gameIsOver must be a boolean value.</p>
     *
     * @return true if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return gameIsOver;
    }

    /**
     * Clears the player's input.
     * <p>Precondition: playerInput must not be null.</p>
     */
    public void clearPlayerInput() {
        assert playerInput != null : "Precondition failed: playerInput cannot be null.";
        playerInput.clear();
    }

    /**
     * Converts an array of strings to a single string.
     * <p>Precondition: arr must not be null.</p>
     *
     * @param arr the array of strings to convert
     * @return the single string representation of the array
     */
    @Override
    public String arrayToString(String[] arr) {
        assert arr != null : "Precondition failed: arr cannot be null.";
        StringBuilder sb = new StringBuilder();
        for (String ch : arr) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Restarts the game.
     */
    public void restartGame() {
        attempts = 0;
        gameIsOver = false;
        incorrectValues.clear();
        CORRECT.clear();
        INCORRECT.clear();
        WRONG_POSITION.clear();
        feedbackMatrix = new CharacterFeedback[6][7]; // 重置反馈矩阵
        currentRow = 0; // 重置当前行
        clearPlayerInput();
        targetEquation="1+1+1=3";
        // 标记状态已改变
        setChanged();
        // 通知所有观察者游戏已重启
        notifyObservers("GameRestarted");
    }

    /**
     * Sets the default equation.
     */
    public void setDefaultEquation() {
        targetEquation = "1+1+1=3";
    }

    /**
     * Gets the target equation.
     *
     * @return the target equation
     */
    public CharacterFeedback[][] getFeedback() {
        return feedbackMatrix;
    }
}
