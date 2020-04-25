package com.moviecatalogue.showin.activity.detailtvshowactivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import com.moviecatalogue.showin.data.database.DatabaseHelper;
import com.moviecatalogue.showin.data.database.FavoriteTvShowHelper;
import com.moviecatalogue.showin.data.entity.Genre;
import com.moviecatalogue.showin.data.entity.TvShow;
import com.moviecatalogue.showin.data.entity.TvShowItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.CONTENT_URI_TVSHOW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.showin.data.database.FavoriteDatabaseContract.favoriteColumns.TITLE;

public class DetailTvShowActivity extends AppCompatActivity implements DetailTvShowView {
    public static final String EXTRA_TVSHOW = "tvshow";
    public static final String STATE_TITLE = "state_title";
    public static final String STATE_LANGUAGE = "state_language";
    public static final String STATE_OVERVIEW = "state_overview";
    public static final String STATE_RELEASE_DATE = "state_release_date";
    public static final String STATE_RATING = "state_rating";
    public static final String STATE_COVER = "state_cover";
    public static final String LIST_GENRE = "list_genre";

    private ImageView tvshowCover;
    private TextView tvshowTitle, tvshowLanguage, tvshowOverview, tvshowRealeaseDate;
    private TextView titleLanguage, titleReleaseDate, titleGenre, titleOverview, dummyGenre;
    private RatingBar ratingtvshow;

    DetailTvShowPresenter presenter;
    ProgressBar progressBar;
    TvShow tvShows;
    LinearLayout linearLayoutGenre;
    HorizontalScrollView horizontalScrollViewTvShow;

    private Boolean isFavorite = false;
    private FavoriteTvShowHelper favoriteTvShowHelper;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        favoriteTvShowHelper = new FavoriteTvShowHelper(this);
        favoriteTvShowHelper.open();
        helper = new DatabaseHelper(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter = new DetailTvShowPresenter(this);

        tvshowCover = findViewById(R.id.detail_img_tvshow_cover);
        tvshowTitle = findViewById(R.id.detail_tvshow_title);
        tvshowLanguage = findViewById(R.id.detail_tvshow_language);
        tvshowOverview = findViewById(R.id.detail_tvshow_description);
        tvshowRealeaseDate = findViewById(R.id.detail_tvshow_relaeaseDate);
        titleLanguage = findViewById(R.id.detail_tvshow_title_language);
        titleReleaseDate = findViewById(R.id.detail_tvshow_title_release_date);
        titleGenre = findViewById(R.id.detail_tvshow_title_genre);
        titleOverview = findViewById(R.id.detail_tvshow_title_overview);
        ratingtvshow = findViewById(R.id.detail_tvshow_ratingbar);
        progressBar = findViewById(R.id.progress_detail_tvshow);
        linearLayoutGenre = findViewById(R.id.linear_genre_tvshow);
        horizontalScrollViewTvShow = findViewById(R.id.horizontalScrollViewTvshow);
        dummyGenre = findViewById(R.id.dummy_genre);

        tvShows = getIntent().getParcelableExtra(EXTRA_TVSHOW);

        if (savedInstanceState == null){
            presenter.getData(tvShows.getId());
        } else {
            String stateTitle = savedInstanceState.getString(STATE_TITLE);
            String stateLanguage = savedInstanceState.getString(STATE_LANGUAGE);
            String stateOverview = savedInstanceState.getString(STATE_OVERVIEW);
            String stateReleaseDate = savedInstanceState.getString(STATE_RELEASE_DATE);
            String stateRating = savedInstanceState.getString(STATE_RATING);
            String stateCover = savedInstanceState.getString(STATE_COVER);
            String stateGenre = savedInstanceState.getString(LIST_GENRE);

            tvshowTitle.setText(stateTitle);
            tvshowLanguage.setText(stateLanguage);
            tvshowOverview.setText(stateOverview);
            tvshowRealeaseDate.setText(stateReleaseDate);
            Float mRating = Float.valueOf(stateRating);
            ratingtvshow.setRating(mRating / 2);

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w780" + stateCover)
                    .apply(new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.drawable.ic_wrongsign))
                    .into(tvshowCover);

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

            progressBar.setVisibility(View.GONE);

            tvshowCover.setVisibility(View.VISIBLE);
            tvshowTitle.setVisibility(View.VISIBLE);
            tvshowLanguage.setVisibility(View.VISIBLE);
            tvshowOverview.setVisibility(View.VISIBLE);
            tvshowRealeaseDate.setVisibility(View.VISIBLE);
            titleLanguage.setVisibility(View.VISIBLE);
            titleReleaseDate.setVisibility(View.VISIBLE);
            titleGenre.setVisibility(View.VISIBLE);
            titleOverview.setVisibility(View.VISIBLE);
            ratingtvshow.setVisibility(View.VISIBLE);
            horizontalScrollViewTvShow.setVisibility(View.VISIBLE);
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
        setTitle(tvShows.getName());
    }

    @Override
    public void loaded() {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showData(TvShowItem tvShowItem) {
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w780" + tvShowItem.getPoster_path())
                .apply(new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.drawable.ic_wrongsign))
                .into(tvshowCover);

        tvshowTitle.setText(tvShowItem.getName());
        tvshowLanguage.setText(tvShowItem.getOriginal_language().toUpperCase());
        tvshowOverview.setText(tvShowItem.getOverview());

        String releaseDate = tvShowItem.getFirst_air_date();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = parser.parse(releaseDate);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.getDefault());
            String formatedDate = format.format(date);

            tvshowRealeaseDate.setText(formatedDate);
        } catch (ParseException e){
            e.printStackTrace();
        }

        float width = getResources().getDimension(R.dimen.heightGenre);
        for(Genre genre : tvShowItem.getGenres()){
            TextView tvGenre = new TextView(new ContextThemeWrapper(this, R.style.TextGenre), null, 0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(20);
            tvGenre.setLayoutParams(lp);
            tvGenre.setText(genre.getName());
            linearLayoutGenre.addView(tvGenre);
            dummyGenre.setText(dummyGenre.getText() + genre.getName() + ",");

        }

        ratingtvshow.setRating(tvShowItem.getVote_average() / 2);

        tvshowCover.setVisibility(View.VISIBLE);
        tvshowTitle.setVisibility(View.VISIBLE);
        tvshowLanguage.setVisibility(View.VISIBLE);
        tvshowOverview.setVisibility(View.VISIBLE);
        tvshowRealeaseDate.setVisibility(View.VISIBLE);
        titleLanguage.setVisibility(View.VISIBLE);
        titleReleaseDate.setVisibility(View.VISIBLE);
        titleGenre.setVisibility(View.VISIBLE);
        titleOverview.setVisibility(View.VISIBLE);
        ratingtvshow.setVisibility(View.VISIBLE);
        horizontalScrollViewTvShow.setVisibility(View.VISIBLE);

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
        Uri uri = Uri.parse(CONTENT_URI_TVSHOW+"/"+tvShows.getId());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            return cursor.getCount() > 0;
        }
        return false;
    }

    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            Uri uri = Uri.parse(CONTENT_URI_TVSHOW+"/"+tvShows.getId());
            getContentResolver().delete(uri, null, null);
            Toast.makeText(this, R.string.toast_remove_favorite, Toast.LENGTH_SHORT).show();
            isFavorite = false;
        } else {
            saveTvShow(tvShows);
            Toast.makeText(this, R.string.toast_add_favorite, Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_border);
            isFavorite = true;
        }
    }

    private void saveTvShow(TvShow tvShow) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, tvShow.getId());
        contentValues.put(TITLE, tvShow.getName());
        contentValues.put(OVERVIEW, tvShow.getOverview());
        contentValues.put(POSTER_PATH, tvShow.getPoster_path());
        getContentResolver().insert(CONTENT_URI_TVSHOW, contentValues);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, tvshowTitle.getText().toString());
        outState.putString(STATE_LANGUAGE, tvshowLanguage.getText().toString());
        outState.putString(STATE_OVERVIEW, tvshowOverview.getText().toString());
        outState.putString(STATE_RELEASE_DATE, tvshowRealeaseDate.getText().toString());
        outState.putString(STATE_RATING, String.valueOf(ratingtvshow.getRating()));
        outState.putString(STATE_COVER, tvShows.getPoster_path());
        outState.putString(LIST_GENRE, dummyGenre.getText().toString());

    }
}
