package com.cctv.langduzhe.adapter;

import android.content.Context;
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
import com.cctv.langduzhe.data.entites.VideoInfoEntity;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by gentleyin
 * on 2018/1/31.
 * 说明：首页视频详情下方视频片段列表adapter
 */
public class HomeSliceAdapter extends BaseRecyclerViewAdapter {


    private List<HomeVideoEntity.DataBean> list;
    private HomeVideoEntity.DataBean videoInfo;
    private Context context;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_slice, parent, false);
            return new ViewHolder(itemView, this);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_video_detail, parent, false);
            return new HeaderHolder(itemView, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).videoView.setUp(videoInfo.getPath(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
            ((HeaderHolder) holder).tvCommentCount.setText(String.valueOf(videoInfo.getCommentSum()));
            ((HeaderHolder) holder).tvThumbsCount.setText(String.valueOf(videoInfo.getLikeSum()));
            ((HeaderHolder) holder).tvVideoPlayCount.setText(String.valueOf(videoInfo.getWatchSum()));
            ((HeaderHolder) holder).tvThumbsCount.setChecked(videoInfo.getIsLike() == 1);
            PicassoUtils.loadImageByurl(context,videoInfo.getImg(),((HeaderHolder) holder).videoView.thumbImageView);
            if (isPause) {
                ((HeaderHolder) holder).videoView.release();
            }
        } else if(holder instanceof ViewHolder){
            HomeVideoEntity.DataBean video = list.get(position);
            ((ViewHolder) holder).tvSliceName.setText(video.getTitle());
            ((ViewHolder) holder).tvSliceTime.setText(DateConvertUtils.secToTime(video.getDuration()));
            PicassoUtils.loadImageByurl(context,videoInfo.getImg(),((ViewHolder) holder).ivSliceCover);
        }
    }

    private boolean isPause;
    public void pauseVideo() {
        isPause = true;
        notifyDataSetChanged();
    }

    public void setData(List<HomeVideoEntity.DataBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        this.list.add(0, null);
        notifyDataSetChanged();
    }

    public void addData(List<HomeVideoEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setVideoInfo(HomeVideoEntity.DataBean videoInfo) {
        this.videoInfo = videoInfo;
    }

    public HomeVideoEntity.DataBean getItemInPosition(int position) {
        return list.get(position);
    }



     class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_slice_cover)
        ImageView ivSliceCover;
        @BindView(R.id.tv_slice_time)
        TextView tvSliceTime;
        @BindView(R.id.tv_slice_name)
        TextView tvSliceName;

        ViewHolder(View itemView, HomeSliceAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//             itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(this));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(2, getLayoutPosition(), false);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }
    }

     class HeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_view)
        JZVideoPlayerStandard videoView;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;

        HeaderHolder(View itemView, HomeSliceAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
