package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.entites.PavilionEntity;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.cctv.langduzhe.view.widget.ClickNotToggleCheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by gentleyin
 * on 2018/2/2.
 * 说明：我的收藏列表adapter
 */
public class MyCollectionAdapter extends ReadPavilionAdapter {

    private boolean isEditStatus;
    private List<HomeVideoEntity.DataBean> list;

    private List<HomeVideoEntity.DataBean> selectList = new ArrayList<>();

    private SetCheckListener setCheckListener = new SetCheckListener();
    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_read_pavilion_video, parent, false);
            return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeVideoEntity.DataBean dataBean = list.get(position);
            //视频
            if (isEditStatus) {
                if (selectList.contains(dataBean)) {
                    ((VideoHolder) holder).cbSelectThisRead.setChecked(true);
                } else {
                    ((VideoHolder) holder).cbSelectThisRead.setChecked(false);
                }
                ((VideoHolder) holder).cbSelectThisRead.setVisibility(View.VISIBLE);
            } else {
                ((VideoHolder) holder).cbSelectThisRead.setVisibility(View.GONE);
            }
            PicassoUtils.loadImageByurl(context,dataBean.getImg(),((VideoHolder) holder).ivCover);
            ((VideoHolder) holder).tvCommentCount.setText(String.valueOf(dataBean.getCommentSum()));
            ((VideoHolder) holder).tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
            ((VideoHolder) holder).tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
            ((VideoHolder) holder).tvUploaderName.setText(dataBean.getReaderName());
            ((VideoHolder) holder).tvUploadTime.setText(dataBean.getCreateDate());
            ((VideoHolder) holder).tvVideoTitle.setText(dataBean.getTitle());
            ((VideoHolder) holder).tvThumbsCount.setChecked(dataBean.getIsLike()==1);
        PicassoUtils.loadImageByurlerr(context, dataBean.getReaderImg(), ((VideoHolder) holder).ivServicePeopleIcon,R.mipmap.head_default_icon);
    }

    public HomeVideoEntity.DataBean getItemInPosition(int position) {
        return list.get(position);
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


    public void setEditStatus(boolean editStatus) {
        isEditStatus = editStatus;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public List<HomeVideoEntity.DataBean> getSelectList() {
        return selectList;
    }

    public void selectAll() {
        selectList = list;
        notifyDataSetChanged();
    }

    public boolean getIsSelectAll() {
        if (selectList.size() == list.size()) {
            return true;
        }else {
            return false;
        }
    }

    public void unSelectAll() {
        selectList = new ArrayList<>();
        notifyDataSetChanged();
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
        ClickNotToggleCheckBox tvThumbsCount;
        @BindView(R.id.cb_select_this_read)
        AppCompatCheckBox cbSelectThisRead;

        VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditStatus) {
                        setCheckListener.setOnCheck(cbSelectThisRead,getLayoutPosition());
                    } else {
                        onItemHolderClick(2, getLayoutPosition(), false);
                    }
                }
            });
            /*tvCommentCount.setOnClickListener(v -> onItemHolderClick(0,getLayoutPosition(),false));
            tvThumbsCount.setOnClickListener(v -> {
                if (CommonUtil.isFastClick()) {
                    onItemHolderClick(1, getLayoutPosition(), !tvThumbsCount.isChecked());
                }
            });*/
        }
    }

    class SetCheckListener {
        void setOnCheck(CheckBox checkBox,int position){
            checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    selectList.add(list.get(position));
                }else {
                    selectList.remove(list.get(position));
                }
        }
    }

}
