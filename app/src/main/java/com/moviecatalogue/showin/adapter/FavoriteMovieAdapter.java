package com.moviecatalogue.showin.adapter;

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
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.activity.detailmovieactivity.DetailMovieActivity;
import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteViewHolder> {
    private Cursor cursor;
    private Context context;
    private ArrayList<Movie> movieList;

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return movieList;
    }

    public void setListMovie(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_movie, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        final Movie movie = getItem(i);
        favoriteViewHolder.coverTitle.setText(movie.getTitle());
        favoriteViewHolder.coverDescription.setText(movie.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185"+movie.getPosterPath())
                .into(favoriteViewHolder.imgCover);

        favoriteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDetailMovieIntent = new Intent(context, DetailMovieActivity.class);
                moveToDetailMovieIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
                moveToDetailMovieIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(moveToDetailMovieIntent);
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

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView coverTitle, coverDescription;
        ImageView imgCover;
        View contentView;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            coverTitle = itemView.findViewById(R.id.view_title_movie_cover);
            coverDescription = itemView.findViewById(R.id.view_movie_description_cover);
            imgCover = itemView.findViewById(R.id.img_movie_cover);
            contentView = itemView;
        }
    }

}
