package com.moviecatalogue.showin.activity.tvshowfragment;

import android.util.Log;

import com.moviecatalogue.showin.data.api.ApiRetrofit;
import com.moviecatalogue.showin.data.entity.TvShowResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowPresenter {
    private TvShowView view;

    public TvShowPresenter(TvShowView tvShowsView){
        this.view = tvShowsView;
    }

    public void searchTvShow(String tvShow) {
        view.loading();
        Call<TvShowResult> listTvShow = ApiRetrofit.getService().getSearchTvShow(tvShow);

        listTvShow.enqueue(new Callback<TvShowResult>() {
            @Override
            public void onResponse(Call<TvShowResult> call, Response<TvShowResult> response) {
                view.loaded();
                if (response.body() != null) {
                    view.showList(response.body().getResult());
                }
                Log.d("TAG", "BERHASIL");
            }

            @Override
            public void onFailure(Call<TvShowResult> call, Throwable t) {
                Log.d("TAG", "GAGAL");
            }
        });
    }

    void getList(){
        view.loading();
        Call<TvShowResult> listTvShow = ApiRetrofit.getService().getTopRated();
        listTvShow.enqueue(new Callback<TvShowResult>() {
            @Override
            public void onResponse(Call<TvShowResult> call, Response<TvShowResult> response) {
                view.loaded();
                if (response.body() != null){
                    view.showList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<TvShowResult> call, Throwable t) {

            }
        });
    }
}
