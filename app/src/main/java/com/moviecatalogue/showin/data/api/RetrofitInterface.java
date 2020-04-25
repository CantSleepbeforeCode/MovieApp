package com.moviecatalogue.showin.data.api;

import com.moviecatalogue.showin.BuildConfig;
import com.moviecatalogue.showin.data.entity.MovieItem;
import com.moviecatalogue.showin.data.entity.MovieResult;
import com.moviecatalogue.showin.data.entity.TvShowItem;
import com.moviecatalogue.showin.data.entity.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("3/movie/now_playing?api_key=" + BuildConfig.TMDB_API_KEY + "&page=1")
    Call<MovieResult> getNowPlaying();

    @GET("3/movie/{id}?api_key=" + BuildConfig.TMDB_API_KEY + "&language-en-US")
    Call<MovieItem> getDetailMovie(@Path("id") String id);

    @GET("3/tv/top_rated?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&page=1")
    Call<TvShowResult> getTopRated();

    @GET("3/tv/{id}?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US")
    Call<TvShowItem> getDetailTvShow(@Path("id") String id);

    @GET("3/search/movie?api_key="+ BuildConfig.TMDB_API_KEY + "&language=en-US")
    Call<MovieResult> getSearchMovie(@Query("query") String string);

    @GET("3/search/tv?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&")
    Call<TvShowResult> getSearchTvShow(@Query("query") String string);
}
