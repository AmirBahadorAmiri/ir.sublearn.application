package ir.sublearn.application.views.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.sublearn.application.R;
import ir.sublearn.application.adapters.SongAdapter;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.SongModel;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import ir.sublearn.application.tools.network_manager.NetworktManager;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class SongsExplorerActivity extends BaseActivity {

    RecyclerView activity_songs_explorer_recycler_view;
    AppCompatImageView activity_songs_explorer_back_btn;
    List<SongModel> songModelList = new ArrayList<>();
    SongAdapter songAdapter;
    IOkHttp iOkHttp = new IOkHttp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_explorer);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_songs_explorer_recycler_view = findViewById(R.id.activity_songs_explorer_recycler_view);
        activity_songs_explorer_back_btn = findViewById(R.id.activity_songs_explorer_back_btn);
    }

    private void setup() {

        activity_songs_explorer_back_btn.setOnClickListener(v -> finish());

        songAdapter = new SongAdapter(this, songModelList);
        activity_songs_explorer_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        activity_songs_explorer_recycler_view.setAdapter(songAdapter);

        if (NetworktManager.isVPNConnected(this)) {
            NetworktManager.showVpnSnackbar(getWindow().getDecorView());
        } else {
            HttpUrl httpUrl = HttpUrl.parse(IOkHttp.getGet_all_songs())
                    .newBuilder()
                    .build();
            iOkHttp.get(httpUrl, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    if (response.isSuccessful()) {
                        try {
                            String body = response.body().string();
                            JSONObject object = new JSONObject(body);
                            int result_code = object.getInt("result_code");
                            switch (result_code) {
                                case 0:
                                    runOnUiThread(() -> Toast.makeText(SongsExplorerActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                    break;
                                case 1:
                                    JSONArray songs = object.getJSONArray("songs");
                                    for (int i = 0; i < songs.length(); i++) {
                                        JSONObject song = songs.getJSONObject(i);
                                        String song_id = song.getString("song_id");
                                        String singer_id = song.getString("singer_id");
                                        String singer_name = song.getString("singer_name");
                                        String song_name = song.getString("song_name");
                                        String song_url = song.getString("song_url");
                                        String song_poster = song.getString("song_poster");
                                        String singer_logo = song.getString("singer_logo");
                                        String song_like = song.getString("song_like");
                                        String sub_link = song.getString("sub_link");
                                        String song_space = song.getString("song_space");
                                        String song_length = song.getString("song_length");
                                        String is_enabled = song.getString("is_enabled");
                                        String is_pinned = song.getString("is_pinned");
                                        songModelList.add(new SongModel(song_id, singer_id, singer_name, song_name, song_url, song_poster, singer_logo, song_like, sub_link, song_space, song_length, is_enabled, is_pinned));
                                    }
                                    runOnUiThread(() -> songAdapter.notifyDataSetChanged());
                                    break;
                            }
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }

    }
}
