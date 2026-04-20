package Karty;

import Enums.CardRank;
import Enums.CardSuit;

public class karta {
    private final CardRank rank;
    private final CardSuit suit;

    public karta(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    public CardRank getRank() {
        return rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public int getValue() {
        return rank.getValue();
    }

    public String getDisplay() {
        return rank.getDisplay() + suit.getIcon();
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}