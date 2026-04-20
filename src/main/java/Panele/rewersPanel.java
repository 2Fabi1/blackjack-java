package Panele;

import java.awt.*;
import javax.swing.*;

public class rewersPanel extends JPanel{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    public rewersPanel(){
        setPreferredSize(new Dimension(width / 24, height / 8));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, width / 24, height / 8, height / 64, height / 64);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, width / 24, height / 8, height / 64, height / 64);

        g2.setColor(Color.BLUE);
        g2.fillRoundRect(5, 5, width / 24 - 10, height / 8 - 10, height / 64, height / 64);
    }
}
