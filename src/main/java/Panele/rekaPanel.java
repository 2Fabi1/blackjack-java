package Panele;

import Karty.karta;
import Rece.Reka;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class rekaPanel extends JPanel {
    private Reka reka;
    private final List<JPanel> karty = new ArrayList<>();
    private JPanel cardsPanel;
    public JLabel label;
    public rekaPanel(Reka reka) {
        this.reka = reka;

        setLayout(new BorderLayout());
        setOpaque(false);
        label = new JLabel("Wynik: " + reka.policzWynik(), SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // pusty border zeby popchnac troche tekst na dół
        add(label, BorderLayout.NORTH);
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setOpaque(false);
        add(cardsPanel, BorderLayout.CENTER);
        for (karta k : reka.getKarty()) {
            JPanel panel;
            if (k.getSuit().getIcon() == null){
                panel = new rewersPanel();
            }
            else{
                panel = new kartaPanel(k);
            }
            karty.add(panel);
            cardsPanel.add(panel);
        }
        Color bgGreen = new Color(0, 52, 0);
        setBackground(bgGreen);
        setOpaque(true);
        revalidate();
        repaint();
    }

    public List<JPanel> getKarty() {
        return karty;
    }

    public void aktualizujWynik() {
        label.setText("Wynik: " + reka.policzWynik());
        label.revalidate();
        label.repaint();
    }

    public void dobierz(karta k) {
        kartaPanel panel = new kartaPanel(k);
        karty.add(panel);
        cardsPanel.add(panel);
        revalidate();
        repaint();
    }

    public void odswiez(Reka nowaReka) {
        this.reka = nowaReka;
        cardsPanel.removeAll(); // usuwa tylko panele kart, nie labelkę

        for (karta k : reka.getKarty()) {
            JPanel panel;
            if (k.getSuit() == null) {
                panel = new rewersPanel();
            } else {
                panel = new kartaPanel(k);
            }
            cardsPanel.add(panel);
        }

        aktualizujWynik(); // aktualizuje labelkę z wynikiem
        revalidate();
        repaint();
    }
}