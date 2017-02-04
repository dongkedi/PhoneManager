package com.zhuoxin.phone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhuoxin.phone.service.MusicService;

public class RebootCompleteReceiver extends BroadcastReceiver {

    //接收到广播后，执行的操作
    @Override
    public void onReceive(Context context, Intent intent) {
        //只有选中开机启动的选项时，才会执行以下操作
        boolean start = context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("startWhenBootComplete", false);
        if (start) {
            Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            Intent musicIntent = new Intent();
            musicIntent.setClass(context, MusicService.class);
            context.startService(musicIntent);
        }

    }
}
