package com.example.android.popularmovies.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorite_movies")
public class FavoriteMovieModel implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private final int id;
    private final String title;
    private final String overview;
    private final String release_date;
    private final double vote_average;
    private final String poster_path;

    public FavoriteMovieModel(int id, String title, String overview, String release_date, double vote_average, String poster_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getOverview() {
        return overview;
    }


    public String getRelease_date() {
        return release_date;
    }


    public double getVote_average() {
        return vote_average;
    }


    public String getPoster_path() {
        return poster_path;
    }

}
