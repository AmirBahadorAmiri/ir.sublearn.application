package ir.sublearn.application.views.activities;

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
import ir.sublearn.application.R;
import ir.sublearn.application.adapters.DictionaryHistoryAdapter;
import ir.sublearn.application.models.WordModel;
import ir.sublearn.application.tools.mydb.MyDB;

public class DictionaryHistoryActivity extends BaseActivity {

    RecyclerView activity_dictionary_history_recyclerview;
    AppCompatImageView activity_dictionary_history_back_btn;
    DictionaryHistoryAdapter historyAdapter;
    List<WordModel> wordModelList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_history);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_dictionary_history_back_btn = findViewById(R.id.activity_dictionary_history_back_btn);
        activity_dictionary_history_recyclerview = findViewById(R.id.activity_dictionary_history_recyclerview);
    }

    private void setup() {
        activity_dictionary_history_back_btn.setOnClickListener(view -> finish());

        historyAdapter = new DictionaryHistoryAdapter(wordModelList);
        activity_dictionary_history_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_dictionary_history_recyclerview.setAdapter(historyAdapter);

        MyDB.getInstance(this)
                .getWordDao()
                .readHistories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull List<WordModel> wordModels) {
                        wordModelList.clear();
                        wordModelList.addAll(wordModels);
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });

    }

}
