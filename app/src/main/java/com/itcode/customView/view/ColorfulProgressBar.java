package com.itcode.customView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.itcode.customView.R;

/**
 * 可变色的progressBar
 * Created by sunalong on 2016/4/28.
 */
public class ColorfulProgressBar extends View {
    /**
     *
     */
    int firstColor;
    /**
     *
     */
    int secondColor;
    /**
     * 速度
     */
    int speed;
    /**
     * 环宽度
     */
    int circleWidth;
    /**
     * 当前进度
     */
    int currentProgress;
    Paint mPaint;
    /**
     * 可以改变颜色
     */
    boolean shouldChangeColor = false;

    public ColorfulProgressBar(Context context) {
        this(context, null, 0);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    currentProgress++;
                    if (currentProgress == 360) {
                        currentProgress = 0;//为了让其无穷尽的转下去
                        shouldChangeColor = !shouldChangeColor;
                    }
                    postInvalidate();//
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfulProgressBar);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ColorfulProgressBar_firstColor:
                    firstColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.ColorfulProgressBar_secondColor:
                    secondColor = typedArray.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.ColorfulProgressBar_circleWidth:
                    circleWidth = typedArray.getDimensionPixelSize(attr, 20);
                    break;
                case R.styleable.ColorfulProgressBar_speed:
                    speed = typedArray.getInt(attr, 20);
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int radius = centerX - circleWidth / 2;//内半径:/2的原因：画圆的时候设置的半径是圆心到画笔的中间 就是宽度一半的那个地方
        mPaint.setStrokeWidth(circleWidth);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);//只画外框stroke不画内容,即空心
//        RectF(float left, float top, float right, float bottom)
        RectF rectF = new RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius);

        if (shouldChangeColor) {
            mPaint.setColor(firstColor);//设置圆环的颜色
            canvas.drawCircle(centerX, centerX, radius, mPaint);//画出圆环
            mPaint.setColor(secondColor);
            canvas.drawArc(rectF, -90, currentProgress, false, mPaint);//根据进度画圆弧
        } else {
            mPaint.setColor(secondColor);
            canvas.drawCircle(centerX, centerX, radius, mPaint);
            mPaint.setColor(firstColor);
            canvas.drawArc(rectF, -90, currentProgress, false, mPaint);
        }
    }
}


































