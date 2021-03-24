package com.example.mynewday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back_ha,iv_glide_ha;
    private TextView tv_history_ha;
    private List<HistoryList> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        iv_back_ha = findViewById(R.id.iv_back_ha);
        tv_history_ha = findViewById(R.id.tv_history_ha);
        iv_glide_ha = findViewById(R.id.iv_glide_ha);

        iv_back_ha.setOnClickListener(this);
        tv_history_ha.setOnClickListener(this);
        Glide.with(this).asGif()
                .load("http://5b0988e595225.cdn.sohucs.com/images/20180930/03872426e7344db8886643b69c1a2bd2.gif")
                .into(iv_glide_ha);
        getListFromOKhttp();


    }

    private void showHistoryList() {
        StringBuilder sb = new StringBuilder();
        for (HistoryList h : list) {
            String title = h.getTitle();
            String year = h.getYear();
            sb.append("      "+year + "年        " + title +"\n");
        }
        tv_history_ha.setText(sb.toString());
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    showHistoryList();
                    break;
            }

        }
    };


    private void getListFromOKhttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //  .url("https://2.sjapi.applinzi.com/")
                            .url("https://www.ipip5.com/today/api.php?type=json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    System.out.println(data);
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length() ;i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String year = object.getString("year");

                        String title = object.getString("title");

                        list.add(new HistoryList(year,title));
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    //message.obj = list;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_history_ha:

                break;
            case R.id.iv_back_ha:
                finish();
                break;
            default:
                break;
        }
    }
}