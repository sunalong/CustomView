package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.itcode.customView.R;
import com.itcode.customView.view.scroll.ObservableScrollView;

public class StickyActivity extends Activity implements ObservableScrollView.Callbacks {

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

    @Override
    public void onScrollchanged(int t) {
        int translation = Math.max(t,mPlaceholderView.getTop());
        txtContent.setTranslationY(translation);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }


}