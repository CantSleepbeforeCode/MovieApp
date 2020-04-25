package com.moviecatalogue.showin.activity.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.activity.remindersettings.ReminderSettingActivity;
import com.moviecatalogue.showin.activity.favoritemoviefragment.FavoriteMovieFragment;
import com.moviecatalogue.showin.activity.favoritetvshowfragment.FavoriteTvShowFragment;
import com.moviecatalogue.showin.activity.moviefragment.MovieFragment;
import com.moviecatalogue.showin.activity.tvshowfragment.TvShowFragment;

import static com.moviecatalogue.showin.base.BaseAppCompatActivity.KEY_FRAGMENT;

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
            case R.id.navigation_favorite_movie:
                pageContent = new FavoriteMovieFragment();
                break;
            case R.id.navigation_favorite_tvshow:
                pageContent = new FavoriteTvShowFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, pageContent)
                .commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu. menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
                break;
            case R.id.menu2:
                Intent reminderIntent = new Intent(this, ReminderSettingActivity.class);
                startActivity(reminderIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
