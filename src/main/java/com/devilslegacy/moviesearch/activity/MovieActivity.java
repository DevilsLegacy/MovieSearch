package com.devilslegacy.moviesearch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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

    private static final String TAG = "MovieActivity";
    private static final String TMDB_API_KEY = "4be57921a8785f27e19252173caec7fe";

    private static final int TOTAL_PAGES = 10;

    private ApiInterface mApiService;
    private LinearLayoutManager mLinearLayoutManager;
    private Map<String, String> mQueryMapData;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private int mCurrentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.movies_recycler_view);

        mMoviesAdapter = new MoviesAdapter(R.layout.list_item_movie, this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new onScrollListener());
        mRecyclerView.setAdapter(mMoviesAdapter);

        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mQueryMapData = new HashMap<>();
        loadMoviePage(mCurrentPage);

    }

    private class onScrollListener extends RecyclerView.OnScrollListener {

        private boolean isScrolling;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int totalItemCount = mLinearLayoutManager.getItemCount();
            int visibleItemCount = mLinearLayoutManager.getChildCount();
            int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();

            Log.d(TAG, "totalItemCount : " + totalItemCount + " : visibleItemCount : " +
                    visibleItemCount + " : firstVisibleItemPosition : " + firstVisibleItemPosition);

            if (isScrolling && firstVisibleItemPosition >= 0 && mCurrentPage < TOTAL_PAGES && (visibleItemCount + firstVisibleItemPosition == totalItemCount)) {
                isScrolling = false;
                mCurrentPage++;
                loadMoviePage(mCurrentPage);
            }
        }
    }

    private void loadMoviePage(int pageNumber) {
        mQueryMapData.clear();
        mQueryMapData.put("api_key", TMDB_API_KEY);
        mQueryMapData.put("page", Integer.toString(pageNumber));

        Call<MoviesResponse> call = mApiService.getTopRatedMovies(mQueryMapData);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.d(TAG, "total results: " + response.body().getTotalResults());
                Log.d(TAG, "total pages: " + response.body().getTotalPages());
                List<Movie> moviesList = response.body().getResults();
                mProgressBar.setVisibility(View.GONE);
                mMoviesAdapter.setMoviesList(moviesList);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }
}
