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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.sublearn.R;
import ir.sublearn.adapters.MoviesAdapter;
import ir.sublearn.adapters.SeriesAdapter;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.MoviesModel;
import ir.sublearn.models.SeriesModel;
import ir.sublearn.tools.iokhttp.IOkHttp;
import ir.sublearn.views.activities.MoviesExplorerActivity;
import ir.sublearn.views.activities.ProfileActivity;
import ir.sublearn.views.activities.SeriesExplorerActivity;
import ir.sublearn.views.activities.SettingsActivity;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class CinemaFragment extends Fragment {

    public static CinemaFragment cinemaFragment;
    AppCompatImageView fragment_cinema_settings_icon, fragment_cinema_profile_icon;
    AppCompatTextView fragment_cinema_cinema_more, fragment_cinema_series_more;
    RecyclerView fragment_cinema_series_recyclerView, fragment_cinema_cinema_recyclerView;
    List<SeriesModel> seriesModelList = new ArrayList<>();
    List<MoviesModel> moviesModels = new ArrayList<>();
    SeriesAdapter seriesAdapter;
    MoviesAdapter moviesAdapter;
    IOkHttp iOkHttp = new IOkHttp();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_cinema, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_cinema_settings_icon = view.findViewById(R.id.fragment_cinema_settings_icon);
        fragment_cinema_profile_icon = view.findViewById(R.id.fragment_cinema_profile_icon);
        fragment_cinema_series_recyclerView = view.findViewById(R.id.fragment_cinema_series_recyclerView);
        fragment_cinema_cinema_recyclerView = view.findViewById(R.id.fragment_cinema_cinema_recyclerView);
        fragment_cinema_cinema_more = view.findViewById(R.id.fragment_cinema_cinema_more);
        fragment_cinema_series_more = view.findViewById(R.id.fragment_cinema_series_more);
    }

    private void setup(View view) {

        fragment_cinema_settings_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        fragment_cinema_profile_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        });

        fragment_cinema_series_more.setOnClickListener(v -> startActivity(new Intent(requireContext(), SeriesExplorerActivity.class)));
        fragment_cinema_cinema_more.setOnClickListener(v -> startActivity(new Intent(requireContext(), MoviesExplorerActivity.class)));

        moviesAdapter = new MoviesAdapter(moviesModels);
        fragment_cinema_cinema_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        fragment_cinema_cinema_recyclerView.setAdapter(moviesAdapter);

        seriesAdapter = new SeriesAdapter(seriesModelList);
        fragment_cinema_series_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        fragment_cinema_series_recyclerView.setAdapter(seriesAdapter);

        if (moviesModels.isEmpty())
            loadMovies();
        if (seriesModelList.isEmpty())
            loadSeries();

    }

    private void loadSeries() {

        HttpUrl seriesUrl = HttpUrl.parse(IOkHttp.getGet_top_series());
        iOkHttp.get(seriesUrl, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject object = new JSONObject(responseString);
                        int result_code = object.getInt("result_code");
                        switch (result_code) {
                            case 0:
                                Toast.makeText(requireContext(), getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                JSONArray jsonArray = object.getJSONArray("series");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String series_id = jsonObject.getString("series_id");
                                    String series_name = jsonObject.getString("series_name");
                                    String series_poster = jsonObject.getString("series_poster");
                                    String is_enabled = jsonObject.getString("is_enabled");
                                    seriesModelList.add(new SeriesModel(series_id, series_name, series_poster, is_enabled));
                                }
                                requireActivity().runOnUiThread(() -> seriesAdapter.notifyDataSetChanged());
                                break;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    private void loadMovies() {

        HttpUrl moviesUrl = HttpUrl.parse(IOkHttp.getGet_top_movies());
        iOkHttp.get(moviesUrl, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject object = new JSONObject(responseString);
                        int result_code = object.getInt("result_code");
                        switch (result_code) {
                            case 0:
                                Toast.makeText(requireContext(), getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                JSONArray jsonArray = object.getJSONArray("movies");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String movie_id = jsonObject.getString("movie_id");
                                    String movie_name = jsonObject.getString("movie_name");
                                    String movie_poster = jsonObject.getString("movie_poster");
                                    String movie_file_url = jsonObject.getString("movie_file_url");
                                    String movie_persian_sub_url = jsonObject.getString("movie_persian_sub_url");
                                    String movie_english_sub_url = jsonObject.getString("movie_english_sub_url");
                                    String is_enabled = jsonObject.getString("is_enabled");
                                    moviesModels.add(new MoviesModel(movie_id, movie_name, movie_poster, movie_file_url, movie_persian_sub_url, movie_english_sub_url, is_enabled));
                                }
                                requireActivity().runOnUiThread(() -> moviesAdapter.notifyDataSetChanged());
                                break;
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

//    private void setupStoryView() {
//        fragment_home_storyview.initWithActivity(requireActivity());
//        ArrayList<MaterialStory> materialStories = new ArrayList<>();
//        Date date = new Date(Long.parseLong("1756728848000"));
//        materialStories.add(new MaterialStory(date, "https://amirbahadoramiri.ir/defineit/series/Wednesday/logo.jpg", null, null, "Wednesday", "سریال ونزدی ساخت 25-2022"));
//        materialStories.add(new MaterialStory(date, "https://amirbahadoramiri.ir/defineit/series/Big Bang Theory/logo.jpg", null, null, "Big Bang Theory", "سریال Big Bang Theory 1996"));
//        materialStories.add(new MaterialStory(date, "https://amirbahadoramiri.ir/defineit/series/Friends/logo.jpg", null, null, "Friends", "سریال فرندز ساخت 1994"));
//        MaterialStoryViewHeaderInfo materialStoryViewHeaderInfo = new MaterialStoryViewHeaderInfo(
//                "سریال های جدید",
//                "https://amirbahadoramiri.ir/defineit/series/Wednesday/logo.jpg",
//                materialStories
//        );
//        fragment_home_storyview.addStory(materialStoryViewHeaderInfo);
//    }

    public static CinemaFragment getCinemaFragment() {
        if (cinemaFragment == null) {
            cinemaFragment = new CinemaFragment();
            cinemaFragment.setArguments(new Bundle());
        }
        return cinemaFragment;
    }
}
