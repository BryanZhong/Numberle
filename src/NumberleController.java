import java.util.Scanner;

public class NumberleController {
    private NumberleModel model;
    private NumberleView view;
    private Scanner scanner;

    public NumberleController(NumberleModel model, NumberleView view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("欢迎来到Numberle！");
        while (!model.isGameOver()) {
            System.out.println("当前输入：" + NumberleModel.listToString(model.playerInput));
            System.out.println("请输入单个字符继续编辑或输入'd'删除最后一个值：");
            String input = scanner.nextLine().trim();
            if ("submit".equalsIgnoreCase(input) && model.playerInput.size() == NumberleModel.MAX_LENGTH) {
                // 提交等式进行校验
                String playerEquation = NumberleModel.listToString(model.playerInput);
                NumberleModel.compareEquations(playerEquation, model.targetEquation);
                model.clearPlayerInput();
                model.incrementAttempts();
            } else {
                // 处理字符添加或删除
                NumberleModel.handleInput(input, model.playerInput);
            }
        }
        // 游戏结束后显示结果
        if (model.attempts >= 6) {
            System.out.println("恭喜你猜对了！游戏胜利。谜底是：" + NumberleModel.targetEquation);
        } else {
            System.out.println("你已达到最大尝试次数。游戏结束。谜底是：" + NumberleModel.targetEquation);
        }
    }
}
