package ir.sublearn.application.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.sublearn.application.R;
import ir.sublearn.application.models.MoviesModel;
import ir.sublearn.application.views.activities.MoviePlayerActivity;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MainHolder> {

    List<MoviesModel> moviesModels;

    public MoviesAdapter(List<MoviesModel> moviesModels) {
        this.moviesModels = moviesModels;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.movies_name_textview.setText(moviesModels.get(position).getMovieName());
        Glide.with(holder.itemView.getContext()).load(moviesModels.get(position).getMoviePoster()).placeholder(R.drawable.image_placeholder).into(holder.movie_recyclerview_poster);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MoviePlayerActivity.class);
            intent.putExtra("movie_file_url", moviesModels.get(position).getMovieFileUrl());
            intent.putExtra("movie_english_sub_url", moviesModels.get(position).getMovieEnglishSubUrl());
            intent.putExtra("movie_persian_sub_url", moviesModels.get(position).getMoviePersianSubUrl());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return moviesModels.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatImageView movie_recyclerview_poster;
        AppCompatTextView movies_name_textview;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            movie_recyclerview_poster = itemView.findViewById(R.id.movie_recyclerview_poster);
            movies_name_textview = itemView.findViewById(R.id.movies_name_textview);
        }
    }

}
