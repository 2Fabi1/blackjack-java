package Karty;

import Enums.CardRank;
import Enums.CardSuit;

public class kartaTwarzowa extends karta {
    public kartaTwarzowa(String face, CardSuit suit) {
        super(switch (face.toUpperCase()) {
            case "J" -> CardRank.JACK;
            case "Q" -> CardRank.QUEEN;
            case "K" -> CardRank.KING;
            case "A" -> CardRank.ACE;
            default -> throw new IllegalArgumentException("Invalid face card: " + face);
        }, suit);
    }
}