import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NumberleController {
    NumberleModel model = new NumberleModel();
    private Scanner scanner;

    /**
     * Constructor for NumberleController.
     *
     * @param model The NumberleModel instance this controller will manage.
     */
    public NumberleController(NumberleModel model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Retrieves the target equation.
     *
     * @return The current target equation.
     */
    public String getTargetEquation() {
        return model.targetEquation;
    }

    /**
     * Sets the target equation.
     *
     * @param equation The new target equation.
     */
    public void setTargetEquation(String equation) {
        model.targetEquation = equation;
    }

    /**
     * Processes the input from the user and updates the player input list accordingly.
     *
     * @param input The new character input by the user.
     * @param playerInput The list of characters representing the player's current input.
     */
    public void handleInput(String input, List<Character> playerInput) {
        model.handleInput(input, playerInput);
    }

    /**
     * Determines if the player's input contains a mathematical operation symbol.
     *
     * @param playerInput The player's input as a string.
     * @return True if an operation symbol is present, false otherwise.
     */
    public boolean isHaveOperationSymbol(String playerInput) {
        return model.isHaveOperationSymbol(playerInput);
    }

    /**
     * Checks if the player's input contains an equals sign.
     *
     * @param playerInput The player's input as a string.
     * @return True if an equals sign is present, false otherwise.
     */
    public boolean isHaveEqualSymbol(String playerInput) {
        return model.isHaveEqualSymbol(playerInput);
    }

    /**
     * Determines if the first character input is an operator, which is not allowed.
     *
     * @param input The character input.
     * @param playerInput The current list of player's inputs.
     * @return True if the first character is an operator, false otherwise.
     */
    public boolean isFirstInputAndOperator(char input, List<Character> playerInput) {
        return model.isFirstInputAndOperator(input, playerInput);
    }

    /**
     * Checks if a given character is an operator.
     *
     * @param input The character to check.
     * @return True if the character is an operator, false otherwise.
     */
    public boolean isOperator(char input) {
        return model.isOperator(input);
    }

    /**
     * Determines if consecutive inputs are operators, which is not allowed.
     *
     * @param input The current character input.
     * @param playerInput The current list of player's inputs.
     * @return True if consecutive characters are operators, false otherwise.
     */
    public boolean isConsecutiveOperator(char input, List<Character> playerInput) {
        return model.isConsecutiveOperator(input, playerInput);
    }

    /**
     * Converts a list of characters into a string.
     *
     * @param list The list of characters.
     * @return A string formed by concatenating the characters in the list.
     */
    public String listToString(List<Character> list) {
        return model.listToString(list);
    }

    /**
     * Compares the player's equation with the target equation and provides feedback.
     *
     * @param playerEq The player's equation.
     * @param targetEq The target equation.
     * @return A string describing the result of the comparison.
     */
    public String compareEquations(String playerEq, String targetEq) {
        return model.compareEquations(playerEq, targetEq);
    }

    /**
     * Restarts the game, resetting all game parameters.
     */
    public void restartGame() {
        model.restartGame();
    }

    /**
     * Sets the game over state.
     *
     * @param isOver A boolean indicating whether the game is over.
     */
    public void setGameOver(boolean isOver) {
        model.setGameOver(isOver);
    }

    /**
     * Validates whether a given equation is mathematically correct.
     *
     * @param equation The equation to validate.
     * @return True if the equation is valid, false otherwise.
     */
    public boolean isValidEquation(String equation) {
        return model.isValidEquation(equation);
    }

    /**
     * Evaluates a mathematical expression to a double value.
     *
     * @param expression The mathematical expression to evaluate.
     * @return The result of the evaluation.
     */
    public double evaluate(String expression) {
        return model.evaluate(expression);
    }

    /**
     * Increments the number of attempts made by the player.
     */
    public void incrementAttempts() {
        model.incrementAttempts();
    }

    /**
     * Clears the player's current input.
     */
    public void clearPlayerInput() {
        model.clearPlayerInput();
    }

    /**
     * Converts an array of strings into a single concatenated string.
     *
     * @param arr The array of strings.
     * @return The concatenated string.
     */
    public String arrayToString(String[] arr) {
        return model.arrayToString(arr);
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * Loads an equation from a specified file path and selects it randomly.
     *
     * @param filePath The path to the file containing equations.
     * @return The selected equation.
     * @throws Exception If the file cannot be read.
     */
    public String loadAndSelectEquation(String filePath) throws Exception {
        return model.loadAndSelectEquation(filePath);
    }

    /**
     * Retrieves the feedback matrix which indicates the status of each character in the player's input.
     *
     * @return A matrix of CharacterFeedback enums representing the feedback for each character.
     */
    public CharacterFeedback[][] getFeedback() {
        return model.getFeedback();
    }

    /**
     * Sets whether error messages should be shown.
     *
     * @param showErrorMessageFlag True to show error messages, false otherwise.
     */
    public void setShowErrorMessageFlag(boolean showErrorMessageFlag) {
        model.setShowErrorMessageFlag(showErrorMessageFlag);
    }

    /**
     * Sets whether the equation should be shown.
     *
     * @param showEquationFlag True to show the equation, false otherwise.
     */
    public void setShowEquationFlag(boolean showEquationFlag) {
        model.setShowEquationFlag(showEquationFlag);
    }

    /**
     * Sets whether the equation should be randomly selected.
     *
     * @param randomEquationFlag True to select the equation randomly, false otherwise.
     */
    public void setRandomEquationFlag(boolean randomEquationFlag) {
        model.setRandomEquationFlag(randomEquationFlag);
    }

    /**
     * Checks if the equation is being shown.
     *
     * @return True if the equation is shown, false otherwise.
     */
    public boolean getShowEquationFlag() {
        return model.getShowEquationFlag();
    }

    /**
     * Checks if error messages are being shown.
     *
     * @return True if error messages are shown, false otherwise.
     */
    public boolean getShowErrorMessageFlag() {
        return model.getShowErrorMessageFlag();
    }

    /**
     * Checks if the equation selection is random.
     *
     * @return True if the equation selection is random, false otherwise.
     */
    public boolean getRandomEquationFlag() {
        return model.getRandomEquationFlag();
    }

    /**
     * Retrieves the list of characters that are correct and in the correct position.
     *
     * @return A list of characters that are correct and in the correct position.
     */
    public ArrayList<String> getCORRECT() {
        return model.getCORRECT();
    }

    /**
     * Retrieves the list of characters that are incorrect.
     *
     * @return A list of characters that are incorrect.
     */
    public ArrayList<String> getINCORRECT() {
        return model.getINCORRECT();
    }

    /**
     * Retrieves the list of characters that are correct but in the wrong position.
     *
     * @return A list of characters that are correct but in the wrong position.
     */
    public ArrayList<String> getWRONG_POSITION() {
        return model.getWRONG_POSITION();
    }

    /**
     * Sets the list of characters that are correct and in the correct position.
     *
     * @param CORRECT A list of characters that are correct and in the correct position.
     */
    public void setCORRECT(ArrayList<String> CORRECT) {
        model.setCORRECT(CORRECT);
    }

    /**
     * Sets the list of characters that are incorrect.
     *
     * @param INCORRECT A list of characters that are incorrect.
     */
    public void setINCORRECT(ArrayList<String> INCORRECT) {
        model.setINCORRECT(INCORRECT);
    }

    /**
     * Sets the list of characters that are correct but in the wrong position.
     *
     * @param WRONG_POSITION A list of characters that are correct but in the wrong position.
     */
    public void setWRONG_POSITION(ArrayList<String> WRONG_POSITION) {
        model.setWRONG_POSITION(WRONG_POSITION);
    }

    /**
     * Gets the number of rows in the game matrix.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return model.getRows();
    }

    /**
     * Gets the number of columns in the game matrix.
     *
     * @return The number of columns.
     */
    public int getCols() {
        return model.getCols();
    }

    /**
     * Retrieves the current game matrix.
     *
     * @return The current game matrix as a 2D array of strings.
     */
    public String[][] getMatrix() {
        return model.getMatrix();
    }

    /**
     * Sets the game matrix to a new matrix.
     *
     * @param matrix The new game matrix as a 2D array of strings.
     */
    public void setMatrix(String[][] matrix) {
        model.setMatrix(matrix);
    }

    /**
     * Retrieves the current row index in the game matrix.
     *
     * @return The current row index.
     */
    public int getCurrentRow() {
        return model.getCurrentRow();
    }

    /**
     * Sets the current row index in the game matrix.
     *
     * @param currentRow The row index to set.
     */
    public void setCurrentRow(int currentRow) {
        model.setCurrentRow(currentRow);
    }

    /**
     * Retrieves the current column index in the game matrix.
     *
     * @return The current column index.
     */
    public int getCurrentCol() {
        return model.getCurrentCol();
    }

    /**
     * Sets the current column index in the game matrix.
     *
     * @param currentCol The column index to set.
     */
    public void setCurrentCol(int currentCol) {
        model.setCurrentCol(currentCol);
    }

    /**
     * Retrieves the error message set by the model.
     *
     * @return The current error message.
     */
    public String getErrorMassage() {
        return model.getErrorMessage();
    }

    /**
     * Sets a new error message in the model.
     *
     * @param errorMessage The new error message to set.
     */
    public void setErrorMessage(String errorMessage) {
        model.setErrorMessage(errorMessage);
    }

    /**
     * Deletes the last character from the current row in the game matrix.
     */
    public void deleteLastCharacter() {
        model.deleteLastCharacter();
    }

}
