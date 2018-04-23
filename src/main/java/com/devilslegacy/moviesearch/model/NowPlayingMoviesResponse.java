package com.devilslegacy.moviesearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NowPlayingMoviesResponse extends MoviesResponse {
    @SerializedName("dates")
    private Dates dates;

    public NowPlayingMoviesResponse(int page, List<Movie> results, Dates dates, int totalResults, int totalPages) {
        super(page, results, totalResults, totalPages);
        this.dates = dates;
    }

    public Dates getDates() {
        return dates;
    }
}
