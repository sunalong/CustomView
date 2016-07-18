package com.itcode.customView.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itcode.customView.R;
import com.itcode.customView.view.scroll.CusScrollView;

public class LauncherActivity extends Activity {

  private int[] images = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

  private CusScrollView mCusScrollView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);

    mCusScrollView = (CusScrollView) this.findViewById(R.id.CusScrollView);
    for (int i = 0; i < images.length; i++) {
      ImageView mImageView = new ImageView(this);
      mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//      mImageView.setBackgroundResource(images[i]);
      //TODO:为何会错,我日
      mImageView.setBackgroundResource(R.drawable.ic_launcher);
      mImageView.setLayoutParams(new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
      mCusScrollView.addView(mImageView);
    }

  }

}