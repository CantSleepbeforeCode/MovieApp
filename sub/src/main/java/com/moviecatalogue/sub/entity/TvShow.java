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

public class TvShow implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String poster_path;

    public TvShow(){

    }

    public TvShow(Cursor cursor) {
        this.name= getColumnString(cursor, TITLE);
        this.id=getColumnString(cursor, ID);
        this.overview=getColumnString(cursor, OVERVIEW);
        this.poster_path=getColumnString(cursor, POSTER_PATH);
    }

    protected TvShow(Parcel parcel){
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.overview = parcel.readString();
        this.poster_path = parcel.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.overview);
        parcel.writeString(this.poster_path);
    }

    public static final Parcelable.Creator<TvShow> CREATOR = new Parcelable.Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel parcel) {
            return new TvShow(parcel);
        }

        @Override
        public TvShow[] newArray(int i) {
            return new TvShow[i];
        }
    };
}

