package ir.sublearn.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.sublearn.R;
import ir.sublearn.views.fragments.WelcomeFragment;

public class LogisterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_logister);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
    }

    private void setup() {
        changeFragment(WelcomeFragment.getWelcomeFragment());
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.commit();
    }

}
