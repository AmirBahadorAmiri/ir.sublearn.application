package ir.sublearn.application.views.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.sublearn.application.R;
import ir.sublearn.application.adapters.MoviesAdapter;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.MoviesModel;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class MoviesExplorerActivity extends BaseActivity {

    MoviesAdapter moviesAdapter;
    List<MoviesModel> moviesModelList = new ArrayList<>();
    RecyclerView activity_movies_explorer_recycler_view;
    AppCompatImageView activity_movies_explorer_back_btn;
    IOkHttp iOkHttp = new IOkHttp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_explorer);
        setViewCompat();
        findVies();
        setup();
    }

    private void findVies() {
        activity_movies_explorer_recycler_view = findViewById(R.id.activity_movies_explorer_recycler_view);
        activity_movies_explorer_back_btn = findViewById(R.id.activity_movies_explorer_back_btn);
    }

    private void setup() {
        activity_movies_explorer_back_btn.setOnClickListener(v -> finish());

        moviesAdapter = new MoviesAdapter(moviesModelList);
        activity_movies_explorer_recycler_view.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        activity_movies_explorer_recycler_view.setAdapter(moviesAdapter);

        HttpUrl moviesUrl = HttpUrl.parse(IOkHttp.getGet_all_movies());
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
                                Toast.makeText(MoviesExplorerActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
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
                                    moviesModelList.add(new MoviesModel(movie_id, movie_name, movie_poster, movie_file_url, movie_persian_sub_url, movie_english_sub_url, is_enabled));
                                }
                                runOnUiThread(() -> moviesAdapter.notifyDataSetChanged());
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

}
