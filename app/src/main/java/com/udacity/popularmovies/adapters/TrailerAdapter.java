package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.models.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private Context mContext;
    private TrailerAdapterClickHandler mClickListener;
    private List<Trailer> mTrailerList;

    public interface TrailerAdapterClickHandler{
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterClickHandler listener, Context context){
        this.mClickListener = listener;
        this.mContext = context;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        holder.mNameTv.setText(mTrailerList.get(position).getmName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerList) return 0;
        return mTrailerList.size();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTv;

        TrailerAdapterViewHolder(View view){
            super(view);
            mNameTv = view.findViewById(R.id.trailer_name_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.onClick(mTrailerList.get(position));
        }
    }

    public void setTrailersData(List<Trailer> trailers){
        this.mTrailerList = trailers;
        this.notifyDataSetChanged();
    }

}
