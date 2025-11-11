package ir.sublearn.tools.assets_image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;

public class AssetsImageLoader {

    public static void load(Context context, String imageAddress, ImageView imageView) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open(imageAddress));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
