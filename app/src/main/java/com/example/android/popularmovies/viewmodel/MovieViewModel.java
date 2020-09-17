package com.example.android.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.model.FavoriteMovieModel;
import com.example.android.popularmovies.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository repository;
    private final LiveData<List<FavoriteMovieModel>> favoriteMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        favoriteMovies = repository.getAllFavoriteMovies();
    }

    public void insert(FavoriteMovieModel favoriteMovieModel) {
        repository.insert(favoriteMovieModel);
    }

    public void delete(FavoriteMovieModel favoriteMovieModel) {
        repository.delete(favoriteMovieModel);
    }

    public FavoriteMovieModel getFavoriteMovie(int id) {
        return repository.getFavoriteMovie(id);
    }

    public LiveData<List<FavoriteMovieModel>> getFavoriteMovies() {
        return favoriteMovies;
    }
}

