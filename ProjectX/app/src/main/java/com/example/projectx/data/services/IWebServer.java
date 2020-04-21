package com.example.projectx.data.services;

import com.example.projectx.data.models.Film;

import java.util.List;

public interface IWebServer {
    void onFilmsFetched(boolean success, List<Film> films, int errorCode, String errorMessage);
}
