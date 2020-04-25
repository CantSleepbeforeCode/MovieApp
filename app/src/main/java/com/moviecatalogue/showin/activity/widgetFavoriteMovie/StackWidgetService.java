package com.moviecatalogue.showin.activity.widgetFavoriteMovie;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViews(this.getApplicationContext(), intent);
    }
}
