package com.hfad.tapestrypoetry4.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.tapestrypoetry4.Data.PoemData;
import com.hfad.tapestrypoetry4.R;

import java.util.List;

public class UserPoemAdapter extends RecyclerView.Adapter<UserPoemAdapter.UserpoemViewHolder> {

    private List<PoemData> mPoems;

    private ReadPoemAdapter.OnItemClickListener mListener;

    public void setOnItemClickListener(ReadPoemAdapter.OnItemClickListener listener)
    {
        mListener =listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public UserpoemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userpoems,parent,false);
        return new UserpoemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserpoemViewHolder holder, int position) {

        PoemData Upoem = mPoems.get(position);
        holder.uptitle.setText(Upoem.getTitle());
        holder.upPoemLines.setText(Upoem.getPoemLines());
        String AUTHOR = Upoem.getAuthors().get(0);
        holder.upAuthor.setText(AUTHOR);
        holder.upTime.setText(Upoem.getTime());

    }

    @Override
    public int getItemCount() {
        return mPoems.size();
    }

    public class UserpoemViewHolder extends RecyclerView.ViewHolder {

        TextView uptitle;
        TextView upPoemLines;
        TextView upAuthor;
        TextView upTime;

        public UserpoemViewHolder(@NonNull View itemView) {
            super(itemView);
            uptitle=itemView.findViewById(R.id.RPPoemTitle);
            upPoemLines=itemView.findViewById(R.id.RPPoem);
            upAuthor=itemView.findViewById(R.id.RPAuthor);
            upTime=itemView.findViewById(R.id.RPTime);
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
}
