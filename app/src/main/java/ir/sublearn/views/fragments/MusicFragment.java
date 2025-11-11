package ir.sublearn.views.fragments;

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
import java.util.Objects;

import ir.sublearn.R;
import ir.sublearn.adapters.MusicAdapter;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.MusicModel;
import ir.sublearn.tools.iokhttp.IOkHttp;
import ir.sublearn.tools.network_manager.NetworktManager;
import ir.sublearn.views.activities.ProfileActivity;
import ir.sublearn.views.activities.SettingsActivity;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class MusicFragment extends Fragment {

    public static MusicFragment musicFragment;

    RecyclerView fragment_music_kids_recycler_view, fragment_music_mobtadi_recycler_view, fragment_music_motevaset_recycler_view, fragment_music_pishrafte_recycler_view;
    AppCompatImageView fragment_music_settings_icon, fragment_music_profile_icon;
    List<MusicModel> musics_mobtadi = new ArrayList<>();
    List<MusicModel> musics_motevaset = new ArrayList<>();
    List<MusicModel> musics_pishrafte = new ArrayList<>();
    List<MusicModel> musics_kids = new ArrayList<>();
    MusicAdapter adapter_mobtadi, adapter_motevaset, adapter_pishrafte, adapter_kids;
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

        adapter_mobtadi = new MusicAdapter(requireContext(), musics_mobtadi);
        fragment_music_mobtadi_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_mobtadi_recycler_view.setAdapter(adapter_mobtadi);

        adapter_motevaset = new MusicAdapter(requireContext(), musics_motevaset);
        fragment_music_motevaset_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_motevaset_recycler_view.setAdapter(adapter_motevaset);

        adapter_pishrafte = new MusicAdapter(requireContext(), musics_pishrafte);
        fragment_music_pishrafte_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_pishrafte_recycler_view.setAdapter(adapter_pishrafte);

        adapter_kids = new MusicAdapter(requireContext(), musics_kids);
        fragment_music_kids_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        fragment_music_kids_recycler_view.setAdapter(adapter_kids);

        if (musics_kids.isEmpty()) {

            if (NetworktManager.isVPNConnected(requireContext())) {
                NetworktManager.showVpnSnackbar(view);
            } else {
                HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(IOkHttp.getGet_musics_top_kids()))
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
                                        JSONArray songs = object.getJSONArray("musics_kids");
                                        for (int i = 0; i < songs.length(); i++) {
                                            JSONObject song = songs.getJSONObject(i);
                                            String song_id = song.getString("song_id");
                                            String singer_name = song.getString("singer_name");
                                            String song_name = song.getString("song_name");
                                            String song_url = song.getString("song_url");
                                            String song_poster = song.getString("song_poster");
                                            String sub_link = song.getString("sub_link");
                                            String is_enabled = song.getString("is_enabled");
                                            String is_pinned = song.getString("is_pinned");
                                            musics_kids.add(new MusicModel(song_id, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            adapter_kids.notifyDataSetChanged();
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

        if (musics_mobtadi.isEmpty()) {

            if (NetworktManager.isVPNConnected(requireContext())) {
                NetworktManager.showVpnSnackbar(view);
            } else {
                HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(IOkHttp.getGet_musics_top_mobtadi()))
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
                                        JSONArray songs = object.getJSONArray("musics_mobtadi");
                                        for (int i = 0; i < songs.length(); i++) {
                                            JSONObject song = songs.getJSONObject(i);
                                            String song_id = song.getString("song_id");
                                            String singer_name = song.getString("singer_name");
                                            String song_name = song.getString("song_name");
                                            String song_url = song.getString("song_url");
                                            String song_poster = song.getString("song_poster");
                                            String sub_link = song.getString("sub_link");
                                            String is_enabled = song.getString("is_enabled");
                                            String is_pinned = song.getString("is_pinned");
                                            musics_mobtadi.add(new MusicModel(song_id, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            adapter_mobtadi.notifyDataSetChanged();
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

        if (musics_motevaset.isEmpty()) {

            if (NetworktManager.isVPNConnected(requireContext())) {
                NetworktManager.showVpnSnackbar(view);
            } else {
                HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(IOkHttp.getGet_musics_top_motevaset()))
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
                                        JSONArray songs = object.getJSONArray("musics_motevaset");
                                        for (int i = 0; i < songs.length(); i++) {
                                            JSONObject song = songs.getJSONObject(i);
                                            String song_id = song.getString("song_id");
                                            String singer_name = song.getString("singer_name");
                                            String song_name = song.getString("song_name");
                                            String song_url = song.getString("song_url");
                                            String song_poster = song.getString("song_poster");
                                            String sub_link = song.getString("sub_link");
                                            String is_enabled = song.getString("is_enabled");
                                            String is_pinned = song.getString("is_pinned");
                                            musics_motevaset.add(new MusicModel(song_id, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            adapter_motevaset.notifyDataSetChanged();
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

        if (musics_pishrafte.isEmpty()) {

            if (NetworktManager.isVPNConnected(requireContext())) {
                NetworktManager.showVpnSnackbar(view);
            } else {
                HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(IOkHttp.getGet_musics_top_pishrafte()))
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
                                        JSONArray songs = object.getJSONArray("musics_pishrafte");
                                        for (int i = 0; i < songs.length(); i++) {
                                            JSONObject song = songs.getJSONObject(i);
                                            String song_id = song.getString("song_id");
                                            String singer_name = song.getString("singer_name");
                                            String song_name = song.getString("song_name");
                                            String song_url = song.getString("song_url");
                                            String song_poster = song.getString("song_poster");
                                            String sub_link = song.getString("sub_link");
                                            String is_enabled = song.getString("is_enabled");
                                            String is_pinned = song.getString("is_pinned");
                                            musics_pishrafte.add(new MusicModel(song_id, singer_name, song_name, song_url, song_poster, sub_link, is_enabled, is_pinned));
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            adapter_pishrafte.notifyDataSetChanged();
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
