package com.moviecatalogue.showin.activity.detailtvshowactivity;

import com.moviecatalogue.showin.data.api.ApiRetrofit;
import com.moviecatalogue.showin.data.entity.TvShowItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTvShowPresenter {
    DetailTvShowView view;

    public DetailTvShowPresenter(DetailTvShowView view){
        this.view = view;
    }

    public void getData(String id){
        view.loading();

        Call<TvShowItem> tvShowItemCall = ApiRetrofit.getService().getDetailTvShow(id);
        tvShowItemCall.enqueue(new Callback<TvShowItem>() {
            @Override
            public void onResponse(Call<TvShowItem> call, Response<TvShowItem> response) {
                view.loaded();
                view.showData(response.body());
            }

            @Override
            public void onFailure(Call<TvShowItem> call, Throwable t) {

            }
        });
    }
}
