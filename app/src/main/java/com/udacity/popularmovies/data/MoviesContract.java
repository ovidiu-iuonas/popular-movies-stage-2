package com.udacity.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_MOVIES = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIES).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
