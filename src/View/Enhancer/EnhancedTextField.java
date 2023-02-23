package src.View.Enhancer;

import javax.swing.*;
import java.awt.*;

public class EnhancedTextField extends JTextField {
    private int arcWidth = 20;
    private int arcHeight = 20;
    private String hintText = "";

    public EnhancedTextField() {
        super();
    }

    public EnhancedTextField(String text) {
        super(text);
    }

    public EnhancedTextField(int columns) {
        super(columns);
    }

    public EnhancedTextField(String text, int columns) {
        super(text, columns);
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

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        super.paintComponent(g);
        if (getText().isEmpty() && !hintText.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            int x = getInsets().left;
            int y = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
            g2.drawString(hintText, x, y);
        }
    }
}