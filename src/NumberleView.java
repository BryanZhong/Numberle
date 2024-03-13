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


public class NumberleView extends JPanel {
    static NumberleModel model = new NumberleModel();
    private static final int rows = 6;
    private static final int cols = 7;
    private final float aspectRatio = 1.0f; // 长宽比设定为1:1
    private final float gapRatio = 0.08f; // 间距占矩形宽度的比例
    private final int cornerRadius = 10; // 圆角半径
    private static String[][] matrix = new String[rows][cols];
    private int currentRow = 0;
    private int currentCol = 0;
    private boolean isEnterPressed = false;
    static final Color COLOR_CORRECT = Color.decode("#1BB295");
    static final Color COLOR_WRONG_POSITION = Color.decode("#F79A6F");
    static final Color COLOR_INCORRECT = Color.decode("#A4AEC4");
    static java.util.List<RoundedButton> buttons = new ArrayList<>();
    private static JLabel equationLabel;
    private static JCheckBox showAnswerCheckbox = new JCheckBox("Show Answer", false);

    static JFrame frame = new JFrame("Numberle");
    public NumberleView() {
        setBackground(Color.decode("#FBFCFF"));
        // 初始化矩阵为空字符串
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = "";
            }
        }
        try {
            model.targetEquation = model.loadAndSelectEquation("equations.txt");
            System.out.println("目标等式：" + model.targetEquation);
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            JOptionPane.showMessageDialog(null, "Failed to load equations: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int availableWidth = (int) (panelWidth - (cols - 1) * gapRatio * panelWidth);
        int rectWidth = (int) ((availableWidth / cols) / (1 + gapRatio));
        int rectHeight = (int) (rectWidth * aspectRatio);
        int gap = (int) (rectWidth * gapRatio);

        int totalHeight = rows * rectHeight + (rows - 1) * gap;
        int startY = 0;

        int totalWidth = cols * rectWidth + (cols - 1) * gap;
        int startX = (panelWidth - totalWidth) / 2;

        Color originalColor = g2d.getColor();
        g2d.setColor(Color.decode("#B1B1B1"));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);
                g2d.drawRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // Load and modify the font directly
        Font customFont = loadFont("D:/download/Montserrat,Open_Sans/Montserrat/Montserrat-VariableFont_wght.ttf", 28f).deriveFont(Font.BOLD);
        g2d.setFont(customFont);
        g2d.setColor(new Color(0x393E4C)); // Set text color
        String[][] matrix_print = new String[rows][cols];
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);

                // 根据 feedbackMatrix 中的状态选择背景色
                Color backgroundColor = Color.decode("#FBFCFF"); // 默认背景色
                if (model.feedbackMatrix[i][j] == CharacterFeedback.CORRECT) {
                    backgroundColor = Color.decode("#1BB295");
                } else if (model.feedbackMatrix[i][j] == CharacterFeedback.WRONG_POSITION) {
                    backgroundColor = Color.decode("#F79A6F");
                } else if (model.feedbackMatrix[i][j] == CharacterFeedback.INCORRECT) {
                    backgroundColor = Color.decode("#A4AEC4");
                }
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // 绘制边框
        g2d.setColor(Color.decode("#B1B1B1"));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);
                g2d.drawRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }

        // 绘制文字
        g2d.setColor(Color.decode("#393E4C")); // 文字颜色
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
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


    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#FBFCFF"));
        frame.add(topPanel, BorderLayout.NORTH);
        NumberleView panel = new NumberleView();
        frame.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout()); // 设置布局管理器为 BorderLayout
// 初始化等式显示标签，但先不显示任何文本
        equationLabel = new JLabel("");
        equationLabel.setHorizontalAlignment(JLabel.CENTER);
        equationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        topPanel.add(equationLabel, BorderLayout.NORTH); // 将等式标签添加到顶部面板
        frame.add(topPanel, BorderLayout.NORTH);

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
            showSettingsDialog(panel);
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
            addButton(numberButtonsPanel, String.valueOf(i), 56, 56, panel.createActionListener(String.valueOf(i)));
        }
        addButton(numberButtonsPanel, "0", 56, 56, panel.createActionListener("0"));

        addButton(operatorButtonsPanel, "Delete", 117, 56, panel.createActionListener("Delete"));
        String[] operators = {"+", "-", "×", "÷", "="};
        for (String operator : operators) {
            addButton(operatorButtonsPanel, operator, 63, 56, panel.createActionListener(operator)); // 使用您指定的运算符按钮大小
        }
        addButton(operatorButtonsPanel, "Enter", 117, 56, panel.createActionListener("Enter"));

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // 居中显示窗口
        frame.setVisible(true);

        // 组件大小改变监听器，触发重新绘制
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.repaint();
            }
        });
    }
    public static void showSettingsDialog(NumberleView view) {
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
        restartButton.addActionListener(e -> {
            // 弹出一个确认对话框，确认后重新开始游戏
            int result = JOptionPane.showConfirmDialog(settingsDialog, "Are you sure you want to restart the game?", "Restart Game", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] = "";
                    }
                }
                for (RoundedButton button : buttons) {
                    button.resetColor();
                }
                showAnswerCheckbox.setSelected(false);
                equationLabel.setText("");
                view.currentRow = 0;
                view.currentCol = 0;
                model.restartGame(); // 重置游戏逻辑
                settingsDialog.dispose(); // 关闭设置对话框
                NumberleView.frame.repaint(); // 重新绘制界面
                view.showLimitDialog("Game Restarted!");
            }
        });

        // 添加第二个按钮，例如“展示答案”的复选框

        showAnswerCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置复选框居中
        settingsDialog.add(showAnswerCheckbox);

        settingsDialog.add(Box.createVerticalGlue()); // 再添加一个垂直的弹性空间

        settingsDialog.setVisible(true);
        // 为复选框添加动作监听器
        showAnswerCheckbox.addActionListener(e -> {
            if (showAnswerCheckbox.isSelected()) {
                // 如果复选框被选中，显示等式
                view.equationLabel.setText("Answer: " + model.targetEquation);
            } else {
                // 如果复选框未被选中，隐藏等式
                view.equationLabel.setText("");
            }
        });
    }

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

    private static void addButton(JPanel panel, String label, int width, int height, ActionListener actionListener) {
        RoundedButton button = new RoundedButton(label);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(actionListener);
        panel.add(button);
        buttons.add(button);
    }

    private ActionListener createActionListener(String label) {
        return e -> {
            if (NumberleModel.gameIsOver) {
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
                    matrix[currentRow][--currentCol] = ""; // 删除当前行的最后一个字符
                    isEnterPressed = false; // 重置 Enter 标志
                } else if (currentRow > 0 && !isEnterPressed) {
                    // 如果当前列为 0，且不在第一行，且未按下 Enter 移动到这一行，不执行删除
                    showLimitDialog("Cannot delete previous row!");
                }
            } else if ("Enter".equals(transformedLabel)) {
                if (currentCol == cols) { // 如果当前行已满
                    if (currentRow < rows - 1) { // 并且不是最后一行
                        if (!NumberleModel.isHaveOperationSymbol(model.arrayToString(matrix[currentRow]))) {
                            // 如果不包含运算符，显示错误提示并让用户修改输入
                            showLimitDialog("Missing Operator!");
                            return; // 直接返回，不提交等式
                        }
                        if (!NumberleModel.isHaveEqualSymbol(model.arrayToString(matrix[currentRow]))) {
                            // 如果不包含等号，显示错误提示并让用户修改输入
                            showLimitDialog("No Equal\'=\' Sign!");
                            return; // 直接返回，不提交等式
                        }
                        if ((model.isValidEquation(model.arrayToString(matrix[currentRow]))) == true) {
                            String result=model.compareEquations(NumberleModel.arrayToString(matrix[currentRow]), model.targetEquation);
                            if (result=="Win") {
                                RoundedButton.updateButtonColors(buttons, model.CORRECT, model.INCORRECT, model.WRONG_POSITION);
                                showLimitDialog("GameOver!You Win!");
                            } else if (result=="Continue") {
                                model.incrementAttempts();
                                RoundedButton.updateButtonColors(buttons, model.CORRECT, model.INCORRECT, model.WRONG_POSITION);
                                showLimitDialog("You have " + (6 - model.attempts) + " chances left.");
                                currentRow++;
                                currentCol = 0;
                            } else {
                                RoundedButton.updateButtonColors(buttons, model.CORRECT, model.INCORRECT, model.WRONG_POSITION);
                                model.incrementAttempts();
                                isEnterPressed = true; // 标记已经按下 Enter
                            }
                        } else {
                            showLimitDialog("Invalid Equation!");
                        }

                    } else {
                            if ((model.isValidEquation(model.arrayToString(matrix[currentRow]))) == true) {
                                String result = model.compareEquations(NumberleModel.arrayToString(matrix[currentRow]), model.targetEquation);
                                model.incrementAttempts();
                                isEnterPressed = true; // 标记已经按下 Enter
                                if(result=="Win"){
                                    showLimitDialog("GameOver!You Win!");
                                } else {
                                        showLimitDialog("GameOver!You Lose!");
                                }
                            } else {
                                showLimitDialog("Invalid Equation!");
                            }
                    }
                } else {
                    // 如果当前行未满且已有输入，显示输入过短的提示
                    showLimitDialog("Too Short!");
                }
            } else {
                if (currentCol < cols) { // 如果当前行未满
                    matrix[currentRow][currentCol++] = transformedLabel; // 添加输入到当前位置
                    isEnterPressed = false; // 重置 Enter 标志
                } else {
                    showLimitDialog("Input limit reached"); // 当前行已满，显示限制对话框
                }
            }
            repaint(); // 重绘以显示更新
        };
    }


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
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!getModel().isPressed() && !colorSetByGame) {
                    setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!getModel().isPressed() && !colorSetByGame) {
                    setBackground(BACKGROUND_COLOR);
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (!colorSetByGame) {
                    setBackground(PRESSED_COLOR);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (!colorSetByGame) {
                    setBackground(getBounds().contains(evt.getPoint()) ? HOVER_COLOR : BACKGROUND_COLOR);
                }
            }
        });
    }
    public void setColorByGame(Color color) {
        setBackground(color);
        colorSetByGame = true;
    }
    public void resetColor() {
        setBackground(BACKGROUND_COLOR);
        colorSetByGame = false;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT));
        super.paintComponent(g);
    }
    public static void updateButtonColors(java.util.List<RoundedButton> buttons, ArrayList<String> CORRECT, ArrayList<String> INCORRECT, ArrayList<String> WRONG_POSITION) {
        // 遍历所有按钮，根据它们的标签更新背景颜色
        for (RoundedButton button : buttons) {
            String buttonText = button.getText();
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
