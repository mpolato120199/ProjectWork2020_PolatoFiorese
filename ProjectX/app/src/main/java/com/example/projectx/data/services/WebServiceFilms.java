package com.example.projectx.data.services;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.projectx.data.database.FilmContentProvider;
import com.example.projectx.data.database.FilmTableHelper;
import com.example.projectx.data.models.FilmResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceFilms {

    private String TODO_BASE_URL = "https://api.themoviedb.org";

    private static WebServiceFilms instance;
    private FilmService filmService;

    private WebServiceFilms (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TODO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        filmService = retrofit.create(FilmService.class);
    }

    public static WebServiceFilms getInstance() {
        if (instance == null)
            instance = new WebServiceFilms();
        return instance;
    }

    public void getFilms(String apiKey, String language, int page, final Context context, final IWebServer callback) {
        Call<FilmResponse> filmsRequest = filmService.getFilms(apiKey, language, page);
        filmsRequest.enqueue(new Callback<FilmResponse>() {
            @Override
            public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                if (response.code() == 200) {
                    FilmResponse results = response.body();
                    List<FilmResponse.SingleFilmResult> filmList = results.getResults();

                    callback.onFilmsFetched(true, filmList, -1, null);

                    context.getContentResolver().delete(FilmContentProvider.FILMS_URI, null, null);

                    for (FilmResponse.SingleFilmResult movie : filmList) {
                        ContentValues cv = new ContentValues();
                        cv.put(FilmTableHelper._ID, movie.getId());
                        cv.put(FilmTableHelper.TITOLO, movie.getTitle());
                        cv.put(FilmTableHelper.DESCRIZIONE, movie.getOverview());
                        cv.put(FilmTableHelper.IMMAGINE_COPERTINA, movie.getPosterPath());
                        cv.put(FilmTableHelper.IMMAGINE_DETTAGLIO, movie.getBackdropPath());
                        context.getContentResolver().insert(FilmContentProvider.FILMS_URI, cv);
                    }
                } else {
                    try {
                        callback.onFilmsFetched(true, null, response.code(), "Error fetching films " + response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        callback.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmResponse> call, Throwable t) {
                callback.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
                Toast.makeText(context, "Server non raggiungibile, verifica la connessione.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchFilms(String apiKey, String query, String language, final Context context, final IWebServer server) {
        Call<FilmResponse> filmsRequest = filmService.searchFilm(apiKey, query, language);

        filmsRequest.enqueue(new Callback<FilmResponse>() {
            @Override
            public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                if (response.code() == 200) {
                    FilmResponse results = response.body();
                    List<FilmResponse.SingleFilmResult> listOfMovies = results.getResults();
                    server.onFilmsFetched(true, listOfMovies, -1, null);

                } else {
                    try {
                        server.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        server.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmResponse> call, Throwable t) {
                server.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }
}
