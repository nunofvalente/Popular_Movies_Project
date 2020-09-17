package com.example.android.popularmovies.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private final static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private final static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging);

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(TheMovieDbConfig.getBaseUrlMovies())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    public static Retrofit getRetrofit() {
       return retrofit;
    }

}
