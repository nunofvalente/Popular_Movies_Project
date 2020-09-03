package com.example.android.popularmovies.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging);

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TheMovieDbConfig.getBaseUrlMovies())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


}
