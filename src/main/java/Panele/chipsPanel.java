package Panele;

import javax.swing.*;
import java.awt.*;

public class chipsPanel extends JPanel {
    private int chips;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    private JButton plusButton;

    public chipsPanel(int chips) {
        this.chips = chips;

        setPreferredSize(new Dimension(100, 50));
        setLayout(new BorderLayout());
        setOpaque(true);
    }

    public void setChips(int chips) {
        this.chips = chips;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();
        g2.setColor(new Color(0, 52, 0));
        g2.fillRoundRect(0, 0, w, h, 0, 0);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, h / 2));

        String text = "Chips: " + String.valueOf(chips);
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(text, x, y);
    }

    private JButton ustawPrzycisk(JButton button, Color color, Color borderColor) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setPreferredSize(new Dimension((int)(width / 11), 50));
        button.setBorder(BorderFactory.createLineBorder(borderColor, 5));
        return button;
    }
}