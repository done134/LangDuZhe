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
import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/2/4.
 * 说明：未发布朗读作品列表adapter
 */
public class NotPostAdapter extends BaseRecyclerViewAdapter {
    /**
     * 音频类型item
     */
    private final int VOICE_TYPE = 0;
    /**
     * 视频类型item
     */
    private final int VIDEO_TYPE = 1;

    private List<NotPostEntity> list;

    private OnButtonClickListener onButtonClickListener;
    private Context context;

    public NotPostAdapter(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null)
            context = parent.getContext();
        if (viewType == VOICE_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_not_post_voice, parent, false);
            return new VoiceHolder(itemView, this);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_not_post_video, parent, false);
            return new VideoHolder(itemView, this);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotPostEntity notPostEntity = list.get(position);
        if (holder instanceof VideoHolder) {
            ((VideoHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onDeleteClick(list.get(position));
                }
            });
            ((VideoHolder) holder).btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onPostClick(list.get(position));
                }
            });
            ((VideoHolder) holder).tvVideoTitle.setText(notPostEntity.readName);
            ((VideoHolder) holder).tvVideoLength.setText(getVoiceDuring(notPostEntity.readFilepath));
            PicassoUtils.loadImageByFile(context,((VideoHolder) holder).ivCover,notPostEntity.coverPath);
        } else if (holder instanceof VoiceHolder) {
            ((VoiceHolder) holder).tvVoiceTitle.setText(notPostEntity.readName);
            ((VoiceHolder) holder).tvVoiceLength.setText(getVoiceDuring(notPostEntity.readFilepath));
            ((VoiceHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onDeleteClick(list.get(position));
                }
            });
            ((VoiceHolder) holder).btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onPostClick(list.get(position));
                }
            });
        }
    }

    public void setList(List<NotPostEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<NotPostEntity> getList() {
        return list;
    }

    public void addList(List<NotPostEntity> newList) {
        if (list == null)
            list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).type == 0) {
            return VOICE_TYPE;
        } else {
            return VIDEO_TYPE;
        }
    }

    static class VoiceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_voice_title)
        TextView tvVoiceTitle;
        @BindView(R.id.btn_delete)
        TextView btnDelete;
        @BindView(R.id.btn_upload)
        TextView btnUpload;
        @BindView(R.id.tv_voice_length)
        TextView tvVoiceLength;
        VoiceHolder(View itemView, NotPostAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(this));
        }
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.btn_delete)
        TextView btnDelete;
        @BindView(R.id.btn_upload)
        TextView btnUpload;
        @BindView(R.id.tv_video_length)
        TextView tvVideoLength;


        VideoHolder(View itemView, NotPostAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(this));
        }
    }

    public interface OnButtonClickListener{
        void onDeleteClick(NotPostEntity postEntity);
        void onPostClick(NotPostEntity postEntity);
    }
    public static String getVoiceDuring(String mUri) {
        String duration = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            if (mUri != null) {
                HashMap<String, String> headers = null;
                headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                mmr.setDataSource(mUri, headers);
            }
            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        return duration;
    }

}
