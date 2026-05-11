package Rece;

import Karty.karta;

import java.util.*;

public class Reka {

    private List<karta> karty = new ArrayList<>();

    public void dobierz(karta k){
        karty.add(k);
        policzWynik();
    }

    public int policzWynik(){
        int wynik = 0;
        int asy = 0;

        for(karta k : karty){
            if("A".equals(k.getRank().getDisplay())){
                asy++;
                wynik += 1;
            } else {
                wynik += k.getRank().getValue();
            }
        }

        for(int i=0;i<asy;i++){
            if(wynik + 10 <= 21){
                wynik += 10;
            }
        }

        return wynik;
    }

    public int ukrytyWynik(){
        int wynik = 0;
        wynik += karty.get(0).getValue();
        if (wynik == 1){
            wynik += 10;
        }
        return wynik;
    }

    public String toString(){
        return karty.toString();
    }

    public List<karta> getKarty() {
        return karty;
    }

    public karta getKarta(int index) {
        return karty.get(index);
    }

    public String ukryte(){
        return karty.get(0).toString();
    }

    public List<karta> removeKarta(int idx) {
        karty.remove(idx);
        return karty;
    }
}