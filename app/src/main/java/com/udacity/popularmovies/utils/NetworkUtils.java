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
    private static final String POPULAR_MOVIES_ENDPOINT = "/popular";
    private static final String TOP_RATED_MOVIES_ENDPOINT = "/top_rated";
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
            Log.e("NetworkUtils", "Built URI - buildUrlByEndpointType");
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
