package com.zhuoxin.phone.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.TelNumberAdapter;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.base.BaseActivity;
import com.zhuoxin.phone.db.DBManager;
import com.zhuoxin.phone.entity.TelNumberInfo;

import java.io.File;
import java.util.List;

public class PhoneNumberActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    final int PERMISSION_REQUEST_CODE = 0;
    List<TelNumberInfo> dataList;
    ListView lv_numberList;
    String number = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        initView();
        initDate();
    }

    private void initDate() {
        //取出标题数据，设置标题
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String title = bundle.getString("title");
        int idx = bundle.getInt("idx", 1);
        //从数据库中读取telNumber的信息
        File targetFile = new File(getFilesDir(), "commonnum.db");
        dataList = DBManager.readTelNumberList(targetFile, idx);
        //对listview设置adapter
        TelNumberAdapter adapter = new TelNumberAdapter(dataList, this);
        lv_numberList.setAdapter(adapter);
        //对ListView设置侦听时间
        lv_numberList.setOnItemClickListener(this);
        initActionBar(true, title, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        lv_numberList = (ListView) findViewById(R.id.lv_numberList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //单机对应的条目时，应该获取到相对应的电话号码
        number = dataList.get(position).number;
        //需要判断当时的版本号是否为6.0（23）版本
        if (Build.VERSION.SDK_INT >= 23) {
            //如果用到敏感权限，需要动态申请-运行时runtime权限
            //1、先检查自己需要的权限是否已经申请
            int hasGot = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (hasGot == PackageManager.PERMISSION_GRANTED) {
                //创建隐式intent，启动拨号程序
                call(number);
            } else {
                //2、没有相应的权限，需要申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            call(number);
        }


    }
    //3、编写权限的回调方法

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //直接处理
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限申请成功
                    call(number);
                } else {
                    //权限申请失败
                    //用对话框进行提示
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("权限提示")
                            .setMessage("您可以跳转到系统界面进行权限设置")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认按钮的单击事件，跳转到系统app界面

                            startActivity(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
                    alertDialog.show();
                    Toast.makeText(this, "权限申请失败，请您重新获取权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void call(final String number) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("拨号提示").setMessage("您是否拨打" + number).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认按钮的单击事件，跳转到拨号界面

                startActivity(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        alertDialog.show();

    }
}
