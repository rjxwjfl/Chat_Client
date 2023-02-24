package View.Enhancer;

import javax.swing.*;
import java.awt.*;

public class EnhancedButton extends JButton {
    private int arcWidth = 20;
    private int arcHeight = 20;

    public EnhancedButton() {
        super();
        setContentAreaFilled(false);
    }

    public EnhancedButton(String text) {
        super(text);
        setContentAreaFilled(false);
    }

    public EnhancedButton(int columns) {
        super();
        setContentAreaFilled(false);
    }

    public EnhancedButton(String text, int columns) {
        super(text);
        setContentAreaFilled(false);
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
        repaint();
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
    }
}
