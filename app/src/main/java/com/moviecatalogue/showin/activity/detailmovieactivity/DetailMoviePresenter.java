package com.moviecatalogue.showin.activity.detailmovieactivity;

import com.moviecatalogue.showin.data.api.ApiRetrofit;
import com.moviecatalogue.showin.data.entity.MovieItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMoviePresenter {
    DetailMovieView view;

    public DetailMoviePresenter(DetailMovieView view){
        this.view = view;
    }

    public void getData(String id){
        view.loading();

        Call<MovieItem> movieItemCall = ApiRetrofit.getService().getDetailMovie(id);

        movieItemCall.enqueue(new Callback<MovieItem>() {
            @Override
            public void onResponse(Call<MovieItem> call, Response<MovieItem> response) {
                view.loaded();
                view.showData(response.body());
            }

            @Override
            public void onFailure(Call<MovieItem> call, Throwable t) {

            }
        });
    }
}
