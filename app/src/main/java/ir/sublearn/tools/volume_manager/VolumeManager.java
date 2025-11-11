package ir.sublearn.tools.volume_manager;

import android.content.Context;
import android.media.AudioManager;

public class VolumeManager {

    private static AudioManager audioManager;

    public static int getVolume(Context context) {
        return getManager(context).getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setVolume(Context context, int volume) {
        getManager(context).setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
    }

    public static AudioManager getManager(Context context) {
        if (audioManager == null)
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager;
    }

}
