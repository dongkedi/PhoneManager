package com.zhuoxin.phone.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.SoftwareAdapter;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static com.zhuoxin.phone.activity.SoftManagerActivity.softTitle;

public class SoftwareActivity extends ActionBarActivity {
    //Listview、List<AppInfo>、SoftwareAdapter
    ListView lv_software;
    ProgressBar pb_softmgr_loading;
    List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    SoftwareAdapter adapter;
    String appType;
    //删除cb和bt
    CheckBox cb_deleteall;
    Button bt_delete;
    //广播接收者
    BroadcastReceiver receiver;

    //Handler
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //处理逻辑，根据不同的msg进行处理
            int flag = msg.what;
            switch (flag) {
                case 1:
                    pb_softmgr_loading.setVisibility(View.GONE);
                    lv_software.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(SoftwareActivity.this, "我就吐个司", Toast.LENGTH_SHORT).show();
            }
            return false;//如果不想让其他的handler处理，传true
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        initActionBar(true, softTitle, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intiView();
        //动态创建receiver，必须反注册
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //重新获取app信息并保存
                saveAppInfos();
                adapter.notifyDataSetChanged();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void intiView() {
        appType = getIntent().getBundleExtra("bundle").getString("appType", "all");
        lv_software = (ListView) findViewById(R.id.lv_software);
        pb_softmgr_loading = (ProgressBar) findViewById(R.id.pb_softmgr_loading);
        cb_deleteall = (CheckBox) findViewById(R.id.cb_deleteall);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        //初始化adapter并和lv_software关联
        adapter = new SoftwareAdapter(appInfoList, this);
        lv_software.setAdapter(adapter);
        //获取手机中的数据，并存入appInfoList中
        saveAppInfos();
        cb_deleteall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //通过for循环，把数据的状态修改掉
                for (int i = 0; i < appInfoList.size(); i++) {
                    if (appType.equals("all")) {
                        if (!appInfoList.get(i).isSystem) {
                            appInfoList.get(i).isDelete = isChecked;
                        }
                    } else if (appType.equals("system")) {
                        appInfoList.get(i).isDelete = false;
                    } else {
                        appInfoList.get(i).isDelete = isChecked;
                    }
                }
                //刷新界面
                adapter.notifyDataSetChanged();
            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //循环取出列表中的App，如果是isDelete，调用删除的方法
                for (AppInfo info : appInfoList) {
                    if (info.isDelete) {
                        if (!info.packagename.equals(getPackageName())) {
                            //调用删除的方法
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + info.packagename));
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private void saveAppInfos() {
        pb_softmgr_loading.setVisibility(View.VISIBLE);
        lv_software.setVisibility(View.INVISIBLE);
        //因为访问数据（文件、网络）是耗时操作，开辟子线程操作，避免ANR现象产生
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList.clear();
                //获取几乎所有的安装包
                List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
                //循环获取所有软件信息
                for (PackageInfo packageInfo : packageInfoList) {
                    //建立ApplicationInfo
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    //创建apptype
                    boolean isSystem;
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        isSystem = true;
                    } else {
                        isSystem = false;
                    }
                    //创建appicon
                    Drawable appicon = getPackageManager().getApplicationIcon(applicationInfo);
                    //创建appname
                    String appname = (String) getPackageManager().getApplicationLabel(applicationInfo);
                    String packagename = packageInfo.packageName;
                    String appversion = packageInfo.versionName;
                    //判断当前页面要展示的数据，把数据存到appInfoList
                    if (SoftwareActivity.this.appType.equals("all")) {
                        AppInfo info = new AppInfo(appicon, appname, isSystem, packagename, appversion, false);
                        appInfoList.add(info);
                    } else if (SoftwareActivity.this.appType.equals("system")) {
                        if (isSystem) {
                            AppInfo info = new AppInfo(appicon, appname, isSystem, packagename, appversion, false);
                            appInfoList.add(info);
                        }
                    } else {
                        if (!isSystem) {
                            AppInfo info = new AppInfo(appicon, appname, isSystem, packagename, appversion, false);
                            appInfoList.add(info);
                        }
                    }
                }
                //1、runOnUiThread   View.post()
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                cb_deleteall.post(new Runnable() {
                    @Override
                    public void run() {
                        cb_deleteall.setChecked(true);
                    }
                });*/
                //2、Handler机制
                Message msg = handler.obtainMessage();
                msg.what = 1;//对msg设置标记
                //msg.setTarget(handler);
                //msg.arg1
                handler.sendMessage(msg);
                //AsyncTask
            }
        }).start();
    }
}
