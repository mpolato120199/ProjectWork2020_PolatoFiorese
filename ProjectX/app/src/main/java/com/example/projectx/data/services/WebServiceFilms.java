package com.example.projectx.data.services;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.projectx.Film.FilmContentProvider;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.data.models.FilmResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//CLIENT NIGERIA
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

    public void getFilms(String apiKey, final Context context, final IWebServer callback) {
        Call<FilmResponse> filmsRequest = filmService.getFilms(apiKey);
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
                        cv.put(FilmTableHelper.ID, movie.getId());
                        cv.put(FilmTableHelper.TITOLO, movie.getTitle());
                        cv.put(FilmTableHelper.DESCRIZIONE, movie.getOverview());
                        cv.put(FilmTableHelper.IMMAGINE_COPERTINA, movie.getPosterPath());
                        cv.put(FilmTableHelper.IMMAGINE_DETTAGLIO, movie.getBackdropPath());
                        context.getContentResolver().insert(FilmContentProvider.FILMS_URI, cv);
                    }

                    System.out.println("Successo onResponse() andato con successo!");
                } else {
                    try {
                        callback.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        callback.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmResponse> call, Throwable t) {
                callback.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
                System.out.println("Errore entrato nell' OnFailure()");
            }
        });
    }
}
