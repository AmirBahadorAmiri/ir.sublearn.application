package ir.sublearn.application.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import ir.sublearn.application.R;
import ir.sublearn.application.views.fragments.DictionaryFragment;
import ir.sublearn.application.views.fragments.HomeFragment;
import ir.sublearn.application.views.fragments.LearnFragment;
import ir.sublearn.application.views.fragments.ProfileFragment;
import ir.sublearn.application.views.fragments.TranslateFragment;

public class MainActivity extends BaseActivity {

    ChipNavigationBar main_activity_bottom_navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        main_activity_bottom_navigation = findViewById(R.id.main_activity_bottom_navigation);
    }

    private void setup() {
        setupBottomNavigation();
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_framelayout, fragment).commit();
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_framelayout, fragment).addToBackStack(null).commit();
    }

    public void setupBottomNavigation() {

        main_activity_bottom_navigation.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                changeFragment(HomeFragment.getHomeFragment());
            }
            if (id == R.id.dictionary) {
                changeFragment(DictionaryFragment.getDictionaryFragment());
            }
            if (id == R.id.translate) {
                changeFragment(TranslateFragment.getTranslateFragment());
            }
            if (id == R.id.learn) {
                changeFragment(LearnFragment.getLearnFragment());
            }
            if (id == R.id.profile) {
                changeFragment(ProfileFragment.getProfileFragment());
            }
        });
        main_activity_bottom_navigation.setItemSelected(R.id.home,true);

    }

}
