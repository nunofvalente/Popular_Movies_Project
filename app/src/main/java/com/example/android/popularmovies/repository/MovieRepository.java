package com.example.android.popularmovies.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.database.MovieDao;
import com.example.android.popularmovies.database.MovieDatabase;
import com.example.android.popularmovies.executor.MovieExecutor;
import com.example.android.popularmovies.model.FavoriteMovieModel;

import java.util.List;



public class MovieRepository {

    private static final String DB_TAG = "Database Operation";

    private final MovieDao movieDao;
    private final LiveData<List<FavoriteMovieModel>> getAllFavoriteMovies;
    private final MovieExecutor movieExecutor = MovieExecutor.getInstance();

    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        getAllFavoriteMovies = movieDao.getAllFavoriteMovies();

    }

    //----------------------------Database------------------------------

    public void insert(FavoriteMovieModel favMovie) {
        movieExecutor.getDatabaseExecutor().execute(() -> movieDao.insertFavoriteMovie(favMovie));
        Log.d(DB_TAG, "Movie inserted");
    }

    public void delete(FavoriteMovieModel favMovie) {
        movieExecutor.getDatabaseExecutor().execute(() -> movieDao.removeFavoriteMovie(favMovie));
        Log.d(DB_TAG, "Movie deleted");
    }

    public FavoriteMovieModel getFavoriteMovie(int id) {
        return movieDao.getFavoriteMovie(id);
    }

    public LiveData<List<FavoriteMovieModel>> getAllFavoriteMovies() {
        return getAllFavoriteMovies;
    }

}

