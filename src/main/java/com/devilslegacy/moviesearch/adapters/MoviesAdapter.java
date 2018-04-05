package com.devilslegacy.moviesearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context context;

    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static String IMAGE_QUALITY = "w300";

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

    public MoviesAdapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.context = context;
        movies = new ArrayList<>();
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movieName.setText(movies.get(position).getTitle());
        holder.movieDescription.setText(movies.get(position).getOverview());
        holder.ratingBar.setRating(movies.get(position).getVoteAverage() / 2);
        holder.movieRating.setText(movies.get(position).getVoteAverage().toString());
        GlideApp
                .with(context)
                .load(IMAGE_BASE_URL + IMAGE_QUALITY + movies.get(position).getBackdropPath())
                .placeholder(R.drawable.movie_backdrop_placeholder)
                .into(holder.movieBanner);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMoviesList(List<Movie> moviesList) {
        movies.addAll(moviesList);
        notifyDataSetChanged();
    }
}
