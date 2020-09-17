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
import com.example.android.popularmovies.api.TheMovieDbConfig;
import com.example.android.popularmovies.listener.RecyclerItemClickListener;
import com.example.android.popularmovies.model.FavoriteMovieModel;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyFavoritesViewHolder> {

    private final List<FavoriteMovieModel> favoriteMoviesList;
    private final Context context;
    private final RecyclerItemClickListener listener;

    public FavoritesAdapter(List<FavoriteMovieModel> favoriteMoviesList, Context context, RecyclerItemClickListener listener) {
        this.favoriteMoviesList = favoriteMoviesList;
        this.context = context;
        this.listener = listener;
    }

    public class MyFavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        final ImageView imageMoviePoster;
        final TextView textMovieTitle;

        public MyFavoritesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMoviePoster = itemView.findViewById(R.id.imageMoviePoster);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitleAdapter);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onClickItem(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MyFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemList = layoutInflater.inflate(R.layout.movie_list_adapter, parent, false);

        return new MyFavoritesViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoritesViewHolder holder, int position) {
        FavoriteMovieModel favorite = favoriteMoviesList.get(position);
        String poster_path = favorite.getPoster_path();
        if (poster_path != null) {
            String url = TheMovieDbConfig.getImage(poster_path);
            Uri uri = Uri.parse(url);
            Glide.with(context).load(uri).error(R.drawable.default_image_thumbnail).into(holder.imageMoviePoster);
        } else {
            holder.imageMoviePoster.setImageResource(R.drawable.default_image_thumbnail);
        }

        holder.textMovieTitle.setText(favorite.getTitle());

    }

    @Override
    public int getItemCount() {
        return favoriteMoviesList.size();
    }
}
