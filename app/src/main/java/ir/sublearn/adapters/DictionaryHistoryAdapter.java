package ir.sublearn.adapters;

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
import ir.sublearn.R;
import ir.sublearn.models.WordModel;
import ir.sublearn.tools.mydb.MyDB;
import ir.sublearn.views.activities.ShowWordActivity;

public class DictionaryHistoryAdapter extends RecyclerView.Adapter<DictionaryHistoryAdapter.MainHolder> {

    List<WordModel> wordModelList;

    public DictionaryHistoryAdapter(List<WordModel> wordModelList) {
        this.wordModelList = wordModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {
        return wordModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView dictionary_word_item_from, dictionary_word_item_to;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        public void findViews(View itemView) {
            dictionary_word_item_from = itemView.findViewById(R.id.dictionary_word_item_from);
            dictionary_word_item_to = itemView.findViewById(R.id.dictionary_word_item_to);
        }

        public void bindHolder(int position) {

            dictionary_word_item_from.setText(wordModelList.get(position).getEnglishWord());
            dictionary_word_item_to.setText(wordModelList.get(position).getPersianWord());

            itemView.setOnClickListener(n -> {
                Intent intent = new Intent(itemView.getContext(), ShowWordActivity.class);
                intent.putExtra("word_id", (wordModelList.get(position).getId()));
                itemView.getContext().startActivity(intent);
            });

            itemView.setOnLongClickListener(v -> {

                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView);
                popupMenu.getMenu().add(0, 1, 0, "حذف تاریخچه");
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case 1:
                            MyDB.getInstance(itemView.getContext()).getWordDao()
                                    .clearFromHistories(wordModelList.get(position).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            wordModelList.remove(position);
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
