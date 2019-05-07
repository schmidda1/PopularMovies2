/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularMovies2.utilities;

import android.content.ContentValues;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public final class OpenMovieJsonUtils {


    public static int maxReviews = 5;
    public static int maxTrailers = 5;


    public static ArrayList<movieModel> getMovieListFromJson(String movieJsonStr)
            throws JSONException {

        final String posterUrlBase = "http://image.tmdb.org/t/p/w185/";


        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        ArrayList<movieModel> moviesList = new ArrayList<>(movieArray.length());

        for (int i = 0; i < movieArray.length(); i++) {
            int movieId;
            String originalTitle;
            String moviePoster;
            String plotSynopsis;
            float vote_average = -1.0f;
            String releaseDate;
            String posterUrl;

            JSONObject movieItemObj = movieArray.getJSONObject(i);
            movieId = movieItemObj.getInt("id");
            originalTitle = movieItemObj.getString("original_title");
            if (originalTitle == "")originalTitle = "No_information";
            moviePoster = movieItemObj.getString("poster_path");
            if(moviePoster == "")moviePoster = "No_information";
            plotSynopsis = movieItemObj.getString("overview");
            if(plotSynopsis == "")plotSynopsis = "No_information";
            vote_average = (float)movieItemObj.getDouble("vote_average");
            releaseDate = movieItemObj.getString("release_date");
            if(releaseDate == "")releaseDate = "No_information";
            posterUrl = posterUrlBase + moviePoster;
            com.example.android.popularMovies2.utilities.movieModel movie_model = new com.example.android.popularMovies2.utilities.movieModel(movieId, originalTitle, moviePoster, plotSynopsis, vote_average, releaseDate, posterUrl);
            moviesList.add(movie_model);
        }

        return moviesList;
    }
    public static String[] getReviewUrls(String reviewsJsonStr)throws JSONException {
        int limit;
        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray("results");
        limit = reviewsArray.length() <= maxReviews ? reviewsArray.length(): maxReviews;
        String[] reviewUrls = new String[limit];
        for (int i = 0; i < limit; ++i){
            JSONObject reviewUrlItem = reviewsArray.getJSONObject(i);
            reviewUrls[i] = reviewUrlItem.getString("url");
        }
        return reviewUrls;
    }
    public static String[] getTrailerUrls(String trailersJsonStr)throws JSONException {
        int limit;
        final String TRAILERS_BASE_URL = "https://www.youtube.com/watch?v=";
        JSONObject trailersJson = new JSONObject(trailersJsonStr);
        JSONArray trailersArray = trailersJson.getJSONArray("results");
        limit = trailersArray.length() <= maxTrailers ? trailersArray.length(): maxTrailers;
        String[] trailerUrls = new String[limit];
        for (int i = 0; i < limit; ++i){
            JSONObject trailerUrlItem = trailersArray.getJSONObject(i);
            trailerUrls[i] = TRAILERS_BASE_URL + trailerUrlItem.getString("key");
        }
        return trailerUrls;
    }

}