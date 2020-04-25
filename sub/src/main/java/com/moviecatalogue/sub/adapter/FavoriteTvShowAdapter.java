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
import com.moviecatalogue.sub.activity.detailtvshowactivity.DetailTvShowActivity;
import com.moviecatalogue.sub.entity.TvShow;

import java.util.ArrayList;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.FavoriteTvShowViewHolder> {
    private Cursor cursor;
    private Context context;
    private ArrayList<TvShow> TvShowList;

    public FavoriteTvShowAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<TvShow> getTvShowList() {
        return TvShowList;
    }

    public void setListTvshow(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteTvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteTvShowViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_tv_show, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteTvShowViewHolder holder, int position) {
        final TvShow tvShow = getItem(position);
        holder.tvShowTitle.setText(tvShow.getName());
        holder.tvShowDescription.setText(tvShow.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w92" + tvShow.getPoster_path())
                .into(holder.tvShowCover);

        holder.tvShowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDetailTvShowActivity = new Intent(context, DetailTvShowActivity.class);
                moveToDetailTvShowActivity.putExtra(DetailTvShowActivity.EXTRA_TVSHOW, tvShow);
                moveToDetailTvShowActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(moveToDetailTvShowActivity);
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

    public class FavoriteTvShowViewHolder extends RecyclerView.ViewHolder {
        private TextView tvShowTitle, tvShowDescription;
        private ImageView tvShowCover;
        private View tvShowView;

        public FavoriteTvShowViewHolder(@NonNull View view) {
            super(view);
            tvShowTitle = view.findViewById(R.id.view_title_tvshow_cover);
            tvShowDescription = view.findViewById(R.id.view_tvshow_description_cover);
            tvShowCover = view.findViewById(R.id.img_tvshow_cover);
            tvShowView = view;
        }

    }
}
