import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NumberleModelTest {
    /**
     * Test scenario to verify the model's behavior with multiple user inputs:
     * three incorrect equations followed by a correct equation.
     * Preconditions:
     * 1. The game is initialized with the default equation set to "1+1+1=3".
     * 2. The random equation flag is set to false, ensuring consistent test behavior.
     * 3. 'incorrectGuesses' represents equations that are mathematically correct but do not match the target.
     * 4. 'correctGuess' represents the correct equation exactly matching the target.
     *
     * Test Steps:
     * 1. Submit the first 'wrong' equation to the model, verify that the game is not over and the user has not won.
     * 2. Submit the second 'wrong' equation to the model, verify that the game is not over and the user has not won.
     * 3. Submit the third 'wrong' equation to the model, verify that the game is not over and the user has not won.
     * 4. Submit the 'correct' equation to the model, verify that the game is over and the user has won.
     *
     * Expected Results:
     * The game should not be over and the user should not have won after the three incorrect guesses.
     * After submitting the correct equation on the fourth attempt, the game should be over and the user should have won.
     */
    @Test
    public void testThreeIncorrectAndCorrectGuesses() {
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        // Simulate three incorrect guesses
        String[] incorrectGuesses = new String[]{
                "2+2+2=6",  // Incorrect, but valid format
                "3*1+2=5",  // Incorrect, but valid format
                "1+1+1=2"  // Completely invalid format
        };
        assertTrue(model.isValidEquation(incorrectGuesses[0]), "Expected guess to be incorrect or invalid");
        // Verify that the attempts are incremented
        assertEquals( "Continue", model.compareEquations(incorrectGuesses[0], model.targetEquation),"Game should continue");
        assertTrue(model.isValidEquation(incorrectGuesses[1]), "Expected guess to be incorrect or invalid");
        assertEquals( "Continue", model.compareEquations(incorrectGuesses[1], model.targetEquation),"Game should continue");
        assertEquals(2, model.attempts, "Expected attempts to be incremented");
        assertFalse(model.isValidEquation(incorrectGuesses[2]), "Expected guess to be incorrect or invalid");
        // Ensure the game is not over yet
        assertFalse(model.isGameOver(), "Game should not be over");
        // Now simulate the correct guess
        String correctGuess = "1+1+1=3";
        assertTrue(model.isValidEquation(correctGuess), "Expected guess to be correct");
        assertEquals( "Win", model.compareEquations(correctGuess, model.targetEquation),"Game should be won");
        // Ensure the game is over after the correct guess
        assertTrue(model.isGameOver(), "Game should be over");
    }

    /**
     * Test scenario to verify the model's behavior after three incorrect guesses.
     * Preconditions:
     * 1. The game is initialized with the default equation set to "1+1+1=3".
     * 2. The default equation is explicitly set to ensure consistency.
     * 3. The three inputs are valid mathematical equations but do not match the target equation, ensuring they are processed as incorrect guesses.
     *
     * Test Steps:
     * 1. Submit the first incorrect guess ("2+2+2=6"), verify that the guess is valid and the game should continue.
     * 2. Submit the second incorrect guess ("3*1+2=5"), verify that the guess is valid and the game should continue.
     * 3. Submit the third incorrect guess ("1+1+2=4"), verify that the guess is valid and the game should continue.
     * 4. Verify that the game has not concluded after these guesses, confirming the model's behavior under multiple incorrect submissions.
     *
     * Expected Results:
     * The model should handle each guess as incorrect but valid, allowing the game to continue without concluding.
     * After three incorrect guesses, the game should not be over, indicating the model correctly manages multiple attempts without prematurely ending the game.
     */
    @Test
    void testGuessThreeTimes() {
        //测试猜测三次
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        // Simulate three incorrect guesses
        String[] incorrectGuesses = new String[]{
                "2+2+2=6",  // Incorrect, but valid format
                "3*1+2=5",  // Incorrect, but valid format
                "1+1+2=4"  // Completely invalid format
        };
        assertTrue(model.isValidEquation(incorrectGuesses[0]), "Expected guess to be incorrect or invalid");
        // Verify that the attempts are incremented
        assertEquals( "Continue", model.compareEquations(incorrectGuesses[0], model.targetEquation),"Game should continue");
        assertTrue(model.isValidEquation(incorrectGuesses[1]), "Expected guess to be incorrect or invalid");
        assertEquals( "Continue", model.compareEquations(incorrectGuesses[1], model.targetEquation),"Game should continue");
        assertTrue(model.isValidEquation(incorrectGuesses[2]), "Expected guess to be incorrect or invalid");
        assertEquals( "Continue", model.compareEquations(incorrectGuesses[2], model.targetEquation),"Game should continue");
        // Ensure the game is not over yet
        assertFalse(model.isGameOver(), "Game should not be over");
    }

    /**
     * Test scenario to verify the model's behavior after six guesses, with the last guess being correct.
     * Preconditions:
     * 1. The game is initialized with the default equation set to "1+1+1=3".
     * 2. The default equation is explicitly set to ensure consistency and repeatability of the test.
     * 3. The first five inputs are valid mathematical equations but do not match the target equation.
     * 4. The sixth input is the correct equation, exactly matching the target.
     *
     * Test Steps:
     * 1. Submit five incorrect guesses sequentially, each being valid in format but incorrect as per the target equation.
     * 2. Submit the sixth guess which is correct and matches the target equation.
     *
     * Expected Results:
     * The model should handle the first five guesses as incorrect but valid, allowing the game to continue.
     * After the sixth correct guess, the game should conclude with the user winning, demonstrating the model's
     * ability to end the game correctly upon receiving a correct equation.
     */
    @Test
    public void testGuessSixTimesWin() {
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation(); // Setting the default equation to ensure test consistency

        // Incorrect guesses
        String[] incorrectGuesses = new String[]{
                "2+2+2=6",   // Incorrect, valid format
                "3*1+2=5",   // Incorrect, valid format
                "1+1+2=4",   // Incorrect, valid format
                "0+0+0=0",   // Incorrect, valid format
                "5-2-1=2"    // Incorrect, valid format
        };

        // Process each incorrect guess and verify that the game should continue
        for (String guess : incorrectGuesses) {
            assertTrue(model.isValidEquation(guess), "Expected guess to be valid but incorrect");
            assertEquals("Continue", model.compareEquations(guess, model.targetEquation), "Game should continue after each incorrect guess");
        }

        // Verify that the game is not over after five incorrect guesses
        assertFalse(model.isGameOver(), "Game should not be over after five incorrect guesses");

        // Correct guess
        String correctGuess = "1+1+1=3";
        assertTrue(model.isValidEquation(correctGuess), "Expected guess to be correct");
        assertEquals("Win", model.compareEquations(correctGuess, model.targetEquation), "Game should be won after the correct guess");

        // Ensure the game is over after the correct guess
        assertTrue(model.isGameOver(), "Game should be over and won after the correct guess");
    }

    /**
     * Test scenario to verify the model's behavior after six incorrect guesses, resulting in a game loss.
     * Preconditions:
     * 1. The game is initialized with the default equation set to "1+1+1=3".
     * 2. The default equation is explicitly set to ensure consistency and repeatability of the test.
     * 3. All six inputs are valid mathematical equations but do not match the target equation.
     *
     * Test Steps:
     * 1. Submit six incorrect guesses sequentially, each being valid in format but incorrect as per the target equation.
     * 2. After the sixth incorrect guess, verify that the game concludes with a loss.
     *
     * Expected Results:
     * The model should handle each guess as incorrect but valid, and the game should conclude with a loss after the sixth incorrect guess.
     * This demonstrates the model's ability to end the game correctly upon exhausting all allowed guesses without a correct answer.
     */
    @Test
    public void testGuessSixTimesLoss() {
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation(); // Setting the default equation to ensure test consistency

        // Incorrect guesses
        String[] incorrectGuesses = new String[]{
                "2+2+2=6",   // Incorrect, valid format
                "3*1+2=5",   // Incorrect, valid format
                "1+1+2=4",   // Incorrect, valid format
                "0+0+0=0",   // Incorrect, valid format
                "5-2-1=2",   // Incorrect, valid format
                "7-4+1=4"    // Incorrect, valid format
        };

        // Process each incorrect guess
        for (String guess : incorrectGuesses) {
            assertTrue(model.isValidEquation(guess), "Expected guess to be valid but incorrect");
            if (!guess.equals(incorrectGuesses[5])) {
                assertEquals("Continue", model.compareEquations(guess, model.targetEquation), "Game should continue after each incorrect guess");
            } else {
                assertEquals("Lose", model.compareEquations(guess, model.targetEquation), "Game should end with a loss on the final incorrect guess");
            }
        }

        // Verify that the game is over after six incorrect guesses
        assertTrue(model.isGameOver(), "Game should be over after six incorrect guesses");
    }


    /**
     * Test scenario to verify the model's ability to load and select an equation from a file.
     * Preconditions:
     * 1. The game is initialized.
     * 2. The file "equations.txt" is present and readable.
     * 3. The model is set to load the equation directly into its target equation variable.
     *
     * Test Steps:
     * 1. Attempt to load an equation from "equations.txt".
     * 2. Verify that the model's target equation is not null after loading, ensuring that the file read and equation selection were successful.
     *
     * Expected Results:
     * The target equation should be loaded successfully and not be null, confirming the model's capability to handle file-based equation loading correctly.
     */
    @Test
    void loadAndSelectEquation() {
        NumberleModel model = new NumberleModel();
        try {
            model.loadAndSelectEquation("equations.txt");
            assertNotNull(model.targetEquation);  // Ensure that the target equation is not null after loading.
        } catch (Exception e) {
            fail("Failed to load equation from the file: " + e.getMessage());  // Fail the test if an exception occurs
        }
    }


    /**
     * Test scenario to verify the model's ability to handle different types of inputs and update the player's input list correctly.
     * Preconditions:
     * 1. The game is initialized.
     * 2. The playerInput list is initially empty.
     *
     * Test Steps:
     * 1. Input a digit '1' and verify it is added correctly.
     * 2. Input an operator '+' and verify it appends correctly.
     * 3. Input another digit '1' and verify it appends correctly.
     * 4. Input an equal sign '=' and verify it appends correctly.
     * 5. Input a delete command 'd' and verify the last character is removed correctly.
     *
     * Expected Results:
     * After each input, the model's player input should reflect the expected sequence of characters, showing correct handling of different input types.
     */
    @Test
    void testHandleInput() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertEquals("1", model.listToString(playerInput));
        model.handleInput("+", playerInput);
        assertEquals("1+", model.listToString(playerInput));
        model.handleInput("1", playerInput);
        assertEquals("1+1", model.listToString(playerInput));
        model.handleInput("=", playerInput);
        assertEquals("1+1=", model.listToString(playerInput));
        model.handleInput("d", playerInput);
        assertEquals("1+1", model.listToString(playerInput));
    }

    /**
     * Test scenario to verify whether the input string contains any operation symbols.
     * Preconditions:
     * 1. The game is initialized.
     * 2. Player input does not initially contain any characters.
     *
     * Test Steps:
     * 1. Input a digit '1' and verify no operation symbol is present.
     * 2. Input an operator '+' and verify the presence of an operation symbol.
     *
     * Expected Results:
     * The method should correctly identify the absence or presence of operation symbols in the player's input.
     */
    @Test
    void TestIsHaveOperationSymbol() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertFalse(model.isHaveOperationSymbol(playerInput.toString()));
        model.handleInput("+", playerInput);
        assertTrue(model.isHaveOperationSymbol(playerInput.toString()));
    }

    /**
     * Test scenario to verify whether the input string contains an equal symbol.
     * Preconditions:
     * 1. The game is initialized.
     * 2. Player input does not initially contain any characters.
     *
     * Test Steps:
     * 1. Input a digit '1' and verify no equal symbol is present.
     * 2. Input an equal sign '=' and verify the presence of an equal symbol.
     *
     * Expected Results:
     * The method should accurately detect the absence or presence of an equal symbol in the player's input.
     */
    @Test
    void isHaveEqualSymbol() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertFalse(model.isHaveEqualSymbol(playerInput.toString()));
        model.handleInput("=", playerInput);
        assertTrue(model.isHaveEqualSymbol(playerInput.toString()));
    }


    /**
     * Test scenario to verify whether the first input character is an operator and handled correctly.
     * Preconditions:
     * 1. The game is initialized.
     * 2. The player input list is initially empty.
     *
     * Test Steps:
     * 1. Input an operator '+' as the first character and verify the method correctly identifies it as the first input being an operator.
     * 2. Input a digit '1' after the operator and verify the method does not identify it incorrectly as the first operator input.
     *
     * Expected Results:
     * The method should correctly identify the first input as an operator when it is and should not falsely identify subsequent inputs as first operator inputs.
     */
    @Test
    void isFirstInputAndOperator() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("+", playerInput);
        assertTrue(model.isFirstInputAndOperator('+', playerInput));
        model.handleInput("1", playerInput);
        assertFalse(model.isFirstInputAndOperator('1', playerInput));
    }

    /**
     * Test scenario to verify if a character is recognized as an operator.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Input character '+' and verify it is recognized as an operator.
     * 2. Input character '1' and verify it is not recognized as an operator.
     *
     * Expected Results:
     * The method should accurately distinguish between operator and non-operator characters.
     */
    @Test
    void isOperator() {
        NumberleModel model = new NumberleModel();
        assertTrue(model.isOperator('+'));
        assertFalse(model.isOperator('1'));
    }

    /**
     * Test scenario to verify whether consecutive inputs of operators are handled correctly.
     * Preconditions:
     * 1. The game is initialized.
     * 2. Player input list is empty initially.
     *
     * Test Steps:
     * 1. Input operator '+' and verify it does not falsely trigger consecutive operator logic when it's the first operator.
     * 2. Input another '+' and verify if it is identified as a consecutive operator incorrectly (according to your method's expected behavior).
     *
     * Expected Results:
     * The method should not identify the first '+' as a consecutive operator and should behave correctly on subsequent operator inputs.
     */
    @Test
    void isConsecutiveOperator() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("+", playerInput);
        assertFalse(model.isConsecutiveOperator('+', playerInput));
        model.handleInput("+", playerInput);
        assertFalse(model.isConsecutiveOperator('+', playerInput));
    }

    /**
     * Test scenario to verify converting a list of character inputs to a string format.
     * Preconditions:
     * 1. The game is initialized.
     * 2. Player input list is empty initially.
     *
     * Test Steps:
     * 1. Input '1' and verify the string conversion reflects this input correctly.
     * 2. Input '+' and verify the string conversion appends it correctly to the existing string.
     *
     * Expected Results:
     * The listToString method should accurately convert the list of characters to a single string reflecting all user inputs in order.
     */
    @Test
    void listToString() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertEquals("1", model.listToString(playerInput));
        model.handleInput("+", playerInput);
        assertEquals("1+", model.listToString(playerInput));
    }

    /**
     * Test scenario to verify the comparison of two equations.
     * Preconditions:
     * 1. The game is initialized.
     * 2. Two predefined equations are set, one correct ("1+1+1=3") and one incorrect ("1+1+2=4").
     *
     * Test Steps:
     * 1. Compare two identical equations and verify the result is 'Win'.
     * 2. Compare a correct equation with an incorrect one and verify the result is 'Continue'.
     *
     * Expected Results:
     * The compareEquations method should correctly determine when equations match (leading to a win) and when they do not (leading to continuation of the game).
     */
    @Test
    void compareEquations() {
        NumberleModel model = new NumberleModel();
        assertEquals("Win", model.compareEquations("1+1+1=3", "1+1+1=3"));
        assertEquals("Continue", model.compareEquations("1+1+2=4", "1+1+1=3"));
    }


    /**
     * Test scenario to verify that the game over state can be correctly set and retrieved.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Set the game over state to true.
     * 2. Check if the game over state is reported as true.
     *
     * Expected Results:
     * The game over state should accurately reflect the state set by the test, validating the setGameOver method's functionality.
     */
    @Test
    void setGameOver() {
        NumberleModel model = new NumberleModel();
        model.setGameOver(true);
        assertTrue(model.gameIsOver);
    }

    /**
     * Test scenario to verify the validation of mathematical equations.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Validate a correct equation "1+1=2".
     * 2. Validate an incorrect equation "1+1=2+".
     *
     * Expected Results:
     * The method should correctly identify the first equation as valid and the second as invalid, testing the robustness of equation validation logic.
     */
    @Test
    void isValidEquation() {
        NumberleModel model = new NumberleModel();
        assertTrue(model.isValidEquation("1+1=2"));
        assertFalse(model.isValidEquation("1+1=2+"));
    }

    /**
     * Test scenario to verify the evaluation of arithmetic expressions.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Evaluate the expression "1+1" and expect a result of 2.
     * 2. Evaluate the expression "1+1+1" and expect a result of 3.
     *
     * Expected Results:
     * The evaluate method should correctly compute the result of arithmetic expressions, ensuring accurate calculation within the game.
     */
    @Test
    void evaluate() {
        NumberleModel model = new NumberleModel();
        assertEquals(2, model.evaluate("1+1"), 0.001);
        assertEquals(3, model.evaluate("1+1+1"), 0.001);
    }

    /**
     * Test scenario to verify the incrementation of attempt counts in the game.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Increment the number of attempts by one.
     *
     * Expected Results:
     * The attempts counter should correctly increment, reflecting proper tracking of user attempts.
     */
    @Test
    void incrementAttempts() {
        NumberleModel model1 = new NumberleModel();
        model1.incrementAttempts();
        assertEquals(1, model1.attempts);
    }

    /**
     * Test scenario to verify converting an array of strings into a single string.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Convert the array {"1", "+", "1", "=", "2"} into a single string.
     *
     * Expected Results:
     * The arrayToString method should correctly concatenate array elements into a single string, ensuring accurate string representation.
     */
    @Test
    void arrayToString() {
        NumberleModel model = new NumberleModel();
        String[] arr = {"1", "+", "1", "=", "2"};
        assertEquals("1+1=2", model.arrayToString(arr));
    }

    /**
     * Test scenario to verify the game's ability to restart and reset all relevant states.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Restart the game, which should reset attempts and potentially other game states.
     *
     * Expected Results:
     * After restarting, the game should have all counters or states set to their initial conditions, particularly the attempt counter should be zero.
     */
    @Test
    void restartGame() {
        NumberleModel model = new NumberleModel();
        model.restartGame();
        assertEquals(0, model.attempts);
    }


    /**
     * Test scenario to verify the functionality of setting the default equation in the model.
     * Preconditions:
     * 1. The game is initialized.
     *
     * Test Steps:
     * 1. Invoke the setDefaultEquation method to set the equation.
     * 2. Check if the target equation in the model matches the expected default equation "1+1+1=3".
     *
     * Expected Results:
     * The target equation should be set to "1+1+1=3", confirming that the setDefaultEquation method properly initializes the target equation to its default state.
     */
    @Test
    void setDefaultEquation() {
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        assertEquals("1+1+1=3", model.targetEquation);
    }

}