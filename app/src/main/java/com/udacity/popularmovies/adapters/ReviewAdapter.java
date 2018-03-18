package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private List<Review> mReviewsList;
    private ReviewAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface ReviewAdapterOnClickHandler{
        void onReviewClick(Review review);
    }

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler, Context context){
        this.mClickHandler = clickHandler;
        this.mContext = context;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        Review review = mReviewsList.get(position);
        String author = review.getmAuthor();
        String content = review.getmContent();
        holder.mUserNameTv.setText(author);
        holder.mReviewContentTv.setText(content);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewsList) return 0;
        return mReviewsList.size();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mUserNameTv;
        private TextView mReviewContentTv;

        ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mUserNameTv = itemView.findViewById(R.id.user_review_tv);
            mReviewContentTv = itemView.findViewById(R.id.review_content_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.onReviewClick(mReviewsList.get(position));
        }
    }

    public void setReviewsData(List<Review> reviews){
        this.mReviewsList = reviews;
        this.notifyDataSetChanged();
    }
}
