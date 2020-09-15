package com.example.android.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.android.popularmovies.model.FavoriteMovieModel;


@Database(entities = {FavoriteMovieModel.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies_favored";
    private static MovieDatabase sInstance;
    public abstract MovieDao movieDao();

    /**
     * Singleton that creates the Database Instance or returns if already created.
     * @param context
     * @return MovieDatabase
     */
    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating Database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DATABASE_NAME).build();
            }
        }
        Log.d(TAG, "Getting database Instance");
        return sInstance;
    }
}
