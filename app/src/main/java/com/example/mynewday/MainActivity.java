package com.example.mynewday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyAdapter.OnRVClickListener, BookTypeAdapter.OnDLClickListener {
    private static final int GO_ADD_EVETNTS = 2;
    public static final int GO_ADD_EVETNTS_RETURN = 20;
    private static final int GO_DAY_DETAILS = 3;
    private int flag = 0;

    private RecyclerView rv_days,rv_dl;
    public static List<Day> list = new ArrayList<>();
    private List<BookType> dl_book_list = new ArrayList<>();
    public static MyAdapter adapter;
    private BookTypeAdapter dlAdapter;

    private DrawerLayout drawerLayout;
    private ImageView iv_mind;
    private ImageView iv_change_view;
    private LinearLayout ll_canvisibility;
    private ImageView iv_add_day_main;
    private TextView show_title,show_aim,show_day,tv_dl_add_book,tv_history_dl;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //main list???
        initlist();

        //???????????????
        rv_days = findViewById(R.id.rv_days);
        tv_history_dl = findViewById(R.id.tv_history_dl);
        tv_dl_add_book = findViewById(R.id.tv_dl_add_book);
        rv_dl = findViewById(R.id.rv_dl);
        show_title = findViewById(R.id.show_title);
        show_aim = findViewById(R.id.show_aim);
        show_day = findViewById(R.id.show_day);
        drawerLayout = findViewById(R.id.dl_main);
        iv_mind = findViewById(R.id.iv_mind);
        iv_change_view = findViewById(R.id.iv_change_view);
        ll_canvisibility = findViewById(R.id.ll_canvisibility);
        iv_add_day_main = findViewById(R.id.iv_add_day_main);
        iv_mind.setOnClickListener(this);
        iv_change_view.setOnClickListener(this);
        iv_add_day_main.setOnClickListener(this);
        tv_dl_add_book.setOnClickListener(this);
        tv_history_dl.setOnClickListener(this);

        //main???adapter???
        adapter = new MyAdapter(list);
        rv_days.setAdapter(adapter);
        rv_days.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.setRVOnClickListener(this);

        //main ???????????????view???
        try {showView(); } catch (ParseException e) {
            e.printStackTrace();
        }

        //dl list ??????list?????????main???list???type?????????
        initDLlist();
        //dl???adapter???
        dlAdapter = new BookTypeAdapter(dl_book_list);
        rv_dl.setAdapter(dlAdapter);
        rv_dl.setLayoutManager(new LinearLayoutManager(this));
        dlAdapter.setDLOnClickListener(this);
    }

    //??????type???rv?????????
    private void initDLlist() {
        dl_book_list.add(new BookType(R.drawable.back,"????????????"));
        dl_book_list.add(new BookType(R.drawable.back,"??????"));
        dl_book_list.add(new BookType(R.drawable.back,"??????"));
        dl_book_list.add(new BookType(R.drawable.back,"??????"));
        dl_book_list.add(new BookType(R.drawable.back,"?????????"));
        List<BookType> ndl_book_list = getMainDataList("choosetype");
        for (BookType bookType : ndl_book_list) {
            dl_book_list.add(bookType);
        }
    }

    //???booktype???sp
    private void setMainDataList(String tag,List<BookType> dataList) {//cun
        SharedPreferences.Editor editor = getSharedPreferences("dataofct",MODE_PRIVATE).edit();
        if(dataList == null || dataList.size() <= 0)
            return;
        Gson gson = new Gson();
        String str = gson.toJson(dataList);
        editor.clear();
        editor.putString(tag,str);
        editor.commit();
    }

    //??????booktype???sp
    private List<BookType> getMainDataList(String tag) {
        sp = getSharedPreferences("dataofct",MODE_PRIVATE);
        List<BookType> newlist = new ArrayList<>();
        String nl = sp.getString(tag,null);
        if(nl == null )
            return newlist;
        Gson gson = new Gson();
        newlist = gson.fromJson(nl,new TypeToken<List<BookType>>(){}.getType());
        return newlist;
    }

    //??????????????????rv?????????
    private void showView() throws ParseException {
        if(list.size() > 0 ) {
            Day day = list.get(0);
            int differ = MyAdapter.caculateDay(day.getDate());
            if (differ > 0) {
                show_title.setText(day.getTitle() + "??????");
                show_day.setText(differ + "");
            } else {
                show_title.setText(day.getTitle() + "??????");
                show_day.setText((0 - differ) + "");
            }
            show_aim.setText("????????????" + day.getDate());
            Log.d("TAG", "showView: " + differ);
        }else{//?????????????????????
            show_title.setText("??????????????????");
            show_day.setText("??????????????????");
            show_aim.setText("?????????????????????");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_mind:
                //Intent i = getIntent();
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_change_view:
                if(flag == 0){
                    ll_canvisibility.setVisibility(View.GONE);
                    rv_days.setLayoutManager(new GridLayoutManager(this,2));
                    flag = 1;
                }else {
                    ll_canvisibility.setVisibility(View.VISIBLE);
                    rv_days.setLayoutManager(new LinearLayoutManager(this));
                    flag = 0;
                }
                break;
            case R.id.iv_add_day_main:
                Intent i = new Intent(this,AddEvents.class);
                startActivityForResult(i,GO_ADD_EVETNTS);
                break;
            case R.id.tv_dl_add_book:
                Toast.makeText(this,"click",Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(this,ChooseType.class);
                startActivity(i1);
                break;
            case R.id.tv_history_dl:
                i = new Intent(this,HistoryActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    //?????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(data == null)
            return;*/
        switch (requestCode) {
            case GO_ADD_EVETNTS:
                if (resultCode == GO_ADD_EVETNTS_RETURN) {//
                    //get day
                    Bundle bundle = data.getBundleExtra("newEvent");
                    Day day = new Day(bundle.getString("title"),
                            bundle.getString("date"),
                            bundle.getString("type"),
                            bundle.getInt("imageid"));
                    Log.d("GO_ADD_EVETNTS", "onActivityResult: " + day.toString());
                    //update rv
                    List<Day> newlist = list;
                    if (newlist.size() == 0) {
                        list.add(day);
                    } else {
                        List<String> titlename = new ArrayList<>();
                        for (Day day1 : newlist) {//??????title????????????????????????????????????
                            titlename.add(day1.getTitle());//????????????title
                        }
                        if (!titlename.contains(day.getTitle())) {//???????????????????????????????????????
                            list.add(day);
                        } else {
                            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //???????????????list
                    setData("putString", list);

                    //??????RV
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallbackDay(newlist, list), true);
                    diffResult.dispatchUpdatesTo(adapter);
                    Log.d("GO_ADD_EVETNTS", list.toString());
                    try {
                        showView();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GO_DAY_DETAILS://refresh rv
                if (resultCode == DayDetails.GO_DAT_DETAILS_RETURN) {
                    Log.d("TAG", "onActivityResult:isclickeditevent " + data.getBooleanExtra("isclickeditevent",false));
                  //  Log.d("TAG", "onActivityResult:isclickeditevent " + data.getBooleanExtra("isclickeditevent",false));
                   // initlist();




                    //?????????????????????
                    Bundle bundle = data.getBundleExtra("R.id.iv_back_dd");
                    if(!bundle.getBoolean("isclickeditevent",false)){//??????????????????
                        break;
                    }
                    String newtitle = bundle.getString("iv_back_dd_title", "????????????title");
                    String newtype = bundle.getString("iv_back_dd_type", "????????????type");
                    String newdate = bundle.getString("iv_back_dd_date", "????????????date");
                    int position = bundle.getInt("iv_back_dd_position");
                    //???list
                    Day day = new Day(newtitle, newdate, newtype, R.drawable.back);
                    List<Day> newlist = list;
                    if (newlist.size() == 0) {
                        list.add(day);
                    } else {
                        //???????????????list?????????????????????????????????rv???position
                        Day oldday = list.get(position);
                        //?????????????????????
                        boolean ischange = true;
                        //????????????????????????????????? ???????????????
                        if (oldday.getTitle().equals(day.getTitle()) && //title ??????
                                oldday.getDate().equals(day.getDate()) && //date ??????
                                oldday.getType().equals(day.getType()) &&  //type ??????
                                oldday.getImageid() == (day.getImageid())) { //image ??????
                            //?????????
                            ischange = false;
                        }
                        if (ischange) {//gaile
                            //list ??????????????????????????????
                            list.remove(position);
                            list.add(day);
                            //??????sp,clear sp
                            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.clear().commit();
                            //write new list
                            setData("putString",list);
                            //??????rv
                            DiffUtil.DiffResult result  = DiffUtil.calculateDiff(new DiffCallbackDay(newlist,list),true);
                            result.dispatchUpdatesTo(adapter);
                            try {
                                showView();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                break;
            default:
                break;
        }
    }

    //???list???????????????sp
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

    //???sp???list?????????
    private List<Day> getData(String tag) {//???list????????????????????????
        sp = (SharedPreferences) getSharedPreferences("data",MODE_PRIVATE);
        List<Day> newlist = new ArrayList<>();
        String strnewlist = sp.getString(tag, null);
        if (strnewlist == null)
            return newlist;
        Gson gson = new Gson();
        newlist = gson.fromJson(strnewlist,new TypeToken<List<Day>>(){}.getType());
        return newlist;
    }

    //??????????????????????????????????????????????????????
    private void initlist() {
        list = getData("putString");
    }

    @Override
    public void onRVClick(String titletext, int position) {
        Intent i = new Intent(this,DayDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",titletext);
        bundle.putInt("position",position);
        bundle.putString("realtitle",list.get(position).getTitle());
        bundle.putString("date",list.get(position).getDate());
        bundle.putString("type",list.get(position).getType());
        bundle.putInt("imageid",list.get(position).getImageid());
        bundle.putInt("datediffer",list.get(position).getDatediffer());
        i.putExtra("onRVClick",bundle);
        startActivityForResult(i,GO_DAY_DETAILS);
        //?????????????????????????????????day?????????????????????
    }

    @Override
    public void onDLClick(int position) {
        Toast.makeText(this," " + position,Toast.LENGTH_SHORT).show();
        BookType picktype = dl_book_list.get(position);
        String name = picktype.getName();
        List<Day> newlist = new ArrayList<>();
        for (Day day : list) {
            if(day.getType().equals(name)){
                newlist.add(day);
            }
        }
        //refresh
        Log.d("onDLClick", "onDLClick: " + newlist);
        adapter.setData(newlist);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallbackDay(null,newlist),true);
        result.dispatchUpdatesTo(adapter);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}