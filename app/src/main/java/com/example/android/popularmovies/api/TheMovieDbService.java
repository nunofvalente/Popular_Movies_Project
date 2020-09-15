package com.example.android.popularmovies.api;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ReviewsModel;
import com.example.android.popularmovies.model.TrailerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    @GET("/3/movie/{category}")
    Call<MovieModel> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("/3/movie/{id}/videos")
    Call<TrailerModel> getTrailers(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

   // https://api.themoviedb.org/3/movie/324668/reviews?api_key=78922c320816ae000b12407eee1b1297&language=en-US&page=1

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsModel> getReviews(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
