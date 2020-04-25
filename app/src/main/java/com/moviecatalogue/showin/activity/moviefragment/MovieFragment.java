package com.moviecatalogue.showin.activity.moviefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.adapter.MovieAdapter;
import com.moviecatalogue.showin.base.BaseFragment;
import com.moviecatalogue.showin.data.entity.Movie;

import java.util.ArrayList;

public class MovieFragment extends BaseFragment implements MovieView {
    private TextInputEditText textInputSearchMovie;
    private RecyclerView recyclerViewMovie;
    private MoviePresenter moviePresenter;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button btnSearch = view.findViewById(R.id.btn_search_movie);
        textInputSearchMovie = view.findViewById(R.id.edt_input_search_movie);

        recyclerViewMovie = view.findViewById(R.id.rv_movie);
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieAdapter = new MovieAdapter(movieList, getActivity());
        recyclerViewMovie.setAdapter(movieAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swipeMovie);

        moviePresenter = new MoviePresenter(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                moviePresenter.getList();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_search_movie:
                        moviePresenter.searchMovie(textInputSearchMovie.getText()+"");
                        break;
                }
            }
        });

        if (savedInstanceState == null){
            moviePresenter.getList();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            movieAdapter.refill(movieList);
        }
    }

    @Override
    public void loading() {
        swipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void loaded() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showList(ArrayList<Movie> data) {
        movieList = data;
        movieAdapter.refill(movieList);

    }

    @Override
    public void noData() {
        Toast.makeText(getContext(), "Periksa Koneksi Internet Anda.", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
        movieList.clear();
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle){
        bundle.putParcelableArrayList(KEY_MOVIES, movieList);
        super.onSaveInstanceState(bundle);

    }
}
