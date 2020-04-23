package com.example.projectx.data.models;

import android.provider.BaseColumns;

public class Film {
    /*
    {
      "Title": "The Avengers",
      "Year": "2012",
      "imdbID": "tt0848228",
      "Type": "movie",
      "Poster": "https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg"
    }
     */

    private String Title;
    private String Year;
    private int ID;
    private String BackDrop;
    private String Poster;

    private String Desc;

    public Film(String title, String year, int ID, String backDrop, String poster) {
        Title = title;
        Year = year;
        this.ID = ID;
        BackDrop = backDrop;
        Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getDesc() { return Desc; }

    public void setDesc(String desc) { Desc = desc; }

    public String getBackDrop() { return BackDrop; }

    public void setBackDrop(String backDrop) { BackDrop = backDrop; }
}
