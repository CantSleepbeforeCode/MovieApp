package com.moviecatalogue.showin.activity.detailmovieactivity;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moviecatalogue.showin.R;
import com.moviecatalogue.showin.activity.widgetFavoriteMovie.FavoriteMovieBannerWidget;
import com.moviecatalogue.showin.data.database.DatabaseHelper;
import com.moviecatalogue.showin.data.database.FavoriteMovieHelper;
import com.moviecatalogue.showin.data.entity.Genre;
import com.moviecatalogue.showin.data.entity.Movie;
import com.moviecatalogue.showin.data.entity.MovieItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_MOVIE;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity implements DetailMovieView {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String STATE_TITLE = "state_title";
    public static final String STATE_LANGUAGE = "state_language";
    public static final String STATE_OVERVIEW = "state_overview";
    public static final String STATE_RELEASE_DATE = "state_release_date";
    public static final String STATE_RATING = "state_rating";
    public static final String STATE_COVER = "state_cover";
    public static final String LIST_GENRE = "list_genre";

    private ImageView movieCover;
    private TextView movieTitle, movieLanguage, movieOverview, movieRealeaseDate;
    private TextView titleLanguage, titleReleaseDate, titleGenre, titleOverview, dummyGenre;
    private RatingBar ratingMovie;

    DetailMoviePresenter presenter;
    ProgressBar progressBarDetail;
    Movie movie;
    LinearLayout linearLayoutGenre;
    HorizontalScrollView horizontalScrollViewMovie;

    private Boolean isFavorite = false;
    private FavoriteMovieHelper favoriteMovieHelper;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        favoriteMovieHelper = new FavoriteMovieHelper(this);
        favoriteMovieHelper.open();
        helper = new DatabaseHelper(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter = new DetailMoviePresenter(this);

        movieCover = findViewById(R.id.detail_img_movie_cover);
        movieTitle = findViewById(R.id.detail_movie_title);
        movieLanguage = findViewById(R.id.detail_movie_language);
        movieOverview = findViewById(R.id.detail_movie_description);
        movieRealeaseDate = findViewById(R.id.detail_movie_relaeaseDate);
        titleLanguage = findViewById(R.id.detail_movie_title_language);
        titleReleaseDate = findViewById(R.id.detail_movie_title_release_date);
        titleGenre = findViewById(R.id.detail_movie_title_genre);
        titleOverview = findViewById(R.id.detail_movie_title_overview);
        ratingMovie = findViewById(R.id.detail_movie_ratingbar);
        progressBarDetail = findViewById(R.id.progress_detail_movie);
        linearLayoutGenre = findViewById(R.id.linear_genre_movie);
        horizontalScrollViewMovie = findViewById(R.id.horizontalScrollViewMovie);
        dummyGenre = findViewById(R.id.dummy_genre);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (savedInstanceState == null){
            presenter.getData(movie.getId());
        } else {
            String stateTitle = savedInstanceState.getString(STATE_TITLE);
            String stateLanguage = savedInstanceState.getString(STATE_LANGUAGE);
            String stateOverview = savedInstanceState.getString(STATE_OVERVIEW);
            String stateReleaseDate = savedInstanceState.getString(STATE_RELEASE_DATE);
            String stateRating = savedInstanceState.getString(STATE_RATING);
            String stateCover = savedInstanceState.getString(STATE_COVER);
            String stateGenre = savedInstanceState.getString(LIST_GENRE);

            movieTitle.setText(stateTitle);
            movieLanguage.setText(stateLanguage);
            movieOverview.setText(stateOverview);
            movieRealeaseDate.setText(stateReleaseDate);
            Float mRating = Float.valueOf(stateRating);
            ratingMovie.setRating(mRating / 2);

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w780" + stateCover)
                    .apply(new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.drawable.ic_wrongsign))
                    .into(movieCover);

            String[] genre = stateGenre.split(",");
            float statewidth = getResources().getDimension(R.dimen.heightGenre);
            for (int i = 0; i<genre.length; i++){
                TextView tvGenre = new TextView(new ContextThemeWrapper(this, R.style.TextGenre), null, 0);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (int) statewidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMarginEnd(20);
                tvGenre.setLayoutParams(lp);
                tvGenre.setText(genre[i]);
                linearLayoutGenre.addView(tvGenre);
            }

            dummyGenre.setText(stateGenre);

            progressBarDetail.setVisibility(View.GONE);

            movieCover.setVisibility(View.VISIBLE);
            movieTitle.setVisibility(View.VISIBLE);
            movieLanguage.setVisibility(View.VISIBLE);
            movieOverview.setVisibility(View.VISIBLE);
            movieRealeaseDate.setVisibility(View.VISIBLE);
            titleLanguage.setVisibility(View.VISIBLE);
            titleReleaseDate.setVisibility(View.VISIBLE);
            titleGenre.setVisibility(View.VISIBLE);
            titleOverview.setVisibility(View.VISIBLE);
            ratingMovie.setVisibility(View.VISIBLE);
            horizontalScrollViewMovie.setVisibility(View.VISIBLE);
        }

        isFavorite = checkFavorite();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }

    @Override
    public void loading() {
        setTitle(movie.getTitle());
    }

    @Override
    public void loaded() {
        progressBarDetail.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showData(MovieItem movieItem) {
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w780" + movieItem.getPoster_path())
                .apply(new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.drawable.ic_wrongsign))
                .into(movieCover);

        movieTitle.setText(movieItem.getTitle());
        movieLanguage.setText(movieItem.getOriginal_language().toUpperCase());
        movieOverview.setText(movieItem.getOverview());

        String releaseDate = movieItem.getRelease_date();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = parser.parse(releaseDate);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.getDefault());
            String formatedDate = format.format(date);

            movieRealeaseDate.setText(formatedDate);
        } catch (ParseException e){
            e.printStackTrace();
        }

        float width = getResources().getDimension(R.dimen.heightGenre);
        for(Genre genre : movieItem.getGenres()){
            TextView tvGenre = new TextView(new ContextThemeWrapper(this, R.style.TextGenre), null, 0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(20);
            tvGenre.setLayoutParams(lp);
            tvGenre.setText(genre.getName());
            linearLayoutGenre.addView(tvGenre);
            dummyGenre.setText(dummyGenre.getText() + genre.getName() + ",");

        }

        ratingMovie.setRating(movieItem.getVote_average() / 2);

        movieCover.setVisibility(View.VISIBLE);
        movieTitle.setVisibility(View.VISIBLE);
        movieLanguage.setVisibility(View.VISIBLE);
        movieOverview.setVisibility(View.VISIBLE);
        movieRealeaseDate.setVisibility(View.VISIBLE);
        titleLanguage.setVisibility(View.VISIBLE);
        titleReleaseDate.setVisibility(View.VISIBLE);
        titleGenre.setVisibility(View.VISIBLE);
        titleOverview.setVisibility(View.VISIBLE);
        horizontalScrollViewMovie.setVisibility(View.VISIBLE);
        ratingMovie.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.navigation_favorite:
                invalidateOptionsMenu();
                toggleFavorite(item);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite) {
            menu.findItem(R.id.navigation_favorite).setIcon(R.drawable.ic_favorite);
        } else {
            menu.findItem(R.id.navigation_favorite).setIcon(R.drawable.ic_favorite_border);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean checkFavorite() {
        Uri uri = Uri.parse(CONTENT_URI_MOVIE+"/"+movie.getId());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            return cursor.getCount() > 0;
        }
        return false;
    }

    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            Uri uri = Uri.parse(CONTENT_URI_MOVIE+"/"+movie.getId());
            getContentResolver().delete(uri, null, null);
            Toast.makeText(this, R.string.toast_remove_favorite, Toast.LENGTH_SHORT).show();
            isFavorite = false;
        } else {
            saveMovie(movie);
            Intent widgetIntent = new Intent(this, FavoriteMovieBannerWidget.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getApplication())
                    .getAppWidgetIds(new ComponentName(getApplication(), FavoriteMovieBannerWidget.class));
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(widgetIntent);
            Toast.makeText(this, R.string.toast_add_favorite, Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_border);
            isFavorite = true;
        }
    }

    private void saveMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, movie.getId());
        contentValues.put(TITLE, movie.getTitle());
        contentValues.put(OVERVIEW, movie.getOverview());
        contentValues.put(POSTER_PATH, movie.getPosterPath());
        getContentResolver().insert(CONTENT_URI_MOVIE, contentValues);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, movieTitle.getText().toString());
        outState.putString(STATE_LANGUAGE, movieLanguage.getText().toString());
        outState.putString(STATE_OVERVIEW, movieOverview.getText().toString());
        outState.putString(STATE_RELEASE_DATE, movieRealeaseDate.getText().toString());
        outState.putString(STATE_RATING, String.valueOf(ratingMovie.getRating()));
        outState.putString(STATE_COVER, movie.getPosterPath());
        outState.putString(LIST_GENRE, dummyGenre.getText().toString());

    }
}
