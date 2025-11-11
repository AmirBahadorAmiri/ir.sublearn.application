package ir.sublearn.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.R;
import ir.sublearn.adapters.TranslatedTextHistoryAdapter;
import ir.sublearn.models.TextModel;
import ir.sublearn.tools.mydb.MyDB;

public class TranslateHistoryActivity extends BaseActivity {

    AppCompatImageView activity_translate_history_back_btn;
    RecyclerView activity_translate_history_recyclerview;
    TranslatedTextHistoryAdapter textHistoryAdapter;
    List<TextModel> textModelList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_translate_history);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_translate_history_back_btn = findViewById(R.id.activity_translate_history_back_btn);
        activity_translate_history_recyclerview = findViewById(R.id.activity_translate_history_recyclerview);
    }

    private void setup() {

        activity_translate_history_back_btn.setOnClickListener(view -> finish());

        textHistoryAdapter = new TranslatedTextHistoryAdapter(textModelList);
        activity_translate_history_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_translate_history_recyclerview.setAdapter(textHistoryAdapter);

        MyDB.getInstance(this)
                .getTextDao()
                .readAllText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<TextModel> textModels) {
                        textModelList.clear();
                        textModelList.addAll(textModels);
                        textHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }


}
