package com.udacity.popularmovies.utils;

import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.models.Review;
import com.udacity.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJsonUtils {

    public static List<Movie> parseMoviesListJsonResponse(String jsonResponse) throws JSONException{
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

    public static List<Review> parseReviewsListJson(String jsonResponse) throws JSONException{
        List<Review> reviews = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray jsonArrayReviews = jsonObject.getJSONArray("results");

        for(int i = 0; i < jsonArrayReviews.length(); i++){
            JSONObject jsonObject1Review = jsonArrayReviews.getJSONObject(i);
            String id = jsonObject1Review.getString("id");
            String author = jsonObject1Review.getString("author");
            String content = jsonObject1Review.getString("content");
            String url = jsonObject1Review.getString("url");

            Review review = new Review(id, author, content, url);

            reviews.add(review);
        }

        return reviews;
    }

    public static List<Trailer> parseTrailersListJson(String jsonResponse) throws JSONException{
        List<Trailer> trailers = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray jsonArrayTrailersQuickTime = jsonObject.getJSONArray("quicktime");
        JSONArray jsonArrayTrailersYoutube = jsonObject.getJSONArray("youtube");

        for(int i = 0; i < jsonArrayTrailersQuickTime.length(); i++){
            JSONObject jsonObject1TrailerQ = jsonArrayTrailersQuickTime.getJSONObject(i);
            String source = jsonObject1TrailerQ.getString("source");
            String name = jsonObject1TrailerQ.getString("name");

            Trailer trailer = new Trailer(Trailer.TRAILER_TYPE.QUICKTIME, source, name);
            trailers.add(trailer);
        }

        for(int i = 0; i < jsonArrayTrailersYoutube.length(); i++){
            JSONObject jsonObject1TrailerY = jsonArrayTrailersYoutube.getJSONObject(i);

            String source = jsonObject1TrailerY.getString("source");
            String name = jsonObject1TrailerY.getString("name");

            Trailer trailer = new Trailer(Trailer.TRAILER_TYPE.YOUTUBE, source, name);
            trailers.add(trailer);
        }

        return trailers;
    }

}
