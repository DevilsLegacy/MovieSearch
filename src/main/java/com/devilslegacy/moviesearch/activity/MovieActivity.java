package com.devilslegacy.moviesearch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.devilslegacy.moviesearch.R;
import com.devilslegacy.moviesearch.adapters.MoviesAdapter;
import com.devilslegacy.moviesearch.model.Movie;
import com.devilslegacy.moviesearch.model.MoviesResponse;
import com.devilslegacy.moviesearch.prefs.PrefConstants;
import com.devilslegacy.moviesearch.prefs.SharedPreferenceHelper;
import com.devilslegacy.moviesearch.rest.ApiClient;
import com.devilslegacy.moviesearch.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = "MovieActivity";
    private static final String TMDB_API_KEY = "4be57921a8785f27e19252173caec7fe";

    private static final int TOTAL_PAGES = 10;
    private static final int GRID_SPAN_COUNT = 2;

    private ApiInterface mApiService;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private Map<String, String> mQueryMapData;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private int mCurrentPage = 1;
    private boolean isLoading = false;

    private boolean isGridType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.movies_recycler_view);

        isGridType = SharedPreferenceHelper.getInstance().getPrefBoolean(this,
                PrefConstants.KEY_GRID_TYPE, false);

        mMoviesAdapter = new MoviesAdapter(this);
        setLayoutManager();
        mRecyclerView.addOnScrollListener(new onScrollListener());
        mRecyclerView.setAdapter(mMoviesAdapter);

        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mQueryMapData = new HashMap<>();
        loadMoviePage(mCurrentPage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_toggle_item_view:
                isGridType = !isGridType;
                SharedPreferenceHelper.getInstance().putPrefBoolean(this, PrefConstants.KEY_GRID_TYPE, isGridType);
                setLayoutManager();
                supportInvalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_toggle_item_view);
        menuItem.setIcon(isGridType ? R.drawable.ic_view_grid : R.drawable.ic_view_list);
        return super.onPrepareOptionsMenu(menu);
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
            int totalItemCount = recyclerView.getAdapter().getItemCount();
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            Log.d(TAG, "totalItemCount : " + totalItemCount + " : visibleItemCount : " +
                    visibleItemCount + " : firstVisibleItemPosition : " + firstVisibleItemPosition);

            // isLoading check is required as there can be multiple onScroll calls for which
            // "visibleItemCount + firstVisibleItemPosition == totalItemCount)" can be true for same values.
            // This can cause IllegalStateException in RecyclerView
            if (!isLoading && firstVisibleItemPosition >= 0 && mCurrentPage < TOTAL_PAGES && (visibleItemCount + firstVisibleItemPosition == totalItemCount)) {
                mCurrentPage++;
                loadMoviePage(mCurrentPage);
            }
        }
    }

    private void setLayoutManager() {
        int scrollPosition = 0;

        // Maintain position while changing layout managers.
        if (null != mRecyclerView.getLayoutManager()) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        if (isGridType) {
            mGridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mMoviesAdapter.getItemViewType(position)) {
                        case MoviesAdapter.TYPE_FOOTER_ITEM:
                            return mGridLayoutManager.getSpanCount();
                        case MoviesAdapter.TYPE_MOVIE_ITEM:
                            return 1;
                        default:
                            return -1;
                    }
                }
            });
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        } else {
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void loadMoviePage(int pageNumber) {
        if (mCurrentPage != 1) {
            isLoading = true;
            mMoviesAdapter.addFooterItem();
        }
        mQueryMapData.clear();
        mQueryMapData.put("api_key", TMDB_API_KEY);
        mQueryMapData.put("page", Integer.toString(pageNumber));

        Call<MoviesResponse> call = mApiService.getTopRatedMovies(mQueryMapData);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.d(TAG, "total results: " + response.body().getTotalResults());
                Log.d(TAG, "total pages: " + response.body().getTotalPages());
                if (mCurrentPage != 1) {
                    isLoading = false;
                    mMoviesAdapter.removeFooterItem();
                }
                List<Movie> moviesList = response.body().getResults();
                mProgressBar.setVisibility(View.GONE);
                mMoviesAdapter.setMoviesList(moviesList);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (mCurrentPage != 1) {
                    isLoading = false;
                    mMoviesAdapter.removeFooterItem();
                }
            }
        });
    }

}
