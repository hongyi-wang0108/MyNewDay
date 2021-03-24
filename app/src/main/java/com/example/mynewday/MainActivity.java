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
        //main list的
        initlist();

        //控件和点击
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

        //main的adapter的
        adapter = new MyAdapter(list);
        rv_days.setAdapter(adapter);
        rv_days.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.setRVOnClickListener(this);

        //main 显示第一个view的
        try {showView(); } catch (ParseException e) {
            e.printStackTrace();
        }

        //dl list 这个list是通过main的list的type来搞的
        initDLlist();
        //dl的adapter的
        dlAdapter = new BookTypeAdapter(dl_book_list);
        rv_dl.setAdapter(dlAdapter);
        rv_dl.setLayoutManager(new LinearLayoutManager(this));
        dlAdapter.setDLOnClickListener(this);
    }

    //显示type的rv的样子
    private void initDLlist() {
        dl_book_list.add(new BookType(R.drawable.back,"首页事件"));
        dl_book_list.add(new BookType(R.drawable.back,"全部"));
        dl_book_list.add(new BookType(R.drawable.back,"生活"));
        dl_book_list.add(new BookType(R.drawable.back,"工作"));
        dl_book_list.add(new BookType(R.drawable.back,"纪念日"));
        List<BookType> ndl_book_list = getMainDataList("choosetype");
        for (BookType bookType : ndl_book_list) {
            dl_book_list.add(bookType);
        }
    }

    //存booktype的sp
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

    //取出booktype的sp
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

    //显示首页不是rv那个的
    private void showView() throws ParseException {
        if(list.size() > 0 ) {
            Day day = list.get(0);
            int differ = MyAdapter.caculateDay(day.getDate());
            if (differ > 0) {
                show_title.setText(day.getTitle() + "已经");
                show_day.setText(differ + "");
            } else {
                show_title.setText(day.getTitle() + "还有");
                show_day.setText((0 - differ) + "");
            }
            show_aim.setText("目标日：" + day.getDate());
            Log.d("TAG", "showView: " + differ);
        }else{//还没数据的时候
            show_title.setText("建一个日子吧");
            show_day.setText("建一个日子吧");
            show_aim.setText("建一个日子吧：");
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

    //需要返回数据的
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
                        for (Day day1 : newlist) {//通过title来判断是否两个纪念日相同
                            titlename.add(day1.getTitle());//记录所有title
                        }
                        if (!titlename.contains(day.getTitle())) {//不包含就添加，包含就不添加
                            list.add(day);
                        } else {
                            Toast.makeText(this, "已经有这个纪念日存过了哦", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //设置数据给list
                    setData("putString", list);

                    //刷新RV
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




                    //先把数据取出来
                    Bundle bundle = data.getBundleExtra("R.id.iv_back_dd");
                    if(!bundle.getBoolean("isclickeditevent",false)){//没点编辑事件
                        break;
                    }
                    String newtitle = bundle.getString("iv_back_dd_title", "没获取到title");
                    String newtype = bundle.getString("iv_back_dd_type", "没获取到type");
                    String newdate = bundle.getString("iv_back_dd_date", "没获取到date");
                    int position = bundle.getInt("iv_back_dd_position");
                    //改list
                    Day day = new Day(newtitle, newdate, newtype, R.drawable.back);
                    List<Day> newlist = list;
                    if (newlist.size() == 0) {
                        list.add(day);
                    } else {
                        //已经知道是list里面的哪个被改了，通过rv的position
                        Day oldday = list.get(position);
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
                            list.remove(position);
                            list.add(day);
                            //改变sp,clear sp
                            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.clear().commit();
                            //write new list
                            setData("putString",list);
                            //刷新rv
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

    //把list，所有存到sp
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

    //从sp把list拿出来
    private List<Day> getData(String tag) {//当list有数据的时候才取
        sp = (SharedPreferences) getSharedPreferences("data",MODE_PRIVATE);
        List<Day> newlist = new ArrayList<>();
        String strnewlist = sp.getString(tag, null);
        if (strnewlist == null)
            return newlist;
        Gson gson = new Gson();
        newlist = gson.fromJson(strnewlist,new TypeToken<List<Day>>(){}.getType());
        return newlist;
    }

    //刚开始页面没东西，要把数据库的读出来
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
        //带回来的可能是有变化的day要考虑是否更新
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