package ir.sublearn.tools.urlchooser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OpenUrl {
    public static void openUrl(Context context, String link) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    public static void openChooserUrl(Context context, String link) {
        context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse(link)), "انتخاب برنامه برای باز کردن لینک"));
    }
}
