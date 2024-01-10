package com.example.s364765;

public class Sted {
    private String navn;
    private String beskrivelse;
    private String adresse;
    private double latitude;
    private double longitude;

    public Sted(String navn, String beskrivelse, String adresse, double latitude, double longitude) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNavn() {
        return navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getAdresse() {
        return adresse;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
