package ir.sublearn.application.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ir.sublearn.application.R;
import ir.sublearn.application.models.EpisodeModel;
import ir.sublearn.application.views.activities.MoviePlayerActivity;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MainHolder> {

    List<EpisodeModel> episodeModelList;

    public EpisodeAdapter(List<EpisodeModel> episodeModelList) {
        this.episodeModelList = episodeModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.item_movie_title.setText(episodeModelList.get(position).getMovieTitle());
        holder.item_cardview.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MoviePlayerActivity.class);
            intent.putExtra("movie_file_url", episodeModelList.get(position).getMovieFileUrl());
            intent.putExtra("movie_english_sub_url", episodeModelList.get(position).getMovieEnglishSubUrl());
            intent.putExtra("movie_persian_sub_url", episodeModelList.get(position).getMoviePersianSubUrl());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return episodeModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatImageView item_movie_play_icon;
        AppCompatTextView item_movie_title;
        MaterialCardView item_cardview;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            item_movie_play_icon = itemView.findViewById(R.id.item_episode_play_icon);
            item_movie_title = itemView.findViewById(R.id.item_episode_title);
            item_cardview = itemView.findViewById(R.id.item_episode_cardview);
        }
    }
}
