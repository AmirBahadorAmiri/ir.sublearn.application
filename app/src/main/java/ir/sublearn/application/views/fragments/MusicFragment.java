package ir.sublearn.application.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
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
import ir.sublearn.application.views.activities.ProfileActivity;
import ir.sublearn.application.views.activities.SettingsActivity;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class MusicFragment extends Fragment {

    public static MusicFragment musicFragment;

    RecyclerView fragment_music_kids_recycler_view, fragment_music_mobtadi_recycler_view, fragment_music_motevaset_recycler_view, fragment_music_pishrafte_recycler_view;
    AppCompatImageView fragment_music_settings_icon, fragment_music_profile_icon;
    List<SongModel> musicsA = new ArrayList<>();
    List<SongModel> musicsB = new ArrayList<>();
    List<SongModel> musicsC = new ArrayList<>();
    List<SongModel> musicsK = new ArrayList<>();
    SongAdapter adapterA;
    SongAdapter adapterB;
    SongAdapter adapterC;
    SongAdapter adapterK;
    IOkHttp iOkHttp = new IOkHttp();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_music_explorer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_music_settings_icon = view.findViewById(R.id.fragment_music_settings_icon);
        fragment_music_profile_icon = view.findViewById(R.id.fragment_music_profile_icon);
        fragment_music_kids_recycler_view = view.findViewById(R.id.fragment_music_kids_recycler_view);
        fragment_music_mobtadi_recycler_view = view.findViewById(R.id.fragment_music_mobtadi_recycler_view);
        fragment_music_motevaset_recycler_view = view.findViewById(R.id.fragment_music_motevaset_recycler_view);
        fragment_music_pishrafte_recycler_view = view.findViewById(R.id.fragment_music_pishrafte_recycler_view);
    }

    private void setup(View view) {

        fragment_music_settings_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        fragment_music_profile_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        });

        adapterA = new SongAdapter(requireContext(), musicsA);
        fragment_music_mobtadi_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_mobtadi_recycler_view.setAdapter(adapterA);

        adapterB = new SongAdapter(requireContext(), musicsB);
        fragment_music_motevaset_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_motevaset_recycler_view.setAdapter(adapterB);

        adapterC = new SongAdapter(requireContext(), musicsC);
        fragment_music_pishrafte_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_pishrafte_recycler_view.setAdapter(adapterC);

        adapterK = new SongAdapter(requireContext(), musicsK);
        fragment_music_kids_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_kids_recycler_view.setAdapter(adapterK);

        if (musicsA.isEmpty() || musicsB.isEmpty() || musicsC.isEmpty() || musicsK.isEmpty()) {
            musicsA.clear();
            musicsB.clear();
            musicsC.clear();
            musicsK.clear();
            if (NetworktManager.isVPNConnected(requireContext())) {
                NetworktManager.showVpnSnackbar(view);
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
                                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                        break;
                                    case 1:
                                        JSONArray songs = object.getJSONArray("songs");
                                        for (int i = 0; i < songs.length(); i++) {
                                            JSONObject song = songs.getJSONObject(i);
                                            String song_id = song.getString("song_id");
                                            String song_class = song.getString("song_class");
                                            String singer_name = song.getString("singer_name");
                                            String song_name = song.getString("song_name");
                                            String song_url = song.getString("song_url");
                                            String song_poster = song.getString("song_poster");
                                            String sub_link = song.getString("sub_link");
                                            String is_enabled = song.getString("is_enabled");
                                            String is_pinned = song.getString("is_pinned");
                                            if (song_class.equals("A")) {
                                                musicsA.add(new SongModel(song_id, song_class, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                            } else if (song_class.equals("B")) {
                                                musicsB.add(new SongModel(song_id, song_class, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                            } else if (song_class.equals("C")) {
                                                musicsC.add(new SongModel(song_id, song_class, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                            } else if (song_class.equals("K")) {
                                                musicsK.add(new SongModel(song_id, song_class, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                            }
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            adapterA.notifyDataSetChanged();
                                            adapterB.notifyDataSetChanged();
                                            adapterC.notifyDataSetChanged();
                                            adapterK.notifyDataSetChanged();
                                        });
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

    public static MusicFragment getMusicFragment() {
        if (musicFragment == null) {
            musicFragment = new MusicFragment();
            musicFragment.setArguments(new Bundle());
        }
        return musicFragment;
    }
}
