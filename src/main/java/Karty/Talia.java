package Karty;

import Enums.CardRank;
import Enums.CardSuit;

import java.util.*;

public class Talia {

    private final List<karta> talia;

    public Talia() {
        talia = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            if (suit == CardSuit.EMPTY) continue;

            for (CardRank rank : CardRank.values()) {
                if (rank == CardRank.EMPTY) continue;

                talia.add(new karta(rank, suit));
            }
        }

        Collections.shuffle(talia);
    }


    public karta dobierzKarte() {
        if (talia.isEmpty()) {
            throw new NoSuchElementException("Deck is empty!");
        }
        return talia.remove(0);
    }
    public int remaining() {
        return talia.size();
    }
    public void shuffle() {
        Collections.shuffle(talia);
    }
    public karta peekNextCard() {
        return talia.get(0); // or whatever your top is
    }
}