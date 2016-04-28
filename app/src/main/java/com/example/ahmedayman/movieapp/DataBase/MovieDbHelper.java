package com.example.ahmedayman.movieapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed Ayman on 27-Apr-16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieData.TABLE_NAME + " (" +
                MovieContract.MovieData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieData.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieData.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieData.COLUMN_IMAGE + " TEXT, " +
                MovieContract.MovieData.COLUMN_IMAGE_COPY + " TEXT, " +
                MovieContract.MovieData.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieData.COLUMN_RATING + " INTEGER, " +
                MovieContract.MovieData.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieData.TABLE_NAME);
        onCreate(db);
    }
}
