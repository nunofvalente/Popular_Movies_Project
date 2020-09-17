package com.example.android.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.TrailerModel;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTrailerViewHolder> {

    final List<TrailerModel.ResultsBean> mListOfTrailers;
    final private RecyclerItemClickListener listener;

    public TrailerAdapter(List<TrailerModel.ResultsBean> mListOfTrailers, RecyclerItemClickListener listener) {
        this.mListOfTrailers = mListOfTrailers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_adapter, parent, false);

        return new MyTrailerViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrailerViewHolder holder, int position) {
        int trailerNumber = position + 1;
        holder.mTextTrailer.setText("Trailer " + trailerNumber);
    }

    @Override
    public int getItemCount() {
        return mListOfTrailers.size();
    }

    public class MyTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mTextTrailer;

        public MyTrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextTrailer = itemView.findViewById(R.id.tv_trailer_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onClickItem(adapterPosition);
        }
    }
}
