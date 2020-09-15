package com.example.android.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MoviesAdapter;
import com.example.android.popularmovies.api.TheMovieDbService;
import com.example.android.popularmovies.config.RetrofitConfig;
import com.example.android.popularmovies.config.TheMovieDbConfig;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * For the App to work you need to replace BuildConfig.ApiKey with your own TheMovieDB API key in TheMovieDbConfig class
 */

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private static final String CATEGORY_TOP_RATED = "top_rated";
    private static final String CATEGORY_POPULAR = "popular";
    public static final String TAG = "movie_details";
    private static int pageCount = 2;

    private ActivityMainBinding mBinding;
    private List<MovieModel.ResultsBean> listOfMovies;
    private MoviesAdapter adapter;
    private Parcelable recyclerViewState;
    private GridLayoutManager gridLayoutManager;
    private String mChosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getMovies(CATEGORY_POPULAR);
        configureRecyclerView();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dpWidth = metrics.widthPixels / metrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void configureRecyclerView() {
        gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mBinding.recyclerMovieList.setLayoutManager(gridLayoutManager);
        mBinding.recyclerMovieList.setHasFixedSize(true);

        mBinding.recyclerMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                   int page = pageCount++;
                   loadMorePages(page);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_popular_movies:
                getMovies(CATEGORY_POPULAR);
                break;
            case R.id.menu_top_rated:
                getMovies(CATEGORY_TOP_RATED);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads more pages when recyclerView reaches its end
     * @param page
     */
    private void loadMorePages(int page) {
        Retrofit retrofit = RetrofitConfig.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<MovieModel> call = service.getMovies(mChosenCategory, TheMovieDbConfig.getMovieDbApiKey(), TheMovieDbConfig.getLanguage(), page);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel result = response.body();
                assert result != null;
                List<MovieModel.ResultsBean> listToAdd = result.getResults();
                listOfMovies.addAll(listToAdd);

               // adapter = new MoviesAdapter(listOfMovies, MainActivity.this, MainActivity.this);
                //recyclerMovies.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showRecyclerView();

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    /**
     * Returns the information from the API to populate the RecyclerView with movies
     * @param category
     */
    private void getMovies(String category) {
        showProgressBar();
        Retrofit retrofit = RetrofitConfig.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        mChosenCategory = category;

        Call<MovieModel> call = service.getMovies(category, TheMovieDbConfig.getMovieDbApiKey(), TheMovieDbConfig.getLanguage(), 1);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel result = response.body();
                assert result != null;
                listOfMovies = result.getResults();

               adapter = new MoviesAdapter(listOfMovies, MainActivity.this, MainActivity.this);
               mBinding.recyclerMovieList.setAdapter(adapter);

                showRecyclerView();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showRecyclerView() {
        mBinding.progressBarMain.setVisibility(View.INVISIBLE);
        mBinding.recyclerMovieList.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mBinding.progressBarMain.setVisibility(View.VISIBLE);
        mBinding.recyclerMovieList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickItem(int clickIndex) {
        MovieModel.ResultsBean movie = listOfMovies.get(clickIndex);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(TAG, movie);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        recyclerViewState = gridLayoutManager.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gridLayoutManager.onRestoreInstanceState(recyclerViewState);
    }
}