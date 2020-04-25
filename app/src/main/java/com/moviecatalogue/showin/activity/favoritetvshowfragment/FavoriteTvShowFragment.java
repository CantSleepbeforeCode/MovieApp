package com.moviecatalogue.showin.activity.favoritetvshowfragment;

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
import com.moviecatalogue.showin.adapter.FavoriteTvShowAdapter;
import com.moviecatalogue.showin.data.database.FavoriteTvShowHelper;
import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowFragment extends Fragment {
    private FavoriteTvShowAdapter adapter;
    private ArrayList<TvShow> tvShows = new ArrayList<>();
    private FavoriteTvShowHelper helper;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView noData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshTvShowFavorite);
        recyclerView = view.findViewById(R.id.rv_listTvShowFavorite);
        noData = view.findViewById(R.id.tvTvShowFavorite);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        helper = new FavoriteTvShowHelper(getContext());
        helper.open();

        adapter = new FavoriteTvShowAdapter(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadTvShowAsync().execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadTvShowAsync().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadTvShowAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            noData.setVisibility(View.INVISIBLE);
            if (tvShows.size() > 0) {
                tvShows.clear();
            }
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI_TVSHOW, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor tvshow) {
            super.onPostExecute(tvshow);
            swipeRefreshLayout.setRefreshing(false);
            noData.setVisibility(View.INVISIBLE);

            Cursor list = tvshow;
            adapter.setListTvshow(list);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
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

