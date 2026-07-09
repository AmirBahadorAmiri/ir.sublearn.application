package ir.sublearn.adapters;

import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ir.sublearn.R;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.models.ConversationModel;
import ir.sublearn.tools.copy_helper.CopyHelper;
import ir.sublearn.tools.speecher.Speecher;
import ir.sublearn.tools.volume_manager.VolumeManager;
import okhttp3.Response;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MainHolder> {

    List<ConversationModel> conversationModelList;

    public ConversationAdapter(List<ConversationModel> conversationModelList) {
        this.conversationModelList = conversationModelList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_from, parent, false));
        else
            return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_to, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemViewType(int position) {
        return conversationModelList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return conversationModelList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        AppCompatTextView conversation_item_text;
        AppCompatImageView conversation_item_copy, conversation_item_speaker;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            conversation_item_text = itemView.findViewById(R.id.conversation_item_text);
            conversation_item_copy = itemView.findViewById(R.id.conversation_item_copy);
            conversation_item_speaker = itemView.findViewById(R.id.conversation_item_speaker);
        }

        public void bindView(int position) {
            conversation_item_text.setText(conversationModelList.get(position).getText());
            conversation_item_copy.setOnClickListener(v -> {
                CopyHelper.initialize(itemView.getContext());
                CopyHelper.insert(conversationModelList.get(position).getText());
            });
            conversation_item_speaker.setOnClickListener(v -> {
                if (VolumeManager.getVolume(itemView.getContext()) == 0) {
                    Snackbar.make(v, "صدای سیستم قطع است", Snackbar.LENGTH_LONG)
                            .setAction("افزایش صدا", n -> VolumeManager.setVolume(itemView.getContext(), VolumeManager.getManager(itemView.getContext()).getStreamMaxVolume(AudioManager.STREAM_MUSIC))).setActionTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blueColor)).show();
                } else {
                    Speecher.initialize(itemView.getContext(), new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            if (Speecher.isSupportLanguage(itemView.getContext(), conversationModelList.get(position).getLanguageCode())) {
                                Speecher.speak(itemView.getContext(), conversationModelList.get(position).getText());
                            } else {
                                Toast.makeText(itemView.getContext(), "بسته نرم افزار صوتی این زبان نصب نشده است", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.d("TAG", "tts manager failure");
                        }
                    });
                }
            });
        }
    }
}
