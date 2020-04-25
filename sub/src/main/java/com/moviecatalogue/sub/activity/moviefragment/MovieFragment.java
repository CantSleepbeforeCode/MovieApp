package com.moviecatalogue.sub.activity.moviefragment;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviecatalogue.sub.R;
import com.moviecatalogue.sub.adapter.FavoriteMovieAdapter;

import static com.moviecatalogue.sub.database.DatabaseContract.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;
    private FavoriteMovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.list_favorite_movie);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshMovie);
        textView = view.findViewById(R.id.tv_favorite_movie);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteMovieAdapter(getActivity());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadFavoriteAsync().execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadFavoriteAsync().execute();
    }

    private class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            textView.setVisibility(View.GONE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movie) {
            super.onPostExecute(movie);
            swipeRefreshLayout.setRefreshing(false);
            textView.setVisibility(View.GONE);
            Cursor list = movie;
            adapter.setListMovie(list);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            int count = 0;

            try {
                count = ((list.getCount() > 0) ? list.getCount():0);
            } catch (Exception e) {
                Log.w("ERROR", e.getMessage());
            }
            if (count == 0) {
                textView.setVisibility(View.VISIBLE);
            }

        }
    }
}
