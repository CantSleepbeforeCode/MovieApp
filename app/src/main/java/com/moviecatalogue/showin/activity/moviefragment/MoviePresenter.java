package com.moviecatalogue.showin.activity.moviefragment;

import com.moviecatalogue.showin.data.api.ApiRetrofit;
import com.moviecatalogue.showin.data.entity.MovieResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePresenter {
    private MovieView view;

    public MoviePresenter(MovieView view) {
        this.view = view;
    }

    public void searchMovie(String movie) {
        view.loading();
        Call<MovieResult> listMovie = ApiRetrofit.getService().getSearchMovie(movie);

        listMovie.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                view.loaded();
                if (response.body() != null) {
                    view.showList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }

    void getList() {
        view.loading();
        Call<MovieResult> listMovie = ApiRetrofit.getService().getNowPlaying();
        listMovie.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                view.loaded();
                if (response.body() != null) {
                    view.showList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }
}
