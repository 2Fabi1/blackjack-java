package Enums;

public enum CardSuit {
    PIK("♠"), KIER("♥"), KARO("♦"), TREFL("♣"), EMPTY(null);

    private final String icon;

    CardSuit(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
