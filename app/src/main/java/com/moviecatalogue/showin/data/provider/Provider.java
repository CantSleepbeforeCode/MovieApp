package com.moviecatalogue.showin.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moviecatalogue.showin.data.database.FavoriteDatabaseContract;
import com.moviecatalogue.showin.data.database.FavoriteMovieHelper;
import com.moviecatalogue.showin.data.database.FavoriteTvShowHelper;

import java.util.Objects;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.AUTHORITY;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_MOVIE;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_TVSHOW;

public class Provider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteMovieHelper movieHelper;

    static {
        uriMatcher.addURI(AUTHORITY, FavoriteDatabaseContract.TABLE_FAVORITE_MOVIE, MOVIE);
        uriMatcher.addURI(AUTHORITY, FavoriteDatabaseContract.TABLE_FAVORITE_MOVIE + "/#", MOVIE_ID);
    }

    private FavoriteTvShowHelper tvshowHelper;

    static {
        uriMatcher.addURI(AUTHORITY, FavoriteDatabaseContract.TABLE_FAVORITE_TVSHOW, TVSHOW);
        uriMatcher.addURI(AUTHORITY, FavoriteDatabaseContract.TABLE_FAVORITE_TVSHOW + "/#", TVSHOW_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = new FavoriteMovieHelper(getContext());
        tvshowHelper = new FavoriteTvShowHelper(getContext());
        movieHelper.open();
        tvshowHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = tvshowHelper.queryProvider();
                break;
            case TVSHOW_ID:
                cursor = tvshowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        Uri content = null;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                content = CONTENT_URI_MOVIE;
                break;
            case TVSHOW:
                added = tvshowHelper.insertProvider(values);
                content = CONTENT_URI_TVSHOW;
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(content + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case TVSHOW_ID:
                deleted = tvshowHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
        }
        if (deleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
