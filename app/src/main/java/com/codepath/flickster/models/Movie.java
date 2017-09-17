package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by michaelsignorotti on 9/13/17.
 */

public class Movie {


    private String posterPath;
    private String originalTitle;
    private String overview;
    private String backdropPath;
    private String voteAverage;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getString("vote_average");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        for (int i = 0; i < array.length(); i++) {
            Movie m = null;
            try {
                m = new Movie(array.getJSONObject(i));
                movieList.add(m);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return movieList;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getVoteAverage() {
        return voteAverage;
    }
}
