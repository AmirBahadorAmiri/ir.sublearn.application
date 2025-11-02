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
import ir.sublearn.application.models.SeriesModel;
import ir.sublearn.application.views.activities.SeriesActivity;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MainHolder> {

    List<SeriesModel> seriesModelList;

    public SeriesAdapter(List<SeriesModel> seriesModelList) {
        this.seriesModelList = seriesModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.series_name_textview.setText(seriesModelList.get(position).getSeriesName());
        Glide.with(holder.itemView.getContext()).load(seriesModelList.get(position).getSeriesPoster()).placeholder(R.drawable.image_placeholder).into(holder.series_recyclerview_poster);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), SeriesActivity.class);
            intent.putExtra("series_id", seriesModelList.get(position).getSeriesId());
            intent.putExtra("series_name", seriesModelList.get(position).getSeriesName());
            intent.putExtra("series_poster", seriesModelList.get(position).getSeriesPoster());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return seriesModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatImageView series_recyclerview_poster;
        AppCompatTextView series_name_textview;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            series_recyclerview_poster = itemView.findViewById(R.id.series_recyclerview_poster);
            series_name_textview = itemView.findViewById(R.id.series_name_textview);
        }
    }

}
