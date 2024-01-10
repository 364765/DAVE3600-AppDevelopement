package com.example.s364765;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "avtaleApp.db";
    private static final int DATABASE_VERSION = 7;

    /*------------Venner tabell---------------------*/
    public static final String TABELL_VENNER = "venner";
    public static final String KOLONNE_ID = "id";
    public static final String KOLONNE_VENN_NAVN = "navn";
    public static final String KOLONNE_VENN_TelefonNr = "telefonNr";

    /*------------Avtaler tabell---------------------*/
    public static final String TABELL_AVTALER = "avtaler";
    public static final String KOLONNE_AVTALE_ID = "avtaleID";
    public static final String KOLONNE_AVTALE_NAVN = "avtaleNavn";
    public static final String KOLONNE_AVTALE_DATO = "avtaleDato";
    public static final String KOLONNE_AVTALE_KLOKKESLETT = "avtaleKlokkeslett";
    public static final String KOLONNE_AVTALE_TREFFSTED = "avtaleTreffsted";

    /*------------Junction tabell---------------------*/
    public static final String TABELL_AVTALE_VENNER = "avtaleVenner";
    public static final String KOLONNE_AVTALE_ID_FK = "avtaleId";
    public static final String KOLONNE_VENNER_ID_FK = "vennId";


    /*------------Oppretter Venner tabell---------------------*/
    private static final String CREATE_TABLE_VENNER = "CREATE TABLE " +
            TABELL_VENNER +
            "(" + KOLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KOLONNE_VENN_NAVN + " TEXT NOT NULL, " +
            KOLONNE_VENN_TelefonNr + " TEXT NOT NULL )";

    /*------------Oppretter avtaler tabell---------------------*/
    private static final String CREATE_TABLE_AVTALER = "CREATE TABLE " +
            TABELL_AVTALER +
            "(" + KOLONNE_AVTALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KOLONNE_AVTALE_NAVN + " TEXT NOT NULL, " +
            KOLONNE_AVTALE_DATO + " TEXT NOT NULL, " +
            KOLONNE_AVTALE_KLOKKESLETT + " TEXT NOT NULL, " +
            KOLONNE_AVTALE_TREFFSTED + " TEXT NOT NULL)";

    /*------------Oppretter junction tabell---------------------*/
    private static final String CREATE_TABLE_AVTALE_VENNER = "CREATE TABLE " +
            TABELL_AVTALE_VENNER +
            "(" + KOLONNE_AVTALE_ID_FK + " INTEGER, " +
            KOLONNE_VENNER_ID_FK + " INTEGER, " +
            "FOREIGN KEY (" + KOLONNE_AVTALE_ID_FK + ") REFERENCES " + TABELL_AVTALER + "(" + KOLONNE_AVTALE_ID + "), " +
            "FOREIGN KEY (" + KOLONNE_VENNER_ID_FK + ") REFERENCES " + TABELL_VENNER + "(" + KOLONNE_ID + "), " +
            "PRIMARY KEY (" + KOLONNE_AVTALE_ID_FK + ", " + KOLONNE_VENNER_ID_FK + ")" +
            ")";


    public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VENNER);
        db.execSQL(CREATE_TABLE_AVTALER);
        db.execSQL(CREATE_TABLE_AVTALE_VENNER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            db.execSQL("CREATE TABLE " +
                    TABELL_AVTALE_VENNER +
                    "(" +
                    KOLONNE_AVTALE_ID_FK + " INTEGER, " +
                    KOLONNE_VENNER_ID_FK + " INTEGER, " +
                    "FOREIGN KEY (" + KOLONNE_AVTALE_ID_FK + ") REFERENCES " + TABELL_AVTALER + "(" + KOLONNE_AVTALE_ID + "), " +
                    "FOREIGN KEY (" + KOLONNE_VENNER_ID_FK + ") REFERENCES " + TABELL_VENNER + "(" + KOLONNE_ID + "), " +
                    "PRIMARY KEY (" + KOLONNE_AVTALE_ID_FK + ", " + KOLONNE_VENNER_ID_FK + ")" +
                    ")");
        }

    }
}
