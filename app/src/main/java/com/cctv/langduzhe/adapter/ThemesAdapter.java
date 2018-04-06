package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.ThemeEntity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public class ThemesAdapter extends BaseRecyclerViewAdapter<ThemesAdapter.ViewHolder> {

    private List<ThemeEntity.DataBean> list;
    private Context context;

    public ThemesAdapter(Context context) {
        this.context = context;
    }

    public List<ThemeEntity.DataBean> getList() {
        return list;
    }

    public void setList(List<ThemeEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ThemeEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_themes, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ThemeEntity.DataBean theme = list.get(position);
        holder.tvThemeTitle.setText(theme.getTitle());
        PicassoUtils.loadImageByurl(context,theme.getImg(),holder.ivThemeCover);
    }

    public ThemeEntity.DataBean getItemInPosition(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_theme_cover)
        ImageView ivThemeCover;
        @BindView(R.id.tv_theme_title)
        TextView tvThemeTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onItemHolderClick(0, getLayoutPosition(), false));

        }
    }
}
