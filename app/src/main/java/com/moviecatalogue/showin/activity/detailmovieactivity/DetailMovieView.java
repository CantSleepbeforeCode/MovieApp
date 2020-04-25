package com.moviecatalogue.showin.activity.detailmovieactivity;

import com.moviecatalogue.showin.data.entity.MovieItem;

public interface DetailMovieView {
    void loading();
    void loaded();
    void showData(MovieItem movieItem);
    void onError();
}
