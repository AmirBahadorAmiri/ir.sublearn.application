package ir.sublearn.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.R;
import ir.sublearn.adapters.DictionaryWordAdapter;
import ir.sublearn.models.WordModel;
import ir.sublearn.tools.mydb.MyDB;
import ir.sublearn.views.activities.DictionaryHistoryActivity;
import ir.sublearn.views.activities.SettingsActivity;
import ir.sublearn.views.activities.StarWordsActivity;

public class DictionaryFragment extends Fragment {

    private final Pattern ENGLISH_CHARACTER = Pattern.compile("[a-zA-Z]");

    public static DictionaryFragment dictionaryFragment;
    List<WordModel> wordModelList = new ArrayList<>();
    DictionaryWordAdapter wordAdapter;
    RecyclerView fragment_dictionary_recyclerview;
    private Observable typingObservable;
    private Disposable typingDisposable;
    AppCompatImageView fragment_dictionary_settings_icon, fragment_dictionary_history_icon, fragment_dictionary_star_icon, fragment_dictionary_mic_icon;
    AppCompatEditText fragment_dictionary_search_edittext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setup(view);
    }

    private void findViews(View view) {
        fragment_dictionary_recyclerview = view.findViewById(R.id.fragment_dictionary_recyclerview);
        fragment_dictionary_settings_icon = view.findViewById(R.id.fragment_dictionary_settings_icon);
        fragment_dictionary_history_icon = view.findViewById(R.id.fragment_dictionary_history_icon);
        fragment_dictionary_star_icon = view.findViewById(R.id.fragment_dictionary_star_icon);
        fragment_dictionary_mic_icon = view.findViewById(R.id.fragment_dictionary_mic_icon);
        fragment_dictionary_search_edittext = view.findViewById(R.id.fragment_dictionary_search_edittext);
    }

    private void setup(View view) {
        fragment_dictionary_history_icon.setOnClickListener(v -> startActivity(new Intent(requireContext(), DictionaryHistoryActivity.class)));
        fragment_dictionary_settings_icon.setOnClickListener(v -> startActivity(new Intent(requireContext(), SettingsActivity.class)));
        fragment_dictionary_star_icon.setOnClickListener(v -> startActivity(new Intent(requireContext(), StarWordsActivity.class)));

        fragment_dictionary_mic_icon.setOnClickListener(n -> {
            fragment_dictionary_search_edittext.setText("");
            Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fa-IR");
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);

            startActivityForResult(voiceIntent, 1002);
        });

        wordAdapter = new DictionaryWordAdapter(wordModelList);
        fragment_dictionary_recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        fragment_dictionary_recyclerview.setAdapter(wordAdapter);
        fragment_dictionary_search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String text = Objects.requireNonNull(editable.toString());
                if (typingDisposable != null && !typingDisposable.isDisposed())
                    typingDisposable.dispose();
                typingObservable = Observable.timer(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                if (text.isEmpty()) {
                    wordModelList.clear();
                    wordAdapter.notifyDataSetChanged();
                    fragment_dictionary_mic_icon.setImageResource(R.drawable.mic_icon_nobg);
                    fragment_dictionary_mic_icon.setOnClickListener(n -> {
                        fragment_dictionary_search_edittext.setText("");
                        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                        startActivityForResult(voiceIntent, 1002);
                    });
                } else {
                    fragment_dictionary_mic_icon.setImageResource(R.drawable.ic_dismiss);
                    fragment_dictionary_mic_icon.setOnClickListener(n -> {
                        fragment_dictionary_search_edittext.setText("");
                    });
                    typingDisposable = typingObservable
                            .subscribe(onNext -> {
                                if (isENGLISH_CHARACTER(text)) {

                                    MyDB.getInstance(requireContext()).getWordDao().readEnglishWord(text)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(newData -> {
                                                if (newData != null && !newData.isEmpty()) {
                                                    addData(newData, false);
                                                } else {
                                                }
                                            }, e -> Log.d("TAG", "err : " + e.getMessage()));

                                } else {
                                    MyDB.getInstance(requireContext()).getWordDao().readPersianWord(text)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(newData -> {
                                                if (newData != null && !newData.isEmpty()) {
                                                    addData(newData, true);
                                                } else {
                                                }
                                            }, e -> Log.d("TAG", "afterTextChanged: " + e.getMessage()));
                                }
                            });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    private boolean isENGLISH_CHARACTER(String str) {
        return ENGLISH_CHARACTER.matcher(str).find();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (typingDisposable != null && !typingDisposable.isDisposed())
            typingDisposable.dispose();
    }

    private void addData(List<WordModel> newData, boolean isPersian) {
        wordAdapter.setPersian(isPersian);
        wordAdapter.loadData(newData);
        wordAdapter.notifyDataSetChanged();
        fragment_dictionary_recyclerview.scrollToPosition(0);
    }

    public static DictionaryFragment getDictionaryFragment() {
        if (dictionaryFragment == null) {
            dictionaryFragment = new DictionaryFragment();
            dictionaryFragment.setArguments(new Bundle());
        }
        return dictionaryFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> spokenSearch = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (spokenSearch != null) {
                    String grabString = spokenSearch.get(0);
                    if (Objects.requireNonNull(fragment_dictionary_search_edittext.getText()).toString().equals(""))
                        fragment_dictionary_search_edittext.setText(grabString);
                    else {
                        String str = fragment_dictionary_search_edittext.getText() + " " + grabString;
                        fragment_dictionary_search_edittext.setText(str);
                    }
                    fragment_dictionary_search_edittext.setSelection(fragment_dictionary_search_edittext.length());
                }
            }
        }
    }
}
