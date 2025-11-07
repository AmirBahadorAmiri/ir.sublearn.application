package ir.sublearn.application.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.application.R;
import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.models.UserModel;
import ir.sublearn.application.tools.Hasher.Hasher;
import ir.sublearn.application.tools.devices.Devices;
import ir.sublearn.application.tools.mydb.MyDB;
import ir.sublearn.application.tools.network_manager.NetworktManager;
import ir.sublearn.application.tools.texttools.TextTools;
import ir.sublearn.application.tools.user_manager.UserManager;
import ir.sublearn.application.views.activities.LogisterActivity;
import ir.sublearn.application.views.activities.MainActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmUserFragment extends Fragment {

    public static ConfirmUserFragment confirmUserFragment;
    private OnBackPressedCallback backPressedCallback;

    AppCompatImageView fragment_confirm_user_back_btn;
    AppCompatTextView fragment_user_confirm_title;
    AppCompatEditText fragment_user_authorization_code;
    MaterialButton fragment_confirm_user_login_btn;
    TextTools textTools;

    public static String user_email = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_confirm_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_confirm_user_back_btn = view.findViewById(R.id.fragment_confirm_user_back_btn);
        fragment_user_confirm_title = view.findViewById(R.id.fragment_user_confirm_title);
        fragment_user_authorization_code = view.findViewById(R.id.fragment_user_authorization_code);
        fragment_confirm_user_login_btn = view.findViewById(R.id.fragment_confirm_user_login_btn);
    }

    private void setup(View view) {

        textTools = new TextTools().inject(requireContext());

        // Show Dialog For Exit From Login Or Register
        fragment_confirm_user_back_btn.setOnClickListener(view1 -> ((LogisterActivity) requireActivity()).changeFragment(WelcomeFragment.getWelcomeFragment()));

        fragment_confirm_user_login_btn.setOnClickListener(v -> {
            textTools.hide_keyboard(view);
            String authorization_code = Objects.requireNonNull(fragment_user_authorization_code.getText()).toString();
            if (authorization_code.isEmpty() || authorization_code.length() < 4) {
                Snackbar.make(view, getString(R.string.insert_authorization_code), Snackbar.LENGTH_LONG).show();
            } else {
                if (NetworktManager.isVPNConnected(requireContext())) {
                    NetworktManager.showVpnSnackbar(requireView());
                } else {
                    RequestBody body = new FormBody.Builder()
                            .add("user_email", getUserEmail())
                            .add("api_authorization_code", authorization_code)
                            .build();
                    UserManager.authorization_confirm(body, new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            if (response.isSuccessful()) {
                                try {
                                    String result = response.body().string();
                                    JSONObject object = new JSONObject(result);
                                    int result_code = object.getInt("result_code");
                                    switch (result_code) {
                                        case 0:
                                            Snackbar.make(view, getString(R.string.login_register_problem), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 1:
                                            Snackbar.make(view, getString(R.string.user_not_found), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 2:
                                            JSONObject user = object.getJSONObject("user");
                                            int user_id = user.getInt("user_id");
                                            String user_name = Hasher.encrypt(user.getString("user_name"), Devices.getUniqueId(requireContext()));
                                            String user_email = Hasher.encrypt(user.getString("user_email"), Devices.getUniqueId(requireContext()));
                                            String api_authorization_key = Hasher.encrypt(user.getString("api_authorization_key"), Devices.getUniqueId(requireContext()));

                                            MyDB.getInstance(requireContext()).getUserDao()
                                                    .insert(new UserModel(user_id, user_name, user_email, api_authorization_key))
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe(new CompletableObserver() {
                                                        @Override
                                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                                        }

                                                        @Override
                                                        public void onComplete() {
                                                            startActivity(new Intent(requireActivity(), MainActivity.class));
                                                            requireActivity().finish();
                                                        }

                                                        @Override
                                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                            Log.e("msg: ", Objects.requireNonNull(e.getMessage()));
                                                        }
                                                    });

                                            break;
                                        case 3:
                                            Snackbar.make(view, getString(R.string.login_user_time_left), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 4:
                                            Snackbar.make(view, getString(R.string.wrong_password), Snackbar.LENGTH_LONG).show();
                                            break;
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
            }
        });

        backPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
//                 getParentFragmentManager().popBackStack();
                ((LogisterActivity) requireContext()).changeFragment(WelcomeFragment.getWelcomeFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), backPressedCallback);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backPressedCallback.remove();
    }

    public static void setUseremail(String user_email) {
        ConfirmUserFragment.user_email = user_email;
    }

    public static String getUserEmail() {
        return user_email;
    }

    public static ConfirmUserFragment getConfirmUserFragment(String email) {
        if (confirmUserFragment == null) {
            confirmUserFragment = new ConfirmUserFragment();
            confirmUserFragment.setArguments(new Bundle());
        }
        setUseremail(email);
        return confirmUserFragment;
    }
}
