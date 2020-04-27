package com.example.projectx.data.services;

import com.example.projectx.data.models.FilmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface FilmService {

// https://api.themoviedb.org/3/trending/movie/week?api_key=a979a7eb2578017177824cf553c182ef
// controlla se cambiano durante la settimana l'ordine dei campi essendo trending della settimana

    @GET("/3/trending/movie/week?api_key=a979a7eb2578017177824cf553c182ef")
    Call<FilmResponse> getFilms(
            @Query("api_key") String apiKey
    );
}
