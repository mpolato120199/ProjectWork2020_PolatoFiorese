package com.example.projectx.data.services;

import com.example.projectx.data.models.FilmResponse;

import java.util.List;

public interface IWebServer {
    void onFilmsFetched(boolean success, List<FilmResponse.SingleFilmResult> films, int errorCode, String errorMessage);
}
