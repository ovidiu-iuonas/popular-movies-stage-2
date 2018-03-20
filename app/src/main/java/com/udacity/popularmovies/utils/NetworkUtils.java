package com.udacity.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POPULAR_MOVIES_ENDPOINT = "/popular";
    private static final String TOP_RATED_MOVIES_ENDPOINT = "/top_rated";
    private static final String TRAILERS_ENDPOINT = "/trailers";
    private static final String REVIEWS_ENDPOINT = "/reviews";
    private static final String API_KEY_QUERY = "api_key";

    public static URL buildUrlByEndpointType(Context context, String endpointType){
        Uri buildUri;

        switch (endpointType){
            case "popular":
                buildUri = Uri.parse(MOVIES_BASE_URL + POPULAR_MOVIES_ENDPOINT).buildUpon()
                        .appendQueryParameter(API_KEY_QUERY, context.getResources().getString(R.string.API_KEY))
                        .build();
                break;
            case "top_rated":
                buildUri = Uri.parse(MOVIES_BASE_URL + TOP_RATED_MOVIES_ENDPOINT).buildUpon()
                        .appendQueryParameter(API_KEY_QUERY, context.getResources().getString(R.string.API_KEY))
                        .build();
                break;
            default:
                buildUri = Uri.parse(MOVIES_BASE_URL + POPULAR_MOVIES_ENDPOINT).buildUpon()
                        .appendQueryParameter(API_KEY_QUERY, context.getResources().getString(R.string.API_KEY))
                        .build();
                break;
        }

        URL finalUrl = null;
        try {
            finalUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("NetworkUtils", "Built URL - buildUrlByEndpointType: ", e);
        }

        return finalUrl;
    }

    public static URL buildUrlById(Context context, String detailType, long movieId){
        Uri buildUri;

        switch (detailType){
            case "trailers":
                buildUri = Uri.parse(MOVIES_BASE_URL + "/" + movieId + TRAILERS_ENDPOINT).buildUpon()
                        .appendQueryParameter(API_KEY_QUERY, context.getResources().getString(R.string.API_KEY))
                        .build();
                break;
            case "reviews":
                buildUri = Uri.parse(MOVIES_BASE_URL + "/" + movieId + REVIEWS_ENDPOINT).buildUpon()
                        .appendQueryParameter(API_KEY_QUERY, context.getResources().getString(R.string.API_KEY))
                        .build();
                break;
            default:
                throw new RuntimeException("Bad detail url type");
        }

        URL finalUrl = null;
        try{
            finalUrl = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("NetworkUtils", "Built URL - buildUrlById: ", e);
        }
        return finalUrl;
    }

    public static URL buildPosterUrl(Context context, String moviePosterUrl){
        Uri buildUri = Uri.parse(MOVIE_POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(context.getString(R.string.image_size))
                .appendEncodedPath(moviePosterUrl).build();

        URL finalUrl = null;
        try{
            finalUrl = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("NetworkUtils", "Build URL - buildPosterUrl: ", e);
        }

        return finalUrl;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
