package com.udacity.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.udacity.popularmovies.adapters.MovieAdapter;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.utils.MovieJsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.movie_list_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        LoaderManager.LoaderCallbacks<List<Movie>> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callback);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {

            List<Movie> mMovieList = null;

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
                URL movieRequestUrl = NetworkUtils.buildUrlByEndpointType(MainActivity.this, "popular");

                try{
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                    List<Movie> movieListResponse = MovieJsonUtils.parseJsonResponse(jsonResponse);

                    return movieListResponse;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                mMovieList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMovieAdapter.setMoviesData(movies);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onClick(int movieId) {
        Toast.makeText(this, String.valueOf(movieId), Toast.LENGTH_SHORT).show();
    }
}
