package ir.sublearn.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.R;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.UserModel;
import ir.sublearn.tools.Hasher.Hasher;
import ir.sublearn.tools.iokhttp.IOkHttp;
import ir.sublearn.tools.mydb.MyDB;
import ir.sublearn.tools.packager.Packager;
import ir.sublearn.tools.version.AppVersionChecker;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    AppCompatTextView activity_spalsh_text_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_spalsh_text_version = findViewById(R.id.activity_spalsh_text_version);
    }

    private void setup() {
        activity_spalsh_text_version.setText(Packager.negareshApplication(this));
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        MyDB myDB = MyDB.getInstance(SplashActivity.this);
                        myDB.getUserDao().getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                new SingleObserver<>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                    }

                                    @Override
                                    public void onSuccess(@NonNull UserModel userModel) {
                                        goNext(true);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        goNext(false);
                                    }
                                }
                        );
                    }
                });
    }

    private void goNext(boolean userFound) {

        Hasher.generateUltraSecurePassword();

        RequestBody requestBody = new FormBody.Builder()
                .add("app_version_code", String.valueOf(Packager.getVersionCode(this)))
                .add("app_version_name", Packager.getVersionName(this))
                .add("API_PASSWORD", Hasher.getPassword())
                .build();

        AppVersionChecker.app_version_checker(requestBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {

                if (response.isSuccessful()) {
                    try {
                        String string = response.body().string();
                        String json = Hasher.decrypt(string, Hasher.getPassword());
                        Log.d("TAG", "onSuccess: " + json);
                        JSONObject jsonObject = new JSONObject(json);
                        int result_code = jsonObject.getInt("result_code");
                        switch (result_code) {
                            case 0:
                                runOnUiThread(() -> Toast.makeText(SplashActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                break;
                            case 1:
                                runOnUiThread(() -> Toast.makeText(SplashActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                break;
                            case 2:
                                boolean can_use = (Integer.parseInt(jsonObject.getString("can_use")) == 1);
                                boolean can_update = (Integer.parseInt(jsonObject.getString("can_update")) == 1);

                                IOkHttp.setLogin(jsonObject.getString("login"));
                                IOkHttp.setRegister(jsonObject.getString("register"));
                                IOkHttp.setAuthorization_confirm(jsonObject.getString("authorization_confirm"));
                                IOkHttp.setChange_password(jsonObject.getString("change_password"));
                                IOkHttp.setForget_passwprd(jsonObject.getString("forget_passwprd"));
                                IOkHttp.setGet_seasons(jsonObject.getString("get_seasons"));
                                IOkHttp.setGet_episode(jsonObject.getString("get_episode"));
                                IOkHttp.setTranslate(jsonObject.getString("translate"));
                                IOkHttp.setGet_all_movies(jsonObject.getString("get_all_movies"));
                                IOkHttp.setGet_series(jsonObject.getString("get_series"));
                                IOkHttp.setGet_musics_kids(jsonObject.getString("get_musics_kids"));
                                IOkHttp.setGet_musics_mobtadi(jsonObject.getString("get_musics_mobtadi"));
                                IOkHttp.setGet_musics_motevaset(jsonObject.getString("get_musics_motevaset"));
                                IOkHttp.setGet_musics_pishrafte(jsonObject.getString("get_musics_pishrafte"));
                                IOkHttp.setGet_musics_top_kids(jsonObject.getString("get_musics_top_kids"));
                                IOkHttp.setGet_musics_top_mobtadi(jsonObject.getString("get_musics_top_mobtadi"));
                                IOkHttp.setGet_musics_top_motevaset(jsonObject.getString("get_musics_top_motevaset"));
                                IOkHttp.setGet_musics_top_pishrafte(jsonObject.getString("get_musics_top_pishrafte"));
                                IOkHttp.setGet_top_movies(jsonObject.getString("get_top_movies"));
                                IOkHttp.setGet_top_series(jsonObject.getString("get_top_series"));

                                runOnUiThread(() -> {
                                    if (can_use) {
                                        if (can_update) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                                            View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.dialog_update, null);
                                            alertDialog.setView(view);
                                            alertDialog.setCancelable(true);
                                            MaterialButton dialog_update_btnUpdate = view.findViewById(R.id.dialog_update_btnUpdate);
                                            dialog_update_btnUpdate.setOnClickListener(v -> Packager.openInMarket(SplashActivity.this));
                                            alertDialog.setOnDismissListener(v -> canNext(userFound));
                                            alertDialog.show();
                                        } else {
                                            canNext(userFound);
                                        }
                                    } else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                                        View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.dialog_update, null);
                                        alertDialog.setView(view);
                                        alertDialog.setCancelable(false);
                                        MaterialButton dialog_update_btnUpdate = view.findViewById(R.id.dialog_update_btnUpdate);
                                        dialog_update_btnUpdate.setOnClickListener(v -> Packager.openInMarket(SplashActivity.this));
                                        alertDialog.setOnDismissListener(v -> finish());
                                        alertDialog.show();
                                    }
                                });
                                break;
                            case 3:
                                runOnUiThread(() -> Toast.makeText(SplashActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                break;
                            case 4:
                                runOnUiThread(() -> Toast.makeText(SplashActivity.this, getString(R.string.support_for_problem), Toast.LENGTH_SHORT).show());
                                break;
                        }

                    } catch (Exception e) {
                        Log.d("TAG", "onFailure2: " + e.getMessage());
                    }
                } else {
                    Log.d("TAG", "onFailure: " + response.code());
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG", "onFailure: " + throwable.getMessage());
            }
        });
    }

    public void canNext(boolean userFound) {
        if (userFound)
            goToMain();
        else
            goToLogister();
    }

    private void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void goToLogister() {
        startActivity(new Intent(SplashActivity.this, LogisterActivity.class));
        finish();
    }

}
