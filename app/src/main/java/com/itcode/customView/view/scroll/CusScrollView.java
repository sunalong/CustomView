package com.itcode.customView.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CusScrollView extends ViewGroup {

    private int lastX = 0;
    private int currX = 0;
    private int offX = 0;

    /**
     * @param context
     */
    public CusScrollView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public CusScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CusScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

    }

    /*
    * (non-Javadoc)
    *
    * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
    */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(0 + i * getWidth(), 0, getWidth() + i * getWidth(),
                    getHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 只考虑水平方向
                lastX = (int) event.getX();
                return true;

            case MotionEvent.ACTION_MOVE:
                currX = (int) event.getX();
                offX = currX - lastX;
                scrollBy(-offX, 0);
                break;

            case MotionEvent.ACTION_UP:
                scrollTo(0, 0);
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}