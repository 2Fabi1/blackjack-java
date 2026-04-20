package Karty;

import Enums.CardRank;
import Enums.CardSuit;

public class kartaLiczbowa extends karta {
    public kartaLiczbowa(int number, CardSuit suit) {
        super(switch (number) {
            case 2 -> CardRank.TWO;
            case 3 -> CardRank.THREE;
            case 4 -> CardRank.FOUR;
            case 5 -> CardRank.FIVE;
            case 6 -> CardRank.SIX;
            case 7 -> CardRank.SEVEN;
            case 8 -> CardRank.EIGHT;
            case 9 -> CardRank.NINE;
            case 10 -> CardRank.TEN;
            default -> throw new IllegalArgumentException("Invalid number card: " + number);
        }, suit);
    }
}