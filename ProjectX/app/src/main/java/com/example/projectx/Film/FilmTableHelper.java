package com.example.projectx.Film;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "Film";
    public static final String ID = "id";
    public static final String TITOLO = "titolo";
    public static final String DESCRIZIONE = "descrizione";
    public static final String IMMAGINE_DETTAGLIO= "immagine_dettaglio";
    public static final String IMMAGINE_COPERTINA = "immagine_copertina";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY, " +
            TITOLO + " TEXT, " +
            DESCRIZIONE + " TEXT, " +
            IMMAGINE_DETTAGLIO + " TEXT, " +
            IMMAGINE_COPERTINA + " TEXT); ";
}
