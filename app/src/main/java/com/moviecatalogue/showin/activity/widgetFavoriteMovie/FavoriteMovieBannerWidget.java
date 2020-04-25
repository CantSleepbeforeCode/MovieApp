package com.moviecatalogue.showin.activity.widgetFavoriteMovie;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.adapter.MovieAdapter;

import java.util.Objects;

public class FavoriteMovieBannerWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.moviecatalogue.showin.TOAST_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_banner);
        remoteViews.setRemoteAdapter(R.id.stack_view_movie, intent);
        remoteViews.setEmptyView(R.id.stack_view_movie, R.id.empty_view_movie);

        Intent toastIntent = new Intent(context, FavoriteMovieBannerWidget.class);
        toastIntent.setAction(FavoriteMovieBannerWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stack_view_movie, toastPendingIntent);
        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if(Objects.requireNonNull(intent.getAction()).equals(TOAST_ACTION)){
            String title  = intent.getStringExtra(MovieAdapter.EXTRA_MOVIE);

            Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
        }
        ComponentName thisWidget = new ComponentName(context, FavoriteMovieBannerWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view_movie);
        super.onReceive(context, intent);
    }
}
