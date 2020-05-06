package com.example.projectx.data.database;

import android.provider.BaseColumns;

import java.security.PublicKey;

public class RateTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "Rate";
    public static final String  VALUTAZIONE = "valutazione";
    public static final String DATA_VALUTAZIONE = "data_valutazione";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER," +
            VALUTAZIONE + " FLOAT," +
            DATA_VALUTAZIONE + " TEXT);";
}
