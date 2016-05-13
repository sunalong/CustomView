package com.itcode.customView.view.drag;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.itcode.customView.R;

/**
 * Created by uatas990232 on 2016/5/9.
 */
public class MyYoutubeLayout extends ViewGroup {
    private float mInitialMotionX;
    private float mInitialMotionY;
    float mDragOffset;
    int mTop;
    int mDragRange;
    private View mHeaderView;
    private View mDescView;
    private ViewDragHelper mDragHelper;

    public MyYoutubeLayout(Context context) {
        this(context, null);
    }

    public MyYoutubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyYoutubeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new YoutubeDragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.header);
        mDescView = findViewById(R.id.desc);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());//mTop是变值，所以这两个View在屏幕上的位置可一直变化
        mDescView.layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);//底可以推出屏幕
    }

    private class YoutubeDragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            mTop = top;
            mDragOffset = (float) top / mDragRange;
            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleX(1 - mDragOffset / 2);
            mHeaderView.setScaleY(1 - mDragOffset / 2);
            mDescView.setAlpha(1 - mDragOffset);
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int top = getPaddingTop();
            if (yvel > 0 || yvel == 0 && mDragOffset > 0.5f) {
                top += mDragRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
//            return super.getViewVerticalDragRange(child);
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            return super.clampViewPositionVertical(child, top, dy);

            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight() - mHeaderView.getPaddingBottom();

            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        if (action != MotionEvent.ACTION_DOWN) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        float x = ev.getX();
        float y = ev.getY();
        boolean interceptTap = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
                break;
            case MotionEvent.ACTION_MOVE:
                float abx = Math.abs(x - mInitialMotionX);
                float aby = Math.abs(y - mInitialMotionY);
                int slop = mDragHelper.getTouchSlop();
                if (aby > slop && abx > aby) {
                    mDragHelper.cancel();
                    return false;
                }
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                float dx = x - mInitialMotionX;
                float dy = y - mInitialMotionY;
                int slop = mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1.0f);
                    } else {
                        smoothSlideTo(0f);

                    }
                }
        }
        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y) || isViewHit(mDescView, (int) x, (int) y);
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() && screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    private boolean smoothSlideTo(float slideOffset) {
        int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);
        if (mDragHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    public void maximize(){
        smoothSlideTo(0.0f);
    }
    public void minimize(){
        smoothSlideTo(1.0f);
    }
}
