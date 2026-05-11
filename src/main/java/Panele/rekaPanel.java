package Panele;

import Enums.CardRank;
import Enums.CardSuit;
import Karty.karta;
import Rece.Reka;

import javax.swing.*;
import java.awt.*;

public class rekaPanel extends JPanel {
    private Reka reka;
    private handPanel hand;
    private boolean dimmed = false;

    public rekaPanel(Reka reka) {
        this.reka = reka;
        setLayout(new BorderLayout());

        hand = new handPanel(reka);
        add(hand, BorderLayout.CENTER);
    }

    public void setDimmed(boolean dimmed) {
        this.dimmed = dimmed;
        repaint();
    }

    public void dobierz(karta karta) {
        reka.dobierz(karta);
        if (reka.getKarta(1).getSuit() == CardSuit.EMPTY|| reka.getKarta(1).getRank() == CardRank.EMPTY) {
            reka.removeKarta(1);
        }
        odswiez();
    }

    public void odswiez() {
        removeAll();
        hand = new handPanel(reka);
        add(hand, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public Reka getReka() {
        return reka;
    }

    public int getKartySize() {
        return reka.getKarty().size();
    }
}