package ir.sublearn.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ir.sublearn.R;
import ir.sublearn.models.LangModel;
import ir.sublearn.tools.assets_image_loader.AssetsImageLoader;
import ir.sublearn.tools.country.Country;
import ir.sublearn.tools.language_manager.LanguageManager;
import ir.sublearn.views.activities.ChangeLanguageActivity;

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.MainHolder> {

    List<LangModel> langModelList;

    public SelectLanguageAdapter(List<LangModel> langModelList) {
        this.langModelList = langModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_langauge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.item_select_language_name.setText(langModelList.get(position).getShowName());
        AssetsImageLoader.load(holder.itemView.getContext(), ("country/" + langModelList.get(position).getImage()), holder.item_select_language_image);
        holder.itemView.setOnClickListener(v -> {
            if (((ChangeLanguageActivity) v.getContext()).isFrom) {
                if (langModelList.get(position).getName().equals(LanguageManager.getToLangauge(v.getContext()))) {
                    LanguageManager.reverceLanguage(v.getContext());
                } else {
                    LanguageManager.setFromLangauge(v.getContext(), position);
                }
            } else {
                if (Country.generateLanguage().get(position).getName().equals(LanguageManager.getFromLangauge(v.getContext()))) {
                    LanguageManager.reverceLanguage(v.getContext());
                } else {
                    LanguageManager.setToLangauge(v.getContext(), position);
                }
            }
            ((Activity) v.getContext()).setResult(1003);
            ((Activity) v.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        return langModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatImageView item_select_language_image;
        AppCompatTextView item_select_language_name;
        MaterialCardView item_select_language_cardview;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            item_select_language_image = itemView.findViewById(R.id.item_select_language_image);
            item_select_language_name = itemView.findViewById(R.id.item_select_language_name);
            item_select_language_cardview = itemView.findViewById(R.id.item_select_language_cardview);
        }
    }
}
