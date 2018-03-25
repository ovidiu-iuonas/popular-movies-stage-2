package com.udacity.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES, MOVIE);
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri){
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                return retCursor;
            }
            case MOVIE_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        strings,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        s1);
                return retCursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MOVIE: {
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_MOVIES, null, contentValues);
                if (_id > 0){
                    returnUri = MoviesContract.MovieEntry.buildMoviesUri(_id);
                } else
                    returnUri = null;
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numRowsDeleted;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                numRowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES, s, strings);
            }
            break;
            case MOVIE_WITH_ID:{
                numRowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
