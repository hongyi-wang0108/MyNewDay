package com.example.mynewday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynewday.datepicker.CustomDatePicker;
import com.example.mynewday.datepicker.DateFormatUtils;

import java.text.ParseException;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int EE_GO_TO_CHOOSE_TYPE = 24;
    public static final int GO_EDIT_EVENT_RETURN = 123;
    private EditText et_edit_title;
    private ImageView iv_back_ee;
    private LinearLayout ll_date_choose_ee,ll_type_choose_ee;
    private TextView tv_pick_time_ee,tv_pick_done_type_ee;
    private Button btn_delete_ee,btn_save_ee;
    private CustomDatePicker cdp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        et_edit_title = findViewById(R.id.et_edit_title);
        iv_back_ee = findViewById(R.id.iv_back_ee);
        ll_date_choose_ee = findViewById(R.id.ll_date_choose_ee);
        ll_type_choose_ee = findViewById(R.id.ll_type_choose_ee);
        tv_pick_time_ee = findViewById(R.id.tv_pick_time_ee);
        tv_pick_done_type_ee = findViewById(R.id.tv_pick_done_type_ee);
        btn_delete_ee = findViewById(R.id.btn_delete_ee);
        btn_save_ee = findViewById(R.id.btn_save_ee);

        iv_back_ee.setOnClickListener(this);
        ll_date_choose_ee.setOnClickListener(this);
        ll_type_choose_ee.setOnClickListener(this);
        btn_delete_ee.setOnClickListener(this);
        btn_save_ee.setOnClickListener(this);

        //得到上一个传来的数据,并且把设置text
        getDayDetails();
    }
    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("0000-01-01", false);
        long currentTimestamp = System.currentTimeMillis();
        long endTimestamp = DateFormatUtils.str2Long("9999-12-31", false);

        //long 变成 date
       // tv_pick_time_ee.setText(DateFormatUtils.long2Str(currentTimestamp, false));

        // 通过时间戳 初始化日期，毫秒级别
        cdp = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_pick_time_ee.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 允许点击屏幕或物理返回键关闭
        cdp.setCancelable(true);
        // 不显示时和分
        cdp.setCanShowPreciseTime(false);
        // 允许循环滚动 12完了是1
        cdp.setScrollLoop(true);
        // 不允许滚动动画  滚动动画就是可以连环滚动
        cdp.setCanShowAnim(true);
    }

    private Day eeday;

    private String date;
    private String title;
    private String type;
    private int differ;

    private void getDayDetails() {//
        Intent i = getIntent();
        Bundle bundle = new Bundle();
        bundle = i.getBundleExtra("R.id.iv_update");
        title = bundle.getString("iv_update_title");
        date = bundle.getString("iv_update_date");
        type = bundle.getString("iv_update_type");
        differ = bundle.getInt("iv_update_diff",0);

        eeday = new Day(title,date,type,R.drawable.back);
        et_edit_title.setHint(title);
        tv_pick_done_type_ee.setText(type);
        Log.d("TAG", "getDayDetails: "+eeday.toString() + title+date+type);

        //日历
        tv_pick_time_ee.setText(date);
        Log.d("woshini", "onCreate11111: " + date);
        initDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_ee:
                finish();
                break;
            case R.id.btn_delete_ee:
                finish();
                break;
            case R.id.ll_date_choose_ee:
                cdp.show(date);
                break;
            case R.id.btn_save_ee:

                //getDay();
                Intent i = getIntent();
                Bundle value = new Bundle();
                boolean sa = et_edit_title.getText().toString().equals("");
                if(sa) {//shi ""kong   没改名
                    value.putString("btn_save_ee_title",title);
                }else {//改了 新名字
                    value.putString("btn_save_ee_title",et_edit_title.getText().toString());
                }
                if(tv_pick_time_ee.getText().toString().equals(date)){
                    value.putString("btn_save_ee_date",date);
                }
                else {
                    value.putString("btn_save_ee_date",tv_pick_time_ee.getText().toString());
                }
                if(tv_pick_done_type_ee.getText().toString().equals(type)){
                    value.putString("btn_save_ee_type",type);
                }
                else {
                    value.putString("btn_save_ee_type",tv_pick_done_type_ee.getText().toString());
                }

                value.putInt("imageid",R.drawable.back);

                i.putExtra("R.id.btn_save_ee",value);
                setResult(GO_EDIT_EVENT_RETURN,i);
                finish();
                break;


               /* //修改这个day，并且改数据库中的，然后前面页面显示这个，主页刷新
                //记录这个day
                Intent i1 = getIntent();//daydetails come
                Log.d("TAG", "onClick: "+et_edit_title.getText());
                Log.d("TAG", "onClick: "+ title);
                Bundle bundle = i1.getBundleExtra("")
                String get = et_edit_title.getText().toString();
                boolean sa = get.equals("");


                if(sa) {//不改名
                    i1.putExtra("nnnewtitle", title);
                    Log.d("TAG", "onClickwoshinull: "+ title);
                }else{//改名 把名字传过去
                    i1.putExtra("nnnewtitle",et_edit_title.getText());
                    Log.d("TAG", "onClick123: "+et_edit_title.getText()+"+++"+title);
                }
                if (tv_pick_time_ee.getText() == null){
                    i1.putExtra("newdate", date);
                }else{
                    i1.putExtra("newdate",tv_pick_time_ee.getText());
                }
                if (tv_pick_done_type_ee.getText() == null){
                    i1.putExtra("newtype",type );
                }else{
                    i1.putExtra("newtype",tv_pick_done_type_ee.getText());
                }
               // i1.putExtra("position",et_edit_title.getText());
                try {
                    int d = MyAdapter.caculateDay(date);
                    i1.putExtra("newdiffer",d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setResult(GO_EDIT_EVENT_RETURN,i1);
                finish();
                break;*/

            case R.id.ll_type_choose_ee:
                i = new Intent(this,ChooseType.class);
                startActivityForResult(i,EE_GO_TO_CHOOSE_TYPE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EE_GO_TO_CHOOSE_TYPE:
                if(resultCode == 2){
                    tv_pick_done_type_ee.setText(data.getStringExtra("booktype"));
                    Log.d("EE_GO_TO_CHOOSE_TYPE", "onActivityResult:111111 " + "  " + data.getStringExtra("booktype"));
                }
                break;
        }
    }
}