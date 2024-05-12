public class NumberleGUI {
    public static void main(String[] args) {
        NumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        NumberleView view = new NumberleView(controller);
    }
}
