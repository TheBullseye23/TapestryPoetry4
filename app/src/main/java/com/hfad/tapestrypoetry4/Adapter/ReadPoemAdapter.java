package com.hfad.tapestrypoetry4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.tapestrypoetry4.Data.PoemData;
import com.hfad.tapestrypoetry4.EditPoem;
import com.hfad.tapestrypoetry4.R;

import java.util.ArrayList;
import java.util.List;

public class ReadPoemAdapter extends RecyclerView.Adapter<ReadPoemAdapter.ReadEditViewHolder> {


    private List<PoemData> mPoems;
    private Context context;

    private  OnItemClickListener mListener;


    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener =listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public ReadPoemAdapter(List<PoemData> mPoems) {
        this.mPoems = mPoems;
    }

    public ReadPoemAdapter (List<PoemData> mPoems, Context context) {
        this.mPoems = mPoems;
        this.context = context;
    }

    @NonNull
    @Override
    public ReadEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_readandedit,parent,false);
        return new ReadEditViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ReadEditViewHolder holder, final int position) {

        PoemData POEM = mPoems.get(position);
        holder.title.setText(POEM.getTitle());
        holder.poemlines.setText(POEM.getPoemLines());
        String AUTHOR = POEM.getAuthors().get(0);
        holder.author.setText(AUTHOR);
        holder.time.setText(POEM.getTime());

    }

    @Override
    public int getItemCount() {
        return mPoems.size();
    }

    public class ReadEditViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView poemlines;
        TextView author;
        TextView time;
        public ReadEditViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.RPPoemTitle);
            poemlines=itemView.findViewById(R.id.RPPoem);
            author=itemView.findViewById(R.id.RPAuthor);
            time=itemView.findViewById(R.id.RPTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("onclick","Onclick was triggered");
                    if(mListener!=null){
                        Log.d("onclick","Onclick was triggered");
                        int position =getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public void updateList(List<PoemData> newList)
    {
        mPoems = new ArrayList<>();
        mPoems.addAll(newList);
        notifyDataSetChanged();
    }
}
