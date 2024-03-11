import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NumberleGameForCLI {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        NumberleModel model = new NumberleModel();
        try {
            System.out.println("欢迎来到Numberle！");
            System.out.println("游戏开始！是否随机选择等式？(y/n)");
            String choice = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(choice)) {
                try {
                    model.targetEquation = model.loadAndSelectEquation("equations.txt"); // 更新文件路径为实际路径
                } catch (Exception e) {
                    System.out.println("加载失败：" + e);
                }
            } else {
                model.targetEquation = "1+1+1=3";
            }
            System.out.println("谜底等式已选定！是否需要展示？(y/n)");
            if ("y".equalsIgnoreCase(scanner.next())) {
                System.out.println("目标等式：" + model.targetEquation);
            }
            Scanner scanner = new Scanner(System.in);
            List<Character> playerInput = new ArrayList<>();
            int attempts = 0;



            while (attempts <= 5) {
                System.out.println("当前输入：" + model.listToString(playerInput));
                System.out.println("请输入单个字符继续编辑或输入'd'删除最后一个值：");
                String input = scanner.next();

                // 输入处理
                model.handleInput(input, playerInput);
                // 提交并对比
                if (playerInput.size() == model.MAX_LENGTH) {
                    System.out.println("当前输入：" + model.listToString(playerInput) + "。是否提交此答案? (y/n)");
                    String decision = scanner.next();
                    if ("y".equalsIgnoreCase(decision)) {
                        if (model.isValidEquation(model.listToString(playerInput))) {
                            String result;
                            result=model.compareEquations(model.listToString(playerInput), model.targetEquation);
                            System.out.println(result);
                            if(result=="Win"){
                                System.exit(0);
                            }
                            if(result=="Lose"){
                                System.exit(0);
                            }
                            model.incrementAttempts();
                            playerInput.clear();
                        } else {
                            System.out.println("输入的等式不符合数学规则，请重新输入。");
                        }

                    }
                }
            }
            System.out.println("你已达到最大尝试次数。游戏结束。");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("加载失败：" + e);
        }
    }
}
