package com.example.android.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.popularmovies.database.MovieDatabase;

public class FavoriteMovieViewModel extends AndroidViewModel {

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
    }
}
