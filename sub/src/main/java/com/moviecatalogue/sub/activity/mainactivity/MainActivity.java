package com.moviecatalogue.sub.activity.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moviecatalogue.sub.R;
import com.moviecatalogue.sub.activity.moviefragment.MovieFragment;
import com.moviecatalogue.sub.activity.tvshowfragment.TvShowFragment;

import static com.moviecatalogue.sub.base.BaseAppCompatActivity.KEY_FRAGMENT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Fragment pageContent = new MovieFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_layout, pageContent)
                    .commit();
        } else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, pageContent);
        }
    }

    protected void onSaveInstanceState(Bundle bundle){
        getSupportFragmentManager().putFragment(bundle, KEY_FRAGMENT, pageContent);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_movie:
                pageContent = new MovieFragment();
                break;
            case R.id.navigation_tvshows:
                pageContent = new TvShowFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, pageContent)
                .commit();
        return true;
    }
}
