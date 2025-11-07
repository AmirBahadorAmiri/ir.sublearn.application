package ir.sublearn.application.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.sublearn.application.R;
import ir.sublearn.application.adapters.EpisodeAdapter;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.EpisodeModel;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SeriesActivity extends BaseActivity {

    String series_id = "", series_name = "", series_poster = "";
    AppCompatImageView activity_series_back_btn, activity_series_background_img;
    AppCompatSpinner activity_series_spinner_view;
    List<String> season_id_list = new ArrayList<>();
    List<String> season_name_list = new ArrayList<>();
    IOkHttp iOkHttp = new IOkHttp();
    List<EpisodeModel> episodeModels = new ArrayList<>();
    RecyclerView activity_cinematv_movies_recyclerview;
    EpisodeAdapter episodeAdapter;
    AppCompatTextView activity_series_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_series_back_btn = findViewById(R.id.activity_series_back_btn);
        activity_series_background_img = findViewById(R.id.activity_series_background_img);
        activity_series_spinner_view = findViewById(R.id.activity_series_spinner_view);
        activity_cinematv_movies_recyclerview = findViewById(R.id.activity_cinematv_movies_recyclerview);
        activity_series_name = findViewById(R.id.activity_series_name);
    }

    private void setup() {
        if (getIntent() != null) {
            series_id = getIntent().getStringExtra("series_id");
            series_name = getIntent().getStringExtra("series_name");
            series_poster = getIntent().getStringExtra("series_poster");
        }

        activity_series_name.setText(series_name);

        activity_series_back_btn.setOnClickListener(v -> finish());

        Glide.with(this).load(series_poster).placeholder(R.drawable.image_placeholder).into(activity_series_background_img);

        episodeAdapter = new EpisodeAdapter(episodeModels);
        activity_cinematv_movies_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_cinematv_movies_recyclerview.setAdapter(episodeAdapter);

        RequestBody requestBody = new FormBody.Builder()
                .add("series_id", series_id)
                .build();
        iOkHttp.post(IOkHttp.getGet_seasons(), requestBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject object = new JSONObject(responseBody);
                        int result_code = object.getInt("result_code");
                        switch (result_code) {
                            case 0:
                                Toast.makeText(SeriesActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(SeriesActivity.this, getString(R.string.series_bot_found), Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                JSONArray movies = object.getJSONArray("movies");
                                for (int i = 0; i < movies.length(); i++) {
                                    JSONObject movie = movies.getJSONObject(i);
                                    String season_id = movie.getString("season_id");
                                    String season_name = movie.getString("season_name");
                                    season_id_list.add(season_id);
                                    season_name_list.add(season_name);
                                }

                                runOnUiThread(() -> {
                                    SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(SeriesActivity.this, android.R.layout.simple_spinner_item, season_name_list);
                                    activity_series_spinner_view.setAdapter(spinnerAdapter);
                                    activity_series_spinner_view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            RequestBody reqbody = new FormBody.Builder()
                                                    .add("season_id", season_id_list.get(position))
                                                    .build();
                                            iOkHttp.post(IOkHttp.getGet_episode(), reqbody, new ResponseListener() {
                                                @Override
                                                public void onSuccess(Response response) {
                                                    if (response.isSuccessful()) {
                                                        try {
                                                            episodeModels.clear();
                                                            String responseBody2 = response.body().string();
                                                            JSONObject object2 = new JSONObject(responseBody2);
                                                            int result_code2 = object2.getInt("result_code");
                                                            switch (result_code2) {
                                                                case 0:
                                                                    Toast.makeText(SeriesActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
                                                                    break;
                                                                case 1:
                                                                    Toast.makeText(SeriesActivity.this, getString(R.string.series_bot_found), Toast.LENGTH_SHORT).show();
                                                                    break;
                                                                case 2:
                                                                    JSONArray movies = object2.getJSONArray("movies");
                                                                    for (int i = 0; i < movies.length(); i++) {
                                                                        JSONObject movie = movies.getJSONObject(i);
                                                                        String episode_id = movie.getString("episode_id");
                                                                        String season_id = movie.getString("season_id");
                                                                        String movie_title = movie.getString("movie_title");
                                                                        String movie_file_url = movie.getString("movie_file_url");
                                                                        String movie_persian_sub_url = movie.getString("movie_persian_sub_url");
                                                                        String movie_english_sub_url = movie.getString("movie_english_sub_url");
                                                                        String is_enabled = movie.getString("is_enabled");
                                                                        episodeModels.add(new EpisodeModel(episode_id, season_id, movie_title, movie_file_url, movie_persian_sub_url, movie_english_sub_url, is_enabled));
                                                                    }
                                                                    runOnUiThread(() -> episodeAdapter.notifyDataSetChanged());
                                                                    break;
                                                            }
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    } else {

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Throwable throwable) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                });
                                break;
                        }
                    } catch (Exception e) {
                        Log.d("TAG", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG", "onFail: " + throwable.getMessage());
            }
        });


    }
}
