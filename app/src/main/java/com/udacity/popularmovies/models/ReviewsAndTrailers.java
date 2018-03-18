package com.udacity.popularmovies.models;

import java.util.List;

public class ReviewsAndTrailers {

    private List<Trailer> mTrailersList;
    private List<Review> mReviewsList;

    public ReviewsAndTrailers(List<Trailer> trailers, List<Review> reviews){
        this.mTrailersList = trailers;
        this.mReviewsList = reviews;
    }

    public List<Trailer> getmTrailersList() {
        return mTrailersList;
    }

    public void setmTrailersList(List<Trailer> mTrailersList) {
        this.mTrailersList = mTrailersList;
    }

    public List<Review> getmReviewsList() {
        return mReviewsList;
    }

    public void setmReviewsList(List<Review> mReviewsList) {
        this.mReviewsList = mReviewsList;
    }

}
