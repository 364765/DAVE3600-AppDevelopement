package com.example.s364765;

import java.util.ArrayList;
import java.util.List;

public class StedListe {
    private static List<Sted> stedListe = new ArrayList<>();

    public static List<Sted> getStedListe() {
        return stedListe;
    }

    public static void setStedListe(List<Sted> liste) {
        stedListe = liste;
    }
}
