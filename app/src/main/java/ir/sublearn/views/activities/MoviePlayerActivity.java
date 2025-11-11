package ir.sublearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import java.util.Arrays;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.sublearn.R;

public class MoviePlayerActivity extends AppCompatActivity {

    private ExoPlayer player;
    String movie_file_url, persian_sub, english_sub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);
        hideSystemUI();

        PlayerView playerView = findViewById(R.id.player_view);

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            movie_file_url = receivedIntent.getStringExtra("movie_file_url");
            persian_sub = receivedIntent.getStringExtra("movie_persian_sub_url");
            english_sub = receivedIntent.getStringExtra("movie_english_sub_url");
        }

        MediaItem.SubtitleConfiguration fa_subtitle =
                new MediaItem.SubtitleConfiguration.Builder(Uri.parse(persian_sub))
                        .setMimeType(MimeTypes.APPLICATION_SUBRIP) // The correct MIME type (required).
                        .setLanguage("fa-IR") // The subtitle language (optional).
                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT) // Selection flags for the track (optional).
                        .build();
        MediaItem.SubtitleConfiguration en_subtitle =
                new MediaItem.SubtitleConfiguration.Builder(Uri.parse(english_sub))
                        .setMimeType(MimeTypes.APPLICATION_SUBRIP) // The correct MIME type (required).
                        .setLanguage("en-US") // The subtitle language (optional).
                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT) // Selection flags for the track (optional).
                        .build();
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(movie_file_url)
                .setSubtitleConfigurations(Arrays.asList(en_subtitle, fa_subtitle))
                .build();

        // Prepare the player with the media item
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release(); // Release the player resources
            player = null;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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

}
