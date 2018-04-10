package com.devilslegacy.moviesearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devilslegacy.moviesearch.R;
import com.devilslegacy.moviesearch.glide.GlideApp;
import com.devilslegacy.moviesearch.glide.MovieAppGlideModule;
import com.devilslegacy.moviesearch.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devil on 3/8/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_QUALITY = "w300";

    private static final int TYPE_FOOTER_ITEM = 0;
    private static final int TYPE_MOVIE_ITEM = 1;

    private List<Movie> movies;
    private Context context;

    private boolean isLoading = false;

    // ViewHolder for Movie Item.
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout movieLayout;
        ImageView movieBanner;
        TextView movieName;
        TextView movieDescription;
        RatingBar ratingBar;
        TextView movieRating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieLayout = itemView.findViewById(R.id.movie_layout);
            movieBanner = itemView.findViewById(R.id.movie_banner);
            movieName = itemView.findViewById(R.id.movie_name);
            movieDescription = itemView.findViewById(R.id.movie_description);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            movieRating = itemView.findViewById(R.id.movie_rating);
        }
    }

    // View Holder for Footer Item. (Loading Progress Bar)
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.footer_progress_bar);
        }
    }

    public MoviesAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MOVIE_ITEM) {
            View movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
            return new MovieViewHolder(movieItemView);
        } else {
            View footerItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_footer, parent, false);
            return new FooterViewHolder(footerItemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            movieViewHolder.movieName.setText(movies.get(position).getTitle());
            movieViewHolder.movieDescription.setText(movies.get(position).getOverview());
            movieViewHolder.ratingBar.setRating(movies.get(position).getVoteAverage() / 2);
            movieViewHolder.movieRating.setText(movies.get(position).getVoteAverage().toString());
            GlideApp
                    .with(context)
                    .load(IMAGE_BASE_URL + IMAGE_QUALITY + movies.get(position).getBackdropPath())
                    .placeholder(R.drawable.movie_backdrop_placeholder)
                    .into(movieViewHolder.movieBanner);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading && (position == movies.size() - 1) ? TYPE_FOOTER_ITEM : TYPE_MOVIE_ITEM;
    }

    public void setMoviesList(List<Movie> moviesList) {
        movies.addAll(moviesList);
        notifyDataSetChanged();
    }

    public void addFooterItem() {
        isLoading = true;
        movies.add(new Movie());
        notifyItemInserted(movies.size() - 1);
    }

    public void removeFooterItem() {
        isLoading = false;
        movies.remove(movies.size() - 1);
        notifyItemRemoved(movies.size());
    }
}
