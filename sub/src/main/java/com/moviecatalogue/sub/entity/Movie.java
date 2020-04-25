package com.moviecatalogue.sub.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.TITLE;
import static com.moviecatalogue.sub.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("id")
    private String id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(id);
    }

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.title = getColumnString(cursor, TITLE);
        this.id=getColumnString(cursor, ID);
        this.overview=getColumnString(cursor, OVERVIEW);
        this.posterPath=getColumnString(cursor, POSTER_PATH);
    }


    protected Movie(Parcel in) {
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        id = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
