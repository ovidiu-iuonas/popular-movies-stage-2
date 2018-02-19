package com.udacity.popularmovies.models;

public class Movie {

    private int mId;
    private String mTitle;
    private String mPosterImageUrl;
    private String mOverview;
    private String mReleaseDate;
    private double mRating;

    public Movie(int id, String title, String posterImageUrl, String overview, double rating, String releaseDate){
        this.mId = id;
        this.mTitle = title;
        this.mPosterImageUrl = posterImageUrl;
        this.mOverview = overview;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPosterImageUrl() {
        return mPosterImageUrl;
    }

    public void setmPosterImageUrl(String mPosterImageUrl) {
        this.mPosterImageUrl = mPosterImageUrl;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }
}
