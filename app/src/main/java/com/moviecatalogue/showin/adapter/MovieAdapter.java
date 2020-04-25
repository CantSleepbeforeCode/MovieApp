package com.moviecatalogue.showin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.activity.detailmovieactivity.DetailMovieActivity;
import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public final static String EXTRA_MOVIE = "movie";
    Context context;
    ArrayList<Movie> movieList;

    public MovieAdapter(ArrayList<Movie> movieList, Context context){
        this.context = context;
        this.movieList = movieList;
    }

    public void refill(List<Movie> items){
        this.movieList = new ArrayList<>();
        this.movieList.addAll(items);

        notifyDataSetChanged();
    }

    @NonNull
    @Nullable
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_movie, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int i){
        viewholder.BindItem(movieList.get(i), context);
    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView coverTitle, coverDescription;
        ImageView imgCover;
        View contentView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            coverTitle = itemView.findViewById(R.id.view_title_movie_cover);
            coverDescription = itemView.findViewById(R.id.view_movie_description_cover);
            imgCover = itemView.findViewById(R.id.img_movie_cover);
            contentView = itemView;
        }

        public void BindItem(final Movie movie, final Context context){
            coverTitle.setText(movie.getTitle());
            coverDescription.setText(movie.getOverview());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185"+movie.getPosterPath())
                    .into(imgCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent moveToDetailMovieIntent = new Intent(context, DetailMovieActivity.class);
                    moveToDetailMovieIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
                    moveToDetailMovieIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(moveToDetailMovieIntent);
                }
            });
        }
    }
}
