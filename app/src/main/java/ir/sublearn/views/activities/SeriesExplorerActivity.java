package ir.sublearn.views.activities;

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

import ir.sublearn.R;
import ir.sublearn.adapters.SeriesAdapter;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.SeriesModel;
import ir.sublearn.tools.iokhttp.IOkHttp;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class SeriesExplorerActivity extends BaseActivity {

    AppCompatImageView activity_series_explorer_back_btn;
    RecyclerView activity_series_explorer_recycler_view;
    List<SeriesModel> seriesModelList = new ArrayList<>();
    SeriesAdapter seriesAdapter;
    IOkHttp iOkHttp = new IOkHttp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_series_explorer);
        setViewCompat();
        findVies();
        setup();
    }

    private void findVies() {
        activity_series_explorer_back_btn = findViewById(R.id.activity_series_explorer_back_btn);
        activity_series_explorer_recycler_view = findViewById(R.id.activity_series_explorer_recycler_view);
    }

    private void setup() {
        activity_series_explorer_back_btn.setOnClickListener(v -> finish());

        seriesAdapter = new SeriesAdapter(seriesModelList);
        activity_series_explorer_recycler_view.setLayoutManager(new GridLayoutManager(this,3, RecyclerView.VERTICAL,false));
        activity_series_explorer_recycler_view.setAdapter(seriesAdapter);

        HttpUrl seriesUrl = HttpUrl.parse(IOkHttp.getGet_series());
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
                                Toast.makeText(SeriesExplorerActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show();
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
                                runOnUiThread(() -> seriesAdapter.notifyDataSetChanged());
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
