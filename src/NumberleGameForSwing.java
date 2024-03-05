public class NumberleGameForSwing {
    public static void main(String[] args) {
        NumberleModel model = new NumberleModel();
        NumberleView view = new NumberleView();
        NumberleController controller = new NumberleController(model, view);

        controller.startGame();
    }
}
