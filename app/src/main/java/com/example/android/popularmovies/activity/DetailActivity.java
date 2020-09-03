package com.example.android.popularmovies.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.config.TheMovieDbConfig;
import com.example.android.popularmovies.model.MovieModel;

public class DetailActivity extends AppCompatActivity {

    private MovieModel.ResultsBean movie;
    private TextView textTitle, textDescription, textYear, textRating;
    private ImageView imageMoviePoster;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeComponents();

        getMovieData();
        setData();
    }

    private void initializeComponents() {
        textTitle = findViewById(R.id.textDetailsMovieTitle);
        textDescription = findViewById(R.id.textDetailsMovieDescription);
        textYear = findViewById(R.id.textDetailsMovieYear);
        imageMoviePoster = findViewById(R.id.imageDetailsPoster);
        textRating = findViewById(R.id.textDetailRating);
        ratingBarDetails = findViewById(R.id.ratingBarDetails);
    }

    private void getMovieData() {
        movie = (MovieModel.ResultsBean) getIntent().getSerializableExtra(MainActivity.TAG);
    }

    private void setData() {
        //Title of the movie
        textTitle.setText(movie.getTitle());

        //Poster Image
        String poster_path = movie.getPoster_path();
        if (poster_path != null) {
            String url = TheMovieDbConfig.getImage(poster_path);
            Uri uri = Uri.parse(url);
            Glide.with(this).load(uri).error(R.drawable.default_image_thumbnail).into(imageMoviePoster);
        } else {
            imageMoviePoster.setImageResource(R.drawable.default_image_thumbnail);
        }

        //Rating text
        String rating = String.valueOf(movie.getVote_average());
        textRating.setText(rating);

        //Description
        textDescription.setText(movie.getOverview());

        //Rating stars
        Drawable drawable = ratingBarDetails.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#53B4DF"), PorterDuff.Mode.SRC_ATOP);
        ratingBarDetails.setRating(Float.parseFloat(rating) * (float) 0.5);

        //Release Date
        String movieDate = movie.getRelease_date();
        textYear.setText(movieDate);

    }
}