package com.example.android.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.FavoritesAdapter;
import com.example.android.popularmovies.adapter.MoviesAdapter;
import com.example.android.popularmovies.api.RetrofitService;
import com.example.android.popularmovies.api.TheMovieDbConfig;
import com.example.android.popularmovies.api.TheMovieDbService;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.FavoriteMovieModel;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.util.NetworkUtils;
import com.example.android.popularmovies.viewmodel.MovieViewModel;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.android.popularmovies.api.TheMovieDbConfig.CATEGORY_POPULAR;
import static com.example.android.popularmovies.api.TheMovieDbConfig.CATEGORY_TOP_RATED;

/**
 * For the App to work you need to replace BuildConfig.ApiKey with your own TheMovieDB API key in TheMovieDbConfig class
 */

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private static final String KEY_STATE_ORIENTATION = "position";
    public static String TAG;
    public static final String FAV_MOVIES = "fav_movies";
    public static final String MOVIE = "movie_details";
    private static int pageCount = 2;

    private List<MovieModel.ResultsBean> listOfMovies;
    private List<FavoriteMovieModel> listOfFavorites;

    private ActivityMainBinding mBinding;
    private MoviesAdapter adapter = null;
    private FavoritesAdapter favoritesAdapter = null;
    private String mChosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.progressBarMain.setVisibility(View.INVISIBLE);


        if(NetworkUtils.checkConnectivity(getApplication())) {
            getMovies(CATEGORY_POPULAR);
        } else {
            getAllFavoriteMovies();
            getSupportActionBar().setTitle(getResources().getString(R.string.favorite_movies));
        }
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

    private void configureRecyclerView(Adapter adapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mBinding.recyclerMovieList.setLayoutManager(gridLayoutManager);
        mBinding.recyclerMovieList.setHasFixedSize(true);
        mBinding.recyclerMovieList.setAdapter(adapter);


        if(!adapter.equals(favoritesAdapter)) {
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
                if(NetworkUtils.checkConnectivity(getApplication())) {
                    getMovies(CATEGORY_POPULAR);
                    getSupportActionBar().setTitle(getResources().getString(R.string.popular_movies));
                } else {
                    getAllFavoriteMovies();
                    getSupportActionBar().setTitle(getResources().getString(R.string.favorite_movies));
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_top_rated:
                if(NetworkUtils.checkConnectivity(getApplication())) {
                    getMovies(CATEGORY_TOP_RATED);
                    getSupportActionBar().setTitle(getResources().getString(R.string.top_rated_movies));
                } else {
                    getAllFavoriteMovies();
                    getSupportActionBar().setTitle(getResources().getString(R.string.favorite_movies));
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_favorites:
                getSupportActionBar().setTitle(getResources().getString(R.string.favorite_movies));
                getAllFavoriteMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllFavoriteMovies() {
        MovieViewModel movieViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MovieViewModel.class);
        movieViewModel.getFavoriteMovies().observe(this, favoriteMovieModels -> {
            listOfFavorites = favoriteMovieModels;
            favoritesAdapter = new FavoritesAdapter(listOfFavorites, MainActivity.this, MainActivity.this);
            configureRecyclerView(favoritesAdapter);
        });
    }

    /**
     * Loads more pages when recyclerView reaches its end
     *
     */
    private void loadMorePages(int page) {
        Retrofit retrofit = RetrofitService.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<MovieModel> call = service.getMovies(mChosenCategory, TheMovieDbConfig.getMovieDbApiKey(), TheMovieDbConfig.getLanguage(), page);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NotNull Call<MovieModel> call, @NotNull Response<MovieModel> response) {
                MovieModel result = response.body();
                assert result != null;
                List<MovieModel.ResultsBean> listToAdd = result.getResults();
                listOfMovies.addAll(listToAdd);

                adapter.notifyDataSetChanged();

                showRecyclerView();

            }

            @Override
            public void onFailure(@NotNull Call<MovieModel> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    /**
     * Returns the information from the API to populate the RecyclerView with movies
     *
     */
    private void getMovies(String category) {
        showProgressBar();
        Retrofit retrofit = RetrofitService.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        mChosenCategory = category;

        Call<MovieModel> call = service.getMovies(category, TheMovieDbConfig.getMovieDbApiKey(), TheMovieDbConfig.getLanguage(), 1);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NotNull Call<MovieModel> call, @NotNull Response<MovieModel> response) {
                MovieModel result = response.body();
                assert result != null;
                listOfMovies = result.getResults();

                adapter = new MoviesAdapter(listOfMovies, MainActivity.this, MainActivity.this);
                mBinding.recyclerMovieList.setAdapter(adapter);
                configureRecyclerView(adapter);
                showRecyclerView();
            }

            @Override
            public void onFailure(@NotNull Call<MovieModel> call, @NotNull Throwable t) {
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
        if(mBinding.recyclerMovieList.getAdapter() != favoritesAdapter) {
            MovieModel.ResultsBean movie = listOfMovies.get(clickIndex);
            Intent intent = new Intent(this, DetailActivity.class);
            TAG = MOVIE;
            intent.putExtra(TAG, movie);
            startActivity(intent);
        } else {
            FavoriteMovieModel movie = listOfFavorites.get(clickIndex);
            Intent intent = new Intent(this, DetailActivity.class);
            TAG = FAV_MOVIES;
            intent.putExtra(TAG, movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        if(NetworkUtils.checkConnectivity(getApplication())) {
            getMovies(CATEGORY_POPULAR);
        } else {
            getAllFavoriteMovies();
            getSupportActionBar().setTitle(getResources().getString(R.string.favorite_movies));
        }
        super.onResume();
    }
}