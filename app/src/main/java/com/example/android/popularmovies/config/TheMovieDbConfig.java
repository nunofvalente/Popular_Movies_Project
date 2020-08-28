package com.example.android.popularmovies.config;


import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.net.URL;

public class TheMovieDbConfig {

    /** For the App to work you need to replace BuildConfig.ApiKey with your own TheMovieDB API key */

    private static final String THE_MOVIE_DATABASE_API_KEY = BuildConfig.ApiKey;
    private static final String BASE_URL_MOVIES = "https://api.themoviedb.org";
    private static final String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/";
    private static final String LANGUAGE = "en-US";
    private static final String CATEGORY_TOP_RATED = "top_rated";
    private static final String CATEGORY_POPULAR = "popular";

    public static String getMovieDbApiKey() {
        return THE_MOVIE_DATABASE_API_KEY;
    }

    public static String getBaseUrlMovies() {
        return BASE_URL_MOVIES;
    }

    public static String getLanguage() {
        return LANGUAGE;
    }

   /* public static URL getImage(String filePath) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("https").authority("image.tmdb.org")
                .path("/t/p/").path(filePath).build();
        URL url = uri.toString();
    }*/
}
