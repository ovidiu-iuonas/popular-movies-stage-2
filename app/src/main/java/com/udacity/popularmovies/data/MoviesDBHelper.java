package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    MoviesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_MOVIES + "(" +
                MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DROP_MOVIES_TABLE = "DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_MOVIES;
        sqLiteDatabase.execSQL(SQL_DROP_MOVIES_TABLE);
        final String SQL_DELETE_FROM_SQL = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MoviesContract.MovieEntry.TABLE_MOVIES + "'";
        sqLiteDatabase.execSQL(SQL_DELETE_FROM_SQL);

        onCreate(sqLiteDatabase);
    }
}
