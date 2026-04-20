package Rece;

import Karty.karta;

public class PlayerHand implements Hand {
    private Reka reka = new Reka();

    public void dobierz(karta karta) {
        reka.dobierz(karta);
    }

    public int policzWynik() {
        return reka.policzWynik();
    }

    public boolean shouldDraw(int opponentScore) {
        return false;
    }

    public Reka getReka() {
        return reka;
    }
}