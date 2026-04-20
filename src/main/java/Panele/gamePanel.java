package Panele;

import javax.swing.*;
import java.awt.*;

public class gamePanel extends JPanel {

    public JPanel tablePanel;

    public JButton hitButton;
    public JButton standButton;
    public JButton doubleButton;

    public chipsPanel chPanel;
    public JPanel chipsContainer;
    public JButton plusButton;
    public potPanel pPanel;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;

    public gamePanel() {
        setLayout(null);
        setBackground(new Color(0, 102, 0)); // poker green

        // MAIN TABLE PANEL
        tablePanel = new JPanel(null);
        tablePanel.setOpaque(false);
        tablePanel.setBounds(0, 0, width, height);
        add(tablePanel);

        // BUTTONS
        hitButton = new JButton("Hit (H)");
        standButton = new JButton("Stand (S)");
        doubleButton = new JButton("Double Down (D)");

        ustawPrzycisk(hitButton, Color.RED, new Color(127, 0, 0));
        ustawPrzycisk(standButton, Color.CYAN, new Color(0, 127, 127));
        ustawPrzycisk(doubleButton, new Color(255, 215, 0), new Color(127, 110, 0));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setOpaque(false);

        buttonsPanel.add(hitButton);
        buttonsPanel.add(standButton);
        buttonsPanel.add(doubleButton);

        buttonsPanel.setBounds((int)(width / 3) + 21, (int)(0.6 * height), (int)(0.3 * width), 70);
        tablePanel.add(buttonsPanel);

        // DECK
        double deckY = (double) (height * 7) / 16;
        rewersPanel deck = new rewersPanel();
        deck.setBounds((int)(width * 0.9), (int)(deckY), width / 24, height / 8);
        tablePanel.add(deck);

        // CHIPS + POT
        pPanel = new potPanel();
        chPanel = new chipsPanel(100);
        chipsContainer = new JPanel();
        chipsContainer.setLayout(new BoxLayout(chipsContainer, BoxLayout.X_AXIS));
        chipsContainer.setPreferredSize(new Dimension(200, 50));
        chipsContainer.setOpaque(false);

        plusButton = new JButton("+");
        ustawPrzycisk(plusButton, Color.GREEN, new Color(20, 230, 20));
        plusButton.setFocusPainted(false);
        plusButton.setPreferredSize(new Dimension(50, 50));

        chipsContainer.add(chPanel);
        chipsContainer.add(Box.createHorizontalStrut(25));
        chipsContainer.add(plusButton);

        chipsContainer.setBounds(50, (int)(0.5 * height - (double) height / 8), width / 8, height / 16);
        tablePanel.add(chipsContainer);

        pPanel.setBounds(50, (int)(0.5 * height), width / 8, height / 16);
        tablePanel.add(pPanel);
    }

    private void ustawPrzycisk(JButton button, Color color, Color borderColor) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setPreferredSize(new Dimension((int)(width / 11), 50));
        button.setBorder(BorderFactory.createLineBorder(borderColor, 5));
    }

    public void addHand(JPanel reka) {
        tablePanel.add(reka);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void resetHands() {
        Component[] comps = tablePanel.getComponents();

        for (Component c : comps) {
            if (c.getClass().getSimpleName().equals("rekaPanel")) {
                tablePanel.remove(c);
            }
        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }
}