package ir.sublearn.application.adapters;

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
import ir.sublearn.application.models.TextModel;
import ir.sublearn.application.tools.mydb.MyDB;

public class TranslatedTextHistoryAdapter extends RecyclerView.Adapter<TranslatedTextHistoryAdapter.MainHolder> {

    List<TextModel> textModelList;

    public TranslatedTextHistoryAdapter(List<TextModel> textModelList) {
        this.textModelList = textModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_translated_text_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.text_translate_from.setText(textModelList.get(position).getText());
        holder.text_translate_to.setText(textModelList.get(position).getTranslation());
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
            popupMenu.getMenu().add(0, 1, 0, "حذف تاریخچه");
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 1:
                        MyDB.getInstance(holder.itemView.getContext()).getTextDao()
                                .delete(textModelList.get(position))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    }

                                    @Override
                                    public void onComplete() {
                                        textModelList.remove(position);
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

    @Override
    public int getItemCount() {
        return textModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView text_translate_to, text_translate_from;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            text_translate_from = itemView.findViewById(R.id.text_translate_from);
            text_translate_to = itemView.findViewById(R.id.text_translate_to);
        }
    }
}
