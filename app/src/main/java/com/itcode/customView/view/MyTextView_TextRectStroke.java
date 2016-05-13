package com.itcode.customView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.itcode.customView.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 有文字，有底色，有边框
 * Created by sunalong on 2016/4/26.
 */
public class MyTextView_TextRectStroke extends View {
    /**
     * 文字描述
     */
    String textDescription;
    /**
     * 文字大小
     */
    int textSize;
    /**
     * 文字颜色
     */
    int textColor;
    /**
     * 文字背景方框颜色
     */
    int rectColor;
    /**
     * 文字背景方框颜色
     */
    int strokeColor;
    /**
     * 文字背景方框颜色
     */
    float strokeWidth;
    /**
     * 文字距边框的padding值
     */
    float textPaddingLeft, textPaddingRight, textPaddingTop, textPaddingBottom;
    /**
     * 文字背景方框
     */
    Rect mRectBackground;
    /**
     * 全局的画笔
     */
    Paint mPaint;

    float width;
    float height;

    public MyTextView_TextRectStroke(Context context) {
        this(context, null, 0);
    }

    public MyTextView_TextRectStroke(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView_TextRectStroke(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textDescription = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(System.currentTimeMillis()));
                requestLayout();
                postInvalidate();
            }
        });
    }

    /**
     * 不重写onMesure出现的问题：
     * 当为自定义控件设置宽度为具体数据时，系统帮我们测量的高度和宽度是自己设置的结果；
     * 但当为自定义控件设置宽度为wrap_content时，系统帮我们测量的高度和宽度都是match_parent。
     * MesureSpec的specMode有三种类型：
     * EXACTLY：一般是设置了明确的值或者是match_parent
     * AT_MOST：子布局限制在一个最大值内，一般为wrap_content
     * UNSPECIFIED：子布局想要多大就多大，很少使用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        float textWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            Log.d("test", "MeasureSpec.EXACTLY textWidth:" + width);
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(textDescription, 0, textDescription.length(), mRectBackground);
            textWidth = mRectBackground.width();
            Log.d("test", "textWidth:" + width);
//            width = (int) (textPaddingLeft + textWidth + textPaddingRight);
            width = textPaddingLeft + textWidth + textPaddingRight;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(textDescription, 0, textDescription.length(), mRectBackground);
            float textHeight = mRectBackground.height();
//            height = (int) (textPaddingTop + textHeight + textPaddingBottom);
            height = textPaddingTop + textHeight + textPaddingBottom;
        }
        Log.i("test","width:"+width+"  height:"+height);
        setMeasuredDimension((int) (width * 1000) / 1000, (int) (height * 1000) / 1000);
//        setMeasuredDimension(width, height);
    }



    /**
     * 注意使用画布的顺序。
     * 后画的会覆盖先画的，所以小的放在后面绘制，大的放在前面绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //边框色
        Log.i("MyTEXTView","strokeWidth:"+strokeWidth);
//        strokeWidth=10;
//        strokeWidth = 3;
        mPaint.setStrokeWidth(strokeWidth);
//        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(strokeColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        //背景区域色块
        mPaint.setColor(rectColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth, mPaint);

        //文字区域
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        //正常情况，将字体居中
        canvas.drawText(textDescription, width / 2 - mRectBackground.width() * 1.0f / 2, height - textPaddingBottom, mPaint);
//        canvas.drawText(textDescription, textPaddingLeft, getMeasuredHeight() - textPaddingBottom, mPaint);
    }

    /**
     * 使用attribute初始化自定的view
     */
    private void initView() {
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mRectBackground = new Rect();
        mPaint.getTextBounds(textDescription, 0, textDescription.length(), mRectBackground);
    }

    /**
     * 初始化Attribute
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ItemView_text:
                    textDescription = typedArray.getString(attr);
                    break;
                case R.styleable.ItemView_textSize:
                    //设置默认值为14sp,TypedValue可以把sp转化为px
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ItemView_textColor:
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ItemView_rectColor:
                    rectColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ItemView_strokeColor:
                    strokeColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.ItemView_strokeWidth:
                    strokeWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
                    break;
//                case R.styleable.MyTextView_paddingLeft:
//                    textPaddingLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
//                    break;
//                case R.styleable.MyTextView_paddingRight:
//                    textPaddingRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
//                    break;
//                case R.styleable.MyTextView_paddingTop:
//                    textPaddingTop = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
//                    break;
//                case R.styleable.MyTextView_paddingBottom:
//                    textPaddingBottom = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
//                    break;
            }
        }
        typedArray.recycle();
    }

}
