package Panele;
import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class dimPanel extends LayerUI<JComponent> {
    private boolean dimmed = true;

    public void setDimmed(boolean dimmed, JLayer<?> layer) {
        this.dimmed = dimmed;
        layer.repaint();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        if (dimmed) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(128, 128, 128, 120));
            g2.fillRect(0, 0, c.getWidth(), c.getHeight());

            // opcjonalnie napis
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("WAIT", c.getWidth()/2 - 30, c.getHeight()/2);

            g2.dispose();
        }
    }
}