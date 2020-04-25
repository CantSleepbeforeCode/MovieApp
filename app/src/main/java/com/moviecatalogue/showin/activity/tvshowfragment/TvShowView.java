package com.moviecatalogue.showin.activity.tvshowfragment;

import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;

public interface TvShowView {
    void loading();
    void loaded();
    void showList(ArrayList<TvShow> data);
    void noData();
}
