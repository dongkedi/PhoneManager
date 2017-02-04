package com.zhuoxin.phone.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    RelativeLayout rl_start;
    RelativeLayout rl_notification;
    RelativeLayout rl_push;
    RelativeLayout rl_help;
    RelativeLayout rl_aboutus;
    ToggleButton tb_start;
    ToggleButton tb_notification;
    ToggleButton tb_push;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initActionBar(true, "系统设置", false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
        mContext = this;
    }

    private void initView() {
        rl_start = (RelativeLayout) findViewById(R.id.rl_start);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        rl_push = (RelativeLayout) findViewById(R.id.rl_push);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        tb_start = (ToggleButton) findViewById(R.id.tb_start);
        tb_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //把状态保存到sp中
                getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean("startWhenBootComplete", tb_start.isChecked()).commit();
            }
        });
        tb_notification = (ToggleButton) findViewById(R.id.tb_notification);
        tb_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //状态变化时，显示通知栏消息，未选中时，清空通知栏消息
                if (isChecked) {
                    Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notificationCompat = new NotificationCompat.Builder(SettingsActivity.this)
                            .setContentTitle("虚假通知")
                            .setContentText("这是我一条我捏造的虚假通知")
                            .setSmallIcon(R.drawable.icon)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(0, notificationCompat);
                } else {
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(0);
                }
            }
        });
        tb_push = (ToggleButton) findViewById(R.id.tb_push);
        rl_start.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
        rl_push.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);
    }

    private void initData() {
        boolean startWhenBootComplete = getSharedPreferences("config", MODE_PRIVATE).getBoolean("startWhenBootComplete", false);
        tb_start.setChecked(startWhenBootComplete);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_start:
                //开机启动按钮，去设置对应的Togglebutton，并且把开机启动的状态保存
                //切换状态
                tb_start.setChecked(!tb_start.isChecked());
                break;
            case R.id.rl_notification:
                tb_notification.setChecked(!tb_notification.isChecked());
                break;
            case R.id.rl_push:
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("旋转跳跃我不停歇");
                progressDialog.setTitle("后续版本即将推出");
                progressDialog.show();
                Toast.makeText(this, "后续版本即将推出", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_help:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSettings", true);
                startActivity(GuideActivity.class, bundle);
                break;
            case R.id.rl_aboutus:
                startActivity(AboutActivity.class);
                break;
        }
    }
}
