package com.zhuoxin.phone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zhuoxin.phone.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/14.
 */

public class PiechartView extends View {
    int backgroundColor;
    int color;
    int angle;

    public PiechartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取出自定义属性的列表
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PiechartView, 0, 0);
        //循环取出列表中的数据
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int indexName = typedArray.getIndex(i);
            //判断当前的indexName属性是否和自定义属性匹配
            switch (indexName) {
                case R.styleable.PiechartView_piechartBackgroundColor:
                    backgroundColor = typedArray.getColor(indexName, Color.GREEN);
                    break;
                case R.styleable.PiechartView_piechartColor:
                    color = typedArray.getColor(indexName, Color.BLUE);
                    break;
                case R.styleable.PiechartView_piechartAngle:
                    angle = typedArray.getInt(indexName, 0);
                    break;
            }
        }
    }

    //2、测量该View的大小

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取真正的宽高数据
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //约定该View的宽高一致
        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        //把真正的参数传递给系统
        setMeasuredDimension(width, height);

    }

    //3、绘图

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1、定义画笔
        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setAntiAlias(true);//抗锯齿
        //2、定义绘画区域
        RectF oval = new RectF(0, 0, getWidth(), getHeight());
        //3、绘画
        //绘制背景
        canvas.drawArc(oval, 0, 360, true, paint);
        //绘制前景
        paint.setColor(color);
        canvas.drawArc(oval, -90, angle, true, paint);
    }

    public void showPiechart(final int targetAngle) {
        angle = 0;
        //Timer和TimerTask
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                angle += 4;
                if (angle >= targetAngle) {
                    angle = targetAngle;
                    postInvalidate();
                    timer.cancel();
                }
                postInvalidate();
            }
        };
        //启动计时器
        timer.schedule(timerTask, 40, 40);
    }
}
