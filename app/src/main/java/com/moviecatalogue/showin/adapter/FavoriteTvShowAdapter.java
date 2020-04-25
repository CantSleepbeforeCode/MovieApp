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
import com.moviecatalogue.showin.activity.detailtvshowactivity.DetailTvShowActivity;
import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.FavoriteViewHolder> {
    private Cursor cursor;
    private Context context;
    private ArrayList<TvShow> tvShowList;

    public FavoriteTvShowAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<TvShow> getListTvShow() {
        return tvShowList;
    }

    public void setListTvshow(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_tv_show, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        final TvShow tvShow = getItem(i);
        favoriteViewHolder.coverTitle.setText(tvShow.getName());
        favoriteViewHolder.coverDescription.setText(tvShow.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185"+tvShow.getPoster_path())
                .into(favoriteViewHolder.imgCover);

        favoriteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDetailTvShowIntent = new Intent(context, DetailTvShowActivity.class);
                moveToDetailTvShowIntent.putExtra(DetailTvShowActivity.EXTRA_TVSHOW, tvShow);
                moveToDetailTvShowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(moveToDetailTvShowIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    private TvShow getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position Invalid");
        }
        return new TvShow(cursor);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView coverTitle, coverDescription;
        ImageView imgCover;
        View contentView;

        public FavoriteViewHolder(@NonNull View view) {
            super(view);
            coverTitle = itemView.findViewById(R.id.view_title_tvshow_cover);
            coverDescription = itemView.findViewById(R.id.view_tvshow_description_cover);
            imgCover = itemView.findViewById(R.id.img_tvshow_cover);
            contentView = itemView;
        }

    }
}
