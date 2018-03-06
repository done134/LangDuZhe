package com.cctv.langduzhe.adapter;


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
import com.cctv.langduzhe.view.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：朗读亭列表adapter
 */
public class ReadPavilionAdapter extends BaseRecyclerViewAdapter {

    /**
     * 音频类型item
     */
    private final int VOICE_TYPE = 0;
    /**
     * 视频类型item
     */
    private final int VIDEO_TYPE = 1;


    private List<HomeVideoEntity.DataBean> list;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VOICE_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_read_pavilion_voice, parent, false);
            return new VoiceHolder(itemView, this);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_read_pavilion_video, parent, false);
            return new VideoHolder(itemView, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeVideoEntity.DataBean dataBean = list.get(position);
        //音频
        if (holder instanceof VoiceHolder) {
            ((VoiceHolder) holder).tvCommentCount.setText(String.valueOf(dataBean.getCommentSum()));
            ((VoiceHolder) holder).tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
            ((VoiceHolder) holder).tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
            ((VoiceHolder) holder).tvUploaderName.setText(dataBean.getReaderName());
            ((VoiceHolder) holder).tvUploadTime.setText(dataBean.getCreateDate());
            ((VoiceHolder) holder).tvVoiceTitle.setText(dataBean.getTitle());
            ((VoiceHolder) holder).tvThumbsCount.setChecked(dataBean.getIsLike()==1);
        } else {
            //视频
            ((VideoHolder) holder).tvCommentCount.setText(String.valueOf(dataBean.getCommentSum()));
            ((VideoHolder) holder).tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
            ((VideoHolder) holder).tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
            ((VideoHolder) holder).tvUploaderName.setText(dataBean.getReaderName());
            ((VideoHolder) holder).tvUploadTime.setText(dataBean.getCreateDate());
            ((VideoHolder) holder).tvVideoTitle.setText(dataBean.getTitle());
            ((VideoHolder) holder).tvThumbsCount.setChecked(dataBean.getIsLike() == 1);
        }
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

    public List<HomeVideoEntity.DataBean> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!list.get(position).getType().equals("video")) {
            return VOICE_TYPE;
        } else {
            return VIDEO_TYPE;
        }
    }

     class VoiceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_service_people_icon)
        CircleImageView ivServicePeopleIcon;
        @BindView(R.id.tv_uploader_name)
        TextView tvUploaderName;
        @BindView(R.id.tv_upload_time)
        TextView tvUploadTime;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;
        @BindView(R.id.tv_voice_title)
                TextView tvVoiceTitle;

        VoiceHolder(View itemView, ReadPavilionAdapter messageAdapter) {
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
            tvThumbsCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onItemHolderClick(1, getLayoutPosition(),false);
                }
            });
        }
    }

     class VideoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_service_people_icon)
        CircleImageView ivServicePeopleIcon;
        @BindView(R.id.tv_uploader_name)
        TextView tvUploaderName;
        @BindView(R.id.tv_upload_time)
        TextView tvUploadTime;
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;

        VideoHolder(View itemView, ReadPavilionAdapter messageAdapter) {
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
            tvThumbsCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onItemHolderClick(1, getLayoutPosition(),false);
                }
            });
        }
    }
}