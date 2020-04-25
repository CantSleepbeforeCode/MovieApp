package com.moviecatalogue.showin.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.TABLE_FAVORITE_MOVIE;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.TITLE;

public class FavoriteMovieHelper {
    private static String DATABASE_TABLE_MOVIE = TABLE_FAVORITE_MOVIE;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public FavoriteMovieHelper(Context context) {
        this.context = context;
    }

    public FavoriteMovieHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public ArrayList<Movie> query() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(ID, movie.getId());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(OVERVIEW, movie.getOverview());
        values.put(TITLE, movie.getTitle());
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public int update(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(ID, movie.getId());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(OVERVIEW, movie.getOverview());
        values.put(TITLE, movie.getTitle());
        return database.update(DATABASE_TABLE_MOVIE, values, ID + " = '" + movie.getId() + "'", null);
    }

    public int delete(String movieId) {
        return database.delete(DATABASE_TABLE_MOVIE, ID + " = '" + movieId + "'", null);
    }

    public Cursor queryByIdProvider(String movieId) {
        return database.query(DATABASE_TABLE_MOVIE,
                null,
                ID + " = ?",
                new String[]{movieId},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                TITLE + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, ID + " = ?", new String[]{id});
    }

}
