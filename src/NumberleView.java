import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class NumberleView extends JPanel {
    NumberleModel model = new NumberleModel();
    private final int rows = 6;
    private final int cols = 7;
    private final float aspectRatio = 1.0f; // 长宽比设定为1:1
    private final float gapRatio = 0.08f; // 间距占矩形宽度的比例
    private final int cornerRadius = 10; // 圆角半径
    private final String[][] matrix = new String[rows][cols];
    private int currentRow = 0;
    private int currentCol = 0;
    private boolean isEnterPressed = false;
    public NumberleView() {
        setBackground(Color.decode("#FBFCFF"));
        // 初始化矩阵为空字符串
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = "";
            }
        }
        try {
            model.targetEquation = model.loadAndSelectEquation("C:\\Users\\m1355\\Desktop\\equations.txt");
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

        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!matrix[i][j].isEmpty()) {
                    int textWidth = fm.stringWidth(matrix[i][j]);
                    int textX = startX + j * (rectWidth + gap) + (rectWidth - textWidth) / 2;
                    int textY = startY + i * (rectHeight + gap) + ((rectHeight - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(matrix[i][j], textX, textY);
                }
            }
        }

        g2d.setColor(originalColor);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Numberle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        NumberleView panel = new NumberleView();
        frame.add(panel, BorderLayout.CENTER);

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
    }
    private ActionListener createActionListener(String label) {
        return e -> {
            if ("Delete".equals(label)) {
                if (currentCol > 0) {
                    matrix[currentRow][--currentCol] = ""; // 删除当前行的最后一个字符
                    isEnterPressed = false; // 重置 Enter 标志
                } else if (currentRow > 0 && !isEnterPressed) {
                    // 如果当前列为 0，且不在第一行，且未按下 Enter 移动到这一行，不执行删除
                    showLimitDialog("Cannot delete previous row!");
                }
            } else if ("Enter".equals(label)) {
                if (currentCol == cols) { // 如果当前行已满
                    if (currentRow < rows - 1) { // 并且不是最后一行
                        currentRow++;
                        currentCol = 0;
                        if((model.isValidEquation(model.arrayToString(matrix[currentRow - 1])))==true) {
                        model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation);
                        model.incrementAttempts();
                        isEnterPressed = true; // 标记已经按下 Enter
                        }else {
                            currentRow--;
                            currentCol = 7;
                            showLimitDialog("Invalid Equation!");
                        }

                    } else {
                        showLimitDialog("Max Rows Reached!"); // 已达到最大行数限制
                    }
                } else if (currentCol == 0) { // 如果当前行未填充任何字符
                    showLimitDialog("Too Short!"); // 输入过短
                } else {
                    // 如果当前行未满且已有输入，显示输入过短的提示
                    showLimitDialog("Too Short!");
                }
            } else {
                if (currentCol < cols) { // 如果当前行未满
                    matrix[currentRow][currentCol++] = label; // 添加输入到当前位置
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
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(BACKGROUND_COLOR);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!getModel().isPressed()) {
                    setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!getModel().isPressed()) {
                    setBackground(BACKGROUND_COLOR);
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                setBackground(PRESSED_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (getBounds().contains(evt.getPoint())) {
                    setBackground(HOVER_COLOR);
                } else {
                    setBackground(BACKGROUND_COLOR);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT));
        super.paintComponent(g);
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
