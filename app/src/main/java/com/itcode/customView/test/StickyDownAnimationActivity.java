package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.itcode.customView.R;
import com.itcode.customView.view.scroll.ObservableScrollView;

public class StickyDownAnimationActivity extends Activity implements ObservableScrollView.Callbacks {
    /**
     * 上滑动状态
     */
    private static final int STATE_ONSCREEN = 0;
    /**
     * 上滑动至完全遮盖住mPlaceholderView
     */
    private static final int STATE_OFFSCREEN = 1;
    /**
     * 完全遮盖住时，下滑状态
     */
    private static final int STATE_RETURING = 2;
    private int mState = STATE_ONSCREEN;


    private int mMaxScrollY;
    private int mQuickReturnHeight;

    private TextView stickyView;
    private ObservableScrollView observableScrollView;
    private View mPlaceholderView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_test);
        mPlaceholderView = findViewById(R.id.placeholder);
        stickyView = (TextView) findViewById(R.id.sticky);
        stickyView.setText(getClass().getSimpleName());
        observableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        observableScrollView.setCallbacks(this);
        /**
         * 当布局绘制完全的时候我们才可以得到view.getTop()等
         */
        observableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScrollchanged(observableScrollView.getScrollY());
                mMaxScrollY = observableScrollView.computeVerticalScrollRange()
                        - observableScrollView.getHeight();
                mQuickReturnHeight = stickyView.getHeight();
            }
        });
    }

    private ScrollSettleHandler mScrollSettleHandler = new ScrollSettleHandler();
    private int mMinRawY = 0;

    @Override
    public void onScrollchanged(int scrollY) {
        scrollY = Math.min(mMaxScrollY, scrollY);

        mScrollSettleHandler.onScroll(scrollY);

        int rawY = mPlaceholderView.getTop() - scrollY;
        int translationY = 0;

        switch (mState) {
            case STATE_OFFSCREEN:
                if (rawY <= mMinRawY) {
                    mMinRawY = rawY;
                } else {
                    mState = STATE_RETURING;
                }
                translationY = rawY;
                break;

            case STATE_ONSCREEN:
                if (rawY < -mQuickReturnHeight) {
                    mState = STATE_OFFSCREEN;
                    mMinRawY = rawY;
                }
                translationY = rawY;
                break;

            case STATE_RETURING:
                translationY = (rawY - mMinRawY) - mQuickReturnHeight;
                if (translationY > 0) {
                    translationY = 0;
                    mMinRawY = rawY - mQuickReturnHeight;
                }

                if (rawY > 0) {
                    mState = STATE_ONSCREEN;
                    translationY = rawY;
                }

                if (translationY < -mQuickReturnHeight) {
                    mState = STATE_OFFSCREEN;
                    mMinRawY = rawY;
                }
                break;
        }
        stickyView.animate().cancel();
        stickyView.setTranslationY(translationY + scrollY);
    }

    @Override
    public void onDownMotionEvent() {
        mScrollSettleHandler.setSettleEnabled(false);
    }

    @Override
    public void onUpOrCancelMotionEvent() {
        mScrollSettleHandler.setSettleEnabled(true);
        mScrollSettleHandler.onScroll(observableScrollView.getScrollY());
    }

    private class ScrollSettleHandler extends Handler {
        private static final int SETTLE_DELAY_MILLIS = 100;

        private int mSettledScrollY = Integer.MIN_VALUE;
        private boolean mSettleEnabled;

        public void onScroll(int scrollY) {
            if (mSettledScrollY != scrollY) {
                // Clear any pending messages and post delayed
                removeMessages(0);
                sendEmptyMessageDelayed(0, SETTLE_DELAY_MILLIS);
                mSettledScrollY = scrollY;
            }
        }

        public void setSettleEnabled(boolean settleEnabled) {
            mSettleEnabled = settleEnabled;
        }

        @Override
        public void handleMessage(Message msg) {
            // Handle the scroll settling.
            if (STATE_RETURING == mState && mSettleEnabled) {
                int mDestTranslationY;
                if (mSettledScrollY - stickyView.getTranslationY() > mQuickReturnHeight / 2) {
                    mState = STATE_OFFSCREEN;
                    mDestTranslationY = Math.max(
                            mSettledScrollY - mQuickReturnHeight,
                            mPlaceholderView.getTop());
                } else {
                    mDestTranslationY = mSettledScrollY;
                }

                mMinRawY = mPlaceholderView.getTop() - mQuickReturnHeight - mDestTranslationY;
                stickyView.animate().translationY(mDestTranslationY);
            }
            mSettledScrollY = Integer.MIN_VALUE; // reset
        }
    }
}