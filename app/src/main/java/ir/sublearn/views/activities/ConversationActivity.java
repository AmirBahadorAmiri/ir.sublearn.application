package ir.sublearn.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.sublearn.R;
import ir.sublearn.adapters.ConversationAdapter;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.ConversationModel;
import ir.sublearn.tools.language_manager.LanguageManager;
import ir.sublearn.tools.network_manager.NetworktManager;
import ir.sublearn.tools.translate_manager.TranslateManager;
import okhttp3.Response;

public class ConversationActivity extends BaseActivity {

    AppCompatImageView activity_conversation_back_btn, activity_conversation_settings_btn, activity_conversation_mic_btn, activity_conversation_reverse_btn, activity_conversation_send_from, activity_conversation_send_to;
    AppCompatEditText activity_conversation_edittext;
    MaterialButton activity_conversation_from_btn, activity_conversation_to_btn;
    RecyclerView activity_conversation_recyclerview;
    List<ConversationModel> conversationModelList = new ArrayList<>();
    ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeEnable();
        setContentView(R.layout.activity_conversation);
        setViewCompat();
        findViews();
        setup();
    }

    private void findViews() {
        activity_conversation_back_btn = findViewById(R.id.activity_conversation_back_btn);
        activity_conversation_settings_btn = findViewById(R.id.activity_conversation_settings_btn);
        activity_conversation_mic_btn = findViewById(R.id.activity_conversation_mic_btn);
        activity_conversation_reverse_btn = findViewById(R.id.activity_conversation_reverse_btn);
        activity_conversation_send_from = findViewById(R.id.activity_conversation_send_from);
        activity_conversation_send_to = findViewById(R.id.activity_conversation_send_to);
        activity_conversation_edittext = findViewById(R.id.activity_conversation_edittext);
        activity_conversation_from_btn = findViewById(R.id.activity_conversation_from_btn);
        activity_conversation_to_btn = findViewById(R.id.activity_conversation_to_btn);
        activity_conversation_recyclerview = findViewById(R.id.activity_conversation_recyclerview);
    }

    private void setup() {
        activity_conversation_back_btn.setOnClickListener(v -> finish());

        activity_conversation_settings_btn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        activity_conversation_reverse_btn.setOnClickListener(v -> {
            LanguageManager.reverceLanguage(this);
            getLanguages();
        });

        activity_conversation_mic_btn.setOnClickListener(v -> {
            Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LanguageManager.getFromLangaugeCode(this));
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fa-IR");
//                voiceIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);

            startActivityForResult(voiceIntent, 1002);
        });

        activity_conversation_to_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            intent.putExtra("isFrom", false);
            startActivityForResult(intent, 1003);
        });
        activity_conversation_from_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            intent.putExtra("isFrom", true);
            startActivityForResult(intent, 1003);
//            can be change request code to 1004 for change language
        });

        activity_conversation_send_from.setOnClickListener(v -> {

            String text = Objects.requireNonNull(activity_conversation_edittext.getText()).toString();
            String from = LanguageManager.getFromLangaugeCode(this);
            String to = LanguageManager.getToLangaugeCode(this);

            conversationModelList.add(new ConversationModel(text, from, 1));
            conversationAdapter.notifyItemInserted((conversationModelList.size() - 1));
            activity_conversation_recyclerview.scrollToPosition((conversationModelList.size() - 1));

            translate(text, from, to);

        });
        activity_conversation_send_to.setOnClickListener(v -> {

            String text = Objects.requireNonNull(activity_conversation_edittext.getText()).toString();
            String from = LanguageManager.getToLangaugeCode(this);
            String to = LanguageManager.getFromLangaugeCode(this);

            conversationModelList.add(new ConversationModel(text, from, 1));
            conversationAdapter.notifyItemInserted((conversationModelList.size() - 1));
            activity_conversation_recyclerview.scrollToPosition((conversationModelList.size() - 1));

            translate(text, from, to);

        });

        conversationAdapter = new ConversationAdapter(conversationModelList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true); // ترتیب معکوس
        layoutManager.setStackFromEnd(true); // شروع از انتها
        activity_conversation_recyclerview.setLayoutManager(layoutManager);
        activity_conversation_recyclerview.setAdapter(conversationAdapter);
    }

    private void translate(String text, String fromLanguage, String toLanguage) {

        activity_conversation_edittext.setText("");
        if (NetworktManager.isVPNConnected(this)) {
            NetworktManager.showVpnSnackbar(getWindow().getDecorView());
        } else {
            if (!text.isEmpty()) {
                googleTranslate(text, fromLanguage, toLanguage);
            }
        }
//        if (NetworktManager.isVpnConnected() ) {
//            Snackbar.make(send_speakLogo, "لطفا vpn خود را غیرفعال نمایید", Snackbar.LENGTH_LONG)
//                    .setTextColor(getResources().getColor(R.color.white)).show();
//        } else {
//                if ( User.getUserStatus() == StaticDatas.USER_STATUS_GOLDEN) {
//                    googleTranslate(text);
//                } else {

//        if (!Objects.requireNonNull(translate_edittext.getText()).toString().isEmpty()) {
//            if (User.userCanUseApp()) {
//                googleTranslate();
//            } else {
//                DialogManager.showAcceptableQuizDialog(requireContext(), true, "برای استفاده از این بخش باید تبلیغات را تماشا کنید، آیا مایل هستید؟", new DefaultListener() {
//                    @Override
//                    public void onSuccess(Object obj) {
//                        AdManager.requestAd(requireActivity(), new DefaultListener() {
//                            @Override
//                            public void onSuccess(Object obj) {
//                                AdManager.showAd(requireActivity(), new AdShowListener() {
//                                    @Override
//                                    public void onRewarded(TapsellPlusAdModel tapsellPlusAdModel) {
//                                        User.setBuyTime(System.currentTimeMillis());
//                                        Toast.makeText(requireContext(), "شما میتوانید به مدت 30 دقیقه از سرویس مترجم استفاده نمایید", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onFailure(Object obj) {
//                                Toast.makeText(requireContext(), "متاسفانه درخواست شما با مشکل مواجه شد", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            }
//        }

//            }
//        }
    }

    private void googleTranslate(String text, String fromLanguage, String toLanguage) {

        ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String body = Objects.requireNonNull(response.body()).string();

                        JSONArray array = new JSONArray(body);
                        JSONArray array_0 = array.getJSONArray(0);
                        StringBuilder translatedText = new StringBuilder();
                        for (int x = 0; x < array_0.length(); x++) {
                            JSONArray dynamic_object = array_0.getJSONArray(x);
                            if (!dynamic_object.isNull(0)) {
                                translatedText.append(dynamic_object.getString(0));
                            }
                        }
                        runOnUiThread(() -> {
                            conversationModelList.add(new ConversationModel(translatedText.toString(), toLanguage, 2));
                            conversationAdapter.notifyItemInserted((conversationModelList.size() - 1));
                            activity_conversation_recyclerview.scrollToPosition((conversationModelList.size() - 1));
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
//                fragment_translate_scrollview.setVisibility(View.INVISIBLE);
//                fragment_translate_copy_btn.setVisibility(View.INVISIBLE);
//                share_text.setVisibility(View.INVISIBLE);
                Toast.makeText(ConversationActivity.this, "متاسفانه مشکلی در برقراری ارتباط پیش آمد", Toast.LENGTH_SHORT).show();
            }
        };

        TranslateManager.translateByGoogle(this, text, fromLanguage, toLanguage, responseListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        getLanguages();
    }

    private void getLanguages() {
        activity_conversation_from_btn.setText(LanguageManager.getFromLangauge(this));
        activity_conversation_to_btn.setText(LanguageManager.getToLangauge(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> spokenSearch = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (spokenSearch != null) {
                    String grabString = spokenSearch.get(0);
                    if (Objects.requireNonNull(activity_conversation_edittext.getText()).toString().equals(""))
                        activity_conversation_edittext.setText(grabString);
                    else {
                        String str = activity_conversation_edittext.getText() + " " + grabString;
                        activity_conversation_edittext.setText(str);
                    }
                    activity_conversation_edittext.setSelection(activity_conversation_edittext.length());
                }
            }
        } else if (requestCode == 1003 && resultCode == 1003) {
            getLanguages();
        }
    }
}