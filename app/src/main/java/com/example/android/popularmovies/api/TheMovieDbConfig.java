package com.example.android.popularmovies.api;


import com.example.android.popularmovies.BuildConfig;

public class TheMovieDbConfig {

    /**
     * For the App to work you need to replace BuildConfig.ApiKey with your own TheMovieDB API key
     */

    private static final String THE_MOVIE_DATABASE_API_KEY = BuildConfig.ApiKey;
    private static final String BASE_URL_MOVIES = "https://api.themoviedb.org";
    private static final String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/";
    private static final String LANGUAGE = "en-US";

    public static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String CATEGORY_TOP_RATED = "top_rated";
    public static final String CATEGORY_POPULAR = "popular";

    public static String getMovieDbApiKey() {
        return THE_MOVIE_DATABASE_API_KEY;
    }

    public static String getBaseUrlMovies() {
        return BASE_URL_MOVIES;
    }

    public static String getLanguage() {
        return LANGUAGE;
    }

    public static String getImage(String poster_path) {
        return BASE_URL_IMAGES + "w185" + poster_path;
    }
}
