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
import ir.sublearn.adapters.StarredWordAdapter;
import ir.sublearn.models.WordModel;
import ir.sublearn.tools.mydb.MyDB;

public class StarWordsActivity extends BaseActivity {

    AppCompatImageView activity_star_words_back_btn;
    RecyclerView activity_star_words_recyclerview;
    List<WordModel> wordModelList = new ArrayList<>();
    StarredWordAdapter wordAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_star_words);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_star_words_back_btn = findViewById(R.id.activity_star_words_back_btn);
        activity_star_words_recyclerview = findViewById(R.id.activity_star_words_recyclerview);
    }

    private void setup() {

        activity_star_words_back_btn.setOnClickListener(v -> finish());

        wordAdapter = new StarredWordAdapter(wordModelList);
        wordAdapter.setPersian(true);
        activity_star_words_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_star_words_recyclerview.setAdapter(wordAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onSuccess(@NonNull List<WordModel> wordModels) {
                wordModelList.clear();
                wordModelList.addAll(wordModels);
                wordAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    public void updateData(SingleObserver<List<WordModel>> singleObserver) {
        MyDB.getInstance(this)
                .getWordDao()
                .readFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(singleObserver);
    }

}
