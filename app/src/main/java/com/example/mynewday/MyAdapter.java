package com.example.mynewday;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Day> mlist;
    private MyViewHolder vh;
    public MyAdapter(List<Day> mlist) {
        this.mlist = mlist;
    }
    public static int realday;
    public List < Day > getData() {
        return mlist;
    }

    public void setData(List < Day > data) {
        mlist = data;
    }
/*        iv_mind.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });*/
    public interface OnRVClickListener{
        void onRVClick(String titletext,int position);
    }
    private OnRVClickListener listener;
    public void setRVOnClickListener(OnRVClickListener listener){
        this.listener = listener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title_item;
        TextView tv_date_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_item = itemView.findViewById(R.id.tv_title_item);
            tv_date_item = itemView.findViewById(R.id.tv_date_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // listener.onRVClick(getAdapterPosition());
                    listener.onRVClick(tv_title_item.getText().toString(),
                            getAdapterPosition());
                }
            });
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        vh = (MyViewHolder) holder;
        Day day = mlist.get(position);
        String title = day.getTitle();
        String date = day.getDate();
        realday = 0;
        try {
            realday = caculateDay(date);
            day.setDatediffer(realday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(realday > 0){
            vh.tv_title_item.setText(title + "已经");
            vh.tv_date_item.setText(realday+"");
            day.setDatediffer(realday);
        }else {
            vh.tv_title_item.setText(title + "还有");
            vh.tv_date_item.setText((0-realday)+"");
            day.setDatediffer(0-realday);
        }

    }
    
    public static int caculateDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date before = sdf.parse(date);
        Long datemills = before.getTime();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Long nowmills = now.getTime();
        realday = (int) ((nowmills - datemills) / (1000 * 60 * 60 * 24 ));
        Log.d("TAG", "caculateDay: " + realday);
        return realday;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
