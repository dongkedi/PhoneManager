package com.zhuoxin.phone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.BaseActivity;
import com.zhuoxin.phone.db.DBManager;

import java.io.File;

public class SplashActivity extends BaseActivity {
    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        startAnimation();
        copyAssets();
    }

    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_set);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Toast.makeText(SplashActivity.this, "动画结束，开始跳转通讯页面", Toast.LENGTH_SHORT).show();
                startActivity(GuideActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation.setAnimationListener(animationListener);
        iv_logo.startAnimation(animation);
    }

    private void copyAssets() {
        //取手机中的存储位置
        File file = this.getFilesDir();
        File targetFile = new File(file, "commonnum.db");
        if (!DBManager.isExistsBD(targetFile)) {
            DBManager.copyAssetsFileToFile(this, "commonnum.db", targetFile);
            //Toast.makeText(SplashActivity.this, "文件复制成功,文件大小为：" + targetFile.length() + "字节，路径为：" + targetFile.getPath(), Toast.LENGTH_SHORT).show();
        }
    }
}
