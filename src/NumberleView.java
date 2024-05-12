import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;
import java.util.Observable;

public class NumberleView extends JPanel implements Observer {
    private static NumberleModel model = new NumberleModel();
    private static NumberleController controller= new NumberleController(model);
    private final float aspectRatio = 1.0f; // 长宽比设定为1:1
    private final float gapRatio = 0.08f; // 间距占矩形宽度的比例
    private final int cornerRadius = 10; // 圆角半径
    private int currentRow = controller.getCurrentRow();
    private int currentCol = controller.getCurrentCol();
    private boolean isEnterPressed = false;
    static final Color COLOR_CORRECT = Color.decode("#1BB295");
    static final Color COLOR_WRONG_POSITION = Color.decode("#F79A6F");
    static final Color COLOR_INCORRECT = Color.decode("#A4AEC4");
    static java.util.List<RoundedButton> buttons = new ArrayList<>();
    private static JLabel equationLabel;
    private static JCheckBox showAnswerCheckbox = new JCheckBox("Show Answer", controller.getShowEquationFlag());
    private static JCheckBox chooseEquationCheckbox = new JCheckBox("Choose Equation", controller.getRandomEquationFlag());
    private static String[][] matrix = controller.getMatrix();
    static JFrame frame = new JFrame("Numberle");

    public NumberleView(NumberleController controller) {
        setBackground(Color.decode("#FBFCFF"));
        if (controller.model != null) {
            controller.model.addObserver(this);
        } else {
            throw new IllegalStateException("Model has not been initialized");
        }
        // 初始化矩阵为空字符串
        for (int i = 0; i < controller.getRows(); i++) {
            for (int j = 0; j < controller.getCols(); j++) {
                matrix[i][j] = "";
            }
        }
        controller.setTargetEquation("1+1+1=3");
    }

    /**
     * Custom paint component for rendering the game grid.
     * This method handles the painting of game elements on the panel.
     * It uses anti-aliasing to smooth out the edges of shapes.
     *
     * The method calculates dimensions dynamically based on the panel size,
     * accounting for the number of rows and columns in the game.
     * It renders each cell with a rounded rectangle and paints the characters
     * entered by the user, applying color coding based on their correctness.
     *
     * @param g The {@link Graphics} context used for drawing on this panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int availableWidth = (int) (panelWidth - (controller.getRows() - 1) * gapRatio * panelWidth);
        int rectWidth = (int) ((availableWidth / controller.getCols()) / (1 + gapRatio));
        int rectHeight = (int) (rectWidth * aspectRatio);
        int gap = (int) (rectWidth * gapRatio);

        int totalHeight = controller.getRows() * rectHeight + (controller.getRows() - 1) * gap;
        int startY = 0;

        int totalWidth = controller.getCols() * rectWidth + (controller.getCols() - 1) * gap;
        int startX = (panelWidth - totalWidth) / 2;

        Color originalColor = g2d.getColor();
        g2d.setColor(Color.decode("#B1B1B1"));

        for (int i = 0; i < controller.getRows(); i++) {
            for (int j = 0; j < controller.getCols(); j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);
                g2d.drawRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // Load and modify the font directly
        Font customFont = loadFont("D:/download/Montserrat,Open_Sans/Montserrat/Montserrat-VariableFont_wght.ttf", 28f).deriveFont(Font.BOLD);
        g2d.setFont(customFont);
        g2d.setColor(new Color(0x393E4C)); // Set text color
        String[][] matrix_print = new String[controller.getRows()][controller.getCols()];
        for (int i = 0; i< matrix.length; i++) {
            for (int j = 0; j< matrix[i].length; j++) {
                if (matrix[i][j].equals("*")) {
                    matrix_print[i][j] = "x";
                } else if (matrix[i][j].equals("/")) {
                    matrix_print[i][j] = "÷";
                }
                else {
                    matrix_print[i][j] = matrix[i][j];
                }
            }
        }
        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < controller.getRows(); i++) {
            for (int j = 0; j < controller.getCols(); j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);

                // 根据 feedbackMatrix 中的状态选择背景色
                Color backgroundColor = Color.decode("#FBFCFF"); // 默认背景色
                CharacterFeedback[][] feedback = controller.getFeedback();
                if (feedback[i][j] == CharacterFeedback.CORRECT) {
                    backgroundColor = Color.decode("#1BB295");
                } else if (feedback[i][j] == CharacterFeedback.WRONG_POSITION) {
                    backgroundColor = Color.decode("#F79A6F");
                } else if (feedback[i][j] == CharacterFeedback.INCORRECT) {
                    backgroundColor = Color.decode("#A4AEC4");
                }
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // 绘制边框
        g2d.setColor(Color.decode("#B1B1B1"));
        for (int i = 0; i < controller.getRows(); i++) {
            for (int j = 0; j < controller.getCols(); j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);
                g2d.drawRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // 绘制文字
        g2d.setColor(Color.decode("#393E4C")); // 文字颜色
        for (int i = 0; i < controller.getRows(); i++) {
            for (int j = 0; j < controller.getCols(); j++) {
                if (!matrix[i][j].isEmpty()) {
                    String text = matrix[i][j].replace("*", "x").replace("/", "÷");
                    int textWidth = fm.stringWidth(text);
                    int textX = startX + j * (rectWidth + gap) + (rectWidth - textWidth) / 2;
                    int textY = startY + i * (rectHeight + gap) + ((rectHeight - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
            }
        }

        g2d.setColor(originalColor);
    }
    /**
     * Responds to updates from the observed object. This method is called whenever the observed object is changed.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) return;
        switch ((String) arg) {
            case "GameRestarted":
                resetViewForNewGame();
                break;
            case "Lose":
                displayLose();
                break;
            case "Win":
                displayWin();
                break;
            case "Continue":
                displayContinue();
                break;
        }
    }

    /**
     * Displays a win message and updates the UI components to reflect the win state.
     */
    private void displayWin() {
        RoundedButton.updateButtonColors(buttons, controller.getCORRECT(), controller.getINCORRECT(), controller.getWRONG_POSITION());
        showLimitDialog("GameOver!You Win!");
    }
    /**
     * Displays a lose message and updates the UI components to reflect the lose state.
     */
    private void displayLose() {
        RoundedButton.updateButtonColors(buttons, controller.getCORRECT(), controller.getINCORRECT(), controller.getWRONG_POSITION());
        isEnterPressed = true; // 标记已经按下 Enter
        showLimitDialog("GameOver!You Lose!");
    }
    /**
     * Displays a continue message indicating the remaining chances and updates the UI to prepare for the next attempt.
     */
    private void displayContinue() {
        RoundedButton.updateButtonColors(buttons, controller.getCORRECT(), controller.getINCORRECT(), controller.getWRONG_POSITION());
        showLimitDialog("You have " + (6 - controller.model.attempts-1) + " chances left.");
        currentRow++;
        currentCol = 0;
    }
    /**
     * Resets the view for a new game by clearing the game matrix, resetting UI components, and repainting the view.
     */
    private void resetViewForNewGame() {
        // 重置视图状态
        for (int i = 0; i < controller.getRows(); i++) {
            Arrays.fill(matrix[i], "");
        }
        for (RoundedButton button : buttons) {
            button.resetColor();
        }
        showAnswerCheckbox.setSelected(controller.getShowEquationFlag());
        equationLabel.setText("");
        currentRow = 0;
        currentCol = 0;
        repaint();
        showLimitDialog("Game Restarted!");
    }

    public static void main(String[] args) {
        NumberleView view=new NumberleView(controller);
        view.initGUI();
    }
    /**
     * Initializes the main GUI of the application.
     * Sets up the layout, buttons, and other UI elements.
     */
    public void initGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#FBFCFF"));
        frame.add(topPanel, BorderLayout.NORTH);

        // 使用当前的实例添加到frame，而不是创建新的实例
        frame.add(this, BorderLayout.CENTER);
        this.setLayout(new BorderLayout());

        // 初始化等式显示标签，但先不显示任何文本
        equationLabel = new JLabel("");
        equationLabel.setHorizontalAlignment(JLabel.CENTER);
        equationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(equationLabel, BorderLayout.NORTH);

        // Replace the existing JLabel for logo with SVGPanel
        SVGPanel logoPanel = new SVGPanel("../Numberle/icon/logo1.svg");
        logoPanel.setOpaque(false); // 如果需要，可以设置为透明
        topPanel.add(logoPanel, BorderLayout.WEST);

        // Do the same for the name SVG, if applicable
        SVGPanel namePanel = new SVGPanel("../Numberle/icon/name.svg");
        topPanel.add(namePanel);

        // 创建右侧按钮面板，并使用适当的布局
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // 如果需要，可以设置为透明
        // 假设已经创建了settingsButton
        ImageToggleButton settingsButton = new ImageToggleButton("../Numberle/icon/setting.svg", "../Numberle/icon/settingSelected.svg");
        buttonPanel.add(settingsButton);
        settingsButton.addActionListener(e -> {
            showSettingsDialog(this);
        });

        // 将两个面板添加到顶部容器中
        topPanel.add(buttonPanel, BorderLayout.EAST);
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 3, 3));
        buttonsPanel.setBackground(Color.decode("#FBFCFF"));
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        int hgapForNumberButtons = 4; // 第一行按钮间的水平间距
        int hgapForOperatorButtons = 8; // 第二行按钮间的水平间距

        JPanel numberButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgapForNumberButtons, 0));
        numberButtonsPanel.setBackground(Color.decode("#FBFCFF"));
        buttonsPanel.add(numberButtonsPanel);

        JPanel operatorButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgapForOperatorButtons, 0));
        operatorButtonsPanel.setBackground(Color.decode("#FBFCFF"));
        buttonsPanel.add(operatorButtonsPanel);

        for (int i = 1; i <= 9; i++) {
            addButton(numberButtonsPanel, String.valueOf(i), 56, 56, createActionListener(String.valueOf(i)));
        }
        addButton(numberButtonsPanel, "0", 56, 56, createActionListener("0"));

        addButton(operatorButtonsPanel, "Delete", 117, 56, createActionListener("Delete"));
        String[] operators = {"+", "-", "×", "÷", "="};
        for (String operator : operators) {
            addButton(operatorButtonsPanel, operator, 63, 56, createActionListener(operator));
        }
        addButton(operatorButtonsPanel, "Enter", 117, 56, createActionListener("Enter"));

        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null); // 居中显示窗口
        frame.setVisible(true);

        // 组件大小改变监听器，触发重新绘制
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }
    /**
     * Shows the settings dialog for game configuration.
     *
     * @param view The current view context from which the dialog is called.
     */
    public void showSettingsDialog(NumberleView view) {
        JDialog settingsDialog = new JDialog();
        settingsDialog.setTitle("Settings");
        settingsDialog.setSize(300, 200); // 设置对话框大小
        settingsDialog.setLocationRelativeTo(null); // 居中显示
        settingsDialog.setLayout(new BoxLayout(settingsDialog.getContentPane(), BoxLayout.Y_AXIS)); // 使用Box布局

        settingsDialog.add(Box.createVerticalGlue()); // 添加一个垂直的弹性空间

        // 添加一个重新开始按钮
        JButton restartButton = new RoundedButton("Restart");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置按钮居中
        settingsDialog.add(restartButton);
        // 根据model.attempts的值禁用或启用重新开始按钮
        restartButton.setEnabled(controller.model.attempts != 0);
        restartButton.addActionListener(e -> {
            // 弹出一个确认对话框，确认后重新开始游戏
            UIManager.put("OptionPane.yesButtonText", "Yes");
            UIManager.put("OptionPane.noButtonText", "No");
            int result = JOptionPane.showConfirmDialog(settingsDialog, "Are you sure you want to restart the game?", "Restart Game", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                controller.restartGame();  // 调用模型的重新开始游戏方法
                settingsDialog.dispose();  // 关闭设置对话框
            }
        });

        showAnswerCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置复选框居中
        settingsDialog.add(showAnswerCheckbox);
        // 为复选框添加动作监听器
        showAnswerCheckbox.addActionListener(e -> {
            if (showAnswerCheckbox.isSelected()) {
                // 如果复选框被选中，显示等式
                controller.setShowEquationFlag(true);
                equationLabel.setText("Answer: " + controller.getTargetEquation());
                settingsDialog.dispose();
            } else {
                // 如果复选框未被选中，隐藏等式
                controller.setShowEquationFlag(false);
                equationLabel.setText("");
                settingsDialog.dispose();
            }
        });
        JCheckBox chooseShowError = new JCheckBox("Show Error Messages", controller.getShowErrorMessageFlag());
        chooseShowError.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsDialog.add(chooseShowError);
        chooseShowError.setSelected(controller.getShowErrorMessageFlag());
        chooseShowError.addActionListener(e -> {
            boolean selected = chooseShowError.isSelected();
            controller.setShowErrorMessageFlag(selected);
            if (selected) {
                controller.setShowErrorMessageFlag(true);
                System.out.println("Error messages will be shown.");
                settingsDialog.dispose();
            } else {
                controller.setShowErrorMessageFlag(false);
                System.out.println("Error messages will not be shown.");
                settingsDialog.dispose();
            }
        });
        chooseEquationCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsDialog.add(chooseEquationCheckbox);
        chooseEquationCheckbox.addActionListener(e -> {
            if (chooseEquationCheckbox.isSelected()) {
                controller.restartGame();
                controller.setRandomEquationFlag(true);
                controller.setShowErrorMessageFlag(false);
                controller.setShowEquationFlag(false);
                chooseShowError.setSelected(controller.getShowErrorMessageFlag());
                showAnswerCheckbox.setSelected(controller.getShowEquationFlag());
                settingsDialog.dispose();
            } else {
                controller.restartGame();
                controller.setRandomEquationFlag(false);
                controller.setShowErrorMessageFlag(false);
                controller.setShowEquationFlag(false);
                chooseShowError.setSelected(controller.getShowErrorMessageFlag());
                showAnswerCheckbox.setSelected(controller.getShowEquationFlag());
                settingsDialog.dispose();
            }
        });

        settingsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // 移除监听器
                removeListeners(settingsDialog);
            }
        });

        settingsDialog.setVisible(true);
        settingsDialog.add(Box.createVerticalGlue()); // 再添加一个垂直的弹性空间
    }
    /**
     * Removes action listeners from the specified dialog.
     *
     * @param dialog The dialog from which to remove action listeners.
     */
    private void removeListeners(JDialog dialog) {
        // 移除showAnswerCheckbox的监听器
        for (ActionListener al : showAnswerCheckbox.getActionListeners()) {
            showAnswerCheckbox.removeActionListener(al);
        }

        // 移除chooseEquationCheckbox的监听器
        for (ActionListener al : chooseEquationCheckbox.getActionListeners()) {
            chooseEquationCheckbox.removeActionListener(al);
        }

        // 移除chooseShowError的监听器（如果已添加到对话框）
        JCheckBox chooseShowError = findChooseShowErrorCheckBox(dialog);
        if (chooseShowError != null) {
            for (ActionListener al : chooseShowError.getActionListeners()) {
                chooseShowError.removeActionListener(al);
            }
        }
    }
    /**
     * Finds the "Show Error Messages" checkbox in the specified container.
     *
     * @param container The container to search for the checkbox.
     * @return The "Show Error Messages" checkbox if found, or {@code null} if not found.
     */
    private JCheckBox findChooseShowErrorCheckBox(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JCheckBox && ((JCheckBox)comp).getText().equals("Show Error Messages")) {
                return (JCheckBox) comp;
            } else if (comp instanceof Container) {
                JCheckBox checkBox = findChooseShowErrorCheckBox((Container) comp);
                if (checkBox != null) {
                    return checkBox;
                }
            }
        }
        return null;
    }
    /**
     * Loads a font from the specified file path and size.
     *
     * @param path The file path of the font to load.
     * @param size The size of the font to create.
     * @return The loaded font, or a default font if the file cannot be loaded.
     */
    private static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new JButton().getFont();
        }
    }
    /**
     * Adds a button to the specified panel with the given label, width, height, and action listener.
     *
     * @param panel           The panel to which the button will be added.
     * @param label           The label text of the button.
     * @param width           The width of the button.
     * @param height          The height of the button.
     * @param actionListener  The action listener to be added to the button.
     */
    private static void addButton(JPanel panel, String label, int width, int height, ActionListener actionListener) {
        RoundedButton button = new RoundedButton(label);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(actionListener);
        panel.add(button);
        buttons.add(button);
    }
    /**
     * Creates an action listener for the specified label.
     *
     * @param label The label text for the action listener.
     * @return The action listener for the specified label.
     */
    private ActionListener createActionListener(String label) {
        return e -> {
            if (controller.isGameOver()==true) {
                return; // 直接返回，不执行任何操作
            }
            String transformedLabel = label;
            // 将UI使用的符号转换为Java识别的符号
            if ("×".equals(label)) {
                transformedLabel = "*";
            } else if ("÷".equals(label)) {
                transformedLabel = "/";
            }
            if ("Delete".equals(transformedLabel)) {
                if (currentCol > 0) {
                    matrix =controller.getMatrix();
                    matrix[currentRow][--currentCol] = ""; // 删除当前行的最后一个字符
                    isEnterPressed = false; // 重置 Enter 标志
                } else if (currentRow > 0 && !isEnterPressed) {
                    // 如果当前列为 0，且不在第一行，且未按下 Enter 移动到这一行，不执行删除
                    showLimitDialog("Cannot delete previous row!");
                }
            } else if ("Enter".equals(transformedLabel)) {
                if (currentCol == controller.getCols()) { // 如果当前行已满
                    if (currentRow < controller.getRows() - 1) { // 并且不是最后一行
                        if (!controller.isHaveOperationSymbol(controller.arrayToString(matrix[currentRow]))) {
                            // 如果不包含运算符，显示错误提示并让用户修改输入
                            if(controller.getShowErrorMessageFlag()){
                                showLimitDialog(controller.getErrorMassage());
                                controller.setErrorMessage("");
                            }
                            return; // 直接返回，不提交等式
                        }
                        if (!controller.isHaveEqualSymbol(controller.arrayToString(matrix[currentRow]))) {
                            // 如果不包含等号，显示错误提示并让用户修改输入
                            if(controller.getShowErrorMessageFlag()){
                                showLimitDialog(controller.getErrorMassage());
                                controller.setErrorMessage("");
                            }
                            return; // 直接返回，不提交等式
                        }
                        if ((controller.isValidEquation(controller.arrayToString(matrix[currentRow]))) == true) {
                            controller.compareEquations(controller.arrayToString(matrix[currentRow]), controller.getTargetEquation());
                        } else {
                            if(controller.getShowErrorMessageFlag()){
                                showLimitDialog(controller.getErrorMassage());
                                controller.setErrorMessage("");
                            }
                        }
                    } else {
                        if ((controller.isValidEquation(controller.arrayToString(matrix[currentRow]))) == true) {
                            controller.compareEquations(controller.arrayToString(matrix[currentRow]), controller.getTargetEquation());
                            controller.incrementAttempts();
                            isEnterPressed = true; // 标记已经按下 Enter
                        } else {
                            if(controller.getShowErrorMessageFlag()){
                                showLimitDialog(controller.getErrorMassage());
                                controller.setErrorMessage("");
                            }
                        }
                    }
                } else {
                    // 如果当前行未满且已有输入，显示输入过短的提示
                    if(controller.getShowErrorMessageFlag()){
                        showLimitDialog("Too short input!");
                        controller.setErrorMessage("");
                    }
                }
            } else {
                if (currentCol < controller.getCols()) { // 如果当前行未满
                    matrix[currentRow][currentCol++] = transformedLabel; // 添加输入到当前位置
                    isEnterPressed = false; // 重置 Enter 标志
                } else {
                    if(controller.getShowErrorMessageFlag()){
                        showLimitDialog(controller.getErrorMassage());
                        controller.setErrorMessage("");
                    }
                }
            }
            repaint(); // 重绘以显示更新
        };
    }

    /**
     * Displays a dialog with the specified message and a rounded rectangle background.
     *
     * @param message The message to display in the dialog.
     */
    private void showLimitDialog(String message) {
        JDialog dialog = new JDialog((Frame) null, true);
        dialog.setSize(300, 100);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // 创建带有圆角的面板
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#FFFFFF"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setLayout(new BorderLayout());
        dialog.add(panel);

        // 使用与按钮相同的字体风格和颜色
        Font font = loadFont("D:/download/Montserrat,Open_Sans/Montserrat/Montserrat-VariableFont_wght.ttf", 20f).deriveFont(Font.BOLD);
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(font);
        label.setForeground(new Color(0x393E4C)); // 设置文本颜色
        panel.add(label);

        // 设置关闭弹窗的定时器
        new Timer(1500, e -> dialog.dispose()).start();

        dialog.setVisible(true);
    }
}


class RoundedButton extends JButton {
    private static final int ARC_WIDTH = 10;
    private static final int ARC_HEIGHT = 10;
    private static final Color BACKGROUND_COLOR = Color.decode("#DCE1ED");
    private static final Color FONT_COLOR = new Color(0x393E4C); // 更新为稍深的颜色
    private static final Color HOVER_COLOR = Color.GRAY;
    private static final Color PRESSED_COLOR = Color.decode("#4B5464");
    private boolean colorSetByGame = false;
    public RoundedButton(String text) {
        super(text);
        // 更新setFont调用以包含Font.BOLD
        Font font = loadFont("D:/download/Montserrat,Open_Sans/Montserrat/Montserrat-VariableFont_wght.ttf", 20f).deriveFont(Font.BOLD);
        setFont(font);
        setForeground(FONT_COLOR); // 使用新的字体颜色
        setBackground(BACKGROUND_COLOR);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);

        // 鼠标悬停效果
        addMouseListener(new MouseAdapter() {
            /**
             * Invoked when the mouse enters the component.
             *
             * @param evt The {@link MouseEvent} associated with this event.
             */
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!getModel().isPressed() && !colorSetByGame) {
                    setBackground(HOVER_COLOR);
                }
            }
            /**
             * Invoked when the mouse exits the component.
             *
             * @param evt The {@link MouseEvent} associated with this event.
             */
            @Override
            public void mouseExited(MouseEvent evt) {
                if (!getModel().isPressed() && !colorSetByGame) {
                    setBackground(BACKGROUND_COLOR);
                }
            }
            /**
             * Invoked when a mouse button has been pressed on a component.
             *
             * @param evt The {@link MouseEvent} associated with this event.
             */
            @Override
            public void mousePressed(MouseEvent evt) {
                if (!colorSetByGame) {
                    setBackground(PRESSED_COLOR);
                }
            }
            /**
             * Invoked when a mouse button has been released on a component.
             *
             * @param evt The {@link MouseEvent} associated with this event.
             */
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (!colorSetByGame) {
                    setBackground(getBounds().contains(evt.getPoint()) ? HOVER_COLOR : BACKGROUND_COLOR);
                }
            }
        });
    }
    /**
     * Sets the background color of the button based on the game feedback.
     *
     * @param color The color to set as the background.
     */
    public void setColorByGame(Color color) {
        setBackground(color);
        colorSetByGame = true;
    }
    /**
     * Resets the background color of the button to the default color.
     */
    public void resetColor() {
        setBackground(BACKGROUND_COLOR);
        colorSetByGame = false;
    }
    /**
     * Paints the component with rounded corners.
     *
     * @param g The {@link Graphics} context used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT));
        super.paintComponent(g);
    }
    /**
     * updates the colors of the buttons based on the feedback from the game.
     *
     * @param buttons The size of the font to create.
     * @param CORRECT The file path of the font to load.
     * @param INCORRECT The list of buttons to update.
     * @param WRONG_POSITION The list of buttons to update.
     */
    public static void updateButtonColors(java.util.List<RoundedButton> buttons, ArrayList<String> CORRECT, ArrayList<String> INCORRECT, ArrayList<String> WRONG_POSITION) {
        // 遍历所有按钮，根据它们的标签更新背景颜色
        for (RoundedButton button : buttons) {
            String buttonText = button.getText();
            if ("×".equals(buttonText)) {
                buttonText = "*";
            } else if ("÷".equals(buttonText)) {
                buttonText = "/";
            }
            if (CORRECT.contains(buttonText)) {
                button.setColorByGame(NumberleView.COLOR_CORRECT);
            } else if (WRONG_POSITION.contains(buttonText)) {
                button.setColorByGame(NumberleView.COLOR_WRONG_POSITION);
            } else if (INCORRECT.contains(buttonText)) {
                button.setColorByGame(NumberleView.COLOR_INCORRECT);
            } // 不涉及到的按钮不做改变，所以这里没有else分支
            button.repaint();
        }

    }

    /**
     * Loads a font from the specified file path and size.
     *
     * @param path The file path of the font to load.
     * @param size The size of the font to create.
     * @return The loaded font, or a default font if the file cannot be loaded.
     */
    private static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new JButton().getFont();
        }
    }
    /**
     * Returns the preferred size of the button.
     *
     * @return The preferred size of the button.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 56);
    }
}


class ImageToggleButton extends JButton {
    private SVGDiagram imageDefault;
    private SVGDiagram imageSelected;
    private static final int ARC_WIDTH = 10;
    private static final int ARC_HEIGHT = 10;
    private static final Color BACKGROUND_COLOR = Color.decode("#DCE1ED");
    private static final Color HOVER_COLOR = Color.decode("#C6ECE4");
    private static final Color PRESSED_COLOR = Color.decode("#4B5464");
    private final SVGUniverse svgUniverse = new SVGUniverse();

    public ImageToggleButton(String imgDefaultPath, String imgSelectedPath) {
        try {
            URI uriDefault = svgUniverse.loadSVG(new File(imgDefaultPath).toURI().toURL());
            URI uriSelected = svgUniverse.loadSVG(new File(imgSelectedPath).toURI().toURL());
            imageDefault = svgUniverse.getDiagram(uriDefault);
            imageSelected = svgUniverse.getDiagram(uriSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(28, 28));
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setBackground(BACKGROUND_COLOR);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BACKGROUND_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(PRESSED_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getBounds().contains(e.getPoint())) {
                    setBackground(HOVER_COLOR);
                } else {
                    setBackground(BACKGROUND_COLOR);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT));

        SVGDiagram img = getModel().isPressed() || getModel().isRollover() ? imageSelected : imageDefault;
        if (img != null) {
            try {
                img.setIgnoringClipHeuristic(true);
                img.render(g2d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


class SVGPanel extends JPanel {
    private SVGDiagram svgDiagram = null;

    public SVGPanel(String svgPath) {
        setBackground(null); // Set background to null for transparency
        setOpaque(false); // Make panel transparent
        try {
            SVGUniverse svgUniverse = new SVGUniverse();
            URI uri = svgUniverse.loadSVG(new File(svgPath).toURI().toURL());
            svgDiagram = svgUniverse.getDiagram(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (svgDiagram != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                svgDiagram.render(g2d);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                g2d.dispose();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (svgDiagram != null) {
            // 确保返回足够大的尺寸以显示整个 SVG 图像
            return new Dimension(Math.max((int) svgDiagram.getWidth(), 100), Math.max((int) svgDiagram.getHeight(), 100));
        } else {
            // SVG 未加载时的默认尺寸
            return new Dimension(100, 100); // 根据需要调整
        }
    }

}
