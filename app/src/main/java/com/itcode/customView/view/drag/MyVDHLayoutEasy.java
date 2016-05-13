package com.itcode.customView.view.drag;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 带ViewDragHelper的ViewGroup
 * 最简单的使用方式
 * Created by sunalong on 2016/5/4.
 */
public class MyVDHLayoutEasy extends LinearLayout {
    ViewDragHelper viewDragHelper;

    public MyVDHLayoutEasy(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViewDragHelper();
    }

    /**
     * 1.创建ViewDragHelper实例
     */
    private void createViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            /**
             * 限制子View在水平轴上的拖动，默认的实现是不允许拖动的;
             * 继承类必须实现这个方法并提供想要的轨道（水平或竖直方向）
             * @param child 被拖动的View
             * @param left 沿轴方向运动的尝试
             * @param dx 建议的向左的改变
             * @return 新的X方向上的固定位置【由于一般给的都是可变的值，所以感受不出来，
             * 将其返回值改为一个固定值即可看出效果】
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //通过使用viewDragHelper.shouldInterceptTouchEvent来决定是否应该拦截当前事件。
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过viewDragHelper.processTouchEvent处理事件
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}



























