package ir.sublearn.application.views.activities;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.masoudss.lib.WaveformSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.application.R;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.LyricsModel;
import ir.sublearn.application.tools.downloader.Downloader;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import ir.sublearn.application.tools.texttools.TextTools;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class SongPlayerActivity extends BaseActivity implements AudioManager.OnAudioFocusChangeListener {

    MediaPlayer mediaPlayer = new MediaPlayer();
    AudioManager audioManager;
    AudioFocusRequest focusRequest;

    List<LyricsModel> lrcList = new ArrayList<>();

    TextTools textTools = new TextTools().inject(this);
    Downloader downloader = new Downloader().inject(this);

    AppCompatImageView big_picture,song_player_back_btn;
    AppCompatTextView media_player_lyric_en, media_player_lyric_fa, endTime, currentTime, song_name_textview;
    WaveformSeekBar media_duration_seekbar;
    AppCompatImageButton media_play_pause, media_backward, media_forward;

    Animation fade_in_fa, fade_out_fa, fade_in_en, fade_out_en;

    String sub_link = "", mp3_file_name = "", song_name = "", song_poster = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        setViewCompat();
        hideSystemUI();
        findViews();
        loadData();
        setupViews();

        loadLyric();
        loadMusic();

    }

    private void findViews() {
        big_picture = findViewById(R.id.big_picture);
        media_player_lyric_en = findViewById(R.id.media_player_lyric_en);
        media_player_lyric_fa = findViewById(R.id.media_player_lyric_fa);
        endTime = findViewById(R.id.endTime);
        currentTime = findViewById(R.id.currentTime);
        song_name_textview = findViewById(R.id.song_name_textview);
        media_duration_seekbar = findViewById(R.id.media_duration_seekbar);
        media_play_pause = findViewById(R.id.media_play_pause);
        media_backward = findViewById(R.id.media_backward);
        media_forward = findViewById(R.id.media_forward);
        song_player_back_btn = findViewById(R.id.song_player_back_btn);
    }

    private void loadData() {
        if (getIntent() != null) {
            sub_link = getIntent().getStringExtra("sub_link");
            mp3_file_name = getIntent().getStringExtra("mp3_file_name");
            song_name = getIntent().getStringExtra("song_name");
            song_poster = getIntent().getStringExtra("song_poster");
        }
    }

    private void setupViews() {
        song_player_back_btn.setOnClickListener( v->{finish();});
        song_name_textview.setText(song_name);
        Glide.with(this).load(song_poster).placeholder(R.drawable.image_placeholder).into(big_picture);

        media_duration_seekbar.setOnProgressChanged((waveformSeekBar, progress, fromUser) -> {
            if (fromUser) {
                mediaPlayer.seekTo((int) (progress * 1000));
                currentTime.setText(textTools.convertSecoundToMinute((int) progress));
            }
        });

        media_play_pause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                playMusic();
            }
        });
        media_forward.setOnClickListener(v -> {
            int max = Math.min((mediaPlayer.getCurrentPosition() + 5000), mediaPlayer.getDuration());
            media_duration_seekbar.setProgress((float) max / 1000);
            mediaPlayer.seekTo(max);
            currentTime.setText(textTools.convertSecoundToMinute(max / 1000));
        });
        media_backward.setOnClickListener(v -> {
            int min = Math.max((mediaPlayer.getCurrentPosition() - 5000), 0);
            media_duration_seekbar.setProgress((float) min / 1000);
            mediaPlayer.seekTo(min);
            currentTime.setText(textTools.convertSecoundToMinute(min / 1000));
        });
    }

    private void loadLyric() {

        IOkHttp iOkHttp = new IOkHttp();
        HttpUrl httpUrl = HttpUrl.parse(sub_link)
                .newBuilder()
                .build();
        iOkHttp.get(httpUrl, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                String str = "";
                try {
                    str = response.body().string();
//                    String originalString = textTools.decodingString(str);
                    /*
                     *
                     *    \[(\d{2}:\d{2}\.\d{2})](.*)\[\^(.*)\^]
                     *    [\1][\2]\[]
                     *
                     * «ترجمه فارسی این آهنگ موجود نیست»
                     *
                     * */
                    Pattern pattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})]\\[(.*)]\\[(.*)]");
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {
                        int time = textTools.convertTimeToSecound(Objects.requireNonNull(matcher.group(1)));
                        String english = Objects.requireNonNull(matcher.group(2)).replace("\\n", "\n");
                        String persian = Objects.requireNonNull(matcher.group(3)).replace("\\n", "\n");
                        lrcList.add(new LyricsModel(english, persian, time));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

//        textTools.readJsonFromFolderObservable(song_name + ".lrc", "lyrics/" + artist_name)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(@NonNull String str) {
//                        if (str != null) {
////                            String originalString = textTools.decodingString(str);
//                            Pattern pattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})](.*)\\[\\^(.*)\\^]");
//                            Matcher matcher = pattern.matcher(str);
//                            while (matcher.find()) {
//                                int time = textTools.convertTimeToSecound(Objects.requireNonNull(matcher.group(1)));
//                                String english = Objects.requireNonNull(matcher.group(2)).replace("\\n", "\n");
//                                String persian = Objects.requireNonNull(matcher.group(3)).replace("\\n", "\n");
//                                lrcList.add(new Lyrics(english, persian, time));
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
    }

    private LyricsModel searchFor(int pos) {
        for (int i = 0; i < lrcList.size(); i++) {
            if (lrcList.get(i).getPos() == pos) {
                return lrcList.get(i);
            }
        }
        return null;
    }

    private void loadMusic() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        try {
            Uri uri = downloader.getMusicFileUri(mp3_file_name);
            mediaPlayer.setDataSource(this, uri);
            Observable.fromAction(() -> mediaPlayer.prepare())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@NonNull Object o) {
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            playMusic();
                        }
                    });

//            Observable.fromAction(() -> media_duration_seekbar.setSampleFrom(uri))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe();
            media_duration_seekbar.setSampleFrom(uri);
            int seconds = mediaPlayer.getDuration() / 1000;
            media_duration_seekbar.setMaxProgress(seconds);
            endTime.setText(textTools.convertSecoundToMinute(seconds));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(this)
                .build();

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        int pos = mediaPlayer.getCurrentPosition();
                        if (pos < mediaPlayer.getDuration()) {
                            media_duration_seekbar.setProgress((float) pos / 1000);
                            currentTime.setText(textTools.convertSecoundToMinute(pos / 1000));
                            LyricsModel lyricsModel = searchFor(pos / 1000);
                            if (lyricsModel != null) {
                                String en = Objects.requireNonNull(lyricsModel).getEn();
                                String fa = Objects.requireNonNull(lyricsModel).getFa();
                                media_player_lyric_en.setAnimation(getFade_out_en());
                                media_player_lyric_fa.setAnimation(getFade_out_fa());
                                getFade_out_en().startNow();
                                getFade_out_fa().startNow();
                                getFade_out_en().setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        media_player_lyric_en.setText(en);
                                        if (!en.isEmpty()) {
                                            media_player_lyric_en.setAnimation(getFade_in_fa());
                                        }
                                        getFade_in_en().startNow();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }
                                });

                                getFade_out_fa().setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        media_player_lyric_fa.setText(fa);
                                        if (!fa.isEmpty()) {
                                            media_player_lyric_fa.setAnimation(getFade_in_fa());
                                        }
                                        getFade_in_fa().startNow();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }
                                });

                            }
                        } else {
                            goFirst();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void playMusic() {
        int result = audioManager.requestAudioFocus(focusRequest);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
            media_play_pause.setImageResource(R.drawable.ic_pause);
        }
    }

    public void pauseMusic() {
        audioManager.abandonAudioFocusRequest(focusRequest);
        mediaPlayer.pause();
        media_play_pause.setImageResource(R.drawable.ic_play);
    }

    public void goFirst() {
        mediaPlayer.seekTo(0);
        currentTime.setText("00:00");
        media_duration_seekbar.setProgress(0);
        media_play_pause.setImageResource(R.drawable.ic_play);
    }

    public Animation getFade_in_en() {
        if (fade_in_en == null)
            fade_in_en = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        return fade_in_en;
    }

    public Animation getFade_in_fa() {
        if (fade_in_fa == null)
            fade_in_fa = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        return fade_in_fa;
    }

    public Animation getFade_out_en() {
        if (fade_out_en == null)
            fade_out_en = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        return fade_out_en;
    }

    public Animation getFade_out_fa() {
        if (fade_out_fa == null)
            fade_out_fa = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        return fade_out_fa;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                if (mediaPlayer.isPlaying())
                    pauseMusic();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying())
                    pauseMusic();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                playMusic();
                break;
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        File cacheDir = getCacheDir();
        for (File file : Objects.requireNonNull(cacheDir.listFiles())) {
            if (file.getName().endsWith(".null")) {
                file.delete();
            }
        }
        for (File file : Objects.requireNonNull(Objects.requireNonNull(getExternalCacheDir()).listFiles())) {
            if (file.getName().startsWith("internal-ampl-cache")) {
                file.delete();
            }
        }
    }

}
