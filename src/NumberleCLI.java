import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NumberleCLI {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        NumberleModel model = new NumberleModel();
        try {
            System.out.println("Welcome to Numberle!");
            System.out.println("Game Start! Is it necessary to show the puzzle? (y/n)");
            String choice1 = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(choice1)) {
                model.setShowEquationFlag(true);
                System.out.println("Target Equation:" + model.targetEquation);
            }else {
                model.setShowEquationFlag(false);
            }
            System.out.println("Is the equation chosen randomly? (y/n)");
            String choice = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(choice)) {
                model.setRandomEquationFlag(true);
            } else {
                model.setRandomEquationFlag(false);
            }

            System.out.println("Is it necessary to display an error message? (y/n)");
            if ("y".equalsIgnoreCase(scanner.next())) {
                model.setShowErrorMessageFlag(true);}
            else {
                model.setShowErrorMessageFlag(false);
            }
            List<Character> playerInput = new ArrayList<>();
            int attempts = 0;
            while (attempts <= 5) {
                System.out.println("Current Input:" + model.listToString(playerInput));
                System.out.println("Please enter a single character to continue editing or type 'd' to delete the last value:");
                String input = scanner.next();
                // 输入处理
                model.handleInput(input, playerInput);
                if(model.getShowErrorMessageFlag()==true){
                    if(model.getShowErrorMessageFlag()==true&& model.getErrorMessage()!=null){
                        System.out.println(model.getErrorMessage());
                        model.setErrorMessage("");
                    }
                }
                // 提交并对比
                if (playerInput.size() == model.MAX_LENGTH) {
                    System.out.println("Current Input:" + model.listToString(playerInput) + ". Is this answer submitted? (y/n)");
                    String decision = scanner.next();
                    if ("y".equalsIgnoreCase(decision)) {
                        if (model.isValidEquation(model.listToString(playerInput))) {
                            String result;
                            result=model.compareEquations(model.listToString(playerInput), model.targetEquation);
                            System.out.println(attempts);
                            System.out.println(result);
                            if(result=="Win"){
                                System.exit(0);
                            }
                            if(result=="Lose"){
                                System.exit(0);
                            }
                            playerInput.clear();
                        } else {
                            System.out.println("The equation entered does not follow the rules of mathematics, please re-enter it.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load:" + e);
        }
    }
}
