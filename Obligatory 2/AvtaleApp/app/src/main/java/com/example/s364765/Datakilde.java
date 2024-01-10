package com.example.s364765;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import java.util.List;

public class Datakilde {
    private SQLiteDatabase database;
    private DatabaseHjelper dbHelper;

    public Datakilde(Context context) {
        dbHelper = new DatabaseHjelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Venner leggInnVenn(String vennNavn, String vennTlf) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_VENN_NAVN, vennNavn);
        values.put(DatabaseHjelper.KOLONNE_VENN_TelefonNr, vennTlf);

        long insertId = database.insert(DatabaseHjelper.TABELL_VENNER, null,
                values);
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null,
                DatabaseHjelper.KOLONNE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Venner nyVenn = cursorTilVenn(cursor);
        cursor.close();
        return nyVenn;
    }


    public Venner cursorTilVenn(Cursor cursor) {
        Venner venn = new Venner();
        venn.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_ID)));
        venn.setNavn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_VENN_NAVN)));
        venn.setTelefonNr(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_VENN_TelefonNr)));
        return venn;
    }


    public List<Venner> finnAlleVenner() {
        List<Venner> venner = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Venner venn = cursorTilVenn(cursor);
            venner.add(venn);
            cursor.moveToNext();
        }
        cursor.close();
        return venner;
    }


    public void slettVenn(long vennId) {
        database.delete(DatabaseHjelper.TABELL_VENNER,
                DatabaseHjelper.KOLONNE_ID + " =? ", new
                        String[]{Long.toString(vennId)});
    }


    public Venner finnVennMedID(long vennID) {
        Venner venn = null;

        // Spør databasen etter ID som matcher vennID ved hjelp av cursor
        Cursor cursor = database.query(
                DatabaseHjelper.TABELL_VENNER,
                null,
                DatabaseHjelper.KOLONNE_ID + " = ?",
                new String[]{String.valueOf(vennID)},
                null,
                null,
                null
        );
        cursor.moveToFirst();

        // Setter venn til cursor sin venn i DB
        venn = cursorTilVenn(cursor);
        cursor.close();

        return venn;
    }


    public Avtaler leggInnAvtale(String avtaleNavn, String avtaleDato, String avtaleKlokkeslett, String avtaleTreffsted, List<Long> vennID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_AVTALE_NAVN, avtaleNavn);
        values.put(DatabaseHjelper.KOLONNE_AVTALE_DATO, avtaleDato);
        values.put(DatabaseHjelper.KOLONNE_AVTALE_KLOKKESLETT, avtaleKlokkeslett);
        values.put(DatabaseHjelper.KOLONNE_AVTALE_TREFFSTED, avtaleTreffsted);

        long avtaleId = database.insert(DatabaseHjelper.TABELL_AVTALER, null, values);

        // Setter inn id'er som FK i avtaleVenner DB table
        for (Long vennId : vennID) {
            ContentValues avtaleVennerFkIds = new ContentValues();
            avtaleVennerFkIds.put(DatabaseHjelper.KOLONNE_AVTALE_ID_FK, avtaleId);
            avtaleVennerFkIds.put(DatabaseHjelper.KOLONNE_VENNER_ID_FK, vennId);
            database.insert(DatabaseHjelper.TABELL_AVTALE_VENNER, null, avtaleVennerFkIds);
        }

        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null, DatabaseHjelper.KOLONNE_AVTALE_ID + " = " + avtaleId, null, null, null, null);
        cursor.moveToFirst();
        Avtaler nyAvtale = cursorTilAvtaler(cursor);
        cursor.close();
        return nyAvtale;
    }


    public Avtaler cursorTilAvtaler(Cursor cursor) {
        Avtaler avtale = new Avtaler();
        avtale.setAvtaleID(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_ID)));
        avtale.setAvtaleNavn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_NAVN)));
        avtale.setAvtaleDato(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_DATO)));
        avtale.setAvtaleKlokkeslett(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_KLOKKESLETT)));
        avtale.setAvtaleTreffsted(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_AVTALE_TREFFSTED)));
        return avtale;
    }

    public List<Avtaler> finnAlleAvtaler() {
        List<Avtaler> avtaler = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHjelper.TABELL_AVTALER, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Avtaler avtale = cursorTilAvtaler(cursor);
            avtaler.add(avtale);
            cursor.moveToNext();
        }
        cursor.close();
        return avtaler;
    }

    public void slettAvtale(long avtaleID) {
        database.delete(DatabaseHjelper.TABELL_AVTALER,
                DatabaseHjelper.KOLONNE_AVTALE_ID + " =? ", new
                        String[]{Long.toString(avtaleID)});
        database.delete(DatabaseHjelper.TABELL_AVTALE_VENNER,
                DatabaseHjelper.KOLONNE_AVTALE_ID_FK + " =? ", new
                        String[]{Long.toString(avtaleID)});
    }


    public Avtaler finnAvtaleMedID(long avtaleID) {
        Avtaler avtale = null;

        // Bruker cursor til å spørre databasen etter id som matcher avtaleID
        Cursor cursor = database.query(
                DatabaseHjelper.TABELL_AVTALER,
                null,
                DatabaseHjelper.KOLONNE_AVTALE_ID + " = ?",
                new String[]{String.valueOf(avtaleID)},
                null,
                null,
                null
        );

        cursor.moveToFirst();

        // Setter avtale til cursor sitt DB objekt
        avtale = cursorTilAvtaler(cursor);
        cursor.close();

        return avtale;
    }


    public List<Venner> finnVennerForAvtale(long avtaleID) {
        List<Venner> venner = new ArrayList<>();

        // Spørring for å finne venner med med avtaleID
        Cursor cursor = database.rawQuery(
                "SELECT venn.* FROM " + DatabaseHjelper.TABELL_VENNER + " venn " +
                        "INNER JOIN " + DatabaseHjelper.TABELL_AVTALE_VENNER + " av ON venn." + DatabaseHjelper.KOLONNE_ID + " = av." + DatabaseHjelper.KOLONNE_VENNER_ID_FK + " " +
                        "WHERE av." + DatabaseHjelper.KOLONNE_AVTALE_ID_FK + " = ?",
                new String[]{String.valueOf(avtaleID)}
        );

        // Legger inn venner med riktig avtale id i liste
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Venner venn = cursorTilVenn(cursor);
            venner.add(venn);
            cursor.moveToNext();
        }
        cursor.close();

        return venner;
    }

    public List<Avtaler> finnAvtaleMedDato(String dato) {
        List<Avtaler> avtaler = new ArrayList<>();

        // Spørring for å finne avtale med dato
        Cursor cursor = database.query(DatabaseHjelper.
                        TABELL_AVTALER,
                null,
                DatabaseHjelper.KOLONNE_AVTALE_DATO + " = ?",
                new String[]{String.valueOf(dato)},
                null,
                null,
                null
        );

        // Legger inn avtaler med riktig dato i liste
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Avtaler avtale = cursorTilAvtaler(cursor);
                avtaler.add(avtale);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return avtaler;
    }

}











