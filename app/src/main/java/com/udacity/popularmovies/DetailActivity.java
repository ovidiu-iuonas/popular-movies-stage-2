package com.udacity.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.utils.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private TextView mMovieRating;
    private TextView mMovieSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePoster = findViewById(R.id.movie_poster_iv);
        mMovieReleaseDate = findViewById(R.id.release_date_tv);
        mMovieRating = findViewById(R.id.rating_tv);
        mMovieSynopsis = findViewById(R.id.synopsis_tv);

        Movie selectedMovie = (Movie) getIntent().getSerializableExtra(Movie.MOVIE_EXTRA);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(selectedMovie.getmTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setMovieData(selectedMovie);
    }

    private void setMovieData(Movie movie){
        Picasso.with(this).load(NetworkUtils.buildPosterUrl(this, movie.getmPosterImageUrl()).toString()).into(mMoviePoster);
        mMovieRating.setText(String.valueOf(movie.getmRating()));
        mMovieReleaseDate.setText(movie.getmReleaseDate());
        mMovieSynopsis.setText(movie.getmOverview());
    }
}
