package ir.sublearn.application.views.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

import ir.sublearn.application.R;
import ir.sublearn.application.tools.tts_manager.TTsSingle;

public class SettingsActivity extends BaseActivity {

    AppCompatImageView activity_settings_back_btn;
    MaterialButton activity_settings_voice_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_settings_back_btn = findViewById(R.id.activity_settings_back_btn);
        activity_settings_voice_btn = findViewById(R.id.activity_settings_voice_btn);
    }

    private void setup() {
        activity_settings_back_btn.setOnClickListener(v -> finish());
        activity_settings_voice_btn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("تنظیمات صدا");
            String[] str = {"آقا", "خانوم"};
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case 0:
                        Toast.makeText(SettingsActivity.this, "صدای آقا انتخاب شد", Toast.LENGTH_SHORT).show();
                        TTsSingle.setMan(SettingsActivity.this,true);
                        dialog.dismiss();
                        break;
                    case 1:
                        Toast.makeText(SettingsActivity.this, "صدای خانوم انتخاب شد", Toast.LENGTH_SHORT).show();
                        TTsSingle.setMan(SettingsActivity.this,false);
                        dialog.dismiss();
                        break;
                }
            };
            if ( TTsSingle.isMan(this) ) {
                alertDialog.setSingleChoiceItems(str, 0, dialogClickListener);
            } else {
                alertDialog.setSingleChoiceItems(str, 1, dialogClickListener);
            }
            alertDialog.show();
        });

    }

}
