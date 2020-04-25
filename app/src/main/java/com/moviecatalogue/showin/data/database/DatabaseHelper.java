package com.moviecatalogue.showin.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "db_favorite";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            FavoriteDatabaseContract.TABLE_FAVORITE_MOVIE,
            FavoriteDatabaseContract.favoriteColumns._ID,
            FavoriteDatabaseContract.favoriteColumns.ID,
            FavoriteDatabaseContract.favoriteColumns.TITLE,
            FavoriteDatabaseContract.favoriteColumns.POSTER_PATH,
            FavoriteDatabaseContract.favoriteColumns.OVERVIEW);

    private static final String SQL_CREATE_TABLE_TVSHOW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            FavoriteDatabaseContract.TABLE_FAVORITE_TVSHOW,
            FavoriteDatabaseContract.favoriteColumns._ID,
            FavoriteDatabaseContract.favoriteColumns.ID,
            FavoriteDatabaseContract.favoriteColumns.TITLE,
            FavoriteDatabaseContract.favoriteColumns.POSTER_PATH,
            FavoriteDatabaseContract.favoriteColumns.OVERVIEW);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TVSHOW);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteDatabaseContract.TABLE_FAVORITE_TVSHOW);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteDatabaseContract.TABLE_FAVORITE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
