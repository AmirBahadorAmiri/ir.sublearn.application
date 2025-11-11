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

public class RegisterFragment extends Fragment {

    public static RegisterFragment registerFragment;
    AppCompatImageView fragment_register_back_btn;
    private OnBackPressedCallback backPressedCallback;
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@gmail+\\.com");
    TextTools textTools;
    MaterialButton fragment_register_register_btn;
    AppCompatEditText fragment_register_namefamily, fragment_register_email;
    TextInputEditText fragment_register_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_register_back_btn = view.findViewById(R.id.fragment_register_back_btn);
        fragment_register_register_btn = view.findViewById(R.id.fragment_register_register_btn);
        fragment_register_namefamily = view.findViewById(R.id.fragment_register_namefamily);
        fragment_register_email = view.findViewById(R.id.fragment_register_email);
        fragment_register_password = view.findViewById(R.id.fragment_register_password);
    }

    private void setup(View view) {
        textTools = new TextTools().inject(requireContext());
        fragment_register_back_btn.setOnClickListener(view1 -> ((LogisterActivity) requireActivity()).changeFragment(WelcomeFragment.getWelcomeFragment()));

        fragment_register_register_btn.setOnClickListener(v -> {
            textTools.hide_keyboard(v);
            Matcher matcher = pattern.matcher(getUserEmail());
            if (matcher.find()) {
                if (getUserPassword().isEmpty() || getUserPassword().length() < 8) {
                    Snackbar.make(v, getString(R.string.insert_your_password), Snackbar.LENGTH_LONG).show();
                } else if (getUserName().isEmpty() || getUserName().length() < 3) {
                    Snackbar.make(v, getString(R.string.insert_your_password), Snackbar.LENGTH_LONG).show();
                } else {
                    if (NetworktManager.isVPNConnected(requireContext())) {
                        NetworktManager.showVpnSnackbar(requireView());
                    } else {
                        RequestBody body = new FormBody.Builder()
                                .add("user_name", getUserName())
                                .add("user_email", getUserEmail())
                                .add("user_password", getUserPassword())
                                .build();

                        UserManager.register(body, new ResponseListener() {
                            @Override
                            public void onSuccess(Response response) {
                                try {
                                    String result = response.body().string();
                                    JSONObject object = new JSONObject(result);
                                    int result_code = object.getInt("result_code");
                                    switch (result_code) {
                                        case 0:
                                            Snackbar.make(v, getString(R.string.login_register_problem), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 1:
                                            Snackbar.make(v, getString(R.string.login_register_problem), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 2:
                                            Snackbar.make(v, getString(R.string.create_user_problem), Snackbar.LENGTH_LONG).show();
                                            break;
                                        case 3:
                                            Snackbar.make(v, getString(R.string.password_send_to_email), Snackbar.LENGTH_SHORT).show();
                                            ((LogisterActivity) requireActivity()).changeFragment(ConfirmUserFragment.getConfirmUserFragment(getUserEmail()));
                                            break;
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
            } else {
                Snackbar.make(v, getString(R.string.email_end_error), Snackbar.LENGTH_LONG).show();
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

    public String getUserEmail() {
        return Objects.requireNonNull(fragment_register_email.getText()).toString().trim();
    }

    public String getUserName() {
        return Objects.requireNonNull(fragment_register_namefamily.getText()).toString().trim();
    }

    public String getUserPassword() {
        return Objects.requireNonNull(fragment_register_password.getText()).toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backPressedCallback.remove();
    }

    public static RegisterFragment getRegisterFragment() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
            registerFragment.setArguments(new Bundle());
        }
        return registerFragment;
    }
}
