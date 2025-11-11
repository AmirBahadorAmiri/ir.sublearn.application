package ir.sublearn.tools.translate_manager;

import android.content.Context;

import java.util.Objects;

import ir.sublearn.listener.ResponseListener;
import ir.sublearn.tools.iokhttp.IOkHttp;
import ir.sublearn.tools.texttools.TextTools;
import okhttp3.HttpUrl;

public class TranslateManager {

    public static void translateByGoogle(Context context, String text, String fromCode, String toCode, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
        text = TextTools.convertPersianDigitsToEnglish(text);
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(IOkHttp.getTranslate()))
                .newBuilder()
                .addQueryParameter("client", "gtx")
                .addQueryParameter("sl", fromCode)
                .addQueryParameter("tl", toCode)
                .addQueryParameter("dt", "t")
                .addQueryParameter("q", text)
                .addQueryParameter("ie", "UTF-8")
                .addQueryParameter("oe", "UTF-8")
                .build();
        iOkHttp.get(httpUrl, responseListener);
    }
}
