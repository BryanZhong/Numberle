import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Numberle {
    private static final int MAX_LENGTH = 7; // 最大长度限制
    private static Set<Character> incorrectValues = new HashSet<>();// 存储完全错误的值
    private static final List<Character> VALID_CHARS = List.of('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '=');

    public static void main(String[] args) {
        //初始化incorrectValues，在游戏开始前清空，以处理多次游戏运行的情况
        incorrectValues.clear();
        try {
            String targetEq = loadAndSelectEquation("C:\\Users\\m1355\\Desktop\\equations.txt"); // 更新文件路径为实际路径
            System.out.println("谜底等式已选定。");

            Scanner scanner = new Scanner(System.in);
            List<Character> playerInput = new ArrayList<>();
            int attempts = 0;

            System.out.println("欢迎来到Numberle！");

            while (attempts < 6) {
                System.out.println("当前输入：" + listToString(playerInput));
                System.out.print("请输入单个字符继续编辑或输入'd'删除最后一个值：");
                String input = scanner.next();

                // 输入处理
                handleInput(input, playerInput);

                // 提交并对比
                if (playerInput.size() == MAX_LENGTH) {
                    System.out.println("当前输入：" + listToString(playerInput) + "。是否提交此答案? (y/n)");
                    String decision = scanner.next();
                    if ("y".equalsIgnoreCase(decision)) {
                        if (isValidEquation(listToString(playerInput))) {
                            compareEquations(listToString(playerInput), targetEq);
                            attempts++;
                            playerInput.clear();
                        } else {
                            System.out.println("输入的等式不符合数学规则，请重新输入。");
                        }
                    }
                }
            }
            System.out.println("你已达到最大尝试次数。游戏结束。谜底是：" + targetEq);
        } catch (Exception e) {
            System.err.println("游戏加载错误：" + e.getMessage());
        }
    }

    private static String loadAndSelectEquation(String filePath) throws Exception {
        List<String> equations = Files.readAllLines(Paths.get(filePath));
        return equations.get(new Random().nextInt(equations.size()));
    }

    private static void handleInput(String input, List<Character> playerInput) {
        if ("d".equalsIgnoreCase(input) && !playerInput.isEmpty()) {
            playerInput.remove(playerInput.size() - 1);
        } else if (input.length() == 1 && isValidInput(input.charAt(0), playerInput)) {
            playerInput.add(input.charAt(0));
        } else {
            System.out.println("无效输入，请确保输入合法字符，且符号之间不相连。");
        }
    }

    private static boolean isValidInput(char input, List<Character> currentInput) {
        return VALID_CHARS.contains(input) && currentInput.size() < MAX_LENGTH;
    }

    private static String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : list) {
            sb.append(ch);
        }
        return sb.toString();
    }

    private static void compareEquations(String playerEq, String targetEq) {
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
            System.out.println("完全不正确的值:");
            incorrectValues.forEach(value -> System.out.println("- " + value));
        } else {
            System.out.println("没有完全错误的值。");
        }

        if (isAllCorrect) {
            System.out.println("恭喜你猜对了！游戏胜利。");
            System.exit(0); // 游戏胜利，结束程序
        }
    }



    // 解析和计算等式两边的值
    private static boolean isValidEquation(String equation) {
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
    private static double evaluate(String expression) {
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
}
