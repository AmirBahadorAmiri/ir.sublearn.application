package ir.sublearn.application.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import ir.sublearn.application.R;
import ir.sublearn.application.views.activities.SongsExplorerActivity;

public class LearnFragment extends Fragment {

    public static LearnFragment learnFragment;
    MaterialCardView fragment_learn_grammer_cardview, fragment_learn_library_cardview, fragment_learn_musics_cardview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_learn_grammer_cardview = view.findViewById(R.id.fragment_learn_grammer_cardview);
        fragment_learn_library_cardview = view.findViewById(R.id.fragment_learn_library_cardview);
        fragment_learn_musics_cardview = view.findViewById(R.id.fragment_learn_musics_cardview);
    }

    private void setup(View view) {
        fragment_learn_grammer_cardview.setOnClickListener(v->{
            Toast.makeText(requireContext(), "این بخش بزودی فعال میگردد", Toast.LENGTH_SHORT).show();
        });
        fragment_learn_library_cardview.setOnClickListener(v->{
            Toast.makeText(requireContext(), "این بخش بزودی فعال میگردد", Toast.LENGTH_SHORT).show();
        });
        fragment_learn_musics_cardview.setOnClickListener(v->{
            startActivity(new Intent(requireContext(), SongsExplorerActivity.class));
        });
    }

    public static LearnFragment getLearnFragment() {
        if (learnFragment == null) {
            learnFragment = new LearnFragment();
            learnFragment.setArguments(new Bundle());
        }
        return learnFragment;
    }
}
