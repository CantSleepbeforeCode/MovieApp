package com.moviecatalogue.showin.activity.moviefragment;

import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;

public interface MovieView {
    void loading();
    void loaded();
    void showList(ArrayList<Movie> data);
    void noData();
}
