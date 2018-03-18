package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.adapters.MovieAdapter;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.utils.MovieJsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    private static final String POPULAR_MOVIES_SORT = "popular";
    private static final String TOP_RATED_MOVIES_SORT = "top_rated";
    private static final String SORT_TYPE_KEY = "sort_type";

    private static String LAST_SELECTED_SORT = POPULAR_MOVIES_SORT;

    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movie_list_rv);
        mProgressBar = findViewById(R.id.loading_indicator_pb);
        mErrorMessage = findViewById(R.id.error_message_tv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, getResources().getInteger(R.integer.number_of_columns));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        Bundle bundle = new Bundle();
        bundle.putString(SORT_TYPE_KEY, LAST_SELECTED_SORT);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundle, MainActivity.this);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        final String sortType = bundle.getString(SORT_TYPE_KEY);

        showOrHideLoadingIndicator(true);
        showOrHideErrorMessage(false);

        return new AsyncTaskLoaderMovies(MainActivity.this, sortType);
    }

    public static class AsyncTaskLoaderMovies extends AsyncTaskLoader<List<Movie>>{
        private WeakReference<Context> mContext;
        private String mSortType;
        private List<Movie> mMovieList = null;

        AsyncTaskLoaderMovies(Context context, String sortType){
            super(context);
            this.mContext = new WeakReference<>(context);
            this.mSortType = sortType;
        }

        @Override
        protected void onStartLoading() {
            if (mMovieList != null){
                deliverResult(mMovieList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Movie> loadInBackground() {
            if (mContext != null) {
                final Context finalContext = mContext.get();
                URL movieRequestUrl = NetworkUtils.buildUrlByEndpointType(finalContext, mSortType);

                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                    return MovieJsonUtils.parseMoviesListJsonResponse(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error - loadInBackground: ", e);
                    return null;
                }
            }
            return null;
        }

        @Override
        public void deliverResult(List<Movie> data) {
            mMovieList = data;
            super.deliverResult(data);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        showOrHideLoadingIndicator(false);
        if (null == movies)
            showOrHideErrorMessage(true);
        mMovieAdapter.setMoviesData(movies);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Movie.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle = new Bundle();
        switch (item.getItemId()){
            case R.id.display_top_rated:
                bundle.putString(SORT_TYPE_KEY, TOP_RATED_MOVIES_SORT);
                LAST_SELECTED_SORT = TOP_RATED_MOVIES_SORT;
                break;
            case R.id.display_popular:
                bundle.putString(SORT_TYPE_KEY, POPULAR_MOVIES_SORT);
                LAST_SELECTED_SORT = POPULAR_MOVIES_SORT;
                break;
            default:
                bundle.putString(SORT_TYPE_KEY, POPULAR_MOVIES_SORT);
                LAST_SELECTED_SORT = POPULAR_MOVIES_SORT;
                break;
        }
        mRecyclerView.smoothScrollToPosition(0);
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, bundle, MainActivity.this);
        return true;
    }

    private void showOrHideLoadingIndicator(boolean toShow){
        if (toShow){
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showOrHideErrorMessage(boolean toShow){
        if(toShow){
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            mErrorMessage.setVisibility(View.GONE);
        }
    }
}
