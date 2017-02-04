package com.zhuoxin.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.PagerGuideAdapter;
import com.zhuoxin.phone.base.BaseActivity;
import com.zhuoxin.phone.db.DBManager;
import com.zhuoxin.phone.service.MusicService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    ViewPager vp_guide;
    //定义像素
    float pixelWidth;
    ImageView iv_circle_red;
    TextView tv_skip;
    boolean isFromSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //获取Bundle中的数据
        Bundle bundle = getIntent().getBundleExtra("bundle");//如果从桌面启动，获得不到bundle
        if (bundle != null) {
            isFromSettings = bundle.getBoolean("isFromSettings", false);
        }
        boolean isFirstRun = getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFromSettings) {
            initView();
            startService(MusicService.class);
        } else if (isFirstRun) {
            initView();
            startService(MusicService.class);
        } else {
            finish();
            startActivity(HomeActivity.class);
        }
    }

    @Override
    public void finish() {
        stopService(MusicService.class);
        super.finish();
    }

    private void initView() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        iv_circle_red = (ImageView) findViewById(R.id.iv_circle_red);
        //计算真正的像素宽
        pixelWidth = 40 * getDensity();
        final List<Integer> idList = new ArrayList<Integer>();
        idList.add(R.drawable.pager_guide1);
        idList.add(R.drawable.pager_guide2);
        idList.add(R.drawable.pager_guide3);
        PagerGuideAdapter pagerGuideAdapter = new PagerGuideAdapter(this, idList);
        vp_guide.setAdapter(pagerGuideAdapter);
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                iv_circle_red.setPadding((int) (position * pixelWidth + positionOffset * pixelWidth), 0, 0, 0);
            }

            @Override
            public void onPageSelected(int position) {
                //判断是否最后一页，最后一页显示tv_skip
                if (position == idList.size() - 1) {
                    tv_skip.setVisibility(View.VISIBLE);
                } else {
                    tv_skip.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存是否第一次运行程序
                //sharedpreference
                getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                copyAssets();
                if (isFromSettings) {
                    finish();
                } else {
                    startActivity(HomeActivity.class);
                    finish();
                }

            }
        });
    }

    private float getDensity() {
        //新建尺寸信息
        DisplayMetrics metrics = new DisplayMetrics();
        //获取当前手机界面的尺寸信息
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //获取密度并返回
        return metrics.density;
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
