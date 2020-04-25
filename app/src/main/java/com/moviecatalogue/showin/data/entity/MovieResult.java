package com.moviecatalogue.showin.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResult {
    @SerializedName("results")
    private ArrayList<Movie> result;

    public ArrayList<Movie> getResult() {
        return result;
    }
}
