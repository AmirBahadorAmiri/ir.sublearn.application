package ir.sublearn.application.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.application.R;
import ir.sublearn.application.models.WordModel;
import ir.sublearn.application.tools.mydb.MyDB;
import ir.sublearn.application.views.activities.ShowWordActivity;

public class StarredWordAdapter extends RecyclerView.Adapter<StarredWordAdapter.MainHolder> {

    List<WordModel> wordModels;
    private boolean isPersian;

    public StarredWordAdapter(List<WordModel> wordModels) {
        this.wordModels = wordModels;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.onBind(position);
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

        public void onBind(int position) {
            if (isPersian) {
                dictionary_word_item_from.setText(wordModels.get(position).getPersianWord());
                dictionary_word_item_to.setText(wordModels.get(position).getEnglishWord());
            } else {
                dictionary_word_item_from.setText(wordModels.get(position).getEnglishWord());
                dictionary_word_item_to.setText(wordModels.get(position).getPersianWord());
            }

            itemView.setOnClickListener(n -> {
                Intent intent = new Intent(itemView.getContext(), ShowWordActivity.class);
                intent.putExtra("word_id", (wordModels.get(position).getId()));
                itemView.getContext().startActivity(intent);
            });

            itemView.setOnLongClickListener(v -> {

                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView);
                popupMenu.getMenu().add(0, 1, 0, "حذف کلمه");
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case 1:
                            MyDB.getInstance(itemView.getContext()).getWordDao()
                                    .clearFromFavorites(wordModels.get(position).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            wordModels.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, getItemCount());
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                        }
                                    });
                            break;
                    }
                    return false;
                });
                popupMenu.show();

                return false;
            });

        }

    }

}
