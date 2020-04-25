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
import com.moviecatalogue.showin.activity.detailtvshowactivity.DetailTvShowActivity;
import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {
    public final static String EXTRA_TVSHOW = "TvShow";
    Context context;
    ArrayList<TvShow> tvShowList;

    public TvShowAdapter(ArrayList<TvShow> tvShowList, Context context){
        this.tvShowList = tvShowList;
        this.context = context;
    }

    public void refill(List<TvShow> items){
        this.tvShowList = new ArrayList<>();
        this.tvShowList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Nullable
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_tv_show, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
        viewHolder.BindItem(tvShowList.get(i), context);
    }

    @Override
    public int getItemCount(){
        return tvShowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coverTitle, coverDescription;
        ImageView imgCover;
        View contentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverTitle = itemView.findViewById(R.id.view_title_tvshow_cover);
            coverDescription = itemView.findViewById(R.id.view_tvshow_description_cover);
            imgCover = itemView.findViewById(R.id.img_tvshow_cover);
            contentView = itemView;
        }

        public void BindItem(final TvShow tvShows, final Context context){
            coverTitle.setText(tvShows.getName());
            coverDescription.setText(tvShows.getOverview());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185"+tvShows.getPoster_path())
                    .into(imgCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent moveToDetailTvShowIntent = new Intent(context, DetailTvShowActivity.class);
                    moveToDetailTvShowIntent.putExtra(DetailTvShowActivity.EXTRA_TVSHOW, tvShows);
                    moveToDetailTvShowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(moveToDetailTvShowIntent);
                }
            });
        }
    }

}
