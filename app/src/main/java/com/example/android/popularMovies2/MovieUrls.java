package com.example.android.popularMovies2;


public class MovieUrls{
    int movieId;
    String[] reviewUrls = null;
    String[] trailerUrls = null;
    public MovieUrls(){
    }
    public MovieUrls(int movieId, String[] reviewUrls, String[] trailerUrls){
        this.movieId = movieId;
        this.reviewUrls = reviewUrls;
        this.trailerUrls = trailerUrls;
    }
}