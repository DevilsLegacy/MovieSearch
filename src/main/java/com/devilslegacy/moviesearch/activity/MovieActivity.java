package com.devilslegacy.moviesearch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.devilslegacy.moviesearch.R;
import com.devilslegacy.moviesearch.adapters.MoviesAdapter;
import com.devilslegacy.moviesearch.model.Movie;
import com.devilslegacy.moviesearch.model.MoviesResponse;
import com.devilslegacy.moviesearch.rest.ApiClient;
import com.devilslegacy.moviesearch.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends Activity {
    private static final String TAG = "DEVIL";
    private static final String TMDB_API_KEY = "4be57921a8785f27e19252173caec7fe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        final RecyclerView recyclerView = findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> data = new HashMap<>();
        data.put("api_key", TMDB_API_KEY);
        data.put("page", "2");
        Call<MoviesResponse> call = apiService.getTopRatedMovies(data);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.e(TAG, "total results: " + response.body().getTotalResults());
                Log.e(TAG, "total pages: " + response.body().getTotalPages());
                List<Movie> moviesList = response.body().getResults();
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new MoviesAdapter(moviesList, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }
}
