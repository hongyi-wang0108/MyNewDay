package com.example.mynewday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ChooseType extends AppCompatActivity implements View.OnClickListener, BookTypeAdapter.OnDLClickListener {
    private Button btn_save_new_type;
    private ImageView iv_back_ct;
    private RecyclerView rv_type_book;
    private List<BookType> list = new ArrayList<>();
    private BookTypeAdapter adapter;
    private static final int GO_ADD_NEW_BOOK_TYPE = 10;
    public static final int GO_ADD_NEW_BOOK_TYPE_RETURN = 11;
    private SharedPreferences sp ;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
        //list
        initBookType();

        //控件
        btn_save_new_type = findViewById(R.id.btn_save_new_type);
        iv_back_ct = findViewById(R.id.iv_back_ct);
        rv_type_book = findViewById(R.id.rv_type_book);
        adapter = new BookTypeAdapter(list);
        btn_save_new_type.setOnClickListener(this);
        iv_back_ct.setOnClickListener(this);
        adapter.setDLOnClickListener(this);

        //adapter的
        rv_type_book.setLayoutManager(new LinearLayoutManager(this));
        rv_type_book.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("qwer", ":stop "+list.toString());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_ct:
                finish();
                break;
            case R.id.btn_save_new_type://add new BookType
                Intent i = new Intent(this,AddNewBookType.class);
                startActivityForResult(i,GO_ADD_NEW_BOOK_TYPE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GO_ADD_NEW_BOOK_TYPE:
                if(resultCode == GO_ADD_NEW_BOOK_TYPE_RETURN){
                    String nn = data.getStringExtra("NewName");
                    //update rv
                    BookType newvt = new BookType(R.drawable.back,nn);
                    List<BookType> oldl = list;
                    if(oldl.size() == 0){
                        list.add(newvt);
                    }else {
                        List<String> bookname = new ArrayList<>();
                        for (BookType bookType : oldl) {
                            bookname.add(bookType.getName());
                        }
                        if(bookname.contains(nn)){
                            Toast.makeText(this,"已经有这个倒数本存过了哦",Toast.LENGTH_SHORT).show();
                        }else {
                            list.add(newvt);
                        }
                    }
                    setDataList("choosetype",list);

                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(oldl,list),true);
                    diffResult.dispatchUpdatesTo(adapter);
                }
                break;
            default:

        }
    }

    private void setDataList(String tag,List<BookType> dataList) {//cun
        editor = getSharedPreferences("dataofct",MODE_PRIVATE).edit();
        if(dataList == null || dataList.size() <= 0)
            return;
        Gson gson = new Gson();
        String str = gson.toJson(dataList);
        editor.clear();
        editor.putString(tag,str);
        editor.commit();
    }

    private List<BookType> getDataList(String tag) {
        sp = getSharedPreferences("dataofct",MODE_PRIVATE);
        List<BookType> newlist = new ArrayList<>();
        String nl = sp.getString(tag,null);
        if(nl == null )
            return newlist;
        Gson gson = new Gson();
        newlist = gson.fromJson(nl,new TypeToken<List<BookType>>(){}.getType());
        return newlist;
    }

    private void initBookType() {
        BookType bookType1 = new BookType(R.drawable.back,"work");
        list.add(bookType1);
        BookType bookType2 = new BookType(R.drawable.back,"love");
        list.add(bookType2);
        BookType bookType3 = new BookType(R.drawable.back,"study");
        list.add(bookType3);
        list = getDataList("choosetype");
    }


    @Override
    public void onDLClick(int position) {
        Intent i = getIntent();
        i.putExtra("booktype",list.get(position).getName());
        Log.d("ChooseTypeChooseType", "setOnRecyclerClickListener: "+list.get(position).getName());
        setResult(AddEvents.CHOOSE_TYPE_RETURN,i);
        finish();
    }
}