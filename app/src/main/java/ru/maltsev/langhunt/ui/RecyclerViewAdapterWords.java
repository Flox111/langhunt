package ru.maltsev.langhunt.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.maltsev.langhunt.R;

public class RecyclerViewAdapterWords extends RecyclerView.Adapter<RecyclerViewAdapterWords.MyViewHolder>{

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
        view = mInflater.inflate(R.layout.cardview_item_word,parent,false);
        return new RecyclerViewAdapterWords.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterWords.MyViewHolder holder, int position) {
        holder.nativeTranslated.setText(mData.get(position).getNativeTranslated());
        holder.translated.setText(mData.get(position).getTranslated());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<Card> cards){
        mData = cards;
        notifyDataSetChanged();
    }

    public Card getCard(int position){
        return mData.get(position);
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
