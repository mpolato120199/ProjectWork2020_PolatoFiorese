package com.example.projectx.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmResponse {
/*
    {
        "Search": [...],
        "totalResults": "99",
        "Response": "True"
    }
 */

    @SerializedName("Search")
    private List<Film> films;
    private String totalResults;
    @SerializedName("Response")
    private String response;

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
