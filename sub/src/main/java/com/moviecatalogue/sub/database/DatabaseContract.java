package com.moviecatalogue.sub.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_FAVORITE_MOVIE = "TABLE_FAVORITE_MOVIE";
    public static final String TABLE_FAVORITE_TVSHOW = "TABLE_FAVORITE_TVSHOW";
    public static final String AUTHORITY = "com.moviecatalogue.showin";
    private static final String SCHEME = "content";

    public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_TVSHOW)
            .build();

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_MOVIE)
            .build();

    public static final class favoriteColumns implements BaseColumns {
        public static String ID = "id";
        public static String TITLE = "title";
        public static String POSTER_PATH = "poster_path";
        public static String OVERVIEW = "overview";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }
}
