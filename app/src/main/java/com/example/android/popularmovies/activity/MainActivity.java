package com.example.android.popularmovies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.api.TheMovieDbService;
import com.example.android.popularmovies.config.RetrofitConfig;
import com.example.android.popularmovies.config.TheMovieDbConfig;
import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerMovies;
    private Spinner spinnerSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit();
    }

    private void retrofit() {
        Retrofit retrofit = RetrofitConfig.getRetrofit();

        TheMovieDbService service = retrofit.create(TheMovieDbService.class);

        Call<MovieModel> call = service.getMovies("top_rated", TheMovieDbConfig.getMovieDbApiKey(), TheMovieDbConfig.getLanguage(), 1);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel result = response.body();
                assert result != null;
                List<MovieModel.ResultsBean> listOfMovies = result.getResults();
                MovieModel.ResultsBean firstMovie = listOfMovies.get(0);
                String url = firstMovie.getPoster_path();


            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}