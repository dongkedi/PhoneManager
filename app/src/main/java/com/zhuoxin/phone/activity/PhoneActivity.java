package com.zhuoxin.phone.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.TelClassListAdapter;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.base.BaseActivity;
import com.zhuoxin.phone.db.DBManager;
import com.zhuoxin.phone.entity.TelClassInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends ActionBarActivity {
    ListView lv_classlist;
    TelClassListAdapter adapter;
    List<TelClassInfo> telClassInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initDate();
        initView();
        initActionBar(true, "电话大全", false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDate() {
        File file = this.getFilesDir();
        File targetFile = new File(file, "commonnum.db");
        telClassInfoList = DBManager.readTelClassList(this, targetFile);
        adapter = new TelClassListAdapter(telClassInfoList, this);
    }

    private void initView() {
        lv_classlist = (ListView) findViewById(R.id.lv_classlist);
        lv_classlist.setAdapter(adapter);
        //对listView设置单击事件
        lv_classlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("idx", telClassInfoList.get(position).idx);
                bundle.putString("title", telClassInfoList.get(position).name);
                startActivity(PhoneNumberActivity.class, bundle);
            }
        });
    }

}

