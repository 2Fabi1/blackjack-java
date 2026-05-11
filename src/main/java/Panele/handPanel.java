package Panele;

import Karty.karta;
import Rece.Reka;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class handPanel extends JPanel {
    private Reka reka;
    private final List<JPanel> karty = new ArrayList<>();
    private JPanel cardsPanel;
    private JLabel label;

    public handPanel(Reka reka) {
        this.reka = reka;

        setLayout(new BorderLayout());

        label = new JLabel("Wynik: " + reka.policzWynik(), SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(label, BorderLayout.NORTH);

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setOpaque(false);
        add(cardsPanel, BorderLayout.CENTER);

        refreshCards();

        setBackground(new Color(0, 52, 0));
        setOpaque(true);
    }

    private void refreshCards() {
        cardsPanel.removeAll();
        karty.clear();

        for (karta k : reka.getKarty()) {
            JPanel panel;
            if (k.getSuit() == null || k.getSuit().getIcon() == null) {
                panel = new rewersPanel();
            } else {
                panel = new kartaPanel(k);
            }
            karty.add(panel);
            cardsPanel.add(panel);
        }
    }

    public void aktualizujWynik() {
        label.setText("Wynik: " + reka.policzWynik());
    }

    public void dobierz(karta k) {
        JPanel panel = new kartaPanel(k);
        karty.add(panel);
        cardsPanel.add(panel);
        revalidate();
        repaint();
    }

    public void odswiez(Reka nowaReka) {
        this.reka = nowaReka;
        refreshCards();
        aktualizujWynik();
        revalidate();
        repaint();
    }
}