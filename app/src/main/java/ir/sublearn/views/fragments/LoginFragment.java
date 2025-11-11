package ir.sublearn.views.fragments;

import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.sublearn.R;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.tools.network_manager.NetworktManager;
import ir.sublearn.tools.texttools.TextTools;
import ir.sublearn.tools.user_manager.UserManager;
import ir.sublearn.views.activities.LogisterActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    public static LoginFragment loginFragment;
    AppCompatImageView fragment_login_back_btn;
    private OnBackPressedCallback backPressedCallback;
    AppCompatTextView fragment_login_forgot_password;
    AppCompatEditText fragment_login_email;
    TextInputEditText fragment_login_password;
    MaterialButton fragment_login_login_btn;
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@gmail+\\.com");
    TextTools textTools;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_login_back_btn = view.findViewById(R.id.fragment_login_back_btn);
        fragment_login_forgot_password = view.findViewById(R.id.fragment_login_forgot_password);
        fragment_login_email = view.findViewById(R.id.fragment_login_email);
        fragment_login_password = view.findViewById(R.id.fragment_login_password);
        fragment_login_login_btn = view.findViewById(R.id.fragment_login_login_btn);
    }

    private void setup(View view) {
        textTools = new TextTools().inject(requireContext());
        fragment_login_back_btn.setOnClickListener(view1 -> ((LogisterActivity) requireActivity()).changeFragment(WelcomeFragment.getWelcomeFragment()));

        fragment_login_forgot_password.setOnClickListener(v -> {
            textTools.hide_keyboard(requireView());
            if (getUserEmail().isEmpty()) {
                Snackbar.make(view, getString(R.string.insert_your_email), Snackbar.LENGTH_LONG).show();
            } else {
                Matcher matcher = pattern.matcher(getUserEmail());
                if (matcher.find()) {
                    if (NetworktManager.isVPNConnected(requireContext())) {
                        NetworktManager.showVpnSnackbar(requireView());
                    } else {
                        RequestBody body = new FormBody.Builder()
                                .add("user_email", getUserEmail())
                                .build();
                        UserManager.forget_passwprd(body, new ResponseListener() {
                            @Override
                            public void onSuccess(Response response) {
                                if (response.isSuccessful()) {
                                    try {
                                        String result = response.body().string();
                                        JSONObject object = new JSONObject(result);
                                        int result_code = object.getInt("result_code");
                                        requireActivity().runOnUiThread(() -> {
                                            switch (result_code) {
                                                case 0:
                                                    Snackbar.make(view, getString(R.string.login_register_problem), Snackbar.LENGTH_LONG).show();
                                                    break;
                                                case 1:
                                                    Snackbar.make(view, getString(R.string.user_not_found), Snackbar.LENGTH_LONG).show();
                                                    break;
                                                case 2:
                                                    Snackbar.make(view, getString(R.string.password_send_to_email), Snackbar.LENGTH_LONG).show();
                                                    ((LogisterActivity) requireActivity()).changeFragment(ConfirmUserFragment.getConfirmUserFragment(getUserEmail()));
                                                    break;
                                                case 3:
                                                    Snackbar.make(view, getString(R.string.login_user_time_left), Snackbar.LENGTH_LONG).show();
                                                    break;
                                            }
                                        });
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
                } else {
                    Snackbar.make(view, getString(R.string.email_end_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        fragment_login_login_btn.setOnClickListener(v -> {
            textTools.hide_keyboard(requireView());
            if (getUserEmail().isEmpty()) {
                Snackbar.make(view, getString(R.string.insert_your_email), Snackbar.LENGTH_LONG).show();
            } else {
                Matcher matcher = pattern.matcher(getUserEmail());
                if (matcher.find()) {
                    if (getUserPassword().isEmpty() || getUserPassword().length() < 8) {
                        Snackbar.make(view, getString(R.string.insert_your_password), Snackbar.LENGTH_LONG).show();
                    } else {
                        if (NetworktManager.isVPNConnected(requireContext())) {
                            NetworktManager.showVpnSnackbar(requireView());
                        } else {
                            RequestBody body = new FormBody.Builder()
                                    .add("user_email", getUserEmail())
                                    .add("user_password", getUserPassword())
                                    .build();
                            UserManager.login(body, new ResponseListener() {
                                @Override
                                public void onSuccess(Response response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String result = response.body().string();
                                            JSONObject object = new JSONObject(result);
                                            int result_code = object.getInt("result_code");
//                                    String description = object.getString("description");
                                            requireActivity().runOnUiThread(() -> {
                                                switch (result_code) {
                                                    case 0:
                                                        Snackbar.make(view, getString(R.string.login_register_problem), Snackbar.LENGTH_LONG).show();
                                                        break;
                                                    case 1:
                                                        Snackbar.make(view, getString(R.string.user_not_found), Snackbar.LENGTH_LONG).show();
                                                        break;
                                                    case 2:
                                                        Snackbar.make(view, getString(R.string.password_send_to_email), Snackbar.LENGTH_SHORT).show();
                                                        ((LogisterActivity) requireActivity()).changeFragment(ConfirmUserFragment.getConfirmUserFragment(getUserEmail()));
                                                        break;
                                                    case 3:
                                                        Snackbar.make(view, getString(R.string.wrong_password), Snackbar.LENGTH_LONG).show();
                                                        break;
                                                    case 4:
                                                        Snackbar.make(view, getString(R.string.login_user_time_left), Snackbar.LENGTH_LONG).show();
                                                        break;
                                                }
                                            });
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
                } else {
                    Snackbar.make(view, getString(R.string.email_end_error), Snackbar.LENGTH_LONG).show();
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

    public static LoginFragment getLoginFragment() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            loginFragment.setArguments(new Bundle());
        }
        return loginFragment;
    }

    public String getUserEmail() {
        return Objects.requireNonNull(fragment_login_email.getText()).toString().trim();
    }

    public String getUserPassword() {
        return Objects.requireNonNull(fragment_login_password.getText()).toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backPressedCallback.remove();
    }
}
