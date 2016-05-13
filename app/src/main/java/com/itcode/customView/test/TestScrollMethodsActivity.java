package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.itcode.customView.R;

/**
 * Created by uatas990232 on 2016/5/11.
 */
public class TestScrollMethodsActivity extends Activity {
      View tvTestedView;
    int contentOffsetLeft = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll_methods);
        tvTestedView = findViewById(R.id.tvTestedView);
        contentOffsetLeft = tvTestedView.getLeft();
        findViewById(R.id.btViewMoveLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestedView.offsetLeftAndRight(50);
            }
        });
        findViewById(R.id.btViewMoveRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestedView.offsetLeftAndRight(-50);
            }
        });
        findViewById(R.id.btContentMoveLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentOffsetLeft += 50;
                tvTestedView.scrollTo(contentOffsetLeft, 0);
            }
        });
        findViewById(R.id.btContentMoveRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentOffsetLeft-=50;
                tvTestedView.scrollTo(contentOffsetLeft,0);
            }
        });
        findViewById(R.id.btByMoveRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestedView.scrollBy(50,0);
            }
        });
    }
}
