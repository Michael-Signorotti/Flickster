package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by michaelsignorotti on 9/14/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        //check if the existing view is being reused
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater =  LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);
            viewHolder.movieDescription = (TextView) convertView.findViewById(R.id.tvMovieDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //this clears the old image from convertView
        viewHolder.movieImage.setImageResource(0);
        viewHolder.movieTitle.setText(movie.getOriginalTitle());
        viewHolder.movieDescription.setText((movie.getOverview()));

        //Selectively add either the poster or backdrop image based on the screen orientation

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext()).load(movie.getPosterPath()).fit().centerCrop()
                    .placeholder(R.drawable.flickster_portrait)
                    .into(viewHolder.movieImage);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(movie.getPosterPath()).fit().centerCrop()
                    .placeholder(R.drawable.flickster_landscape)
                    .into(viewHolder.movieImage);
        }


        return convertView;

    }



    // View lookup cache
    private static class ViewHolder {
        TextView movieTitle;
        TextView movieDescription;
        ImageView movieImage;
    }



}
