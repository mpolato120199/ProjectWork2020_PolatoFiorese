package com.example.projectx.data.services;

import com.example.projectx.data.models.Film;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmService {

    @GET("?apikey=8e53b138&s=avengers")
    Call<List<Film>> getFilms();
}
