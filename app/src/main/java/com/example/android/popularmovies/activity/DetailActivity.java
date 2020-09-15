package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewsAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.api.TheMovieDbService;
import com.example.android.popularmovies.config.RetrofitConfig;
import com.example.android.popularmovies.config.TheMovieDbConfig;
import com.example.android.popularmovies.databinding.ActivityDetailBinding;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ReviewsModel;
import com.example.android.popularmovies.model.TrailerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private static final int PAGE = 1;

    private MovieModel.ResultsBean mMovie;
    private TrailerAdapter mAdapterTrailers;
    private ReviewsAdapter mAdapterReviews;
    private List<TrailerModel.ResultsBean> mListOfTrailers;
    private List<ReviewsModel.ResultsBean> mListOfReviews;

    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

       mBinding.progressBarTrailers.setVisibility(View.INVISIBLE);

        getMovieData();
        setData();
        configureRecyclerViews();
        getTrailers();
        getReviews();
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
        mMovie = (MovieModel.ResultsBean) getIntent().getSerializableExtra(MainActivity.TAG);
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

    private void getTrailers() {
        showProgressBar();
        Retrofit retrofit = RetrofitConfig.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<TrailerModel> call = service.getTrailers(
                mMovie.getId(),
                TheMovieDbConfig.getMovieDbApiKey(),
                TheMovieDbConfig.getLanguage());

        call.enqueue(new Callback<TrailerModel>() {
            @Override
            public void onResponse(Call<TrailerModel> call, Response<TrailerModel> response) {
                TrailerModel result = response.body();
                assert result != null;
                mListOfTrailers = result.getResults();

                mAdapterTrailers = new TrailerAdapter(mListOfTrailers, DetailActivity.this);
                mBinding.mRecyclerTrailers.setAdapter(mAdapterTrailers);

                showRecyclerView();
            }

            @Override
            public void onFailure(Call<TrailerModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

   private void getReviews() {
        Retrofit retrofit = RetrofitConfig.getRetrofit();
        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<ReviewsModel> call = service.getReviews(
                mMovie.getId(),
                TheMovieDbConfig.getMovieDbApiKey(),
                TheMovieDbConfig.getLanguage(),
                PAGE
        );

        call.enqueue(new Callback<ReviewsModel>() {
            @Override
            public void onResponse(Call<ReviewsModel> call, Response<ReviewsModel> response) {
                ReviewsModel result = response.body();
                assert result != null;
                mListOfReviews = result.getResults();

                mAdapterReviews = new ReviewsAdapter(mListOfReviews, DetailActivity.this);
                mBinding.mRecyclerReviews.setAdapter(mAdapterReviews);

            }

            @Override
            public void onFailure(Call<ReviewsModel> call, Throwable t) {
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
     * @param clickIndex
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}