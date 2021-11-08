package ru.maltsev.langhunt.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.ui.activity.SetActivity;
import ru.maltsev.langhunt.ui.activity.WordActivity;

public class RecyclerViewAdapterWords extends RecyclerView.Adapter<RecyclerViewAdapterWords.MyViewHolder>{

    private Context mContext;
    private List<Word> mData;

    public RecyclerViewAdapterWords(Context mContext, List<Word> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterWords.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_word,parent,false);
        return new RecyclerViewAdapterWords.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterWords.MyViewHolder holder, int position) {
        holder.nativeTranslated.setText(mData.get(position).getNativeTranslated());
        holder.translated.setText(mData.get(position).getTranslated());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WordActivity.class);

                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<Word> words){
        mData = words;
        notifyDataSetChanged();
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView nativeTranslated;
        TextView translated;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            nativeTranslated = itemView.findViewById(R.id.native_translated);
            translated = itemView.findViewById(R.id.translated);
            cardView = itemView.findViewById(R.id.cardview_word_id);
        }
    }
}
