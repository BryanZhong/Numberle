import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class NumberleView extends JFrame {

    private static final int GRID_ROWS = 6;
    private static final int GRID_COLS = 7;
    private static final int BUTTON_ROWS = 2;
    private static final int BUTTON_COLS = 5;

    private JPanel gridPanel;
    private JPanel buttonPanel;

    public NumberleView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Numberle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, 5, 5));
        initializeGrid();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(BUTTON_ROWS, BUTTON_COLS, 5, 5));
        initializeButtons();

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_ROWS * GRID_COLS; i++) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            gridPanel.add(panel);
        }
    }

    private void initializeButtons() {
        String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Delete", "+", "-", "*", "/", "=", "Enter"};

        for (String buttonText : buttons) {
            RoundedButton button = new RoundedButton(buttonText);
            buttonPanel.add(button);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NumberleView().setVisible(true));
    }

    private static class RoundedButton extends JButton {
        private static final int ARC_WIDTH = 15;
        private static final int ARC_HEIGHT = 15;

        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isArmed()) {
                g2.setColor(Color.LIGHT_GRAY); // Button is pressed
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);

            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(this.getText(), g2).getBounds();

            int textX = (getWidth() - stringBounds.width) / 2;
            int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);
            g2.dispose();

            super.paintComponent(g);
        }
    }
}
