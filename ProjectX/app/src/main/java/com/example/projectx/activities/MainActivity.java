package com.example.projectx.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
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
    List<FilmResponse.SingleFilmResult> searchedFilm;

    private WebServiceFilms webServiceFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Project X");

        recyclerView = findViewById(R.id.recyclerFilms);

        if (checkConnection()) {
            webServiceFilms = WebServiceFilms.getInstance();
            connectionOK();
        } else {
            connectionKO();
        }

        /*int orientation = getResources().getConfiguration().orientation;
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
        }*/

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
        webServiceFilms.getFilms(API_KEY, MainActivity.this, new IWebServer() {;
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    Toast.makeText(MainActivity.this, "Film caricati con successo ☑", Toast.LENGTH_SHORT).show();
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        filmAdapter = new FilmAdapter(films, MainActivity.this);
                        searchedFilm = new ArrayList<>();
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        recyclerView.setAdapter(filmAdapter);
                        filmAdapter.notifyDataSetChanged();
                    } else {
                        filmAdapter = new FilmAdapter(films, MainActivity.this);
                        searchedFilm = new ArrayList<>();
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        recyclerView.setAdapter(filmAdapter);
                        filmAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("cosa vuoi cercare");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (checkConnection()) {
                    if (!newText.isEmpty()) {
                        searchFilms(newText);
                    } else {
                        filmAdapter.resetFilms();
                        connectionOK();
                    }
                } else {
                    if (!newText.isEmpty()) {
                        filmAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
        //films
    }

    private void searchFilms(String QUERY) {
        webServiceFilms.searchFilms(QUERY, API_KEY, MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    filmAdapter.resetFilms();
                    searchedFilm.clear();
                    searchedFilm.addAll(films);
                    filmAdapter.setFilms(searchedFilm);
                    filmAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //GITHUB FA SCHIFO
    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        int scrollPosition = recyclerView.computeVerticalScrollOffset();
        Toast.makeText(this, "onSaveInstanceState " + scrollPosition, Toast.LENGTH_SHORT).show();
        savedInstanceState.putInt("Posizione", scrollPosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int scrollPosition = savedInstanceState.getInt("Posizione");
        Toast.makeText(this, "onRestoreInstanceState " + scrollPosition, Toast.LENGTH_SHORT).show();
        recyclerView.getLayoutManager().scrollToPosition(scrollPosition);
    }*/
}
