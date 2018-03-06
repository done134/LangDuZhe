package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.feature.home.HomeVideoDetailActivity;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：首页列表adapter
 */
public class HomeVideoAdapter extends BaseRecyclerViewAdapter<HomeVideoAdapter.ViewHolder> {

    private List<HomeVideoEntity.DataBean> list;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeVideoEntity.DataBean videoEntity = list.get(position);
        PicassoUtils.loadImageByurl(context,videoEntity.getImg(),holder.ivHomeVideoCover);
        holder.tvHomeVideoTag.setText("第"+videoEntity.getSeasonNum()+"季");
        holder.tvCommentCount.setText(String.valueOf(videoEntity.getCommentSum()));
        holder.tvThumbsCount.setText(String.valueOf(videoEntity.getLikeSum()));
        holder.tvVideoPlayCount.setText(String.valueOf(videoEntity.getWatchSum()));
        holder.tvVideoLength.setText(DateConvertUtils.secToTime(videoEntity.getDuration()));
        holder.tvVideoTitle.setText(videoEntity.getTitle());
        holder.tvThumbsCount.setChecked(videoEntity.getIsLike()==1);

    }

    public void setData(List<HomeVideoEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<HomeVideoEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public HomeVideoEntity.DataBean getItemInPosition(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<HomeVideoEntity.DataBean>  getList() {
        return list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_home_video_cover)
        ImageView ivHomeVideoCover;
        @BindView(R.id.tv_home_video_tag)
        TextView tvHomeVideoTag;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;

        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.tv_video_length)
        TextView tvVideoLength;

        ViewHolder(View itemView, HomeVideoAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(2, getLayoutPosition(), false);
                }
            });
            tvCommentCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(0,getLayoutPosition(),false);
                }
            });
            tvThumbsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(1, getLayoutPosition(),tvThumbsCount.isChecked());
                }
            });
        }
    }
}
