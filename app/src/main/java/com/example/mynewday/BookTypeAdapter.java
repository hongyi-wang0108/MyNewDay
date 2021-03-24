package com.example.mynewday;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class BookTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookType> list;
    private  BookTypeViewHolder vh ;
    public BookTypeAdapter(List<BookType> list) {
        this.list = list;
    }

    public interface OnDLClickListener{
        void onDLClick(int position);
    }
    private OnDLClickListener listener;
    public void setDLOnClickListener(OnDLClickListener listener){
        this.listener = listener;
    }
    class BookTypeViewHolder extends RecyclerView.ViewHolder{
        TextView tv_item_type;
        ImageView iv_item_type;
        View v;
        public BookTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            tv_item_type = itemView.findViewById(R.id.tv_item_type);
            iv_item_type = itemView.findViewById(R.id.iv_item_type);
            itemView.setOnClickListener(new View.OnClickListener() {//先点了item
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        //它调用了onclick方法 并且传了数值给他，现在该进行这个方法具体怎么写了，在choosetype类写
                       listener.onDLClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type,parent,false);
        BookTypeViewHolder vh = new BookTypeViewHolder(view);
 /*       vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = vh.getAdapterPosition();
                String name = list.get(a).getName();
                Intent i = getIntent();
                i.putExtra("booktype",);
                setResult(RESULT_OK,i);
                finish();
            }
        });*/
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        vh = (BookTypeViewHolder) holder;
        BookType bt = list.get(position);
        vh.iv_item_type.setImageResource(bt.getImageid());
        vh.tv_item_type.setText(bt.getName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
