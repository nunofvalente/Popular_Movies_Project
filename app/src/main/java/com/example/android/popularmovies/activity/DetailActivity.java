package com.example.android.popularmovies.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewsAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.api.TheMovieDbService;
import com.example.android.popularmovies.api.RetrofitService;
import com.example.android.popularmovies.api.TheMovieDbConfig;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.FavoriteMovieModel;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ReviewsModel;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.util.NetworkUtils;
import com.example.android.popularmovies.viewmodel.MovieViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private MovieViewModel movieViewModel;

    private static final int PAGE = 1;

    private FavoriteMovieModel mMovie;
    private TrailerAdapter mAdapterTrailers;
    private ReviewsAdapter mAdapterReviews;
    private List<TrailerModel.ResultsBean> mListOfTrailers;
    private List<ReviewsModel.ResultsBean> mListOfReviews;
    private ActivityDetailBinding mBinding;

    private Retrofit retrofit;
    private TheMovieDbService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mBinding.progressBarTrailers.setVisibility(View.INVISIBLE);
        if(NetworkUtils.checkConnectivity(getApplication())) {
            mBinding.tvNoConnection.setVisibility(View.INVISIBLE);
        }
        movieViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MovieViewModel.class);


        getMovieData();
        setData();
        configureRecyclerViews();
    }


    private void configureRecyclerViews() {
        RecyclerView.LayoutManager layoutManagerTrailers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.mRecyclerTrailers.setLayoutManager(layoutManagerTrailers);

        RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        mBinding.mRecyclerReviews.setLayoutManager(layoutManagerReviews);
        mBinding.mRecyclerReviews.setHasFixedSize(true);
    }

    /**
     * Gets the intent with the information regarding the movie that was clicked
     */

    private void getMovieData() {
        if (MainActivity.TAG.equals(MainActivity.MOVIE)) {
            MovieModel.ResultsBean movie = (MovieModel.ResultsBean) getIntent().getSerializableExtra(MainActivity.TAG);
            assert movie != null;
            mMovie = new FavoriteMovieModel(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getRelease_date(), movie.getVote_average(), movie.getPoster_path());
        } else if (MainActivity.TAG.equals(MainActivity.FAV_MOVIES)) {
            mMovie = (FavoriteMovieModel) getIntent().getSerializableExtra(MainActivity.TAG);
        }
    }

    /**
     * This method sets the UI using the movie information that was retrieved from previous activity.
     */
    private void setData() {
        //Title of the movie
        mBinding.textDetailsMovieTitle.setText(mMovie.getTitle());

        //Poster Image
        String poster_path = mMovie.getPoster_path();
        if (poster_path != null) {
                String url = TheMovieDbConfig.getImage(poster_path);
                Uri uri = Uri.parse(url);
                Glide.with(this).load(uri).error(R.drawable.default_image_thumbnail).into(mBinding.imageDetailsPoster);
        } else {
            mBinding.imageDetailsPoster.setImageResource(R.drawable.default_image_thumbnail);
        }

        //Rating text
        String rating = String.valueOf(mMovie.getVote_average());
        mBinding.textDetailRating.setText(rating);

        //Description
        mBinding.textDetailsMovieDescription.setText(mMovie.getOverview());

        //Rating stars
        RatingBar ratingBarDetails = mBinding.ratingBarDetails;
        Drawable drawable = ratingBarDetails.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#53B4DF"), PorterDuff.Mode.SRC_ATOP);
        ratingBarDetails.setRating(Float.parseFloat(rating) * (float) 0.5);

        //Release Date
        String movieDate = mMovie.getRelease_date();
        mBinding.textDetailsMovieYear.setText(movieDate);
    }

    /**
     * Method to get Trailers from API
     */
    private void getTrailers() {
        showProgressBar();
        retrofit = RetrofitService.getRetrofit();
        service = retrofit.create(TheMovieDbService.class);
        if (NetworkUtils.checkConnectivity(getApplication())) {
            Call<TrailerModel> call = service.getTrailers(
                    mMovie.getId(),
                    TheMovieDbConfig.getMovieDbApiKey(),
                    TheMovieDbConfig.getLanguage());

            call.enqueue(new Callback<TrailerModel>() {
                @Override
                public void onResponse(@NotNull Call<TrailerModel> call, @NotNull Response<TrailerModel> response) {
                    TrailerModel result = response.body();
                    assert result != null;
                    mListOfTrailers = result.getResults();

                    mAdapterTrailers = new TrailerAdapter(mListOfTrailers, DetailActivity.this);
                    mBinding.mRecyclerTrailers.setAdapter(mAdapterTrailers);

                    showRecyclerView();
                }

                @Override
                public void onFailure(@NotNull Call<TrailerModel> call, @NotNull Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            mBinding.progressBarTrailers.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method to get reviews from API
     */

    private void getReviews() {
        retrofit = RetrofitService.getRetrofit();
        service = retrofit.create(TheMovieDbService.class);
        Call<ReviewsModel> call = service.getReviews(
                mMovie.getId(),
                TheMovieDbConfig.getMovieDbApiKey(),
                TheMovieDbConfig.getLanguage(),
                PAGE
        );

        call.enqueue(new Callback<ReviewsModel>() {
            @Override
            public void onResponse(@NotNull Call<ReviewsModel> call, @NotNull Response<ReviewsModel> response) {
                ReviewsModel result = response.body();
                assert result != null;
                mListOfReviews = result.getResults();

                mAdapterReviews = new ReviewsAdapter(mListOfReviews, DetailActivity.this);
                mBinding.mRecyclerReviews.setAdapter(mAdapterReviews);

            }

            @Override
            public void onFailure(@NotNull Call<ReviewsModel> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showRecyclerView() {
        mBinding.progressBarTrailers.setVisibility(View.INVISIBLE);
        mBinding.mRecyclerTrailers.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mBinding.progressBarTrailers.setVisibility(View.VISIBLE);
        mBinding.mRecyclerTrailers.setVisibility(View.INVISIBLE);
    }

    /**
     * Set OnClick Event for the trailers RecyclerView opening Youtube app to play the trailer
     *
     *
     */
    @Override
    public void onClickItem(int clickIndex) {
        TrailerModel.ResultsBean trailer = mListOfTrailers.get(clickIndex);
        String key = trailer.getKey();
        String url = TheMovieDbConfig.YOUTUBE_VIDEO_BASE_URL + key;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.getItem(0);
        int id = mMovie.getId();
        AsyncTask<Integer, Void, FavoriteMovieModel> task = new AsyncTask<Integer, Void, FavoriteMovieModel>() {

            @Override
            protected FavoriteMovieModel doInBackground(Integer... integers) {
                return movieViewModel.getFavoriteMovie(integers[0]);
            }

            @Override
            protected void onPostExecute(FavoriteMovieModel favoriteMovieModel) {
                if (favoriteMovieModel == null) {
                    item.setIcon(R.drawable.ic_favorite_empty);
                } else {
                    item.setIcon(R.drawable.ic_favorite_full);
                }
            }
        };
        task.execute(id);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Button to add or remove a movie from Favorites
     *
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = mMovie.getId();
        AsyncTask<Integer, Void, FavoriteMovieModel> task = new AsyncTask<Integer, Void, FavoriteMovieModel>() {

            @Override
            protected FavoriteMovieModel doInBackground(Integer... integers) {
                return movieViewModel.getFavoriteMovie(integers[0]);
            }

            @Override
            protected void onPostExecute(FavoriteMovieModel favoriteMovieModel) {
                if (favoriteMovieModel == null) {
                    FavoriteMovieModel favoriteMovie = new FavoriteMovieModel(mMovie.getId(), mMovie.getTitle(), mMovie.getOverview(), mMovie.getRelease_date(), mMovie.getVote_average(), mMovie.getPoster_path());
                    movieViewModel.insert(favoriteMovie);
                    item.setIcon(R.drawable.ic_favorite_full);
                    Toast.makeText(DetailActivity.this, "Movie added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    movieViewModel.delete(favoriteMovieModel);
                    item.setIcon(R.drawable.ic_favorite_empty);
                    Toast.makeText(DetailActivity.this, "Movie removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (item.getItemId() == R.id.action_favorite) {
            task.execute(id);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        getTrailers();
        getReviews();
        super.onStart();
    }
}
