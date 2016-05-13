package com.itcode.customView.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.itcode.customView.callback.ItemTouchHelperViewHolder;
/**
 * @ClassName: ViewHoder
 * @Description: 通用viewHoder
 */
public class CommentViewHoder extends ViewHolder implements ItemTouchHelperViewHolder {
    private Context mContext;
    private SparseArray<View> views;
    private View parent;
    private static CommentViewHoder hoder;
    private int possion;

    public static CommentViewHoder getInstent(Context mContext, int possion, View arg1) {
        hoder = (CommentViewHoder) arg1.getTag();
        if (hoder == null) {
            synchronized (CommentViewHoder.class) {
                hoder = new CommentViewHoder(mContext, possion, arg1);
                arg1.setTag(hoder);
            }
        }
        return hoder;
    }

    public void setViewTag(Object Object) {
        if (parent != null) {
            parent.setTag(Object);
        }
    }

    protected CommentViewHoder(Context mContext, int possion, View arg1) {
        super(arg1);
        this.mContext = mContext;
        this.views = new SparseArray<View>();
        this.parent = arg1;
        this.possion = possion;
    }

    /**
     * @param @param  viewId
     * @param @return
     * @return T
     * @throws
     * @Title: getView
     * @Description: 获取控件
     */
    public <T> T getView(int viewId) {
        T view = (T) views.get(viewId);
        if (view == null) {
            synchronized (CommentViewHoder.class) {
                view = (T) parent.findViewById(viewId);
                views.put(viewId, (View) view);
            }
        }
        return view;
    }

    public void setPossion(int possion) {
        this.possion = possion;
    }

    public int getPossion() {
        return possion;
    }

    /**
     * @param @param viewId
     * @param @param text
     * @return void
     * @throws
     * @Title: setTextView
     * @Description: 设置textView内容
     */
    public CommentViewHoder setTextView(int viewId, String text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  strId
     * @param @return
     * @return ViewHoder
     * @throws
     * @Title: setTextView
     * @Description: 设置textView通过id
     */
    public CommentViewHoder setTextView(int viewId, int strId) {
        setTextView(viewId, mContext.getResources().getString(strId));
        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  tag
     * @param @return
     * @return ViewHoder
     * @throws
     * @Title: setTag
     * @Description: 设置标志
     */
    public CommentViewHoder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  resId
     * @param @return
     * @return ViewHoder
     * @throws
     * @Title: setBa
     * @Description:设置背景资源
     */
    public CommentViewHoder setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  color
     * @param @return
     * @return ViewHoder
     * @throws
     * @Title: setBackgroundColor
     * @Description:设置控件背景色
     */
    public CommentViewHoder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  listener
     * @param @return
     * @return ViewHoder
     * @throws
     * @Title: setViewOnclick
     * @Description: 设置控件点击事件
     */
    public CommentViewHoder setViewOnclick(int viewId, OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public CommentViewHoder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(colorId));
        return this;
    }

    /**
     * 设置字体粗体样式
     * @param viewId
     * @param shouldBold
     * @return
     */
    public CommentViewHoder setTextBold(int viewId, boolean shouldBold) {
        TextView view = getView(viewId);
        if (shouldBold) {
            view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else
            view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        return this;
    }

    /**
     * @param @param  viewId
     * @param @param  resId
     * @param @return
     * @return CommentViewHoder
     * @throws
     * @Title: setImageResource
     * @Description: 设置控件布局
     */
    public CommentViewHoder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    @Override
    public void onItemSelect() {
        itemView.setBackgroundColor(Color.LTGRAY);

    }

    @Override
    public void itemClear() {
        itemView.setBackgroundColor(0);
    }
}
