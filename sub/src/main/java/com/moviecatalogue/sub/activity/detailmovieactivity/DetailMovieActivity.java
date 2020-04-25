package com.moviecatalogue.sub.activity.detailmovieactivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moviecatalogue.sub.R;
import com.moviecatalogue.sub.entity.Movie;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.moviecatalogue.sub.database.DatabaseContract.CONTENT_URI_MOVIE;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";

    TextView movieTitle, movieDescription;
    CircleImageView moviePoster;
    private ActionBar actionBar;
    private Movie movie;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitle = findViewById(R.id.movie_title);
        movieDescription = findViewById(R.id.movie_description);
        moviePoster = findViewById(R.id.movie_poster);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(movie.getTitle());
        }

        isFavorite = checkFavorite();

        loaded();
    }

    private void loaded() {
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getOverview());
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w780" + movie.getPosterPath())
                .into(moviePoster);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite) {
            menu.findItem(R.id.btn_favorite).setIcon(R.drawable.ic_favorite);
        } else {
            menu.findItem(R.id.btn_favorite).setIcon(R.drawable.ic_favorite_border);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_in_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.btn_favorite:
                invalidateOptionsMenu();
                toggleFavorite(item);
                return true;
        }
        return false;
    }

    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            Uri uri = Uri.parse(CONTENT_URI_MOVIE+"/" + movie.getId());
            getContentResolver().delete(uri,null,null);
            Toast.makeText(this,R.string.remove_favorite, Toast.LENGTH_SHORT).show();
            isFavorite = false;
        } else {
            saveMovie(movie);
            //fvMovieHelper.insert(mv);
            Toast.makeText(this,R.string.add_favorite, Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_border);
            isFavorite = true;
        }
    }

    private void saveMovie(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, movie.getId());
        contentValues.put(TITLE, movie.getTitle());
        contentValues.put(OVERVIEW, movie.getOverview());
        contentValues.put(POSTER_PATH, movie.getPosterPath());
        getContentResolver().insert(CONTENT_URI_MOVIE, contentValues);
    }

    private boolean checkFavorite(){
        Uri uri = Uri.parse(CONTENT_URI_MOVIE+"/"+movie.getId());
        Cursor c = getContentResolver().query(uri,null,null,null,null);
        if(c!=null){
            return c.getCount()>0;
        }
        return false;
    }
}
