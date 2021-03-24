package com.example.mynewday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddNewBookType extends AppCompatActivity implements View.OnClickListener {
    private Button bt_save_anbt;
    private EditText et_add_title_anbt;
    private ImageView iv_back_anbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book_type);

        bt_save_anbt = findViewById(R.id.bt_save_anbt);
        et_add_title_anbt = findViewById(R.id.et_add_title_anbt);
        iv_back_anbt = findViewById(R.id.iv_back_anbt);

        iv_back_anbt.setOnClickListener(this);
        bt_save_anbt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_anbt:
                finish();
                break;
            case R.id.bt_save_anbt:
                String nt =  et_add_title_anbt.getText().toString();
                Log.d("bt_save_anbt", "onClick: "+nt);
                Intent i = getIntent();
                i.putExtra("NewName",nt);//把  新type的名字 拿回去了
                setResult(ChooseType.GO_ADD_NEW_BOOK_TYPE_RETURN,i);
                finish();
                break;
            default:
                break;
        }
    }
}