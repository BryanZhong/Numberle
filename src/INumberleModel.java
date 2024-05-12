import java.util.ArrayList;
import java.util.List;


public interface INumberleModel {
    /**
     * Loads and selects an equation from a specified file.
     *
     * @param filePath the path to the file containing equations
     * @return the selected equation as a string
     * @throws Exception if the file cannot be read
     */
    String loadAndSelectEquation(String filePath) throws Exception;

    /**
     * Handles user input by updating the player's input list based on the current input character.
     *
     * @param input the character input by the user
     * @param playerInput the current list of characters input by the player
     */
    void handleInput(String input, List<Character> playerInput);

    /**
     * Checks if the player's input contains any mathematical operation symbols.
     *
     * @param playerInput the string of player's inputs
     * @return true if there is an operation symbol; false otherwise
     */
    boolean isHaveOperationSymbol(String playerInput);

    /**
     * Checks if the player's input contains an equal sign.
     *
     * @param playerInput the string of player's inputs
     * @return true if there is an equal sign; false otherwise
     */
    boolean isHaveEqualSymbol(String playerInput);

    /**
     * Determines if the first input is an operator, which is not allowed.
     *
     * @param input the input character
     * @param playerInput the current list of player's inputs
     * @return true if the first input is an operator; false otherwise
     */
    boolean isFirstInputAndOperator(char input, List<Character> playerInput);

    /**
     * Checks if a character is an operator.
     *
     * @param input the character to check
     * @return true if the character is an operator; false otherwise
     */
    boolean isOperator(char input);

    /**
     * Checks if consecutive inputs are operators, which is not allowed.
     *
     * @param input the current input character
     * @param playerInput the current list of player's inputs
     * @return true if the last and current inputs are operators; false otherwise
     */
    boolean isConsecutiveOperator(char input, List<Character> playerInput);

    /**
     * Converts a list of characters to a string.
     *
     * @param list the list of characters
     * @return the concatenated string
     */
    String listToString(List<Character> list);

    /**
     * Compares the player's equation with the target equation.
     *
     * @param playerEq the player's equation
     * @param targetEq the target equation
     * @return a string describing the result of the comparison
     */
    String compareEquations(String playerEq, String targetEq);

    /**
     * Sets the game over state.
     *
     * @param isOver the game over state to set
     */
    void setGameOver(boolean isOver);

    /**
     * Validates whether the given equation is mathematically correct.
     *
     * @param equation the equation to validate
     * @return true if the equation is valid; false otherwise
     */
    boolean isValidEquation(String equation);

    /**
     * Evaluates a mathematical expression.
     *
     * @param expression the expression to evaluate
     * @return the result of the expression
     */
    double evaluate(String expression);

    /**
     * Increments the number of attempts made by the player.
     */
    void incrementAttempts();

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over; false otherwise
     */
    boolean isGameOver();

    /**
     * Clears the player's input.
     */
    void clearPlayerInput();

    /**
     * Converts an array of strings to a single string.
     *
     * @param arr the array to convert
     * @return the concatenated string
     */
    String arrayToString(String[] arr);

    /**
     * Gets the feedback matrix which shows the state of each character in the player's equation.
     *
     * @return the feedback matrix
     */
    CharacterFeedback[][] getFeedback();

    /**
     * Restarts the game, resetting all necessary fields and states.
     */
    void restartGame();

    /**
     * Sets the flag indicating whether error messages should be shown.
     *
     * @param showErrorMessageFlag the flag to set
     */
    void setShowErrorMessageFlag(boolean showErrorMessageFlag);

    /**
     * Sets the flag indicating whether the equation should be shown.
     *
     * @param showEquationFlag the flag to set
     */
    void setShowEquationFlag(boolean showEquationFlag);

    /**
     * Sets the flag indicating whether equations are selected randomly.
     *
     * @param randomEquationFlag the flag to set
     */
    void setRandomEquationFlag(boolean randomEquationFlag);

    /**
     * Gets the flag indicating whether the equation is shown.
     *
     * @return true if the equation is shown; false otherwise
     */
    boolean getShowEquationFlag();

    /**
     * Gets the flag indicating whether error messages are shown.
     *
     * @return true if error messages are shown; false otherwise
     */
    boolean getShowErrorMessageFlag();

    /**
     * Gets the flag indicating whether equations are selected randomly.
     *
     * @return true if equations are selected randomly; false otherwise
     */
    boolean getRandomEquationFlag();

    /**
     * Sets the error message to display.
     *
     * @param errorMessage the error message to set
     */
    void setErrorMessage(String errorMessage);

    /**
     * Gets the current error message.
     *
     * @return the current error message
     */
    String getErrorMessage();

}
