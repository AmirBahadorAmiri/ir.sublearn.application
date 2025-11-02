package ir.sublearn.application.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.sublearn.application.R;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.SongModel;
import ir.sublearn.application.tools.downloader.Downloader;
import ir.sublearn.application.views.activities.SongPlayerActivity;
import okhttp3.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MainHolder> {

    List<SongModel> songModelList;
    Downloader downloader;

    public SongAdapter(Context context, List<SongModel> songModelList) {
        this.songModelList = songModelList;
        if (downloader == null) downloader = new Downloader().inject(context);
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_songs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.artist_name_textview.setText(songModelList.get(position).getSingerName());
        holder.song_name_textview.setText(songModelList.get(position).getSongName());
        Glide.with(holder.itemView.getContext()).load(songModelList.get(position).getSongPoster()).placeholder(R.drawable.image_placeholder).into(holder.small_circle_image_view);
        holder.itemView.setOnClickListener(v -> {

            Dexter.withContext(holder.itemView.getContext())
                    .withPermissions(
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_MEDIA_AUDIO
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                downloader.downloadMusic(songModelList.get(position).getSongUrl(), songModelList.get(position).getSavedMP3FileName(), new ResponseListener() {
                                    @Override
                                    public void onSuccess(Response response) {
                                        Intent intent = new Intent(holder.itemView.getContext(), SongPlayerActivity.class);
                                        intent.putExtra("sub_link", songModelList.get(position).getSubLink());
                                        intent.putExtra("mp3_file_name", songModelList.get(position).getSavedMP3FileName());
                                        intent.putExtra("song_name", songModelList.get(position).getShowSongName());
                                        intent.putExtra("song_poster", songModelList.get(position).getSongPoster());
                                        holder.itemView.getContext().startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }
                                });
                            } else {
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        });
    }

    @Override
    public int getItemCount() {
        return songModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView song_name_textview, artist_name_textview;
        CircleImageView small_circle_image_view;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            small_circle_image_view = itemView.findViewById(R.id.small_circle_image_view);
            song_name_textview = itemView.findViewById(R.id.song_name_textview);
            artist_name_textview = itemView.findViewById(R.id.artist_name_textview);
        }
    }
}
