package com.moviecatalogue.sub.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moviecatalogue.sub.R;
import com.moviecatalogue.sub.activity.detailmovieactivity.DetailMovieActivity;
import com.moviecatalogue.sub.entity.Movie;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder> {
    private Cursor cursor;
    private Context context;
    private ArrayList<Movie> movieList;

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public void setListMovie(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteMovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieViewHolder holder, int position) {
        final Movie movie = getItem(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieDescription.setText(movie.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w92" + movie.getPosterPath())
                .into(holder.movieCover);

        holder.movieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDetailMovieActivity = new Intent(context, DetailMovieActivity.class);
                moveToDetailMovieActivity.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
                moveToDetailMovieActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(moveToDetailMovieActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position Invalid");
        }
        return new Movie(cursor);
    }

    public class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {
        private TextView movieTitle, movieDescription;
        private ImageView movieCover;
        private View movieView;

        public FavoriteMovieViewHolder(@NonNull View view) {
            super(view);
            movieTitle = view.findViewById(R.id.view_title_movie_cover);
            movieDescription = view.findViewById(R.id.view_movie_description_cover);
            movieCover = view.findViewById(R.id.img_movie_cover);
            movieView = view;
        }

    }
}
