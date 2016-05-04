package com.itcode.customView.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用途：加深对ViewGroup的理解
 * 自定义ViewGroup步骤：
 * 1.确定该ViewGroup的LayoutParams
 * 2.在onMeasure中计算childView的测量值及模式。并设置自己的宽高
 * 3.onLayout对其所有childView进行定位（设置childView的绘制区域）
 *
 * Created by sunalong on 2016/5/3.
 */
public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 决定该ViewGroup的LayoutParams
     * 本例只需要ViewGroup能够支持margin即可，所以直接使用系统的MarginLayoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);
        Log.i(getClass().getSimpleName(), "generateLayoutParams----------AttributeSet---------");
        return new MarginLayoutParams(getContext(), attrs);
    }

    View childView;//childView
    int childWidth;//childView的宽
    int childHeight;//childView的高
    MarginLayoutParams childLayoutParams;//childView的layoutParams
    int totalWidthUp = 0;//上面两个ChildView的宽度
    int totalWidthDown = 0;//下面两个ChildView的宽度
    int totalHeightLeft = 0;//左面两个ChildView的高度
    int totalHeightRight = 0;//右面两个ChildView的高度
    int childViewCount;//childView的个数

    /**
     * 在onMeasure中计算childView的测量值及模式。并设置自己的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        获取此ViewGroup上级容器为其推荐的宽高及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算出所有的childView的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        totalWidthUp = totalWidthDown = totalHeightLeft = totalHeightRight = 0;//因为onMeasure会多次执行，所以要在此赋值为0
        childViewCount = getChildCount();
        //根据childView的宽、高及margin,计算ViewGroup在wrap_content时的宽和高
        for (int index = 0; index < childViewCount; index++) {
            childView = getChildAt(index);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
            switch (index) {
                case 0:
                    totalWidthUp += childWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
                    totalHeightLeft += childHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin;
                    Log.i(getClass().getSimpleName(), "==0===twu:" + totalWidthUp + " cw:" + childWidth + " lM:" + childLayoutParams.leftMargin + " rM:" + childLayoutParams.rightMargin);
                    Log.i(getClass().getSimpleName(), "==0===thl:" + totalHeightLeft + " ch:" + childHeight + " tM:" + childLayoutParams.topMargin + " bM:" + childLayoutParams.bottomMargin);
                    break;
                case 1:
                    totalWidthUp += childWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
                    totalHeightRight += childHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin;
                    Log.i(getClass().getSimpleName(), "==1===twu:" + totalWidthUp + " cw:" + childWidth + " lM:" + childLayoutParams.leftMargin + " rM:" + childLayoutParams.rightMargin);
                    Log.i(getClass().getSimpleName(), "==1===thl:" + totalHeightLeft + " ch:" + childHeight + " tM:" + childLayoutParams.topMargin + " bM:" + childLayoutParams.bottomMargin);
                    break;
                case 2:
                    totalWidthDown += childWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
                    totalHeightLeft += childHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin;
                    Log.i(getClass().getSimpleName(), "==2===twu:" + totalWidthUp + " cw:" + childWidth + " lM:" + childLayoutParams.leftMargin + " rM:" + childLayoutParams.rightMargin);
                    Log.i(getClass().getSimpleName(), "==2===thl:" + totalHeightLeft + " ch:" + childHeight + " tM:" + childLayoutParams.topMargin + " bM:" + childLayoutParams.bottomMargin);
                    break;
                case 3:
                    totalWidthDown += childWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
                    totalHeightRight += childHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin;
                    Log.i(getClass().getSimpleName(), "==3===twu:" + totalWidthUp + " cw:" + childWidth + " lM:" + childLayoutParams.leftMargin + " rM:" + childLayoutParams.rightMargin);
                    Log.i(getClass().getSimpleName(), "==3===thl:" + totalHeightLeft + " ch:" + childHeight + " tM:" + childLayoutParams.topMargin + " bM:" + childLayoutParams.bottomMargin);
                    break;
            }
        }
        Log.i(getClass().getSimpleName(), "====before=======widthSize:" + widthSize + " heightSize:" + heightSize);
        //如果宽高属性值为wrap_content,则设置为上述计算的值;否则设置为其父容器传入的宽高
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = Math.max(totalWidthUp, totalWidthDown);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = Math.max(totalHeightRight, totalHeightLeft);
        }
        Log.i(getClass().getSimpleName(), "=====after======widthSize:" + widthSize + " heightSize:" + heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * onLayout对其所有childView进行定位（设置childView的绘制区域）
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int index = 0; index < childViewCount; index++) {
            childView = getChildAt(index);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childLeft = 0, childTop = 0, childBottom = 0, childRight = 0;//因为onLayout会多次执行，所以要在此赋值为0
            switch (index) {
                case 0://右上角的marginLeft、marginBottom不起作用
                    childLeft = childLayoutParams.leftMargin;
                    childTop = childLayoutParams.topMargin;
                    Log.i("MyViewGroup", "childWidth:" + childWidth + " childHeight:" + childHeight + "layParams.leftMargin:" + childLayoutParams.leftMargin + " rightMargin:" + childLayoutParams.rightMargin + " ====0=======childLeft:" + childLeft + " childTop:" + childTop + " getWidth:" + getWidth() + " getHeight:" + getHeight());
                    break;
                case 1://左上角的marginBottom、marginRight不起作用
                    //此处的getWidth获取的是onMeasure中确定的ViewGroup的宽
                    childLeft = getWidth() - childWidth - childLayoutParams.rightMargin;
                    childTop = childLayoutParams.topMargin;
                    Log.i("MyViewGroup", "childWidth:" + childWidth + " childHeight:" + childHeight + "layParams.leftMargin:" + childLayoutParams.leftMargin + " rightMargin:" + childLayoutParams.rightMargin + " ====1=======childLeft:" + childLeft + " childTop:" + childTop + " getWidth:" + getWidth() + " getHeight:" + getHeight());
                    break;
                case 2://左下角的marginTop、marginRight不起作用
                    childLeft = childLayoutParams.leftMargin;
                    childTop = getHeight() - childHeight - childLayoutParams.bottomMargin;
                    Log.i("MyViewGroup", "childWidth:" + childWidth + " childHeight:" + childHeight + "layParams.leftMargin:" + childLayoutParams.leftMargin + " rightMargin:" + childLayoutParams.rightMargin + " ====2=======childLeft:" + childLeft + " childTop:" + childTop + " getWidth:" + getWidth() + " getHeight:" + getHeight());
                    break;
                case 3://右下角的marginLeft、marginTop不起作用
                    childLeft = getWidth() - childWidth - childLayoutParams.rightMargin;
                    childTop = getHeight() - childHeight - childLayoutParams.bottomMargin;
                    Log.i("MyViewGroup", "childWidth:" + childWidth + " childHeight:" + childHeight + "layParams.leftMargin:" + childLayoutParams.leftMargin + " rightMargin:" + childLayoutParams.rightMargin + " ====3=======childLeft:" + childLeft + " childTop:" + childTop + " getWidth:" + getWidth() + " getHeight:" + getHeight());
                    break;
            }
            childRight = childLeft + childWidth;
            childBottom = childTop + childHeight;
            childView.layout(childLeft, childTop, childRight, childBottom);
        }
    }

}