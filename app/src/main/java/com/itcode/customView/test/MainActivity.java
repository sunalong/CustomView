package com.itcode.customView.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.itcode.customView.R;
import com.itcode.customView.adapter.CommentViewHoder;
import com.itcode.customView.adapter.RecyclerviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunalong on 2016/5/9.
 */
public class MainActivity extends Activity {
    List<Class> data;
    RecyclerView mRecyclerView;
    RecyclerviewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_single_touch_view);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        data = new ArrayList();
        data.add(ItemViewActivity.class);
        data.add(MyGroupViewTestView.class);
        data.add(MyWeichatViewActivity.class);
        data.add(MyColorfulProgressbarActivity.class);
        data.add(MyViewDragHelperActivity.class);
        data.add(YoutubeActivity.class);
        data.add(StickyActivity.class);
        data.add(StickyDownActivity.class);
        data.add(StickyDownAnimationActivity.class);
        data.add(TestScrollMethodsActivity.class);
        data.add(LauncherActivity.class);
        data.add(SingleTouchViewTestActivity.class);
        data.add(MatrixActivity.class);
        data.add(SingleTouchTextViewTestActivity.class);
        data.add(TestPathActivity.class);
        data.add(TextViewTestActivity.class);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
//        mRecyclerView.addItemDecoration(new Vertical);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdapter = new RecyclerviewAdapter<Class>(this, data, R.layout.item_recycler_view_main) {
            @Override
            public void toDoSelf(CommentViewHoder hoder, int position, Class clazz) {
                Log.i("ceshi", (position + 1) + ". " + clazz.getSimpleName());
                hoder.setTextView(R.id.tvNameItemMain, (position + 1) + ". " + clazz.getSimpleName());
            }
        };
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClick(new RecyclerviewAdapter.OnItemClick() {
            @Override
            public void itemClick(View view, int position) {
                openActivity(data.get(position));
            }
        });
    }

    private void openActivity(Class destClass) {
        Intent intent = new Intent(this, destClass);
        startActivity(intent);
    }
}
