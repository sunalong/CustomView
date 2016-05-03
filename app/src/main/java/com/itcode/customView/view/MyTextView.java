package com.itcode.customView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.itcode.customView.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO:有bug，应让view可自适应文字改变大小
 * Created by sunalong on 2016/4/26.
 */
public class MyTextView extends View {
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
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
     * 控件背景边框颜色
     */
    int strokeColor;
    /**
     * 控件背景边框粗细
     */
    float strokeWidth;
    /**
     * 文字距图片的padding值
     */
    float textPaddingTop;
    /**
     * 文字背景方框/设置背景色
     */
    Rect textRect;
    /**
     * 图片所在的方框
     */
    Rect imageRect;
    /**
     * 全局的画笔
     */
    Paint mPaint;
    /**
     * 控件中的ImageView
     */
    Bitmap imageIcon;
    /**
     * 控件中ImageView的缩放规则
     */
    int imageIconScaleType;
    /**
     * 测量得到的控件的宽高
     */
    float width, height;

    public MyTextView(Context context) {
        this(context, null, 0);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mPaint.setTextSize(textSize);
        mPaint.getTextBounds(textDescription, 0, textDescription.length(), textRect);
        /**
         * 设置宽度的测量规则
         */
        if (widthMode == MeasureSpec.EXACTLY) {//match_parent,accurate
            width = widthSize;
            Log.d("test", "MeasureSpec.EXACTLY textWidth:" + width);
        } else {
            //计算出由谁决定宽：图片宽，则宽度则图片决定;文字宽，则宽度则文字决定。
            float widthByImg;
            float widthByText = getPaddingLeft() + getPaddingRight() + textRect.width();
            if (imageIcon != null) {
                widthByImg = getPaddingLeft() + getPaddingRight() + imageIcon.getWidth();
            } else {
                widthByImg = widthByText;
            }
            if (widthMode == MeasureSpec.AT_MOST) {//wrap_content
                width = Math.min(Math.max(widthByImg, widthByText), widthSize);
            }
        }
        /**
         * 设置高度的测量规则
         */
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            if (imageIcon != null)
                height = getPaddingTop() + textPaddingTop + getPaddingBottom() + imageIcon.getHeight() + textRect.height();
            else
                height = getPaddingTop() + textPaddingTop + getPaddingBottom() + textRect.height();
            height = Math.min(heightSize, height);
        }
        Log.i("test", "width:" + width + "  height:" + height);
        setMeasuredDimension((int) (width * 1000) / 1000, (int) (height * 1000) / 1000);
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
        Log.i("MyTEXTView", "strokeWidth:" + strokeWidth);
        mPaint.setStrokeWidth(strokeWidth);
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

        if (textRect.width() > width) {//当前设置的宽度小于字体需要的宽度，将字体缩略显示【MeasureSpec.EXACTLY】
            TextPaint textPaint = new TextPaint(mPaint);
            textDescription = TextUtils.ellipsize(textDescription, textPaint, width - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(textDescription, getPaddingLeft(), height - getPaddingBottom(), mPaint);
        } else {//字体居中显示
            canvas.drawText(textDescription, (width - textRect.width()) / 2, height - getPaddingBottom(), mPaint);
        }
        imageRect.left = getPaddingLeft();
        imageRect.right = (int) (width - getPaddingRight());
        imageRect.top = getPaddingTop();
        imageRect.bottom = (int) (height - getPaddingBottom() - textRect.height());
        switch (imageIconScaleType) {
            case IMAGE_SCALE_FITXY:
                Log.i("TAG", "==========1=====switch:");
                canvas.drawBitmap(imageIcon, null, imageRect, mPaint);
                break;
            case IMAGE_SCALE_CENTER:
                Log.i("TAG", "=======2========switch:");
                imageRect.left = ((int) (width - imageIcon.getWidth())) / 2;
                imageRect.right = ((int) (width + imageIcon.getWidth())) / 2;
                imageRect.top = ((int) (height - textRect.height() - imageIcon.getHeight())) / 2;
                imageRect.bottom = (int) ((height - textRect.height()) / 2 + imageIcon.getHeight() / 2);
                canvas.drawBitmap(imageIcon, null, imageRect, mPaint);
                break;
        }
    }

    /**
     * 使用attribute初始化自定的view
     */
    private void initView() {
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        textRect = new Rect();
        imageRect = new Rect();
        mPaint.getTextBounds(textDescription, 0, textDescription.length(), textRect);
    }

    /**
     * 初始化Attribute
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyTextView_text:
                    textDescription = typedArray.getString(attr);
                    break;
                case R.styleable.MyTextView_textSize:
                    //设置默认值为14sp,TypedValue可以把sp转化为px
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyTextView_textColor:
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.MyTextView_rectColor:
                    rectColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyTextView_strokeColor:
                    strokeColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.MyTextView_strokeWidth:
                    strokeWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyTextView_textPaddingTop:
                    textPaddingTop = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyTextView_imageIcon:
                    imageIcon = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.MyTextView_imageIconScaleType:
                    imageIconScaleType = typedArray.getInt(attr, 0);
                    break;
            }
        }
        typedArray.recycle();
    }

}
