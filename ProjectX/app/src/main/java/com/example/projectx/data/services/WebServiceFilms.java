package com.example.projectx.data.services;

import android.util.Log;

import com.example.projectx.data.models.Film;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceFilms {

    private String TODO_BASE_URL = "http://www.omdbapi.com/";

    private static WebServiceFilms instance;
    private FilmService todoService;

    private WebServiceFilms (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TODO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        todoService = retrofit.create(FilmService.class);
    }

    public static WebServiceFilms getInstance() {
        if (instance == null)
            instance = new WebServiceFilms();
        return instance;
    }

    public void getFilms(final IWebServer callback) {
        Call<List<Film>> todosRequest = todoService.getFilms();
        //enqueue metodo asincrono di retrofit per la chiamata
        //con una callback ci avvisa alla fine dell'esecuzione
        todosRequest.enqueue(new Callback<List<Film>>() {
            @Override
            //se la chiamata va bene
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                //controllo che sia 200 perche entra nel onResponse quando restituisce un codice sia negativo che positivo
                if (response.code() == 200) {
                    callback.onFilmsFetched(true, response.body(), -1, null);
                } else {
                    try {
                        callback.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        callback.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }
            //se la chiamata non va bene
            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {
                callback.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }
}
