package com.example.android.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.ReviewsModel;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyReviewViewHolder> {

    final private List<ReviewsModel.ResultsBean> mListOfReviews;
    final private RecyclerItemClickListener listener;

    public ReviewsAdapter(List<ReviewsModel.ResultsBean> mListOfReviews, RecyclerItemClickListener listener) {
        this.mListOfReviews = mListOfReviews;
        this.listener = listener;
    }

    public class MyReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mTextUserName;
        final TextView mTextReview;

        public MyReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextUserName = itemView.findViewById(R.id.tv_author_name);
            mTextReview = itemView.findViewById(R.id.tv_review);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClickItem(position);
        }
    }

    @NonNull
    @Override
    public MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_adapter, parent, false);

       return new MyReviewViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewViewHolder holder, int position) {
       ReviewsModel.ResultsBean review = mListOfReviews.get(position);
       String name = review.getAuthor();
       String content = review.getContent();
       holder.mTextUserName.setText(name);
       holder.mTextReview.setText(content);
    }

    @Override
    public int getItemCount() {
        return mListOfReviews.size();
    }

}
