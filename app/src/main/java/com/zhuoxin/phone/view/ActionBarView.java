package com.zhuoxin.phone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoxin.phone.R;

public class ActionBarView extends LinearLayout {
    ImageView iv_back;
    ImageView iv_menu;
    TextView tv_title;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.shape_blue);
        this.setTranslationZ(4);
        this.setElevation(4);

        //需要先把actionbar的布局引入过来
        inflate(context, R.layout.layout_actionbar, this);
        //找到对应的view
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        tv_title = (TextView) findViewById(R.id.tv_title);

    }

    //添加一个初始化ActionBar的方法
    public void initActionBar(boolean hasBack, String title, boolean hasMenu, OnClickListener listener) {
        if (hasBack) {
            iv_back.setOnClickListener(listener);
        } else {
            iv_back.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(title);
        if (hasMenu) {
            iv_menu.setOnClickListener(listener);
        } else {
            iv_menu.setVisibility(View.INVISIBLE);
        }
    }
}
