package com.devilslegacy.moviesearch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.devilslegacy.moviesearch.R;
import com.devilslegacy.moviesearch.model.MoviesResponse;
import com.devilslegacy.moviesearch.rest.ApiClient;
import com.devilslegacy.moviesearch.rest.ApiInterface;

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

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(TMDB_API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.e(TAG, "response status: "+ response.headers().get("status"));
                Log.e(TAG, "response value: "+ String.valueOf(response.body().getTotalPages()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }
}
