package com.moviecatalogue.sub.activity.tvshowfragment;


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
import com.moviecatalogue.sub.adapter.FavoriteTvShowAdapter;

import static com.moviecatalogue.sub.database.DatabaseContract.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;
    private FavoriteTvShowAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.list_favorite_tvshow);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshTvShow);
        textView = view.findViewById(R.id.tv_favorite_tvshow);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteTvShowAdapter(getActivity());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TvShowFragment.LoadFavoriteTvShowAsync().execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new TvShowFragment.LoadFavoriteTvShowAsync().execute();
    }

    private class LoadFavoriteTvShowAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            textView.setVisibility(View.GONE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI_TVSHOW, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movie) {
            super.onPostExecute(movie);
            swipeRefreshLayout.setRefreshing(false);
            textView.setVisibility(View.GONE);
            Cursor list = movie;
            adapter.setListTvshow(list);
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
