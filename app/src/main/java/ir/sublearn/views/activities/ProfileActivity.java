package ir.sublearn.views.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.R;
import ir.sublearn.models.UserModel;
import ir.sublearn.tools.Hasher.Hasher;
import ir.sublearn.tools.devices.Devices;
import ir.sublearn.tools.mydb.MyDB;

public class ProfileActivity extends BaseActivity {

    AppCompatImageView activity_profile_back_btn;
    MaterialButton activity_profile_username, activity_profile_user_email;
    private String TAG = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_profile);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_profile_back_btn = findViewById(R.id.activity_profile_back_btn);
        activity_profile_username = findViewById(R.id.activity_profile_username);
        activity_profile_user_email = findViewById(R.id.activity_profile_user_email);
    }

    private void setup() {
        activity_profile_back_btn.setOnClickListener(v -> finish());
        MyDB.getInstance(this)
                .getUserDao()
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull UserModel userModel) {
                        try {
                            activity_profile_username.setText(Hasher.decrypt(userModel.getUser_name(), Devices.getUniqueId(ProfileActivity.this)));
                            activity_profile_user_email.setText(Hasher.decrypt(userModel.getUser_email(), Devices.getUniqueId(ProfileActivity.this)));
                        } catch (Exception e) {
                            Log.d(TAG, "onSuccess: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
