package Panele;

import Enums.CardSuit;
import Karty.karta;

import javax.swing.*;
import java.awt.*;

public class kartaPanel extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    private final karta karta;

    public kartaPanel(karta karta) {
        this.karta = karta;
        setPreferredSize(new Dimension(width / 24, height / 8));
        setOpaque(false);
    }

    public Karty.karta getKarta() {
        return karta;
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

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        int padding = 5;

        g2.setColor((karta.getSuit().equals(CardSuit.KARO) || karta.getSuit().equals(CardSuit.KIER)) ? Color.RED : Color.BLACK);

        g2.drawString(karta.getDisplay(), padding, 20 + padding);
        FontMetrics fm = g2.getFontMetrics();
        int displayWidth = fm.stringWidth(karta.getDisplay());
        g2.drawString(karta.getDisplay(), getWidth() - displayWidth - padding, getHeight() - padding);
        g2.setFont(new Font("SansSerif", Font.BOLD, 35));
        fm = g2.getFontMetrics();
        if (karta.getSuit().getIcon() != null){
            String symbol = karta.getSuit().getIcon();
            int symbolWidth = fm.stringWidth(symbol);
            int symbolHeight = fm.getAscent();
            g2.drawString(symbol, (getWidth() - symbolWidth) / 2, (getHeight() + symbolHeight) / 2);
        }
    }
}