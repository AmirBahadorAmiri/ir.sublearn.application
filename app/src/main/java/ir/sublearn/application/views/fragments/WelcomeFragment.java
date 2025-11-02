package ir.sublearn.application.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import ir.sublearn.application.R;
import ir.sublearn.application.views.activities.LogisterActivity;

public class WelcomeFragment extends Fragment {

    public static WelcomeFragment welcomeFragment;
    MaterialButton fragment_welcome_login_user, fragment_welcome_register_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_welcome_login_user = view.findViewById(R.id.fragment_welcome_login_user);
        fragment_welcome_register_user = view.findViewById(R.id.fragment_welcome_register_user);
    }

    private void setup(View view) {
        fragment_welcome_login_user.setOnClickListener(view1 -> ((LogisterActivity) requireActivity()).changeFragment(LoginFragment.getLoginFragment()));
        fragment_welcome_register_user.setOnClickListener(view1 -> ((LogisterActivity) requireActivity()).changeFragment(RegisterFragment.getRegisterFragment()));
    }

    public static WelcomeFragment getWelcomeFragment() {
        if (welcomeFragment == null) {
            welcomeFragment = new WelcomeFragment();
            welcomeFragment.setArguments(new Bundle());
        }
        return welcomeFragment;
    }
}
