package com.example.android.popularMovies2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularMovies2.utilities.NetworkUtils;
import com.example.android.popularMovies2.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReviewsTrailersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {
    MovieUrls mMovieUrls;
    String[] reviewLabels;
    String[] trailerLabels;
    String[] reviewUrls;
    String[] trailerUrls;
    String[] mRTJson;
    int movieId;
    final int DEFAULT_ID = -1;
    ListView mReviewsLv;
    ListView mTrailersLv;
    TextView mErrorMsg;
    Intent intent;
    private static final int MOVIE_URLS_LOADER = 100;
    private static final String MOVIE_ID_EXTRA = "movie_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_trailers);
        mReviewsLv = (ListView) findViewById(R.id.reviews_lv);
        mTrailersLv = (ListView) findViewById(R.id.trailers_lv);
        mErrorMsg = (TextView) findViewById(R.id.error_message);
        intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        movieId = intent.getIntExtra(DetailActivity.MOVIE_ID, DEFAULT_ID);
        if (movieId == DEFAULT_ID) {
            // MOVIE_ID not found in intent
            closeOnError();
            return;
        }
        //the delay below is needed but I don't know why?
        getSupportLoaderManager().initLoader(MOVIE_URLS_LOADER, null, this);
        getMovieUrls();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            reviewUrls = OpenMovieJsonUtils.getReviewUrls(mRTJson[0]);
            trailerUrls = OpenMovieJsonUtils.getTrailerUrls(mRTJson[1]);
        } catch (JSONException e){
            e.printStackTrace();
        }

        mMovieUrls = new MovieUrls(movieId, reviewUrls, trailerUrls);
        createLabels();
        ArrayAdapter<String> reviewsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviewLabels);
        mReviewsLv.setAdapter(reviewsAdapter);
        ArrayAdapter<String> trailersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trailerLabels);
        mTrailersLv.setAdapter(trailersAdapter);
        mTrailersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openWebPage(mMovieUrls.trailerUrls[position]);
            }
        });
        mReviewsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openWebPage(mMovieUrls.reviewUrls[position]);
            }
        });

    }//end onCreate()
    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mRTJson2;

            @Override
            public void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (mRTJson2 != null) {
                    deliverResult(mRTJson2);
                } else {
                    forceLoad();
                }
            }//end onStartLoading()

            @Override
            public void forceLoad() {
                super.forceLoad();
            }

            @Override
            public String[] loadInBackground() {
                int movieId = args.getInt(MOVIE_ID_EXTRA);
                if (movieId == 0) {
                    return null;
                }
                try {
                    String[] RTJson3 = new String[2];
                    RTJson3[0] = NetworkUtils.getReviewsJson(movieId);
                    RTJson3[1] = NetworkUtils.getTrailersJson(movieId);
                    mRTJson = RTJson3;
                    return RTJson3;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                mRTJson2 = data;
                super.deliverResult(data);
            }
        };//end AsyncTaskLoader()
    }//end onCreateLoader
    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        if (data == null){
            showErrorMessage();
        } else {
            showMovieLists();
            mRTJson = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    private void getMovieUrls(){
        Bundle movieUrlsBundle = new Bundle();
        movieUrlsBundle.putInt(MOVIE_ID_EXTRA, movieId);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String[]> movieUrlsLoader = loaderManager.getLoader(MOVIE_URLS_LOADER);
        if (movieUrlsLoader == null) {
            loaderManager.initLoader(MOVIE_URLS_LOADER, movieUrlsBundle, this).startLoading();
        } else {
            loaderManager.restartLoader(MOVIE_URLS_LOADER, movieUrlsBundle, this).startLoading();
        }

    }//end getMovieUrls()
    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private void createLabels(){
        int reviewUrlsSize = mMovieUrls.reviewUrls.length;
        int trailerUrlsSize = mMovieUrls.trailerUrls.length;
        reviewLabels = new String[reviewUrlsSize];
        trailerLabels = new String[trailerUrlsSize];
        for (int i = 0; i < reviewUrlsSize; ++i){
            reviewLabels[i] = "Review " + (i + 1);
        }
        for (int i = 0; i < trailerUrlsSize; ++i){
            trailerLabels[i] = "Trailer " + (i + 1);
        }
    }
    private void showErrorMessage(){
        mErrorMsg.setVisibility(View.VISIBLE);
        mTrailersLv.setVisibility(View.INVISIBLE);
        mReviewsLv.setVisibility(View.INVISIBLE);
    }
    private void showMovieLists(){
        mErrorMsg.setVisibility(View.INVISIBLE);
        mTrailersLv.setVisibility(View.VISIBLE);
        mReviewsLv.setVisibility(View.VISIBLE);
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
