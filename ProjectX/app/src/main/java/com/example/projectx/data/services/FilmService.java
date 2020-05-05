package com.example.projectx.data.services;

import com.example.projectx.data.models.FilmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface FilmService {

// https://api.themoviedb.org/3/trending/movie/week?api_key=a979a7eb2578017177824cf553c182ef
    @GET("/3/trending/movie/week?")
    Call<FilmResponse> getFilms(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("/3/search/movie?")
    Call<FilmResponse> searchFilm(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );
}
