package com.itcode.customView.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @ClassName: RecyclerviewAdapter
 * @Description: 通用adapter
 */
public abstract class RecyclerviewAdapter<T> extends RecyclerView.Adapter {
    protected static final int VIEW_TYPE_ENABLE = 0;
    protected static final int VIEW_TYPE_DISABLE = 1;

    private Context mContext;
    protected List<T> data;
    private int layoutId;

    public RecyclerviewAdapter(Context mContext, List<T> data, int layoutId) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.layoutId = layoutId;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    public Object getItem(int arg1) {
        return data.get(arg1);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof CommentViewHoder) {
            CommentViewHoder commentViewHoder = (CommentViewHoder) holder;
            commentViewHoder.setPossion(position);
            commentViewHoder.setViewTag(commentViewHoder);
            toDoSelf(commentViewHoder,position, data.get(position));
        }
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClick.itemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, arg0, false);
        Log.d("RecyclerviewAdapter", arg1 + "");
        if (onItemClick != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        CommentViewHoder hoder = (CommentViewHoder) v.getTag();
                        onItemClick.itemClick(v, hoder.getPossion());
                    }
                }
            });
        }
        return CommentViewHoder.getInstent(mContext, arg1, view);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: toDoSelf
     * @Description: 功能类
     */
    public abstract void toDoSelf(CommentViewHoder hoder,int position, T data);

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void itemClick(View view, int position);
    }


}
