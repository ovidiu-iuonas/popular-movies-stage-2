package com.udacity.popularmovies.models;

public class Trailer {

    public enum TRAILER_TYPE{
        YOUTUBE,
        QUICKTIME
    }

    private TRAILER_TYPE mType;
    private String mSource;
    private String mName;

    public Trailer(TRAILER_TYPE trailerType, String source, String name){
        this.mSource = source;
        this.mName = name;
        this.mType = trailerType;
    }

    public TRAILER_TYPE getmType() {
        return mType;
    }

    public void setmType(TRAILER_TYPE mType) {
        this.mType = mType;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

}
