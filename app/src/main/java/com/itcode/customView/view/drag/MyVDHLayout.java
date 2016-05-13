package com.itcode.customView.view.drag;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 带ViewDragHelper的ViewGroup
 * Created by sunalong on 2016/5/4.
 */
public class MyVDHLayout extends LinearLayout {
    ViewDragHelper viewDragHelper;
    View firstView;
    View secondView;
    View thirdView;
//    public MyVDHLayout(Context context) {
//        this(context, null, 0);
//    }

    public MyVDHLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        super(context, attrs);
        createViewDragHelper();
    }

//    public MyVDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
////        createViewDragHelper();
//    }

    /**
     * 1.创建ViewDragHelper实例
     */
    private void createViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child !=thirdView;//第三个View不允许直接拖动
            }

            /**
             * 限制子View在水平轴上的拖动，默认的实现是不允许拖动的;继承类必须实现这个方法并提供想要的轨道（水平或竖直方向）
             * @param child 被拖动的View
             * @param left 沿轴方向运动的尝试
             * @param dx 建议的向左的改变
             * @return 新的X方向上的固定位置【由于一般给的都是可变的值，所以感受不出来，将其返回值改为一个固定值即可看出效果】
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                return super.clampViewPositionHorizontal(child, left, dx);
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                Log.i(getClass().getSimpleName(), "======getWidth:"+getWidth()+" leftBound:" + leftBound +" rightBound:"+rightBound+ " childWidth:"+child.getWidth()+" left:" + left + " dx:" + dx+" newLeft:"+newLeft);
//                return left;
//
                return newLeft;
//                return child.getLeft();
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
//                return super.clampViewPositionVertical(child, top, dy);
                return top;
//                return 200;
//                return child.getTop();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if(releasedChild == secondView){//手指松开后，第二个view可以回到原处
                    viewDragHelper.settleCapturedViewAt(secondViewOriginPos.x,secondViewOriginPos.y);
                    invalidate();
                }
            }

            /**
             * 我们在onEdgeDragStarted回调方法中，主动通过captureChildView对其进行捕获，
             * 该方法可以绕过tryCaptureView，所以我们的tryCaptureView虽然并未返回true，但却不影响。
             * 注意如果需要使用边界检测需要添加上viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                viewDragHelper.captureChildView(thirdView,pointerId);
            }

            /**
             * 必须重写这两个方法，否则无法拖动view
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
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

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    private Point secondViewOriginPos = new Point();

    /**
     * 在onLayout之后保存第二个View的位置信息到point中
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        secondViewOriginPos.x = secondView.getLeft();
        secondViewOriginPos.y = secondView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstView = getChildAt(0);
        secondView = getChildAt(1);
        thirdView = getChildAt(2);
    }
}



























