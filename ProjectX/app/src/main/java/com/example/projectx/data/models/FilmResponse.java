package com.example.projectx.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmResponse {
    /*
        {
      "page": 12,
      "total_results": 7180,
      "total_pages": 359,
      "results": Array[20][
        {
          "popularity": 11.888,
          "vote_count": 948,
          "video": false,
          "poster_path": "/rO2Fq0AZZx9obs52KJdx4mRE8p5.jpg",
          "id": 1092,
          "adult": false,
          "backdrop_path": "/mYI1VlxvuSnHoK6GYwkFzAgcJXh.jpg",
          "original_language": "en",
          "original_title": "The Third Man",
          "genre_ids": Array[2][
            9648,
            53
          ],
          "title": "Il terzo uomo",
          "vote_average": 8,
          "overview": "Nel 1946 in una Vienna devastata dalla guerra e divisa in quattro zone di occupazione, lo scrittore americano di western Holly Martins (Cotten) assiste ai funerali dell'amico Harry Lime (Welles), ma è veramente morto? Inseguimento finale nelle fogne della città.",
          "release_date": "1949-08-31"
        },
        ...
      ]
    }
     */
    private int page;

    private List<SingleFilmResult> results = null;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SingleFilmResult> getResults() {
        return results;
    }

    public void setResults(List<SingleFilmResult> results) {
        this.results = results;
    }

    public static class SingleFilmResult {

        private String topRatedFilms;

        private boolean adult;

        @SerializedName("backdrop_path")
        private String backdropPath;

        @SerializedName("genre_ids")
        private List<Integer> genreIds = null;

        private int id;

        @SerializedName("original_language")
        private String language;

        @SerializedName("original_title")
        private String originalTitle;

        private String overview;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("release_date")
        private String releaseDate;

        private String title;

        private boolean video;

        @SerializedName("vote_average")
        private double voteAverage;

        @SerializedName("vote_count")
        private int voteCount;

        private double popularity;

        @SerializedName("media_type")
        private String mediaType;

        public String getTopRatedFilms() {
            return topRatedFilms;
        }

        public void setTopRatedFilms(String topRatedFilms) {
            this.topRatedFilms = topRatedFilms;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }
    }
}
