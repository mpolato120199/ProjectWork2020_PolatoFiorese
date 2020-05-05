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
    private int PAGE = 1;

    private RecyclerView recyclerView;
    private FilmAdapter filmAdapter;
    private SwipeRefreshLayout swipeLayout;
    private GridLayoutManager layoutManager;
    List<FilmResponse.SingleFilmResult> filmsToDisplay;
    List<FilmResponse.SingleFilmResult> filmsNoConnection;
    List<FilmResponse.SingleFilmResult> searchedFilms;

    private WebServiceFilms webServiceFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Project X");

        setLayout();

        filmsToDisplay = new ArrayList<>();
        filmAdapter = new FilmAdapter(filmsToDisplay, MainActivity.this);
        recyclerView.setAdapter(filmAdapter);

        if (checkConnection()) {
            connectionOK();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        hideKeyboard(MainActivity.this);
                    }
                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        PAGE++;
                        connectionOK();
                    }
                }
            });
        } else {
            connectionKO();
        }

    }

    public void setLayout() {
        recyclerView = findViewById(R.id.recyclerFilms);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(MainActivity.this, 4);
        } else {
            layoutManager = new GridLayoutManager(MainActivity.this, 2);
        }

        recyclerView.setLayoutManager(layoutManager);

        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkConnection()) {
                    connectionOK();
                } else {
                    swipeLayout.setRefreshing(false);
                    //connectionKO();
                    System.out.println("connectionKO dello swipe");
                }
                //Toast.makeText(MainActivity.this, "Film ricaricati con successo ☑", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
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
        webServiceFilms.getFilms(API_KEY, LANGUAGE, PAGE, MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    filmsToDisplay.addAll(films);
                    filmAdapter.setFilms(filmsToDisplay);
                    filmAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Film pagina " + PAGE + " caricati correttamente ☑", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Errore: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        System.out.println(filmsToDisplay.size() + " filmtodisplay size");
        System.out.println("connOK()");
    }

    public void connectionKO() {
        webServiceFilms = WebServiceFilms.getInstance();
        filmsNoConnection = new ArrayList<>();
        final Cursor movies = MainActivity.this.getContentResolver().query(FilmContentProvider.FILMS_URI, null, null, null, null);
        webServiceFilms.getFilms(API_KEY, LANGUAGE, PAGE,MainActivity.this, new IWebServer() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                if (success) {
                    Toast.makeText(MainActivity.this, "Errore, success non dovrebbe essere true", Toast.LENGTH_SHORT).show();
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

                filmAdapter.setFilms(filmsNoConnection);
                filmAdapter.notifyDataSetChanged();
            }
        });

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
                /*if (checkConnection()) {
                    if (newText != null && !newText.isEmpty()) {
                        searchFilms(newText);
                    } else {
                        filmAdapter.resetFilms();
                        connectionOK();
                        filmAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (newText != null && !newText.isEmpty() ) {
                        searchFilms(newText);
                    } else {
                        filmAdapter.resetFilms();
                        connectionKO();
                        filmAdapter.notifyDataSetChanged();
                    }
                }*/
                if (newText != null && !newText.isEmpty()){
                    searchFilms(newText);
                } else if (checkConnection()){
                    filmAdapter.resetFilms();
                    connectionOK();
                } else {
                    filmAdapter.resetFilms();
                    connectionKO();
                }
                filmAdapter.notifyDataSetChanged();
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
                filmAdapter.resetFilms();
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
        searchedFilms = new ArrayList<>();
        final Cursor cursorNoConnection = MainActivity.this.getContentResolver().query(FilmContentProvider.FILMS_URI, null, FilmTableHelper.TITOLO + " LIKE \'%" + QUERY + "%\'", null, null);

        if (checkConnection()) {
            webServiceFilms.searchFilms(API_KEY, QUERY, LANGUAGE, MainActivity.this, new IWebServer() {
                @Override
                public void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage) {
                    filmAdapter.resetFilms();
                    searchedFilms.clear();
                    if (success) {
                        searchedFilms.addAll(films);
                        filmAdapter.setFilms(searchedFilms);
                        filmAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Connessione Internet assente!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            System.out.println("count del cursor ricerca" + cursorNoConnection.getCount());
            if (cursorNoConnection != null) {
                while (cursorNoConnection.moveToNext()) {
                    FilmResponse.SingleFilmResult film = new FilmResponse.SingleFilmResult();
                    film.setId(cursorNoConnection.getColumnIndex(FilmTableHelper._ID));
                    film.setTitle(cursorNoConnection.getString(cursorNoConnection.getColumnIndex(FilmTableHelper.TITOLO)));
                    film.setOverview(cursorNoConnection.getString(cursorNoConnection.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                    film.setPosterPath(cursorNoConnection.getString(cursorNoConnection.getColumnIndex(FilmTableHelper.IMMAGINE_COPERTINA)));
                    film.setBackdropPath(cursorNoConnection.getString(cursorNoConnection.getColumnIndex(FilmTableHelper.IMMAGINE_DETTAGLIO)));

                    searchedFilms.add(film);
                }
                System.out.println("size del searchedfilms : " + searchedFilms.size());
                filmAdapter.resetFilms();
                filmAdapter.setFilms(searchedFilms);
                filmAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Errore, cursor movies null ", Toast.LENGTH_SHORT).show();
            }
        }
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
