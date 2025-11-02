package ir.sublearn.application.tools.texttools;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TextTools {

    private Context icontext;

    public TextTools inject(Context context) {
        this.icontext = context;
        return this;
    }

    public static String convertPersianDigitsToEnglish(String input) {
        return input.replaceAll("۰", "0")
                .replaceAll("۱", "1")
                .replaceAll("۲", "2")
                .replaceAll("۳", "3")
                .replaceAll("۴", "4")
                .replaceAll("۵", "5")
                .replaceAll("۶", "6")
                .replaceAll("۷", "7")
                .replaceAll("۸", "8")
                .replaceAll("۹", "9");
    }

    public Integer convertTimeToSecound(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);

        String[] secParts = parts[1].split("\\.");
        int seconds = Integer.parseInt(secParts[0]);
        float centiseconds = ((float) Integer.parseInt(secParts[1]) / 100);

        return (minutes * 60) + seconds + Math.round(centiseconds);
    }

    public String convertSecoundToMinute(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public Observable<String> readJsonFromFolderObservable(String fileName, String file_dir) {
        return Observable.fromCallable(() -> readJsonFromFolder(fileName, file_dir))
                .subscribeOn(Schedulers.io());
    }

    public String readJsonFromFolder(String fileName, String file_dir) {
        File jsonFile = new File(getContext().getExternalFilesDir(null) + ("/" + file_dir), fileName);
        if (!jsonFile.exists()) {
            Log.e("JSON", "فایل وجود ندارد");
            return null;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonBuilder.toString();
    }

    public Observable<String> readTextFromAssetsObservable(String fullPath) {
        return Observable.fromCallable(() -> readTextFromAssets(fullPath))
                .subscribeOn(Schedulers.io());
    }

    public String readTextFromAssets(String fullPath) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = getContext().getAssets();

        try (InputStream inputStream = assetManager.open(fullPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return stringBuilder.toString();
    }

    public void hide_keyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public String base64HighEncoding(String str) {
        return base64Encode(reverse(base64Encode(str)));
    }

    public String base64HighDecoding(String str) {
        return base64Decode(reverse(base64Decode(str)));
    }

    public String base64Decode(String str) {
        byte[] data = Base64.decode(str, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);
    }

    public String base64Encode(String str) {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    public Context getContext() {
        return icontext;
    }
}
