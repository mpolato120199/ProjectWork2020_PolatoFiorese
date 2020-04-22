package com.example.projectx.data.services;

import com.example.projectx.data.models.Film;
import com.example.projectx.data.models.FilmResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmService {

    @GET("&s=avengers")
    Call<FilmResponse> getFilms();
}
