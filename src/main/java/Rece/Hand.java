package Rece;

import Karty.karta;

public interface Hand {
    void dobierz(karta karta);
    int policzWynik();

    Reka getReka();

    boolean shouldDraw(int opponentScore);
}