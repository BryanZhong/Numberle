import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NumberleModel {
    public static final int MAX_LENGTH = 7;
    private static final List<Character> VALID_CHARS =
            Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '=');
    public static Set<Character> incorrectValues = new HashSet<>();
    public static String targetEquation;
    public List<Character> playerInput = new ArrayList<>();
    protected static int attempts = 0;
    private static Scanner scanner = new Scanner(System.in); // Static scanner for class-wide access
    public static ArrayList<String> history = new ArrayList<>();

    public NumberleModel() {
        //初始化incorrectValues，在游戏开始前清空，以处理多次游戏运行的情况
        incorrectValues.clear();

    }

    // 加载并选择等式
    public static String loadAndSelectEquation(String filePath) throws Exception {
        List<String> equations = Files.readAllLines(Paths.get(filePath));
        return equations.get(new Random().nextInt(equations.size()));
    }
    // 处理输入,添加或删除字符,提交等式进行校验
    public static void handleInput(String input, List<Character> playerInput) {
        if ("d".equalsIgnoreCase(input) && !playerInput.isEmpty()) {
            playerInput.remove(playerInput.size() - 1);
        } else if (input.length() == 1 && VALID_CHARS.contains(input.charAt(0))) {
            // 检查是否是第一次输入且为运算符
            if (isFirstInputAndOperator(input.charAt(0), playerInput)) {
                //NumberleView.displayInvalidInputMessage();
                System.out.println("无效输入，请确保输入合法字符，第一位输入不能为符号，且符号之间不相连。");
                // 检查是否存在连续的运算符
            } else if (isConsecutiveOperator(input.charAt(0), playerInput)) {
                //NumberleView.displayInvalidInputMessage();
                System.out.println("无效输入，请确保输入合法字符，第一位输入不能为符号，且符号之间不相连。");
            } else {
                playerInput.add(input.charAt(0));
            }
        } else {
            //NumberleView.displayInvalidInputMessage();
            System.out.println("无效输入，请确保输入合法字符，第一位输入不能为符号，且符号之间不相连。");
        }
    }
    public static boolean isFirstInputAndOperator(char input, List<Character> playerInput) {
        // 检查是否为第一个输入且为运算符
        return playerInput.isEmpty() && isOperator(input);
    }
    public static boolean isOperator(char input) {
        // 检查字符是否为运算符
        return input == '+' || input == '-' || input == '*' || input == '/'|| input == '=';
    }

    public static boolean isConsecutiveOperator(char input, List<Character> playerInput) {
        // 检查最后一个输入是否为运算符，以及当前输入是否也为运算符
        if (!playerInput.isEmpty() && isOperator(input)) {
            char lastInput = playerInput.get(playerInput.size() - 1);
            return isOperator(lastInput);
        }
        return false;
    }
    public static boolean isValidInput(char input, List<Character> currentInput) {
        return VALID_CHARS.contains(input) && currentInput.size() < MAX_LENGTH;
    }

    public static String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : list) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public static void compareEquations(String playerEq, String targetEq) {
        StringBuilder feedback = new StringBuilder();
        boolean isAllCorrect = true; // 假设玩家输入完全正确

        for (int i = 0; i < playerEq.length(); i++) {
            char pChar = playerEq.charAt(i);
            char tChar = targetEq.charAt(i);

            if (pChar == tChar) {
                feedback.append(pChar).append("位置正确内容正确 ");
            } else {
                isAllCorrect = false; // 存在错误
                if (targetEq.indexOf(pChar) != -1) {
                    feedback.append(pChar).append("内容正确位置错误 ");
                } else {
                    feedback.append(pChar).append("完全错误 ");
                    incorrectValues.add(pChar); // 将完全错误的值添加到Set中
                }
            }
        }

        System.out.println(feedback.toString()); // 打印对比反馈

        if (!incorrectValues.isEmpty()) {
            //NumberleView.displayCompleteIncorrectValues(incorrectValues);
            System.out.println("完全错误的值：" + incorrectValues.toString());
        } else {
            System.out.println("没有完全错误的值。");
        }

        if (isAllCorrect) {
            System.out.println("恭喜你猜对了！游戏胜利。");
            System.exit(0); // 游戏胜利，结束程序
        }else {
            // 在这里打印猜测次数信息
            //NumberleView.displayAttempts(attempts);
            System.out.println("当前为第 " + (attempts+1) + " 次猜测，你还有 " + (6 - attempts-1) + " 次机会。");

        }
    }



    // 解析和计算等式两边的值
    public static boolean isValidEquation(String equation) {
        String[] parts = equation.split("=");
        if (parts.length != 2) {
            return false; // 等式必须只有一个等号，并且能够分成两部分
        }

        try {
            double leftValue = evaluate(parts[0]);
            double rightValue = evaluate(parts[1]);
            return Math.abs(leftValue - rightValue) < 0.0001; // 比较两边的值是否相等，考虑到浮点数计算的精度问题
        } catch (Exception e) {
            return false; // 如果解析或计算过程中发生错误，则认为等式无效
        }
    }

    // 计算表达式的值
    public static double evaluate(String expression) {
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
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // 解析加法和减法
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // 加法
                    else if (eat('-')) x -= parseTerm(); // 减法
                    else return x;
                }
            }

            // 解析乘法和除法
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // 乘法
                    else if (eat('/')) x /= parseFactor(); // 除法
                    else return x;
                }
            }

            // 解析数字
            double parseFactor() {
                if (eat('+')) return parseFactor(); // 一元加法
                if (eat('-')) return -parseFactor(); // 一元减法

                double x;
                int startPos = this.pos;
                if (eat('(')) { // 括号
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // 数字
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                return x;
            }
        }.parse();
    }

    public static void incrementAttempts() {
        attempts++;
        if (attempts >= 6) {
            isGameOver();
        }
    }
    public static boolean isGameOver() {
        if (attempts >= 6) {
            System.out.println("很遗憾，你没有在规定次数内猜对。游戏结束。");
            System.exit(0); // 游戏失败，结束程序
        }
        return false;
    }
    public void attemptSolution(String input) {
        // 处理用户的输入
        handleInput(input, playerInput);

        // 增加尝试次数
        incrementAttempts();

        // 检查游戏是否结束
        if (isGameOver()) {
            // 如果游戏结束，显示游戏结束信息并退出
            //NumberleView.displayGameOver(false, targetEquation);
            if (attempts >= 6) {
                System.out.println("很遗憾，你没有在规定次数内猜对。游戏结束。");
                System.exit(0);
            }else {
                System.out.println("恭喜你猜对了！游戏胜利。");
                System.exit(0);
            }
            System.exit(0);
        }
    }
    public void clearPlayerInput() {
        playerInput.clear();
    }



}
