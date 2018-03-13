package com.devilslegacy.moviesearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.devilslegacy.moviesearch.R;
import com.devilslegacy.moviesearch.model.Movie;

import java.util.List;

/**
 * Created by devil on 3/8/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context context;


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

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
