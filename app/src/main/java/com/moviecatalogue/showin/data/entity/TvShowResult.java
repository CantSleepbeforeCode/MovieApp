package com.moviecatalogue.showin.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvShowResult {
    @SerializedName("results")
    private ArrayList<TvShow> result;

    public ArrayList<TvShow> getResult(){
        return result;
    }
}
