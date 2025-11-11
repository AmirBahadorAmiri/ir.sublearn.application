package ir.sublearn.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ir.sublearn.R;
import ir.sublearn.adapters.SelectLanguageAdapter;
import ir.sublearn.tools.country.Country;

public class ChangeLanguageActivity extends BaseActivity {

    AppCompatImageView activity_change_language_back_btn;
    RecyclerView activity_change_language_recyclerview;
    SelectLanguageAdapter selectLanguageAdapter;
    public boolean isFrom = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_change_language);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_change_language_back_btn = findViewById(R.id.activity_change_language_back_btn);
        activity_change_language_recyclerview = findViewById(R.id.activity_change_language_recyclerview);
    }

    private void setup() {
        activity_change_language_back_btn.setOnClickListener(v -> finish());

        isFrom = Objects.requireNonNull(getIntent().getExtras()).getBoolean("isFrom");

        selectLanguageAdapter = new SelectLanguageAdapter(Country.generateLanguage());
        activity_change_language_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_change_language_recyclerview.setAdapter(selectLanguageAdapter);

    }

}
