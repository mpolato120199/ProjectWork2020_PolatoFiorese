package com.example.projectx.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.Film.FilmContentProvider;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.R;
import com.example.projectx.adapters.FilmAdapter;
import com.example.projectx.data.models.FilmResponse;
import com.example.projectx.data.services.IWebServer;
import com.example.projectx.data.services.WebServiceFilms;
import com.example.projectx.interfaces.IConnectivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IConnectivity, IWebServer {

    private final String API_KEY = "a979a7eb2578017177824cf553c182ef";

    private RecyclerView recyclerView;
    private FilmAdapter filmAdapter;
    List<FilmResponse.SingleFilmResult> savedFilms;

    private WebServiceFilms webServiceFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("PROJECT X");

        recyclerView = findViewById(R.id.recyclerFilms);

        if (checkConnection()) {
            webServiceFilms = WebServiceFilms.getInstance();
            connectionOK();
        } else {
            connectionKO();
        }
        webServiceFilms = WebServiceFilms.getInstance();
    }

    @Override
    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void connectionOK() {
        webServiceFilms.getFilms(API_KEY, MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        filmAdapter = new FilmAdapter(films, MainActivity.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        recyclerView.setAdapter(filmAdapter);
                    } else {
                        filmAdapter = new FilmAdapter(films, MainActivity.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        recyclerView.setAdapter(filmAdapter);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Errore: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void connectionKO() {
        savedFilms = new ArrayList<>();
        Cursor movies = MainActivity.this.getContentResolver().query(FilmContentProvider.FILMS_URI, null, null, null, null);

        if (movies != null) {
            while (movies.moveToNext()) {
                FilmResponse.SingleFilmResult film = new FilmResponse.SingleFilmResult();

                film.setId(movies.getColumnIndex(FilmTableHelper.ID));
                film.setTitle(movies.getString(movies.getColumnIndex(FilmTableHelper.TITOLO)));
                film.setOverview(movies.getString(movies.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                film.setPosterPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMMAGINE_COPERTINA)));
                film.setBackdropPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMMAGINE_DETTAGLIO)));

                savedFilms.add(film);
            }

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                filmAdapter = new FilmAdapter(savedFilms, MainActivity.this);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                recyclerView.setAdapter(filmAdapter);
                filmAdapter.notifyDataSetChanged();
            } else {
                filmAdapter = new FilmAdapter(savedFilms, MainActivity.this);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(filmAdapter);
                filmAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(MainActivity.this, "Errore, cursor movies null ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkConnection()) {
            webServiceFilms = WebServiceFilms.getInstance();
            connectionOK();
        } else {
            connectionKO();
        }
    }

    @Override
    public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {

    }
}
