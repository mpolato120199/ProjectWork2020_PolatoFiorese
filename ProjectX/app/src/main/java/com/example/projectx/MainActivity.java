package com.example.projectx;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectx.data.models.Film;
import com.example.projectx.data.services.IWebServer;
import com.example.projectx.data.services.WebServiceFilms;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ProgressBar loadingBar;
    RecyclerView recyclerView;
    List<Film> filmList = new ArrayList<>();
    FilmAdapter filmAdapter;
    ProgressDialog progressDialog;

    private WebServiceFilms webServiceFilms;

    private IWebServer webServerListener = new IWebServer() {
        @Override
        public void onFilmsFetched(boolean success, List<Film> films, int errorCode, String errorMessage) {
            if (success) {
                //filmAdapter.setFilmList(films);
                filmAdapter.notifyDataSetChanged();
                loadingBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                loadingBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MainActivity");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerFilms);
        webServiceFilms = WebServiceFilms.getInstance();
        loadingBar = findViewById(R.id.loading_bar);

        //metto 100 caselle di prova e uso il gridlayout
        for (int i=0; i<99; i++){
            filmList.add(new Film("Film " + i, "20" + i, "10" + i, "Film", ""));
        }

        filmAdapter = new FilmAdapter(filmList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(filmAdapter);
        filmAdapter.notifyDataSetChanged();
    }

    private void loadFilms() {
        loadingBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        webServiceFilms.getFilms(webServerListener);
    }
}
