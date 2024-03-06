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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int availableWidth = (int) (panelWidth - (cols - 1) * gapRatio * panelWidth);
        int rectWidth = (int) ((availableWidth / cols) / (1 + gapRatio));
        int rectHeight = (int) (rectWidth * aspectRatio); // 保持长宽比
        int gap = (int) (rectWidth * gapRatio);

        // 确保矩形顶部对齐，上边距为0
        int totalHeight = rows * rectHeight + (rows - 1) * gap;
        int startY = 0; // 从顶部开始绘制

        // 根据面板宽度调整开始绘制的x坐标，以使矩阵居中
        int totalWidth = cols * rectWidth + (cols - 1) * gap;
        int startX = (panelWidth - totalWidth) / 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (rectWidth + gap);
                int y = startY + i * (rectHeight + gap);
                g2d.drawRoundRect(x, y, rectWidth, rectHeight, cornerRadius, cornerRadius);
            }
        }
        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!matrix[i][j].isEmpty()) {
                    // 计算文字居中的位置
                    int textWidth = fm.stringWidth(matrix[i][j]);
                    int textX = startX + j * (rectWidth + gap) + (rectWidth - textWidth) / 2;
                    int textY = startY + i * (rectHeight + gap) + ((rectHeight - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(matrix[i][j], textX, textY);
                }
            }
        }
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

        JPanel numberButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        numberButtonsPanel.setBackground(Color.decode("#FBFCFF"));
        buttonsPanel.add(numberButtonsPanel);

        JPanel operatorButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
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
                    matrix[currentRow][--currentCol] = "";
                    isEnterPressed = false; // 重置 Enter 标志
                } else if (currentRow > 0 && !isEnterPressed) {
                    // 如果当前列为0，且不在第一行，且未按下 Enter 移动到这一行，不执行删除
                }
            } else if ("Enter".equals(label)) {
                if (currentRow < rows - 1) {
                    currentRow++;
                    currentCol = 0;
                    isEnterPressed = true; // 标记已经按下 Enter
                } else {
                    showLimitDialog(); // 已到达输入限制
                }
            } else {
                if (currentCol < cols) {
                    matrix[currentRow][currentCol++] = label;
                    isEnterPressed = false; // 重置 Enter 标志
                } else {
                    showLimitDialog(); // 当前行已满，显示限制对话框
                }
            }
            repaint();
        };
    }


    private void showLimitDialog() {
        JDialog dialog = new RoundedDialog(null, "您已达到输入限制");
        dialog.setVisible(true);
    }
}

 class RoundedDialog extends JDialog {
    private String message;

    public RoundedDialog(Frame owner, String message) {
        super(owner, true);
        setUndecorated(true);
        setSize(300, 100);
        setLocationRelativeTo(owner);
        add(new JLabel(message, JLabel.CENTER));

        // 点击对话框任何位置关闭对话框
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        // 使用Timer实现自动消失
        new Timer(1000, e -> dispose()).start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30)); // 绘制圆角矩形
        super.paint(g);
    }
}
class RoundedButton extends JButton {
    private static final int ARC_WIDTH = 10;
    private static final int ARC_HEIGHT = 10;
    private static final Color BACKGROUND_COLOR = Color.decode("#DCE1ED");
    private static final Color FONT_COLOR = Color.decode("#5A6376");
    private static final Color HOVER_COLOR = Color.GRAY;
    private static final Color PRESSED_COLOR = Color.decode("#4B5464");
    public RoundedButton(String text) {
        super(text);
        setFont(loadFont("D:/download/Montserrat,Open_Sans/Montserrat/Montserrat-VariableFont_wght.ttf", 20f));
        setForeground(FONT_COLOR);
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
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, ARC_WIDTH, ARC_HEIGHT));
        super.paintComponent(g);
    }

    // 注意：确保将字体文件放在您的文件系统上的正确路径
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

    // 覆盖getPreferredSize方法，以确保按钮大小是您想要的尺寸
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 56);
    }
}