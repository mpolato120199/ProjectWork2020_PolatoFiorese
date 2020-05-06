package com.example.projectx.data.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FilmContentProvider extends ContentProvider {


    public static final String AUTORITY = "com.example.projectx.data.database.FilmContentProvider";
    public static final String BASE_PATH_FILMS = "films";
    public static final String BASE_PATH_RATE= "rate";
    public static final int ALL_FILM = 1;
    public static final int SINGLE_FILM = 0;
    public static final int ALL_RATE = 2;
    public static final int SINGLE_RATE = 3;
    public static final String MIME_TYPE_FILMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.all_films";
    public static final String MIME_TYPE_FILM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.single_film";
    public static final String MIME_TYPE_RATES = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.all_rates";
    public static final String MIME_TYPE_RATE = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.single_rate";
    public static final Uri FILMS_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTORITY + "/" + BASE_PATH_FILMS);
    public static Uri RATE_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTORITY + "/" + BASE_PATH_RATE);
    private FilmDB mDb;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTORITY, BASE_PATH_FILMS, ALL_FILM);
        mUriMatcher.addURI(AUTORITY, BASE_PATH_FILMS + "/#", SINGLE_FILM);
        mUriMatcher.addURI(AUTORITY, BASE_PATH_RATE, ALL_RATE);
        mUriMatcher.addURI(AUTORITY, BASE_PATH_RATE + "/#", SINGLE_RATE);
    }


    @Override
    public boolean onCreate() {
        mDb = new FilmDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase vDb = mDb.getReadableDatabase();
        SQLiteQueryBuilder vBuilder = new SQLiteQueryBuilder();
        switch (mUriMatcher.match(uri)) {
            case SINGLE_FILM:
                vBuilder.setTables(FilmTableHelper.TABLE_NAME);
                vBuilder.appendWhere(FilmTableHelper._ID + " = " + uri.getLastPathSegment());
                break;
            case ALL_FILM:
                vBuilder.setTables(FilmTableHelper.TABLE_NAME);

                break;
            case SINGLE_RATE:
                vBuilder.setTables(RateTableHelper.TABLE_NAME);
                vBuilder.appendWhere(RateTableHelper._ID + " = " + uri.getLastPathSegment());
                break;
            case ALL_RATE:
                vBuilder.setTables(RateTableHelper.TABLE_NAME);
                break;
        }

        Cursor vCursor = vBuilder.query(vDb, projection, selection, selectionArgs, null, null, sortOrder);
        vCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return vCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case SINGLE_FILM:
                return MIME_TYPE_FILM;

            case ALL_FILM:
                return MIME_TYPE_FILMS;

            case SINGLE_RATE:
                return MIME_TYPE_RATE;

            case ALL_RATE:
                return MIME_TYPE_RATES;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase vDb = mDb.getWritableDatabase();
        long vResult = 0;
        String vResultString = "";
        switch (mUriMatcher.match(uri)) {
            case ALL_FILM:
                vResult = vDb.insert(FilmTableHelper.TABLE_NAME, null, values);
                vResultString = ContentResolver.SCHEME_CONTENT + "://" + BASE_PATH_FILMS + "/" + vResult;

            case ALL_RATE:
                vResult = vDb.insert(RateTableHelper.TABLE_NAME, null, values);
                vResultString = ContentResolver.SCHEME_CONTENT + "://" + BASE_PATH_RATE + "/" + vResult;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(vResultString);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String vTable = "", vQuery = "";
        SQLiteDatabase vDb = mDb.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case ALL_FILM:
                vTable = FilmTableHelper.TABLE_NAME;
                vQuery = selection;
                break;
            case SINGLE_FILM:
                vTable = FilmTableHelper.TABLE_NAME;
                vQuery = FilmTableHelper._ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    vQuery += " AND " + selection;
                }
                break;
            case ALL_RATE:
                vTable = RateTableHelper.TABLE_NAME;
                vQuery = selection;
                break;
            case SINGLE_RATE:
                vTable = RateTableHelper.TABLE_NAME;
                vQuery = RateTableHelper._ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    vQuery += " AND " + selection;
                }
                break;
        }
        int vDeletedRows = vDb.delete(vTable, vQuery, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return vDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String vTable = "", vQuery = "";
        SQLiteDatabase vDb = mDb.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case ALL_FILM:
                vTable = FilmTableHelper.TABLE_NAME;
                vQuery = selection;
                break;
            case SINGLE_FILM:
                vTable = FilmTableHelper.TABLE_NAME;
                vQuery = FilmTableHelper._ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    vQuery += " AND " + selection;
                }
                break;
            case ALL_RATE:
                vTable = RateTableHelper.TABLE_NAME;
                vQuery = selection;
                break;
            case SINGLE_RATE:
                vTable = RateTableHelper.TABLE_NAME;
                vQuery = RateTableHelper._ID + " = " + uri.getLastPathSegment();
                if (selection != null) {
                    vQuery += " AND " + selection;
                }
                break;
        }
        int vUpdatedRows = vDb.update(vTable, values, vQuery, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return vUpdatedRows;
    }
}
