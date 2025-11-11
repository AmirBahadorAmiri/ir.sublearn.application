package ir.sublearn.tools.language_manager;

import android.content.Context;

import ir.sublearn.tools.country.Country;
import ir.sublearn.tools.shared_helper.SharedSingle;

public class LanguageManager {

    public static final String TRANSLATE_TO = "TRANSLATE_TO";

    public static final String DEFAULT_VOICE_PACK = "DEFAULT_VOICE_PACK";

    public static final String TRANSLATE_FROM = "TRANSLATE_FROM";

    public static void setDefaultVoiceLanguage(Context context, String locale) {
        SharedSingle.getSharedHelper(context).insert(DEFAULT_VOICE_PACK, locale);
    }

    public static String getDefaultVoiceLanguage(Context context) {
        if (SharedSingle.getSharedHelper(context).readString(DEFAULT_VOICE_PACK).isEmpty())
            setDefaultVoiceLanguage(context, "en-US");
        return SharedSingle.getSharedHelper(context).readString(DEFAULT_VOICE_PACK);
    }

    public static String getFromLangauge(Context context) {
        if (SharedSingle.getSharedHelper(context).readInt(TRANSLATE_FROM) == 0)
            setFromLangauge(context, 0);
        return Country.generateLanguage().get(SharedSingle.getSharedHelper(context).readInt(TRANSLATE_FROM) - 1).getName();
    }

    public static String getToLangauge(Context context) {
        if (SharedSingle.getSharedHelper(context).readInt(TRANSLATE_TO) == 0)
            setToLangauge(context, 1);
        return Country.generateLanguage().get(SharedSingle.getSharedHelper(context).readInt(TRANSLATE_TO) - 1).getName();
    }

    public static String getFromLangaugeCode(Context context) {
        return Country.generateLanguage().get(SharedSingle.getSharedHelper(context).readInt(TRANSLATE_FROM) - 1).getCode();
    }

    public static String getToLangaugeCode(Context context) {
        return Country.generateLanguage().get(SharedSingle.getSharedHelper(context).readInt(TRANSLATE_TO) - 1).getCode();
    }

    public static void reverceLanguage(Context context) {
        int to = SharedSingle.getSharedHelper(context).readInt(TRANSLATE_TO) - 1;
        int from = SharedSingle.getSharedHelper(context).readInt(TRANSLATE_FROM) - 1;

        setFromLangauge(context, to);
        setToLangauge(context, from);
    }

    public static void setFromLangauge(Context context, int pos) {
        SharedSingle.getSharedHelper(context).insert(TRANSLATE_FROM, pos + 1);
    }

    public static void setToLangauge(Context context, int pos) {
        SharedSingle.getSharedHelper(context).insert(TRANSLATE_TO, pos + 1);
    }

}
