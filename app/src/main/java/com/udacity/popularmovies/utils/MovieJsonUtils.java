package com.udacity.popularmovies.utils;


import com.udacity.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJsonUtils {

    public static List<Movie> parseJsonResponse(String jsonResponse) throws JSONException{
        List<Movie> moviesList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray jsonArrayMovies = jsonObject.getJSONArray("results");

        for(int i = 0; i < jsonArrayMovies.length(); i++){
            JSONObject jsonObjectMovie = jsonArrayMovies.getJSONObject(i);

            int id = jsonObjectMovie.getInt("id");
            String title = jsonObjectMovie.getString("title");
            String overview = jsonObjectMovie.getString("overview");
            String releaseDate = jsonObjectMovie.getString("release_date");
            String posterImageUrl = jsonObjectMovie.getString("poster_path");
            double rating = jsonObjectMovie.getDouble("vote_average");

            Movie movie = new Movie(id, title, posterImageUrl, overview, rating, releaseDate);

            moviesList.add(movie);
        }

        return moviesList;
    }

}
