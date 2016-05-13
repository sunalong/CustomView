package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.itcode.customView.R;
import com.itcode.customView.view.scroll.ObservableScrollView;

public class StickyDownActivity extends Activity implements ObservableScrollView.Callbacks {
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
    /**
     * 高度
     */
    private int mViewHeight;
    private int minRaw;



    private TextView txtContent;
    private ObservableScrollView observableScrollView;
    private View mPlaceholderView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_test);
        mPlaceholderView = (View)findViewById(R.id.placeholder);
        txtContent = (TextView) findViewById(R.id.sticky);
        txtContent.setText(getClass().getSimpleName());
        observableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        observableScrollView.setCallbacks(this);
        /**
         * 当布局绘制完全的时候我们才可以得到view.getTop()等
         */
        observableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScrollchanged(observableScrollView.getScrollY());

            }
        });
    }

//    @Override
//    public void onScrollchanged(int t) {
//        int translation = Math.max(t,mPlaceholderView.getTop());
//        txtContent.setTranslationY(translation);
//    }

    @Override
    public void onScrollchanged(int t) {
        int raw = mPlaceholderView.getTop() - t;
        int translationY = 0;
        switch (mState) {
            case STATE_ONSCREEN:
                // Log.d("TAG","STATE_ONSCREEN");
                if (raw < -mViewHeight) {
                    mState = STATE_OFFSCREEN;
                    minRaw = raw;
                }
                translationY = raw;
                break;
            case STATE_OFFSCREEN:
                // Log.d("TAG","STATE_OFFSCREEN");
                if (raw<=minRaw){
                    minRaw = raw;
                }
                else{
                    mState = STATE_RETURING;
                }
                translationY = raw;
                break;
            case STATE_RETURING:
                translationY = (raw - minRaw) - mViewHeight;
                Log.d("TAG", "translationY:" + translationY);
                if (translationY > 0) {
                    translationY = 0;
                    minRaw = raw - mViewHeight;
                }

                if (raw > 0) {
                    mState = STATE_ONSCREEN;
                    translationY = raw;
                }

                if (translationY < -mViewHeight) {
                    mState = STATE_OFFSCREEN;
                    minRaw = raw;
                }
                break;
        }
        txtContent.setTranslationY(translationY+t);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }


}