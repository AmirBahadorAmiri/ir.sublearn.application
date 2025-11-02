package ir.sublearn.application.views.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.application.R;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.WordModel;
import ir.sublearn.application.tools.copy_helper.CopyHelper;
import ir.sublearn.application.tools.mydb.MyDB;
import ir.sublearn.application.tools.tts_manager.TTsSingle;
import ir.sublearn.application.tools.volume_manager.VolumeManager;
import okhttp3.Response;

public class ShowWordActivity extends BaseActivity {

    AppCompatImageView activity_show_word_back_btn, activity_show_word_settings_icon, activity_show_word_star_icon, activity_show_word_speaker, activity_show_word_copy;
    AppCompatTextView activity_show_word_from_textview, activity_show_word_to_textview;
    int word_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_show_word_back_btn = findViewById(R.id.activity_show_word_back_btn);
        activity_show_word_settings_icon = findViewById(R.id.activity_show_word_settings_icon);
        activity_show_word_star_icon = findViewById(R.id.activity_show_word_star_icon);
        activity_show_word_speaker = findViewById(R.id.activity_show_word_speaker);
        activity_show_word_copy = findViewById(R.id.activity_show_word_copy);
        activity_show_word_from_textview = findViewById(R.id.activity_show_word_from_textview);
        activity_show_word_to_textview = findViewById(R.id.activity_show_word_to_textview);
    }

    private void setup() {

        activity_show_word_back_btn.setOnClickListener(v -> finish());
        activity_show_word_settings_icon.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        activity_show_word_speaker.setOnClickListener(v -> {
            if (!activity_show_word_from_textview.getText().toString().isEmpty()) {
                if (VolumeManager.getVolume(this) == 0) {
                    Snackbar.make(v, "صدای سیستم قطع است", Snackbar.LENGTH_LONG)
                            .setAction("افزایش صدا", n -> VolumeManager.setVolume(this, VolumeManager.getManager(this).getStreamMaxVolume(AudioManager.STREAM_MUSIC))).setActionTextColor(ContextCompat.getColor(this, R.color.blueColor)).show();
                } else {
                    TTsSingle.initialize(this, new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            if (TTsSingle.isSupportLanguage(ShowWordActivity.this, "en-US")) {
                                TTsSingle.speak(ShowWordActivity.this, activity_show_word_from_textview.getText().toString());
                            } else {
                                Toast.makeText(ShowWordActivity.this, "بسته نرم افزار صوتی این زبان نصب نشده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.d("TAG", "tts manager failure");
                        }
                    });
                }
            }
        });

        activity_show_word_copy.setOnClickListener(v -> {
            if (!activity_show_word_from_textview.getText().toString().isEmpty()) {
                CopyHelper.initialize(this);
                CopyHelper.insert("");
            }
        });

        if (getIntent() != null) {
            word_id = getIntent().getIntExtra("word_id", 0);
        }

        MyDB.getInstance(this)
                .getWordDao()
                .getWordById(word_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull WordModel wordModel) {
                        wordModel.setView_time(System.currentTimeMillis());
                        MyDB.getInstance(ShowWordActivity.this).getWordDao().update(wordModel).subscribe();

                        activity_show_word_from_textview.setText(wordModel.getEnglishWord());
                        activity_show_word_to_textview.setText(wordModel.getPersianWord());

                        if (wordModel.isFavorite()) {
                            activity_show_word_star_icon.setImageResource(R.drawable.star_icon);
                        }
                        activity_show_word_star_icon.setOnClickListener(v -> {
                            if (wordModel.isFavorite()) {
                                wordModel.setFavorite(false);
                                activity_show_word_star_icon.setImageResource(R.drawable.star_icon_outline);
                            } else {
                                wordModel.setFavorite(true);
                                activity_show_word_star_icon.setImageResource(R.drawable.star_icon);
                            }
                            MyDB.getInstance(ShowWordActivity.this)
                                    .getWordDao()
                                    .update(wordModel)
                                    .subscribe();
                        });

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }
}
