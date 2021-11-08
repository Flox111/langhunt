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

public class RecyclerViewAdapterSets extends RecyclerView.Adapter<RecyclerViewAdapterSets.MyViewHolder> {

    private Context mContext;
    private List<SetWords> mData;

    public RecyclerViewAdapterSets(Context mContext, List<SetWords> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_set,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(mData.get(position).getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SetActivity.class);

                intent.putExtra("Title", mData.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("SetId", mData.get(holder.getAdapterPosition()).getSetId());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<SetWords> sets){
        mData = sets;
        notifyDataSetChanged();
    }

    public SetWords getSet(int position){
        return mData.get(position);
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView textView;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.set_title_id);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
