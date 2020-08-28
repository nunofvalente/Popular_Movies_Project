package com.example.android.popularmovies.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TheMovieDbConfig.getBaseUrlMovies())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
