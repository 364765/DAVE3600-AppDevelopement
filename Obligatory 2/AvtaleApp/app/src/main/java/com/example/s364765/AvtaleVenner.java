package com.example.s364765;

public class AvtaleVenner {

    private long avtaleId;
    private long vennId;


    public AvtaleVenner(long avtaleId, long vennId) {
        this.avtaleId = avtaleId;
        this.vennId = vennId;

    }

    public AvtaleVenner() {

    }

    public long getAvtaleId() {
        return avtaleId;
    }

    public void setAvtaleId(long avtaleId) {
        this.avtaleId = avtaleId;
    }

    public long getVennId() {
        return vennId;
    }

    public void setVennId(long vennId) {
        this.vennId = vennId;
    }
}
