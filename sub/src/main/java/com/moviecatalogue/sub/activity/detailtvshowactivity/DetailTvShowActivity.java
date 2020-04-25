package com.moviecatalogue.sub.activity.detailtvshowactivity;

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
import com.moviecatalogue.sub.entity.TvShow;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.moviecatalogue.sub.database.DatabaseContract.CONTENT_URI_TVSHOW;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.ID;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.POSTER_PATH;
import static com.moviecatalogue.sub.database.DatabaseContract.favoriteColumns.TITLE;

public class DetailTvShowActivity extends AppCompatActivity {
    public static final String EXTRA_TVSHOW = "extra_tvshow";

    TextView tvShowTitle, tvShowDescription;
    CircleImageView tvShowPoster;
    private ActionBar actionBar;
    private TvShow tvShow;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);

        tvShowTitle = findViewById(R.id.tvshow_title);
        tvShowDescription = findViewById(R.id.tvshow_description);
        tvShowPoster = findViewById(R.id.tvshow_poster);

        tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);

        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(tvShow.getName());
        }

        isFavorite = checkFavorite();

        loaded();
    }

    private void loaded() {
        tvShowTitle.setText(tvShow.getName());
        tvShowDescription.setText(tvShow.getOverview());
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w780" + tvShow.getPoster_path())
                .into(tvShowPoster);
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
            Uri uri = Uri.parse(CONTENT_URI_TVSHOW+"/" + tvShow.getId());
            getContentResolver().delete(uri,null,null);
            Toast.makeText(this,R.string.remove_favorite, Toast.LENGTH_SHORT).show();
            isFavorite = false;
        } else {
            saveTvShow(tvShow);
            //fvMovieHelper.insert(mv);
            Toast.makeText(this,R.string.add_favorite, Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_border);
            isFavorite = true;
        }
    }

    private void saveTvShow(TvShow tvShow){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, tvShow.getId());
        contentValues.put(TITLE, tvShow.getName());
        contentValues.put(OVERVIEW, tvShow.getOverview());
        contentValues.put(POSTER_PATH, tvShow.getPoster_path());
        getContentResolver().insert(CONTENT_URI_TVSHOW, contentValues);
    }

    private boolean checkFavorite(){
        Uri uri = Uri.parse(CONTENT_URI_TVSHOW+"/"+tvShow.getId());
        Cursor c = getContentResolver().query(uri,null,null,null,null);
        if(c!=null){
            return c.getCount()>0;
        }
        return false;
    }
}
