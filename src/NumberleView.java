import java.util.List;
import java.util.Set;

public class NumberleView {

    public static void displayWelcomeMessage() {
        System.out.println("欢迎来到Numberle！");
    }
    public static void displayLoadSuccess() {
        System.out.println("谜底等式已选定！是否需要展示？(y/n)");
    }
    public static void displayLoadError(Exception e) {
        System.out.println("加载失败：" + e);
    }

    public static void displayCurrentInput(String input) {
        System.out.println("当前输入：" + input);
    }
    public static void displayAttempts(int attempts) {
        System.out.println("当前为第 " + (attempts+1) + " 次猜测，你还有 " + (6 - attempts-1) + " 次机会。");
    }
    public static void displayTargetEquation(String targetEquation) {
        System.out.println("目标等式：" + targetEquation);
    }
    public static void displayAskForApply(List<Character> playerInput){
        System.out.println("当前输入：" + NumberleModel.listToString(playerInput) + "。是否提交此答案? (y/n)");
    }

    public static void displayPrompt() {
        System.out.println("请输入单个字符继续编辑或输入'd'删除最后一个值：");
    }

    public static void displayInvalidInputMessage() {
        System.out.println("无效输入，请确保输入合法字符，第一位输入不能为符号，且符号之间不相连。");
    }
    public static void displayInvalidEquationMessage() {
        System.out.println("输入的等式不符合数学规则，请重新输入。");
    }
    public static void displayMaxAttemptsMessage() {
        System.out.println("你已达到最大尝试次数。游戏结束。");
    }

    //打印完全错误的值
    public static void displayCompleteIncorrectValues( Set<Character> incorrectValues) {
        System.out.println("完全错误的值：" + incorrectValues.toString());
    }
    public static void displayGameOver(boolean isWin, String targetEquation) {
        if (isWin) {
            System.out.println("恭喜你猜对了！游戏胜利。谜底是：" + targetEquation);
        } else {
            System.out.println("你已达到最大尝试次数。游戏结束。谜底是：" + targetEquation);
        }
    }
}
