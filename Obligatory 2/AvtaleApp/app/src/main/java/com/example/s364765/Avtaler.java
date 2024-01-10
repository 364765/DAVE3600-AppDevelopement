package com.example.s364765;


public class Avtaler {

    private long avtaleID;
    private String avtaleNavn;
    private String avtaleDato;
    private String avtaleKlokkeslett;
    private String avtaleTreffsted;

    @Override
    public String toString() {
        return avtaleNavn + " - " + avtaleDato;
    }

    public Avtaler(long avtaleID, String avtaleNavn, String avtaleDato, String avtaleKlokkeslett, String avtaleTreffsted) {
        this.avtaleID = avtaleID;
        this.avtaleNavn = avtaleNavn;
        this.avtaleDato = avtaleDato;
        this.avtaleKlokkeslett = avtaleKlokkeslett;
        this.avtaleTreffsted = avtaleTreffsted;

    }

    public Avtaler() {

    }

    public long getAvtaleID() {
        return avtaleID;
    }

    public void setAvtaleID(long avtaleID) {
        this.avtaleID = avtaleID;
    }

    public String getAvtaleNavn() {
        return avtaleNavn;
    }

    public void setAvtaleNavn(String avtaleNavn) {
        this.avtaleNavn = avtaleNavn;
    }

    public String getAvtaleDato() {
        return avtaleDato;
    }

    public void setAvtaleDato(String avtaleDato) {
        this.avtaleDato = avtaleDato;
    }

    public String getAvtaleKlokkeslett() {
        return avtaleKlokkeslett;
    }

    public void setAvtaleKlokkeslett(String avtaleKlokkeslett) {
        this.avtaleKlokkeslett = avtaleKlokkeslett;
    }

    public String getAvtaleTreffsted() {
        return avtaleTreffsted;
    }

    public void setAvtaleTreffsted(String avtaleTreffsted) {
        this.avtaleTreffsted = avtaleTreffsted;
    }


}
