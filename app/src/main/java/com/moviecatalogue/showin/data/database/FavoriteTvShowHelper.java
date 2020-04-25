package com.moviecatalogue.showin.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.TABLE_FAVORITE_TVSHOW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.TITLE;

public class FavoriteTvShowHelper {
    private static String DATABASE_TABLE_TVSHOW = TABLE_FAVORITE_TVSHOW;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public FavoriteTvShowHelper(Context context) {
        this.context = context;
    }

    public FavoriteTvShowHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public ArrayList<TvShow> query() {
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_TVSHOW,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                tvShow.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(tvShow);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(TvShow tvShow) {
        ContentValues values = new ContentValues();
        values.put(ID, tvShow.getId());
        values.put(POSTER_PATH, tvShow.getPoster_path());
        values.put(OVERVIEW, tvShow.getOverview());
        values.put(TITLE, tvShow.getName());
        return database.insert(DATABASE_TABLE_TVSHOW, null, values);
    }

    public int update(TvShow tvShow) {
        ContentValues values = new ContentValues();
        values.put(ID, tvShow.getId());
        values.put(POSTER_PATH, tvShow.getPoster_path());
        values.put(OVERVIEW, tvShow.getOverview());
        values.put(TITLE, tvShow.getName());
        return database.update(DATABASE_TABLE_TVSHOW, values, ID + " = '" + tvShow.getId() + "'", null);
    }

    public int delete(String tvShowId) {
        return database.delete(DATABASE_TABLE_TVSHOW, ID + " = '" + tvShowId + "'", null);
    }

    public Cursor queryByIdProvider(String tvShowId) {
        return database.query(DATABASE_TABLE_TVSHOW,
                null,
                ID + " = ?",
                new String[]{tvShowId},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE_TVSHOW,
                null,
                null,
                null,
                null,
                null,
                TITLE + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE_TVSHOW, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE_TVSHOW, ID + " = ?", new String[]{id});
    }
}
