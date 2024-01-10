package com.example.s364765;

public class Venner {
    private long id;
    private String navn;
    private String telefonNr;

    @Override
    public String toString() {
        return navn;
    }

    public Venner(long id, String navn, String telefonNr) {
        this.id = id;
        this.navn = navn;
        this.telefonNr = telefonNr;
    }

    public Venner() {
    }

    public Venner(long id) {
        this.id = id;
    }

    public Venner(String navn, String telefonNr) {
        this.navn = navn;
        this.telefonNr = telefonNr;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getTelefonNr() {
        return telefonNr;
    }

    public void setTelefonNr(String telefonNr) {
        this.telefonNr = telefonNr;
    }


}