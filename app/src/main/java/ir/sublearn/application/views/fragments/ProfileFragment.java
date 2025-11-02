package ir.sublearn.application.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import ir.sublearn.application.R;
import ir.sublearn.application.tools.packager.Packager;
import ir.sublearn.application.tools.urlchooser.OpenUrl;
import ir.sublearn.application.views.activities.ProfileActivity;
import ir.sublearn.application.views.activities.SettingsActivity;

public class ProfileFragment extends Fragment {

    public static ProfileFragment profileFragment;
    AppCompatTextView fragment_profile_negaresh_textview;
    AppCompatImageView fragment_profile_settings_icon, fragment_profile_profile_icon, fragment_profile_rubika, fragment_profile_telegram, fragment_profile_instagram;
    MaterialCardView fragment_profile_user_account_cardview, fragment_profile_settings_cardview, fragment_profile_support_cardview, fragment_profile_invite_cardview, fragment_profile_negaresh_cardview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_profile_settings_icon = view.findViewById(R.id.fragment_profile_settings_icon);
        fragment_profile_profile_icon = view.findViewById(R.id.fragment_profile_profile_icon);
        fragment_profile_settings_cardview = view.findViewById(R.id.fragment_profile_settings_cardview);
        fragment_profile_user_account_cardview = view.findViewById(R.id.fragment_profile_user_account_cardview);
        fragment_profile_support_cardview = view.findViewById(R.id.fragment_profile_support_cardview);
        fragment_profile_invite_cardview = view.findViewById(R.id.fragment_profile_invite_cardview);
        fragment_profile_negaresh_cardview = view.findViewById(R.id.fragment_profile_negaresh_cardview);
        fragment_profile_rubika = view.findViewById(R.id.fragment_profile_rubika);
        fragment_profile_telegram = view.findViewById(R.id.fragment_profile_telegram);
        fragment_profile_instagram = view.findViewById(R.id.fragment_profile_instagram);
        fragment_profile_negaresh_textview = view.findViewById(R.id.fragment_profile_negaresh_textview);
    }

    private void setup(View view) {

        fragment_profile_negaresh_textview.setText(Packager.getVersionName(requireContext()));

        fragment_profile_settings_cardview.setOnClickListener(v -> startActivity(new Intent(requireContext(), SettingsActivity.class)));

        fragment_profile_user_account_cardview.setOnClickListener(v -> startActivity(new Intent(requireContext(), ProfileActivity.class)));

        fragment_profile_support_cardview.setOnClickListener(v -> {
            String adminUsername = "https://t.me/sublearn_admin";
            OpenUrl.openUrl(requireContext(), adminUsername);
        });

        fragment_profile_invite_cardview.setOnClickListener(v -> {
        });

        fragment_profile_rubika.setOnClickListener(v -> {
            String rubikaChannel = "https://rubika.ir/SubLearn_ir";
            OpenUrl.openUrl(requireContext(), rubikaChannel);
        });

        fragment_profile_telegram.setOnClickListener(v -> {
//            String channelUsername = "tg://resolve?domain=SubLearn_ir";
            String channelUsername = "https://t.me/SubLearn_ir";
            OpenUrl.openUrl(requireContext(), channelUsername);
        });

        fragment_profile_instagram.setOnClickListener(v -> {
//            String url = "http://instagram.com/SubLearn_ir";
            String url = "http://instagram.com/_u/SubLearn_ir";
            OpenUrl.openUrl(requireContext(), url);
        });

        fragment_profile_settings_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        fragment_profile_profile_icon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        });

    }

    public static ProfileFragment getProfileFragment() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            profileFragment.setArguments(new Bundle());
        }
        return profileFragment;
    }
}
