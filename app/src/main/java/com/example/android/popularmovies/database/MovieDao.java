package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.popularmovies.model.FavoriteMovieModel;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY vote_average")
    LiveData<List<FavoriteMovieModel>> getAllFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovieModel favMovie);

    @Delete
    void removeFavoriteMovie(FavoriteMovieModel favMovie);

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    FavoriteMovieModel getFavoriteMovie(int id);

}
