package com.moviecatalogue.showin.activity.tvshowfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.adapter.TvShowAdapter;
import com.moviecatalogue.showin.base.BaseFragment;
import com.moviecatalogue.showin.data.entity.TvShow;

import java.util.ArrayList;

public class TvShowFragment extends BaseFragment implements TvShowView {
    private TextInputEditText textInputSearchTvShow;
    private RecyclerView recyclerViewTvShows;
    private TvShowPresenter presenter;
    private TvShowAdapter adapter;
    private ArrayList<TvShow> tvshowList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button btnSearch = view.findViewById(R.id.btn_search_tv_show);
        textInputSearchTvShow = view.findViewById(R.id.edt_input_search_tv_show);

        recyclerViewTvShows = view.findViewById(R.id.rv_tvShows);
        recyclerViewTvShows.setHasFixedSize(true);
        recyclerViewTvShows.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new TvShowAdapter(tvshowList,getActivity());
        recyclerViewTvShows.setAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.swipeTvShows);

        presenter = new TvShowPresenter(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getList();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_search_tv_show:
                        presenter.searchTvShow(textInputSearchTvShow.getText()+"");
                        break;
                }
            }
        });

        if(savedInstanceState == null){
            presenter.getList();
        } else {
            tvshowList = savedInstanceState.getParcelableArrayList(KEY_TVSHOWS);
            adapter.refill(tvshowList);
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
    public void showList(ArrayList<TvShow> data) {
        tvshowList = data;
        adapter.refill(tvshowList);
    }

    @Override
    public void noData() {
        Toast.makeText(getContext(), "Periksa Koneksi Internet Anda.", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
        tvshowList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle){
        bundle.putParcelableArrayList(KEY_TVSHOWS, tvshowList);
        super.onSaveInstanceState(bundle);
    }
}

