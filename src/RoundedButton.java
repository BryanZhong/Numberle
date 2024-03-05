import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private static final int ARC_WIDTH = 15; // 圆角弧度
    private static final int ARC_HEIGHT = 15; // 圆角弧度

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制圆角矩形背景
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT));

        // 绘制文字
        g2.setColor(getForeground());
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();

        super.paintComponent(g);
    }
}
