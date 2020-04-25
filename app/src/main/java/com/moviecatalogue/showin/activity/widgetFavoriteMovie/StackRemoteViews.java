package com.moviecatalogue.showin.activity.widgetFavoriteMovie;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.adapter.MovieAdapter;
import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_MOVIE;

public class StackRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Movie> widgetItem = new ArrayList<>();
    private Context mContext;

    StackRemoteViews(Context context, Intent intent) {
        mContext = context;
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        widgetItem.clear();
        final long identifyToken = Binder.clearCallingIdentity();
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                widgetItem.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        if (cursor != null) {
            cursor.close();
        }
        Binder.restoreCallingIdentity(identifyToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItem.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Movie movie;
        Bundle bundle = new Bundle();
        Bitmap bitmap = null;
        String releaseDate = null;
        String baseUrl = "https://image.tmdb.org/t/p/w500";
        try {
            movie = widgetItem.get(i);
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(baseUrl + movie.getPosterPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            releaseDate = movie.getReleaseDate();
            bundle.putString(MovieAdapter.EXTRA_MOVIE, movie.getTitle());
        } catch (InterruptedException | ExecutionException | IndexOutOfBoundsException e) {}
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_movie_item);
        remoteViews.setImageViewBitmap(R.id.banner_image_movie_view, bitmap);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.banner_image_movie_view, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
