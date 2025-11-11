package ir.sublearn.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import ir.sublearn.R;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.MusicModel;
import ir.sublearn.tools.downloader.Downloader;
import ir.sublearn.tools.logger.Logger;
import ir.sublearn.views.activities.SongPlayerActivity;
import okhttp3.Response;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MainHolder> {

    List<MusicModel> musicModelList;
    Downloader downloader;

    public MusicAdapter(Context context, List<MusicModel> musicModelList) {
        this.musicModelList = musicModelList;
        if (downloader == null) downloader = new Downloader().inject(context);
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_songs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.artist_name_textview.setText(musicModelList.get(position).getSingerName());
        holder.song_name_textview.setText(musicModelList.get(position).getSongName());
        Glide.with(holder.itemView.getContext()).load(musicModelList.get(position).getSongPoster()).placeholder(R.drawable.image_placeholder).into(holder.item_song_imageview);
        holder.item_song_cardview.setOnClickListener(v -> goToSong(holder.itemView.getContext(), position));
        holder.itemView.setOnClickListener(v -> goToSong(holder.itemView.getContext(), position));
    }

    private void goToSong(Context context, int position) {

        String[] str;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            str = new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.READ_MEDIA_AUDIO};
        } else {
            str = new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        Dexter.withContext(context)
                .withPermissions(str)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            downloader.downloadMusic(musicModelList.get(position).getSongUrl(), musicModelList.get(position).getSavedMP3FileName(), new ResponseListener() {
                                @Override
                                public void onSuccess(Response response) {
                                    Intent intent = new Intent(context, SongPlayerActivity.class);
                                    intent.putExtra("sub_link", musicModelList.get(position).getSubLink());
                                    intent.putExtra("mp3_file_name", musicModelList.get(position).getSavedMP3FileName());
                                    intent.putExtra("song_name", musicModelList.get(position).getShowSongName());
                                    intent.putExtra("song_poster", musicModelList.get(position).getSongPoster());
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Logger.logd(throwable.getMessage());
                                }
                            });
                        } else {
                            Logger.logd("Permission Denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public int getItemCount() {
        return musicModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView song_name_textview, artist_name_textview;
        AppCompatImageView item_song_imageview;
        MaterialCardView item_song_cardview;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            item_song_imageview = itemView.findViewById(R.id.item_song_imageview);
            song_name_textview = itemView.findViewById(R.id.song_name_textview);
            artist_name_textview = itemView.findViewById(R.id.artist_name_textview);
            item_song_cardview = itemView.findViewById(R.id.item_song_cardview);
        }
    }
}
