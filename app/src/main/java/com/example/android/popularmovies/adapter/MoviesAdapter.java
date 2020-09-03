package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.config.TheMovieDbConfig;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyMovieViewHolder> {

    final private List<MovieModel.ResultsBean> resultsBeanList;
    final private Context context;
    final private RecyclerItemClickListener listener;

    public MoviesAdapter(List<MovieModel.ResultsBean> resultsBeanList, Context context, RecyclerItemClickListener listener) {
        this.resultsBeanList = resultsBeanList;
        this.context = context;
        this.listener = listener;
    }

    public class MyMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        final ImageView imageMoviePoster;
        final TextView textMovieTitle;

        public MyMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMoviePoster = itemView.findViewById(R.id.imageMoviePoster);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitleAdapter);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MyMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemList = layoutInflater.inflate(R.layout.movie_list_adapter, parent, false);

        return new MyMovieViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMovieViewHolder holder, int position) {
        MovieModel.ResultsBean resultBean = resultsBeanList.get(position);
        String poster_path = resultBean.getPoster_path();
        if (poster_path != null) {
            String url = TheMovieDbConfig.getImage(poster_path);
            Uri uri = Uri.parse(url);
            Glide.with(context).load(uri).error(R.drawable.default_image_thumbnail).into(holder.imageMoviePoster);
        } else {
            holder.imageMoviePoster.setImageResource(R.drawable.default_image_thumbnail);
        }

        holder.textMovieTitle.setText(resultBean.getTitle());

    }

    @Override
    public int getItemCount() {
        return resultsBeanList.size();
    }
}

