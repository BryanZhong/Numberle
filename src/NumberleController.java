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
        view.displayWelcomeMessage();
        while (!model.isGameOver()) {
            view.displayCurrentInput(NumberleModel.listToString(model.playerInput));
            view.displayPrompt();
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
        view.displayGameOver(false, model.targetEquation);
    }
}
