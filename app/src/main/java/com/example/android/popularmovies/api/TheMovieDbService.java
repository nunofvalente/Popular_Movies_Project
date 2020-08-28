package com.example.android.popularmovies.api;

import com.example.android.popularmovies.model.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// https://api.themoviedb.org/3/movie/latest?api_key=78922c320816ae000b12407eee1b1297&language=en-US

public interface TheMovieDbService {

    @GET("/3/movie/{category}")
    Call<MovieModel> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

}
