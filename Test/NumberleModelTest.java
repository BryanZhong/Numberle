import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NumberleModelTest {
    @Test
    void testGuessThreeTimes() {
        //测试猜测三次
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("4", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        System.out.println(model.listToString(model.playerInput));
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("-", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("1", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("3", model.playerInput);
        assertEquals("Win", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
    }

    @Test
    //测试猜测四次
    void testGuessFourTimesWin(){
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("4", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("-", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("1", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("4", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("6", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("3", model.playerInput);
        assertEquals("Win", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
    }
    @Test
    //测试猜测6次
    void testGuessSixTimesWin(){
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("4", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("-", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("1", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("4", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("6", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("5", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("7", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("7", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("9", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("3", model.playerInput);
        assertEquals("Win", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
    }
    @Test
    //测试猜测6次输掉游戏
    void testGuessSixTimesLoss(){
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("4", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("-", model.playerInput);
        model.handleInput("2", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("1", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("4", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("6", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("5", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("7", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("7", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("9", model.playerInput);
        assertEquals("Continue", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
        model.incrementAttempts();
        model.clearPlayerInput();
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("1", model.playerInput);
        model.handleInput("+", model.playerInput);
        model.handleInput("3", model.playerInput);
        model.handleInput("=", model.playerInput);
        model.handleInput("5", model.playerInput);
        assertEquals("Lose", model.compareEquations(model.listToString(model.playerInput), model.targetEquation));
    }
    @Test
    void loadAndSelectEquation() {
        //测试加载等式
        NumberleModel model = new NumberleModel();
        try {
            model.loadAndSelectEquation("equations.txt");
            assertNotNull(model.targetEquation);
        } catch (Exception e) {
            System.out.println("加载失败：" + e);
        }
    }

    @Test
    void testHandleInput() {
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertEquals("1", model.listToString(playerInput));
        //测试输入为运算符
        model.handleInput("+", playerInput);
        assertEquals("1+", model.listToString(playerInput));
        //测试输入为数字
        model.handleInput("1", playerInput);
        assertEquals("1+1", model.listToString(playerInput));
        //测试输入为等号
        model.handleInput("=", playerInput);
        assertEquals("1+1=", model.listToString(playerInput));
        //测试输入为删除符
        model.handleInput("d", playerInput);
        assertEquals("1+1", model.listToString(playerInput));
    }

    @Test
    void TestIsHaveOperationSymbol() {
        //测试是否有运算符
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertFalse(model.isHaveOperationSymbol(playerInput.toString()));
        model.handleInput("+", playerInput);
        assertTrue(model.isHaveOperationSymbol(playerInput.toString()));
    }

    @Test
    void isHaveEqualSymbol() {
        //测试是否有等号
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertFalse(model.isHaveEqualSymbol(playerInput.toString()));
        model.handleInput("=", playerInput);
        assertTrue(model.isHaveEqualSymbol(playerInput.toString()));
    }

    @Test
    void isFirstInputAndOperator() {
        //测试是否第一个输入为运算符
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("+", playerInput);
        assertTrue(model.isFirstInputAndOperator('+', playerInput));
        model.handleInput("1", playerInput);
        assertFalse(model.isFirstInputAndOperator('1', playerInput));
    }

    @Test
    void isOperator() {
        //测试是否为运算符
        NumberleModel model = new NumberleModel();
        assertTrue(model.isOperator('+'));
        assertFalse(model.isOperator('1'));
    }

    @Test
    void isConsecutiveOperator() {
        //测试是否连续输入运算符
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("+", playerInput);
        assertFalse(model.isConsecutiveOperator('+', playerInput));
        model.handleInput("+", playerInput);
        assertFalse(model.isConsecutiveOperator('+', playerInput));
    }

    @Test
    void listToString() {
        //测试list转string
        NumberleModel model = new NumberleModel();
        List<Character> playerInput = new ArrayList<>();
        model.handleInput("1", playerInput);
        assertEquals("1", model.listToString(playerInput));
        model.handleInput("+", playerInput);
        assertEquals("1+", model.listToString(playerInput));
    }

    @Test
    void compareEquations() {
        //测试比较等式
        NumberleModel model = new NumberleModel();
        assertEquals("Win", model.compareEquations("1+1+1=3", "1+1+1=3"));
        assertEquals("Continue", model.compareEquations("1+1+2=4", "1+1+1=3"));
    }

    @Test
    void setGameOver() {
        //测试设置游戏结束
        NumberleModel model = new NumberleModel();
        model.setGameOver(true);
        assertTrue(model.gameIsOver);
    }

    @Test
    void isValidEquation() {
        //测试是否为合法等式
        NumberleModel model = new NumberleModel();
        assertTrue(model.isValidEquation("1+1=2"));
        assertFalse(model.isValidEquation("1+1=2+"));
    }

    @Test
    void evaluate() {
        //测试计算等式
        NumberleModel model = new NumberleModel();
        assertEquals(2, model.evaluate("1+1"));
        assertEquals(3, model.evaluate("1+1+1"));
    }

    @Test
    void incrementAttempts() {
        //测试增加尝试次数
        NumberleModel model1 = new NumberleModel();
        model1.incrementAttempts();
        assertEquals(1, model1.attempts);
    }


    @Test
    void arrayToString() {
        //测试数组转字符串
        NumberleModel model = new NumberleModel();
        String[] arr = {"1", "+", "1", "=", "2"};
        assertEquals("1+1=2", model.arrayToString(arr));

    }

    @Test
    void restartGame() {
        //测试重新开始游戏
        NumberleModel model = new NumberleModel();
        model.restartGame();
        assertEquals(0, model.attempts);
    }

    @Test
    void setDefaultEquation() {
        //测试设置默认等式
        NumberleModel model = new NumberleModel();
        model.setDefaultEquation();
        assertEquals("1+1+1=3", model.targetEquation);
    }
}