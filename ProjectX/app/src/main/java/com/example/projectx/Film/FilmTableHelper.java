package com.example.projectx.Film;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "Film";
    public static final String TITOLO = "titolo";
    public static final String DESCRIZIONE = "descrizione";
    public static final String  IMMAGINE_DETTAGLIO= "dataInserimento";
    public static final String IMMAGINE_COPERTINA = "dataEseguita";
    public static final String DONE = "eseguita";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME;/* + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITOLO + " TEXT, " +
            DESCRIZIONE + " TEXT, " +
            DATA_INSERIMENTO + " TEXT, " +
            DONE + " INTEGER, " +
            DATA_ESEGUITA + " TEXT ); ";*/
}
