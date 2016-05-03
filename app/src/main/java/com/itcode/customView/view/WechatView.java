package com.itcode.customView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.itcode.customView.R;

/**
 * Created by uatas990232 on 2016/4/28.
 */
public class WechatView extends View {
    int afterColor;//变化成的颜色
    int circleWidth;//色环的宽度
    int beforeColor;//最初的环的底色
    int splitSize;//每个块的间隔大小
    int spaceAngle;//空闲的角度
    int dotCount;//小块的个数
    Bitmap bgIcon;//圆环中间的图
    Paint mPaint;//全局通用的画笔
    Rect rect;//图片使用的rect
    /**
     * 当前进度
     */
    private int mCurrentCount = 3;

    public WechatView(Context context) {
        this(context, null, 0);
    }

    public WechatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WechatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        rect = new Rect();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WechatView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.WechatView_beforeColor:
                    beforeColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.WechatView_afterColor:
                    afterColor = typedArray.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.WechatView_circleWidth:
                    circleWidth = typedArray.getDimensionPixelSize(attr, 20);
                    break;
                case R.styleable.WechatView_dotCount:
                    dotCount = typedArray.getInt(attr, 20);
                    break;
                case R.styleable.WechatView_splitSize:
                    splitSize = typedArray.getInt(attr, 20);
                    break;
                case R.styleable.WechatView_spaceAngle:
                    spaceAngle = typedArray.getInt(attr, 0);
                case R.styleable.WechatView_bgIcon:
                    bgIcon = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(circleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//定义stroke两端形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        int centerX = getWidth() / 2; // 获取圆心的x坐标
        int radius = centerX - circleWidth / 2;//半径

        drawOval(canvas, centerX, radius);

        int relRadius = centerX - circleWidth / 2;
        rect.top = (int) (centerX - Math.sqrt(2) * 1.0f / 2 * (centerX - circleWidth));//内切正方形距离顶部长度
        rect.left = rect.top;
        rect.bottom = (int) (2 * centerX - rect.left);//内切正方形距离底部长度
        rect.right = rect.bottom;
        Log.i("distance", "relRadius:" + relRadius + " centerX:" + centerX + " rect.top:" + rect.top + " left:" + rect.left + " right:" + rect.right + " bottom:" + rect.bottom);
        if (bgIcon.getWidth() < Math.sqrt(2) * relRadius) {//图片比较小，将图片原尺寸放到正中心
            rect.left = radius - bgIcon.getWidth() / 2;
            rect.right = radius + bgIcon.getWidth() / 2;
            rect.top = rect.left;
            rect.bottom = rect.right;
            Log.i("2distance", "relRadius:" + relRadius + " centerX:" + centerX + " rect.top:" + rect.top + " left:" + rect.left + " right:" + rect.right + " bottom:" + rect.bottom);
        }
        canvas.drawBitmap(bgIcon, null, rect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     *
     * @param canvas
     * @param centerX
     * @param radius
     */
    private void drawOval(Canvas canvas, int centerX, int radius) {
        /**
         * 根据需要画的个数及间隙计算每个方块所占的比例*360
         */
        float startAngleCalcu = 90 + spaceAngle / 2;
        float sweepAngleCalcu = 360 - spaceAngle;
        float itemSize = (sweepAngleCalcu - (dotCount - 1) * splitSize) / dotCount;
        RectF oval = new RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius);
        mPaint.setColor(beforeColor);
        for (int i = 0; i < dotCount; i++) {
            canvas.drawArc(oval, i * (itemSize + splitSize) + startAngleCalcu, itemSize, false, mPaint);//根据小块个数画圆弧
        }
        mPaint.setColor(afterColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + splitSize) + startAngleCalcu, itemSize, false, mPaint);//根据小块个数画圆弧
        }
    }

    int xDown;
    int xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown)//下滑
                    decreaseCount(true);
                else
                    decreaseCount(false);
                break;
        }
        return true;
    }

    /**
     * 增加或减少变化的块的个数
     *
     * @param shouldDecrease
     */
    private void decreaseCount(boolean shouldDecrease) {
        if (shouldDecrease) {
            if (mCurrentCount > 0)
                mCurrentCount--;
        } else {
            if (mCurrentCount < dotCount)
                mCurrentCount++;
        }

        postInvalidate();
    }

}
