package com.cctv.langduzhe.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by gentleyin on 2018/1/13.
 * 所有RecyclerView的adapter的基类
 * 封装了item 点击事件，以及列表数据的增删操作
 */
public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    protected List<T> mItemList = new ArrayList<>();

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    protected void onItemHolderClick(int optType, int position,boolean yesOrNo) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(optType,position,yesOrNo);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public List<T> getItemList() {
        return mItemList;
    }

    @UiThread
    public void setItemList(List<T> itemList) {
        mItemList.clear();
        mItemList = itemList;

        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mItemList.get(position);
    }

    @UiThread
    public void add(@NonNull T item) {
        mItemList.add(item);
        notifyItemInserted(mItemList.size() - 1);
    }

    @UiThread
    public void addAll(@NonNull T[] items) {
        addAll(Arrays.asList(items));
    }

    @UiThread
    public void addAll(@NonNull Collection<T> items) {
        mItemList.addAll(items);
        notifyItemRangeInserted(mItemList.size() - items.size(), items.size());
    }

    @UiThread
    public void add(int position, @NonNull T item) {
        mItemList.add(position, item);
        notifyItemInserted(position);
    }

    @UiThread
    public void remove(int position) {
        mItemList.remove(position);
        notifyItemRemoved(position);
    }

    @UiThread
    public void remove(@NonNull T item) {
        remove(mItemList.indexOf(item));
    }

    @UiThread
    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mItemList == null || mItemList.isEmpty();
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param position The position of the view in the adapter.
         */
        void onItemClick(int optType, int position,boolean yesOrNo);
    }


}
