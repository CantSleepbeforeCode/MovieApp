package com.moviecatalogue.showin.activity.favoritemoviefragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.adapter.FavoriteMovieAdapter;
import com.moviecatalogue.showin.data.database.FavoriteMovieHelper;
import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {
    private FavoriteMovieAdapter favoriteAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();
    private FavoriteMovieHelper favoriteMovieHelper;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView noData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMovieFavorite);
        recyclerView = view.findViewById(R.id.rv_listMovieFavorite);
        noData = view.findViewById(R.id.tvMovieFavorite);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        favoriteMovieHelper = new FavoriteMovieHelper(getContext());
        favoriteMovieHelper.open();

        favoriteAdapter = new FavoriteMovieAdapter(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadMovieAsync().execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMovieAsync().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            noData.setVisibility(View.INVISIBLE);
            if (movies.size() > 0) {
                movies.clear();
            }
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movie) {
            super.onPostExecute(movie);
            swipeRefreshLayout.setRefreshing(false);
            noData.setVisibility(View.INVISIBLE);

            Cursor list = movie;
            favoriteAdapter.setListMovie(list);
            favoriteAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(favoriteAdapter);
            int count = 0;
            try {
                count = ((list.getCount() > 0) ? list.getCount() : 0);
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
            if (count == 0) {
                noData.setVisibility(View.VISIBLE);
            }
        }
    }

}
