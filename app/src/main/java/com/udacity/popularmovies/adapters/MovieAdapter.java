package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.models.Movie;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private List<Movie> mMoviesList;
    private MovieAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, Context context){
        this.mClickHandler = clickHandler;
        this.mContext = context;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.mMovieNameTv.setText(mMoviesList.get(position).getmTitle());
        String moviePosterPath = mMoviesList.get(position).getmPosterImageUrl();
        URL moviePosterUrl = NetworkUtils.buildPosterUrl(mContext, moviePosterPath);
        Picasso.with(mContext).load(moviePosterUrl.toString()).into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesList) return 0;
        return mMoviesList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mMoviePosterImageView;
        TextView mMovieNameTv;

        MovieAdapterViewHolder(View view){
            super(view);
            mMoviePosterImageView = view.findViewById(R.id.movie_image_iv);
            mMovieNameTv = view.findViewById(R.id.movie_name_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = mMoviesList.get(adapterPosition);
            mClickHandler.onClick(selectedMovie);
        }
    }

    public void setMoviesData(List<Movie> moviesList){
        this.mMoviesList = moviesList;
        notifyDataSetChanged();
    }

}
