package com.example.android.popularMovies2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularMovies2.R;
import com.example.android.popularMovies2.utilities.NetworkUtils;
import com.example.android.popularMovies2.utilities.OpenMovieJsonUtils;
import com.example.android.popularMovies2.utilities.movieModel;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_ID = "movie_id";
    private static final int DEFAULT_POSITION = -1;
    public static int movieDataSize;
    private movieModel movie;
    ImageView mThumbnail;
    TextView mOriginalTitle;
    TextView mPlotSynopsis;
    TextView mVoteAverage;
    TextView mReleaseDate;
    TextView mErrorMsg;
    Intent intent;
    //ArrayList<NetworkUtils.MovieUrls> mMovieUrlsList = new ArrayList<>(movieDataSize);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);
        mThumbnail = (ImageView) findViewById(R.id.thumbnail);
        mOriginalTitle = (TextView) findViewById(R.id.original_title);
        mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis);
        mVoteAverage = (TextView) findViewById(R.id.vote_average);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        movie = intent.getParcelableExtra("movie");

        populateUI(movie);
        /*
        int index = getIndexIfInMovieUrlsList(movie.getMovieId());
        if (index == -1) {
            new FetchMovieUrls().execute(movie.getMovieId());
        } else {
            mMovieUrls = mMovieUrlsList.get(index);
        }
        */


    }
    public void onClickOpenRtActivity(View v) {
        launchRtActivity(movie.getMovieId());
    }
    private void launchRtActivity(int movie_Id){
        Context context = this;
        Class destClass = ReviewsTrailersActivity.class;
        Intent intent = new Intent(context, destClass);
        intent.putExtra(MOVIE_ID, movie_Id);
        startActivity(intent);
    }


/*
    private int getIndexIfInMovieUrlsList(int movieId){
        for (int i = 0; i < mMovieUrlsList.size(); ++i){
            if (movieId == mMovieUrlsList.get(i).movieId){
                return i;
            }
        }
        return -1;
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.back_to_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_main) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
            Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void populateUI(movieModel movie) {
        int sortPref = NetworkUtils.sortPref;
        Picasso.get()
                .load(movie.getPosterUrl())
                .error(R.drawable.android2)
                .into(mThumbnail);
        if (sortPref==0)
            setTitle(getString(R.string.popular_movies_title));
        else
            setTitle(getString(R.string.top_rated_title));
        mOriginalTitle.setText(movie.getOriginalTitle());
        mPlotSynopsis.setText(movie.getPlotSynopsis());
        mVoteAverage.setText(String.valueOf(movie.getVote_average()));
        mReleaseDate.setText(movie.getReleaseDate());

    }

}
