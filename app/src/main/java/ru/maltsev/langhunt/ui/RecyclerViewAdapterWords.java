package ru.maltsev.langhunt.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import ru.maltsev.langhunt.R;

public class RecyclerViewAdapterWords extends RecyclerView.Adapter<RecyclerViewAdapterWords.MyViewHolder> {

    private Context mContext;
    private List<Card> mData;

    public RecyclerViewAdapterWords(Context mContext, List<Card> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterWords.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_word, parent, false);
        return new RecyclerViewAdapterWords.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterWords.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nativeTranslated.setText(mData.get(position).getNativeTranslated());
        holder.translated.setText(mData.get(position).getTranslated());

        holder.textToSpeech = new TextToSpeech(mContext.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int lang = holder.textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        holder.speaker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mData.get(position).getNativeTranslated();
                Bundle bundle = new Bundle();
                bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                holder.textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, bundle, null);
            }
        });

        if (mData.get(position).getImageUrl() != null) {
            Glide.with(mContext).load(mData.get(position).getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<Card> cards) {
        mData = cards;
        notifyDataSetChanged();
    }

    public Card getCard(int position) {
        return mData.get(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nativeTranslated;
        TextView translated;
        CardView cardView;
        Button speaker_btn;
        TextToSpeech textToSpeech;
        ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            nativeTranslated = itemView.findViewById(R.id.native_translated);
            translated = itemView.findViewById(R.id.translated);
            cardView = itemView.findViewById(R.id.cardview_word_id);
            speaker_btn = itemView.findViewById(R.id.speaker);
            imageView = itemView.findViewById(R.id.image_for_word);
        }
    }
}
