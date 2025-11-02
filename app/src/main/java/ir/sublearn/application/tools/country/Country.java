package ir.sublearn.application.tools.country;

import java.util.ArrayList;
import java.util.List;

import ir.sublearn.application.models.LangModel;

public class Country {

    private static List<LangModel> langModels;

    public static List<LangModel> generateLanguage() {
        if (langModels == null) {
            langModels = new ArrayList<>();
            langModels.add(new LangModel("فارسی", "fa-IR", "ic_iran.png"));
            langModels.add(new LangModel("انگلیسی", "en-GB", "ic_united_kingdom.png"));
            langModels.add(new LangModel("آمریکایی", "en-US", "ic_usa.png"));
            langModels.add(new LangModel("فرانسوی", "fr-FR", "ic_france.png"));
            langModels.add(new LangModel("آلمانی", "de-DE", "ic_germany.png"));
            langModels.add(new LangModel("ایتالیایی", "it-IT", "ic_italy.png"));
            langModels.add(new LangModel("اسپانیایی", "es-ES", "ic_spain.png"));
            langModels.add(new LangModel("هلندی", "nl-NL", "ic_netherlands.png"));
            langModels.add(new LangModel("عربی", "ar-SA", "ic_saudi.png"));
            langModels.add(new LangModel("ترکیه ای", "tr-TR", "ic_turkey.png"));
            langModels.add(new LangModel("چینی", "zh-CN", "ic_china.png"));
            langModels.add(new LangModel("روسی", "ru-RU", "ic_russia.png"));
            langModels.add(new LangModel("کره ای", "ko-KR", "ic_south_korea.png"));
            langModels.add(new LangModel("ارمنی", "hy-AM", "ic_armenia.png"));
            langModels.add(new LangModel("استرالیایی", "en-AU", "ic_australia.png"));
            langModels.add(new LangModel("آذربایجانی", "az-AZ", "ic_azerbaijan.png"));
            langModels.add(new LangModel("بنگلادشی", "bn-BD", "ic_bangladesh.png"));
            langModels.add(new LangModel("بوسنیه ای", "bs-BA", "ic_bosnia.png"));
            langModels.add(new LangModel("بلغاری", "bg-BG", "ic_bulgaria.png"));
            langModels.add(new LangModel("کمبوجیه ای", "km-KH", "ic_cambodia.png"));
            langModels.add(new LangModel("کانادایی", "en-CA", "ic_canada.png"));
            langModels.add(new LangModel("کرواسی", "hr-HR", "ic_croatia.png"));
            langModels.add(new LangModel("چکی", "cs-CZ", "ic_czech.png"));
            langModels.add(new LangModel("دانمارکی", "da-DK", "ic_denmark.png"));
            langModels.add(new LangModel("استونی", "et-EE", "ic_estonia.png"));
            langModels.add(new LangModel("فنلاندی", "fi-FI", "ic_finland.png"));
            langModels.add(new LangModel("گرجی", "ka-GE", "ic_georgia.png"));
            langModels.add(new LangModel("یونانی", "el-GR", "ic_greece.png"));
            langModels.add(new LangModel("مجارستانی", "hu-HU", "ic_hungary.png"));
            langModels.add(new LangModel("هندی", "hi-IN", "ic_india.png"));
            langModels.add(new LangModel("اندونزیایی", "id-ID", "ic_indonesia.png"));
            langModels.add(new LangModel("ایرلندی", "en-IE", "ic_ireland.png"));
            langModels.add(new LangModel("اسرائیلی", "he-IL", "ic_israel.png"));
            langModels.add(new LangModel("ژاپنی", "ja-JP", "ic_japan.png"));
            langModels.add(new LangModel("مالزیایی", "ms-MY", "ic_malaysia.png"));
            langModels.add(new LangModel("مکزیکی", "es-MX", "ic_mexico.png"));
            langModels.add(new LangModel("نپالی", "ne-NP", "ic_nepal.png"));
            langModels.add(new LangModel("نروژی", "no-NO", "ic_norway.png"));
            langModels.add(new LangModel("پاکستانی", "en-PK", "ic_pakistan.png"));
            langModels.add(new LangModel("لهستانی", "pl-PL", "ic_poland.png"));
            langModels.add(new LangModel("پرتغالی", "pt-PT", "ic_portugal.png"));
            langModels.add(new LangModel("رومانیایی", "ro-RO", "ic_romania.png"));
            langModels.add(new LangModel("صربستانی", "sr-RS", "ic_serbia.png"));
            langModels.add(new LangModel("اسلواکیایی", "sk-SK", "ic_slovakia.png"));
            langModels.add(new LangModel("سوئدی", "sv-SE", "ic_sweden.png"));
            langModels.add(new LangModel("تایوانی", "zh-TW", "ic_taiwan.png"));
            langModels.add(new LangModel("تایلندی", "th-TH", "ic_thailand.png"));
            langModels.add(new LangModel("اکراینی", "uk-UA", "ic_ukraine.png"));
            langModels.add(new LangModel("ازبکی", "uz-UZ", "ic_uzbekistan.png"));
            langModels.add(new LangModel("ویتنامی", "vi-VN", "ic_vietnam.png"));
            langModels.add(new LangModel("ولزی", "cy-GB", "ic_wales.png"));
        }
        return langModels;
    }

}
