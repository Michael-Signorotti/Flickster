package com.codepath.flickster.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.R.attr.name;
import static android.R.attr.orientation;
import static java.security.AccessController.getContext;


/**
 * Created by michaelsignorotti on 9/17/17.
 */

public class DetailsActivity extends AppCompatActivity {

    private ImageView ivMovieDetailsImage;
    private TextView tvBudget;
    private TextView tvGenres;
    private TextView tvReleaseDate;
    private TextView tvRevenue;
    private TextView tvRuntime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivMovieDetailsImage = (ImageView) findViewById(R.id.ivMovieDetailsImage);
        tvBudget = (TextView) findViewById(R.id.tvBudget);
        tvGenres = (TextView) findViewById(R.id.tvGenres);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvRevenue = (TextView) findViewById(R.id.tvRevenue);
        tvRuntime = (TextView) findViewById(R.id.tvRuntime);


        //create the url for getting movie-specific information as well as trailers
        String url = "https://api.themoviedb.org/3/movie/";
        url += getIntent().getStringExtra("id");
        url += "?api_key=";
        url += getString(R.string.the_movie_database_api_key);
        url += "&append_to_response=videos";
        Log.d("DEBUG", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String originalTitle = "Flickster";
                String budget = "";
                String genres = "";
                String releaseDate = "";
                String revenue = "";
                String runtime = "";
                String voteAverage = "";
                String trailer = "";
                String backdropPath = "";

                try {
                    if (response.optString("original_title") != null) {
                        originalTitle = response.getString("original_title");
                    }
                    if (response.optString("budget") != null) {
                        budget = response.getString("budget");
                    }
                    if (response.optString("release_date") != null) {
                        releaseDate = response.getString("release_date");
                    }
                    if (response.optString("revenue") != null) {
                        revenue = response.getString("revenue");
                    }
                    if (response.optString("runtime") != null) {
                        runtime = response.getString("runtime");
                    }
                    if (response.optString("backdrop_path") != null) {
                        backdropPath = response.getString("backdrop_path");
                    }
                    if (response.optJSONArray("genres") != null) {
                        JSONArray jsonArray = response.getJSONArray("genres");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj =  (JSONObject) jsonArray.get(i);
                            if (obj.optString("name") != null) {
                                if (i + 1 == jsonArray.length()) {
                                    genres += "and " + obj.getString("name");
                                } else {

                                    genres += obj.getString("name") + ", ";
                                }

                            }

                        }
                    }

                    //update the action bar title
                    setTitle(originalTitle);


                    tvBudget.setText(convertDollarAmt(budget));
                    tvGenres.setText(genres);
                    tvReleaseDate.setText(releaseDate);
                    tvRevenue.setText(convertDollarAmt(revenue));
                    String runtimeStr = runtime + " minutes";
                    tvRuntime.setText(runtimeStr);


                    Picasso.with(DetailsActivity.this).load(String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath))
                            .transform(new RoundedCornersTransformation(5, 5))
                            .fit().centerCrop()
                            .placeholder(R.drawable.flickster_portrait).into(ivMovieDetailsImage);


                    Log.d("DEBUG", "Retrieved the following information");
                    Log.d("DEBUG", originalTitle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public String convertDollarAmt(String amount) {
        String adjBudget = "$";
        int len = amount.length();
        char[] amountArr = amount.toCharArray();
        for (int i = 0; i < len; i++) {
            adjBudget += amountArr[i];
            int amtLeft = len - i;
            if (i != 0 && amtLeft % 3 == 1 && amtLeft > 3) {
                adjBudget += ",";
            }
        }
        return adjBudget;
    }


}
