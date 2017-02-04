package com.zhuoxin.phone.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.RocketAdapter;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.biz.MemoryManager;

public class RocketActivity extends ActionBarActivity {
    TextView tv_brand;
    TextView tv_version;
    TextView tv_rocket_space;
    ProgressBar pb_rocket;
    ProgressBar pb_rocket_loading;
    ListView lv_rocket;
    Button bt_rocket;
    //创建Adapter
    RocketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        initView();
        initDate();
        initActionBar(true, "手机加速", false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initListView();
    }

    /**
     * 再次打开时进行文件读取和刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        //输出ListView
        initListView();
        //刷新运行空间
        getRuntimeMemory();
    }

    private void initView() {
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_rocket_space = (TextView) findViewById(R.id.tv_rocket_space);
        pb_rocket = (ProgressBar) findViewById(R.id.pb_rocket);
        pb_rocket_loading = (ProgressBar) findViewById(R.id.pb_rocket_loading);
        lv_rocket = (ListView) findViewById(R.id.lv_rocket);
        bt_rocket = (Button) findViewById(R.id.bt_rocket);
        //初始化事件

        bt_rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加速按钮
                //杀死进程ActivityManager
                MemoryManager.killRunning(RocketActivity.this);
                //输出ListView
                initListView();
                //刷新运行空间
                getRuntimeMemory();
            }
        });
    }

    private void initDate() {
        //获取当前的品牌、系统版本、运行内存
        String brand = Build.BRAND;
        tv_brand.setText(brand);
        String version = "android " + Build.VERSION.RELEASE;
        tv_version.setText(version);
        getRuntimeMemory();
    }

    public void getRuntimeMemory() {
        //获取运行内存总大小和当前剩余大小
        //获取ActivityManager

        //设置进度条和文本信息的显示
        pb_rocket.setProgress(MemoryManager.usedPresent(this));
        tv_rocket_space.setText(MemoryManager.avaiableRAMString(this) + "可用 | " + MemoryManager.totalRAMString(this));
    }

    private void initListView() {
        //4.0之前，可以通过ActivityManager来获取运行中的进程
        /*
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> rapi = am.getRunningAppProcesses();
        for (int i = 0; i < rapi.size(); i++) {
            Log.e("进程信息", rapi.get(i).processName);
        }*/
        //4.0之后，获取不到运行的进程了。并且，在5.0的时候，谷歌把权限也删除了
        adapter = new RocketAdapter(MemoryManager.getRunning(this), this);
        lv_rocket.setAdapter(adapter);
        pb_rocket_loading.setVisibility(View.GONE);
        lv_rocket.setVisibility(View.VISIBLE);
    }
}
