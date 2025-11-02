package ir.sublearn.application.tools.tts_manager;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import ir.sublearn.application.StaticDatas;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.tools.language_manager.LanguageManager;
import ir.sublearn.application.tools.shared_helper.SharedSingle;

public class TTsSingle {

    private static TextToSpeech textToSpeech;

    public static final String MAN = "MAN";
    public static final String WOMAN = "WOMAN";

    public static final float TTS_MAN = 0.6f;
    public static final float TTS_WOMAN = 1.0f;

    public static void initialize(Context context, ResponseListener responseListener) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) responseListener.onSuccess(null);
                else responseListener.onFailure(null);
            });
        } else {
            responseListener.onSuccess(null);
        }
    }

    public static boolean isSupportLanguage(Context context, String locale) {
        int result = textToSpeech.setLanguage(new Locale(locale));
        boolean supportedLanguage = !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED);
        if (supportedLanguage) {
            if (!LanguageManager.getDefaultVoiceLanguage(context).equals(locale)) {
                LanguageManager.setDefaultVoiceLanguage(context, locale);
                Toast.makeText(context, "درحال بارگیری بسته صوتی زبان ...", Toast.LENGTH_SHORT).show();
            }
        }
        return supportedLanguage;
    }

    public static void speak(Context context, String text) {
        if (isMan(context)) {
            textToSpeech.setPitch(TTS_MAN);
        } else {
            textToSpeech.setPitch(TTS_WOMAN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public static boolean isMan(Context context) {
        if (SharedSingle.getSharedHelper(context).readString(StaticDatas.TTS).equals(""))
            SharedSingle.getSharedHelper(context).insert(StaticDatas.TTS, WOMAN);
        return SharedSingle.getSharedHelper(context).readString(StaticDatas.TTS).equals(MAN);
    }

    public static void setMan(Context context, boolean isMan) {
        if (isMan) SharedSingle.getSharedHelper(context).insert(StaticDatas.TTS, MAN);
        else SharedSingle.getSharedHelper(context).insert(StaticDatas.TTS, WOMAN);
    }

}