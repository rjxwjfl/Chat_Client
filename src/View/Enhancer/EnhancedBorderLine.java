package View.Enhancer;

import javax.swing.border.LineBorder;
import java.awt.*;

public class EnhancedBorderLine extends LineBorder {

    private int arcWidth;
    private int arcHeight;

    public EnhancedBorderLine(Color color, int thickness, int arcWidth, int arcHeight) {
        super(color, thickness);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getLineColor());
        g2d.setStroke(new BasicStroke(getThickness()));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);
        g2d.dispose();
    }
}