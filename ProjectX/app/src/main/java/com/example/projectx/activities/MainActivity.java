package com.example.projectx.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectx.R;
import com.example.projectx.adapters.FilmAdapter;
import com.example.projectx.data.database.FilmContentProvider;
import com.example.projectx.data.database.FilmTableHelper;
import com.example.projectx.data.models.FilmResponse;
import com.example.projectx.data.services.IWebServer;
import com.example.projectx.data.services.WebServiceFilms;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IWebServer {

    private final String API_KEY = "a979a7eb2578017177824cf553c182ef";
    private final String LANGUAGE = "it-IT";

    private RecyclerView recyclerView;
    private FilmAdapter filmAdapter;
    private SwipeRefreshLayout swipeLayout;
    List<FilmResponse.SingleFilmResult> filmsToDisplay;
    List<FilmResponse.SingleFilmResult> searchedFilm;
    int visibleItems, totalItems, firstVisibleItemPosition;

    private WebServiceFilms webServiceFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Project X");

        setLayout();

        if (checkConnection()) {
            connectionOK();
        } else {
            connectionKO();
        }

    }

    public void setLayout() {
        recyclerView = findViewById(R.id.recyclerFilms);
        swipeLayout = findViewById(R.id.swipeLayout);

        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkConnection()) {
                    connectionOK();
                } else {
                    connectionKO();
                    System.out.println("connectionKO dello swipe");
                }
                //Toast.makeText(MainActivity.this, "Film ricaricati con successo ☑", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        }

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    hideKeyboard(MainActivity.this);
                }
            }
        });

        System.out.println("setLayout()");
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        System.out.println("checkConn()");
        return activeNetworkInfo != null;
    }

    public void connectionOK() {
        webServiceFilms = WebServiceFilms.getInstance();
        filmsToDisplay = new ArrayList<>();
        IWebServer webServer = new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    setLayoutAdapter(films);
                    Toast.makeText(MainActivity.this, "Film caricati con successo ☑", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Errore: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        };
        webServiceFilms.getFilms(API_KEY, LANGUAGE, MainActivity.this, webServer);


        System.out.println("connOK()");
    }

    public void connectionKO() {
        webServiceFilms = WebServiceFilms.getInstance();
        final List<FilmResponse.SingleFilmResult> filmsNoConnection = new ArrayList<>();
        filmsToDisplay = new ArrayList<>();
        final Cursor movies = MainActivity.this.getContentResolver().query(FilmContentProvider.FILMS_URI, null, null, null, null);
        webServiceFilms.getFilms(API_KEY, LANGUAGE, MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    Toast.makeText(MainActivity.this, "no ga da entrar qua", Toast.LENGTH_SHORT).show();
                } else {
                    if (movies != null) {
                        while (movies.moveToNext()) {
                            FilmResponse.SingleFilmResult film = new FilmResponse.SingleFilmResult();
                            film.setId(movies.getColumnIndex(FilmTableHelper._ID));
                            film.setTitle(movies.getString(movies.getColumnIndex(FilmTableHelper.TITOLO)));
                            film.setOverview(movies.getString(movies.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                            film.setPosterPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMMAGINE_COPERTINA)));
                            film.setBackdropPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMMAGINE_DETTAGLIO)));

                            filmsNoConnection.add(film);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Errore, cursor movies null ", Toast.LENGTH_SHORT).show();
                    }
                }
                System.out.println("connKO()");
                System.out.println(movies.getCount() + " dio");
            }
        });
        filmsToDisplay.addAll(filmsNoConnection);
        setLayoutAdapter(filmsToDisplay);
    }

    private void setLayoutAdapter(List<FilmResponse.SingleFilmResult> films) {
        filmAdapter = new FilmAdapter(films, MainActivity.this);
        recyclerView.setAdapter(filmAdapter);
        filmAdapter.notifyDataSetChanged();
        System.out.println("setLayoutAdapter()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Cosa vuoi cercare");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilms(query);
                searchView.clearFocus();
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
                    System.out.println(newText + " dio");
                } else {
                    if (!newText.isEmpty() || newText != "" || newText != null) {
                        filmAdapter.getFilter().filter(newText);
                    } else {
                        filmAdapter.resetFilms();
                        connectionKO();
                        filmAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.onActionViewCollapsed();
                swipeLayout.setEnabled(false);
                searchView.onActionViewExpanded();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                swipeLayout.setEnabled(true);
                searchView.clearFocus();
                if (checkConnection()) {
                    connectionOK();
                } else {
                    connectionKO();
                }
                return true;
            }
        });
        System.out.println("oncreateoptionmenu()");
        return true;
    }

    @Override
    public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
        //films
    }

    private void searchFilms(String QUERY) {
        searchedFilm = new ArrayList<>();
        //final Cursor movies = MainActivity.this.getContentResolver().query(FilmContentProvider.FILMS_URI, null, null, null, null);

        webServiceFilms.searchFilms(API_KEY, QUERY, MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                filmAdapter.resetFilms();
                searchedFilm.clear();
                if (success) {
                    searchedFilm.addAll(films);
                } else {
                    Toast.makeText(MainActivity.this, "Connessione Internet assente!", Toast.LENGTH_SHORT).show();
                }
                filmAdapter.setFilms(searchedFilm);
                filmAdapter.notifyDataSetChanged();
            }
        });
    }

    //metodo per nascondere la tastiera
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
