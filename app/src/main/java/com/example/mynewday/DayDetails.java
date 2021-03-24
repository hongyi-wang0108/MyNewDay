package com.example.mynewday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayDetails extends AppCompatActivity implements View.OnClickListener {
    private static final int GO_EDIT_EVENT = 333;
    public static final int GO_DAT_DETAILS_RETURN = 311;
    private static boolean ISPASS = false;
    private ImageView iv_back_dd,iv_update;
    private TextView tv_title_dd,tv_day_dd,tv_differ_dd;
    private LinearLayout ll_background_dd,ll_history_dd,ll_savedraw_dd;
    private SharedPreferences sp;
    private Day day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        //控件和点击
        iv_back_dd = findViewById(R.id.iv_back_dd);
        tv_differ_dd = findViewById(R.id.tv_differ_dd);
        iv_update = findViewById(R.id.iv_update);
        tv_title_dd = findViewById(R.id.tv_title_dd);
        tv_day_dd = findViewById(R.id.tv_day_dd);
        ll_background_dd = findViewById(R.id.ll_background_dd);
        ll_history_dd = findViewById(R.id.ll_history_dd);
        ll_savedraw_dd = findViewById(R.id.ll_savedraw_dd);
        iv_back_dd.setOnClickListener(this);
        iv_update.setOnClickListener(this);

        //view的显示
        getIntentData();//得到选中的day的信息,并且显示

    }

    private String newtype;
    private String newtitle;
    private String newdate;
    private int newdiff;

    private String type;
    private int position;
    private String date;
    private String title;
    private int diff;
    private boolean isclickeditevent = false;

    private void getIntentData() {
        Intent i = getIntent();
        Bundle bundle  = new Bundle();
        bundle = i.getBundleExtra("onRVClick");
        tv_title_dd.setText(bundle.getString("title","没获取到"));
        tv_day_dd.setText("起始日： " + bundle.getString("date","没获取到"));
        tv_differ_dd.setText(bundle.getInt("datediffer",-1) + "");


        type = bundle.getString("type");
        position = bundle.getInt("position",0);
        date = bundle.getString("date","没获取到date");
        title = bundle.getString("realtitle","没获取到list的title");
        diff = bundle.getInt("datediffer",0);


    }

    //    String title;
    //    String date;
    //    String type;
    //    int imageid;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_dd:
                //把一个不管新不新的day带回去，带到main里面再做判断，一样的话，不改list，不一样的话，改list
                Intent intent = getIntent();//
                Bundle bundle = new Bundle();
                Log.d("TAG", "onClick: " + newtitle + newdate + newtype);
                if(newtitle == null && newdate == null && newtype == null){//没有点编辑事件
                    bundle.putBoolean("isclickeditevent",false);
                    intent.putExtra("R.id.iv_back_dd",bundle);
                    //intent.putExtra("goedit",false);
                    setResult(GO_DAT_DETAILS_RETURN,intent);
                    finish();
                    break;
                }
                Log.d("TAG", "onClick: " + "能走到这说明点了编辑");
                bundle.putString("iv_back_dd_title",newtitle);
                bundle.putString("iv_back_dd_type",newtype);
                bundle.putString("iv_back_dd_date",newdate);
                bundle.putInt("iv_back_dd_position",position);
                bundle.putBoolean("isclickeditevent",true);
               // Day a = new Day(newtitle,date,newtype,R.drawable.back);
               // Log.d("Day a", "onClick: "+a.toString());
                intent.putExtra("R.id.iv_back_dd",bundle);
               // intent.putExtra("goedit",true);
                setResult(GO_DAT_DETAILS_RETURN,intent);
                finish();
               /* List<Day> old = new ArrayList<>();
                old = MainActivity.list;
                MainActivity.list.remove(position);
                MainActivity.list.add(a);
                setData("putString",MainActivity.list);*/

                break;
            case R.id.iv_update:
                Intent i = new Intent(this,EditEventActivity.class);
                //设置此时的信息带过去，然后显示hint
                bundle = new Bundle();
                bundle.putString("iv_update_title",title);
                bundle.putString("iv_update_date",date);
                bundle.putString("iv_update_type",type);
                bundle.putInt("iv_update_diff",diff);

                i.putExtra("R.id.iv_update",bundle);
                startActivityForResult(i,GO_EDIT_EVENT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_EDIT_EVENT:
                if (resultCode == EditEventActivity.GO_EDIT_EVENT_RETURN) {
                    //更新UI
                    Bundle bundle = data.getBundleExtra("R.id.btn_save_ee");
                    newtitle = bundle.getString("btn_save_ee_title", "没获取到title");
                    newdate = bundle.getString("btn_save_ee_date", "没获取到date");
                    newtype = bundle.getString("btn_save_ee_type", "没获取到type");
                    day = new Day(newtitle, newdate, newtype, R.drawable.back);
                    int realday = 0;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date before = sdf.parse(newdate);
                        Long datemills = before.getTime();
                        Calendar calendar = Calendar.getInstance();
                        Date now = calendar.getTime();
                        Long nowmills = now.getTime();
                        realday = (int) ((nowmills - datemills) / (1000 * 60 * 60 * 24));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    newdiff = realday;
                    if (newdiff > 0) {
                        tv_title_dd.setText(newtitle + "已经");
                        tv_differ_dd.setText(newdiff + "");
                    } else {
                        tv_title_dd.setText(newtitle + "还有");
                        tv_differ_dd.setText((0 - newdiff) + "");
                    }
                    tv_day_dd.setText("起始日：" + newdate);

                  /*  //更新UI完了之后更新list  走到这说明一定点了编辑事件
                    //改list
                    Day day = new Day(newtitle, newdate, newtype, R.drawable.back);
                    List<Day> newlist = MainActivity.list;
                    if (newlist.size() == 0) {
                        MainActivity.list.add(day);
                    } else {
                        //已经知道是list里面的哪个被改了，通过rv的position
                        Day oldday = MainActivity.list.get(position);
                        //判断被改变了吗
                        boolean ischange = true;
                        //这几个之中只要改了一个 就说明改了
                        if (oldday.getTitle().equals(day.getTitle()) && //title 没改
                                oldday.getDate().equals(day.getDate()) && //date 没改
                                oldday.getType().equals(day.getType()) &&  //type 没改
                                oldday.getImageid() == (day.getImageid())) { //image 没改
                            //全没改
                            ischange = false;
                        }
                        if (ischange) {//gaile
                            //list 去掉原先的，添加新的
                            MainActivity.list.remove(position);
                            MainActivity.list.add(day);
                            //改变sp,clear sp
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.clear().commit();
                            //write new list
                            setData("putString", MainActivity.list);
                            //刷新rv
                            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallbackDay(newlist, MainActivity.list), true);
                            result.dispatchUpdatesTo(MainActivity.adapter);
                        }
                    }*/
                }
                break;
        }
    }

    private void setData(String tag,List<Day> list) {
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        if(list == null || list.size() <= 0)
            return;
        Gson gson  = new Gson();
        String strlist = gson.toJson(list);
        editor.clear();
        editor.putString(tag,strlist);
        editor.commit();
    }
}