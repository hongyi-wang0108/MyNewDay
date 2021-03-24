package com.example.mynewday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewday.datepicker.CustomDatePicker;
import com.example.mynewday.datepicker.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEvents extends AppCompatActivity implements View.OnClickListener {
    private static final int GO_CHOOSE_TYPE = 1;
    public static final int CHOOSE_TYPE_RETURN = 2;
    private ImageView iv_back;
    private EditText et_add_title;
    private Day day = new Day();
    private LinearLayout ll_date_choose,ll_type_choose;
    private Button tbtn_choose_gregorian_solar,btn_save;
    private TextView tv_pick_time,tv_pick_done_type;
    private CustomDatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        iv_back = findViewById(R.id.iv_back);
        btn_save = findViewById(R.id.btn_save);
        tv_pick_done_type = findViewById(R.id.tv_pick_done_type);
        tbtn_choose_gregorian_solar = findViewById(R.id.tbtn_choose_gregorian_solar);//ischecked
        tv_pick_time = findViewById(R.id.tv_pick_time);
        et_add_title = findViewById(R.id.et_add_title);
        ll_date_choose = findViewById(R.id.ll_date_choose);
        ll_type_choose = findViewById(R.id.ll_type_choose);


        iv_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        ll_date_choose.setOnClickListener(this);
        tbtn_choose_gregorian_solar.setOnClickListener(this);
        ll_type_choose.setOnClickListener(this);

        try {//日历
            tv_pick_time.setText(DateFormatUtils.long2Str(System.currentTimeMillis(),false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDatePicker();//日历

        //getDay();
        //button实现intent
        //Intent
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("0000-01-01", false);
        long currentTimestamp = System.currentTimeMillis();
        long endTimestamp = DateFormatUtils.str2Long("9999-12-31", false);

        //long 变成 date
        tv_pick_time.setText(DateFormatUtils.long2Str(currentTimestamp, false));

        // 通过时间戳 初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_pick_time.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(true);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 允许循环滚动 12完了是1
        mDatePicker.setScrollLoop(true);
        // 不允许滚动动画  滚动动画就是可以连环滚动
        mDatePicker.setCanShowAnim(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_date_choose://show date picker
                mDatePicker.show(tv_pick_time.getText().toString());
                Toast.makeText(this,"click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tbtn_choose_gregorian_solar://change date type
                if(tbtn_choose_gregorian_solar.isClickable()) {//after click 阴历

                }else{//公历

                }
                break;
            case R.id.ll_type_choose:
                Intent i = new Intent(this,ChooseType.class);
                startActivityForResult(i,GO_CHOOSE_TYPE);
                break;
            case R.id.btn_save:
                //getDay();
                i = getIntent();
                Bundle value = new Bundle();
                value.putString("title",(et_add_title.getText().toString()));
                value.putString("date",tv_pick_time.getText().toString());
                value.putString("type",(tv_pick_done_type.getText().toString()));
                value.putInt("imageid",R.drawable.back);
                i.putExtra("newEvent",value);
                setResult(MainActivity.GO_ADD_EVETNTS_RETURN,i);
                finish();
            default:
                break;
        }
    }
    private void getDay() {
        //1. editText 的title
        day.setTitle(et_add_title.getText().toString());
        Log.d("AddEvents", "onCreate: "+et_add_title.getText().toString());
        //2. 得到date
        day.setDate(tv_pick_time.getText().toString());
        //3.得到type
        day.setType(tv_pick_done_type.getText().toString());
        //4. imageid
        day.setImageid(R.drawable.back);
        //return  day;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GO_CHOOSE_TYPE:
                if(resultCode == CHOOSE_TYPE_RETURN){
                    Log.d("addeventsaddevents", "onActivityResult: "+data.getStringExtra("booktype"));
                    tv_pick_done_type.setText(data.getStringExtra("booktype"));
                  //  type = data.getStringExtra("booktype");
                }
                break;
            default:
                break;
        }
    }
}