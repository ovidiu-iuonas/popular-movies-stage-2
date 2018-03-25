package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.adapters.ReviewAdapter;
import com.udacity.popularmovies.adapters.TrailerAdapter;
import com.udacity.popularmovies.data.MoviesContract;
import com.udacity.popularmovies.data.MoviesProvider;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.models.Review;
import com.udacity.popularmovies.models.ReviewsAndTrailers;
import com.udacity.popularmovies.models.Trailer;
import com.udacity.popularmovies.utils.MovieJsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ReviewsAndTrailers>{

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int MOVIE_DETAILS_LOADER_ID = 1;
    private static final String MOVIE_ID_KEY = "movie_id";

    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private TextView mMovieRating;
    private TextView mMovieSynopsis;
    private ImageButton mAddToFavBtn;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRv;
    private RecyclerView mReviewRv;

    private boolean isMovieAddedToFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePoster = findViewById(R.id.movie_poster_iv);
        mMovieReleaseDate = findViewById(R.id.release_date_tv);
        mMovieRating = findViewById(R.id.rating_tv);
        mMovieSynopsis = findViewById(R.id.synopsis_tv);

        mTrailerRv = findViewById(R.id.movie_trailers_rv);
        mReviewRv = findViewById(R.id.movies_reviews_rv);

        final Movie selectedMovie = (Movie) getIntent().getSerializableExtra(Movie.MOVIE_EXTRA);
        isMovieAddedToFavorites = checkIfMovieIsAddedToFavorites(selectedMovie.getmId());

        mAddToFavBtn = findViewById(R.id.add_to_favorites_btn);
        if (mAddToFavBtn != null){
            mAddToFavBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    addOrDeleteMovieFromFav(selectedMovie);
                }
            });
            if (isMovieAddedToFavorites){
                mAddToFavBtn.setImageResource(R.drawable.icon_star_yellow);
            } else {
                mAddToFavBtn.setImageResource(R.drawable.icon_star_grey);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager linearLayoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRv.setLayoutManager(linearLayoutManagerReview);
        mReviewRv.setHasFixedSize(true);
        mTrailerRv.setLayoutManager(linearLayoutManagerTrailer);
        mTrailerRv.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(this, this);
        mTrailerAdapter = new TrailerAdapter(this, this);

        mTrailerRv.setAdapter(mTrailerAdapter);
        mReviewRv.setAdapter(mReviewAdapter);

        setMovieData(selectedMovie);

        Bundle bundle = new Bundle();
        bundle.putLong(MOVIE_ID_KEY, selectedMovie.getmId());
        getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, bundle, DetailActivity.this);
    }

    private void addOrDeleteMovieFromFav(Movie selectedMovie){
        if (!isMovieAddedToFavorites){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MovieEntry._ID, selectedMovie.getmId());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, selectedMovie.getmTitle());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, selectedMovie.getmOverview());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_POSTER, selectedMovie.getmPosterImageUrl());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING, selectedMovie.getmRating());
            contentValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, selectedMovie.getmReleaseDate());

            Uri insertedMovieUri = getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
            if (insertedMovieUri != null){
                Toast.makeText(DetailActivity.this, "Movie added to favorites", Toast.LENGTH_SHORT).show();
            }
            mAddToFavBtn.setImageResource(R.drawable.icon_star_yellow);
            isMovieAddedToFavorites = true;
        } else {
            getContentResolver().delete(MoviesContract.MovieEntry.buildMoviesUri(selectedMovie.getmId()), null, null);
            mAddToFavBtn.setImageResource(R.drawable.icon_star_grey);
            isMovieAddedToFavorites = false;
        }
    }

    private boolean checkIfMovieIsAddedToFavorites(long id){
        Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.buildMoviesUri(id), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        return false;
    }

    @Override
    public Loader<ReviewsAndTrailers> onCreateLoader(int i, Bundle bundle) {
        long movieId = bundle.getLong(MOVIE_ID_KEY);
        return new AsyncTaskLoaderDetails(DetailActivity.this, movieId);
    }

    public static class AsyncTaskLoaderDetails extends AsyncTaskLoader<ReviewsAndTrailers> {

        private WeakReference<Context> mContext;
        private List<Review> mReviewsList;
        private List<Trailer> mTrailersList;
        private long mMovieId;

        AsyncTaskLoaderDetails(Context context, long movieId){
            super(context);
            this.mContext = new WeakReference<>(context);
            this.mMovieId = movieId;
        }

        @Override
        protected void onStartLoading() {
            if (mReviewsList != null && mTrailersList != null){
                deliverResult(new ReviewsAndTrailers(mTrailersList, mReviewsList));
            } else {
                forceLoad();
            }
        }

        @Override
        public ReviewsAndTrailers loadInBackground() {
            if (mContext != null){
                final Context finalContext = mContext.get();

                URL reviewsRequestUrl = NetworkUtils.buildUrlById(finalContext, "reviews", mMovieId);
                URL trailersRequestUrl = NetworkUtils.buildUrlById(finalContext, "trailers", mMovieId);

                try {
                    String jsonResponseReviews = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
                    String jsonResponseTrailers = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);

                    List<Review> reviews = MovieJsonUtils.parseReviewsListJson(jsonResponseReviews);
                    List<Trailer> trailers = MovieJsonUtils.parseTrailersListJson(jsonResponseTrailers);

                    return new ReviewsAndTrailers(trailers, reviews);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error - loadInBackground: ", e);
                    return null;
                }
            }
            return null;
        }

        @Override
        public void deliverResult(ReviewsAndTrailers data) {
            mReviewsList = data.getmReviewsList();
            mTrailersList = data.getmTrailersList();
            super.deliverResult(data);
        }
    }

    @Override
    public void onLoadFinished(Loader<ReviewsAndTrailers> loader, ReviewsAndTrailers data) {
        mReviewAdapter.setReviewsData(data.getmReviewsList());
        mTrailerAdapter.setTrailersData(data.getmTrailersList());
    }

    @Override
    public void onLoaderReset(Loader<ReviewsAndTrailers> loader) {

    }

    private void setMovieData(Movie movie){
        Picasso.with(this).load(NetworkUtils.buildPosterUrl(this, movie.getmPosterImageUrl()).toString()).into(mMoviePoster);
        mMovieRating.setText(String.valueOf(movie.getmRating()));
        mMovieReleaseDate.setText(movie.getmReleaseDate());
        mMovieSynopsis.setText(movie.getmOverview());
    }

    @Override
    public void onReviewClick(Review review) {
        openWebPage(DetailActivity.this, review.getmUrl());
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        watchYoutubeVideo(DetailActivity.this, trailer.getmSource());
    }

    public static void openWebPage(Context context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void watchYoutubeVideo(Context context, String source){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + source));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + source));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
