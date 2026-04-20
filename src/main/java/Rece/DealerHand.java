package Rece;
import Karty.karta;

public class DealerHand implements Hand {
    private final Reka reka = new Reka();

    public void dobierz(karta karta) {
        reka.dobierz(karta);
    }

    public int policzWynik() {
        return reka.policzWynik();
    }

    public boolean isSoft() {
        int score = 0;
        int aces = 0;

        for (karta k : reka.getKarty()) {
            if ("A".equals(k.getRank().getDisplay())) {
                aces++;
                score += 1;
            } else {
                score += k.getRank().getValue();
            }
        }
        return aces > 0 && score <= 11;
    }

    public boolean shouldDraw(int opponentScore) {
        int score = policzWynik();

        if (score < 17) return true;

        // hit na soft 17
        return score == 17 && isSoft();
    }

    public Reka getReka() {
        return reka;
    }
}