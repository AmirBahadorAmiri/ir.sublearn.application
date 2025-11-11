package ir.sublearn;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.tools.copy_helper.CopyHelper;
import ir.sublearn.tools.language_manager.LanguageManager;
import ir.sublearn.tools.tts_manager.TTsSingle;
import okhttp3.Response;

public class AppManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupFont();
        setupTTS();
        CopyHelper.initialize(this);
    }

    private void setupTTS() {
        TTsSingle.initialize(getApplicationContext(), new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                TTsSingle.isSupportLanguage(getApplicationContext(), LanguageManager.getDefaultVoiceLanguage(getBaseContext()));
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void setupFont() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/vazir.ttf")
                                .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                                .build()))
                .build());
    }

}
