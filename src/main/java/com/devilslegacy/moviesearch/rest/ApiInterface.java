package com.devilslegacy.moviesearch.rest;

import com.devilslegacy.moviesearch.model.MoviesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by devil on 3/5/2018.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@QueryMap Map<String, String> options);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
