package ir.sublearn.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sublearn.R;
import ir.sublearn.models.WordModel;
import ir.sublearn.views.activities.ShowWordActivity;

public class DictionaryWordAdapter extends RecyclerView.Adapter<DictionaryWordAdapter.MainHolder> {

    List<WordModel> wordModels;
    private boolean isPersian;

    public DictionaryWordAdapter(List<WordModel> wordModels) {
        this.wordModels = wordModels;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.onBind(holder, position);
    }

    @Override
    public int getItemCount() {
        return wordModels.size();
    }

    public void setPersian(boolean persian) {
        isPersian = persian;
    }

    public void loadData(List<WordModel> newWordModels) {
        wordModels.clear();
        wordModels.addAll(newWordModels);
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView dictionary_word_item_from, dictionary_word_item_to;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            dictionary_word_item_from = itemView.findViewById(R.id.dictionary_word_item_from);
            dictionary_word_item_to = itemView.findViewById(R.id.dictionary_word_item_to);
        }

        public void onBind(MainHolder holder, int position) {
            if (isPersian) {
                holder.dictionary_word_item_from.setText(wordModels.get(position).getPersianWord());
                holder.dictionary_word_item_to.setText(wordModels.get(position).getEnglishWord());
            } else {
                holder.dictionary_word_item_from.setText(wordModels.get(position).getEnglishWord());
                holder.dictionary_word_item_to.setText(wordModels.get(position).getPersianWord());
            }

            holder.itemView.setOnClickListener(n -> {
                Intent intent = new Intent(holder.itemView.getContext(), ShowWordActivity.class);
                intent.putExtra("word_id", (wordModels.get(position).getId()));
                holder.itemView.getContext().startActivity(intent);
            });
        }

    }

}
