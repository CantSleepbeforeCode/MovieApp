package com.moviecatalogue.showin.activity.detailtvshowactivity;

import com.moviecatalogue.showin.data.entity.TvShowItem;

public interface DetailTvShowView {
    void loading();
    void loaded();
    void showData(TvShowItem tvShowItem);
    void onError();
}
