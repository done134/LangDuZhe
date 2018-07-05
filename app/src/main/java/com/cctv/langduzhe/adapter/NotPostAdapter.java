package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.shuyu.waveview.AudioPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

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
    private boolean playEnd;

    private OnButtonClickListener onButtonClickListener;
    private Context context;

    public NotPostAdapter(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null)
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
//            /storage/emulated/0/JCamera/picture_1521053459903.jpg
            ((VideoHolder) holder).ivCover.setUp(notPostEntity.readFilepath, JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
            ((VideoHolder) holder).btnDelete.setOnClickListener(v -> onButtonClickListener.onDeleteClick(list.get(position)));
            ((VideoHolder) holder).btnUpload.setOnClickListener(v -> onButtonClickListener.onPostClick(list.get(position)));
            ((VideoHolder) holder).tvVideoTitle.setText(notPostEntity.readName);
            ((VideoHolder) holder).tvVideoLength.setText(notPostEntity.mediaLength);
            PicassoUtils.loadImageByurl(context, notPostEntity.coverPath, ((VideoHolder) holder).ivCover.thumbImageView);
        } else if (holder instanceof VoiceHolder) {
            ((VoiceHolder) holder).tvVoiceTitle.setText(notPostEntity.readName);
            ((VoiceHolder) holder).tvVoiceTime.setText(notPostEntity.mediaLength);
            ((VoiceHolder) holder).btnDelete.setOnClickListener(v -> onButtonClickListener.onDeleteClick(list.get(position)));
            ((VoiceHolder) holder).btnUpload.setOnClickListener(v -> onButtonClickListener.onPostClick(list.get(position)));
            ((VoiceHolder) holder).cbVoiceTitle.setOnClickListener(v -> playVoice(notPostEntity, (VoiceHolder) holder));

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
        return list == null ? 0 : list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).type == 0) {
            return VOICE_TYPE;
        } else {
            return VIDEO_TYPE;
        }
    }

    public int getPlayPosition() {
        if (playingHolder != null) {
            return playingHolder.getAdapterPosition();
        } else {
            return -1;
        }
    }

    static class VoiceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_delete)
        TextView btnDelete;
        @BindView(R.id.btn_upload)
        TextView btnUpload;
        @BindView(R.id.tv_voice_title)
        TextView tvVoiceTitle;
        @BindView(R.id.cb_voice_title)
        CheckBox cbVoiceTitle;
        @BindView(R.id.cb_voice_time)
        CheckBox cbVoiceTime;
        @BindView(R.id.tv_voice_time)
        TextView tvVoiceTime;

        VoiceHolder(View itemView, NotPostAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(this));
        }
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_view)
        JZVideoPlayerStandard ivCover;
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

    public interface OnButtonClickListener {
        void onDeleteClick(NotPostEntity postEntity);

        void onPostClick(NotPostEntity postEntity);
    }

    private AudioPlayer player;
    private Timer timer;
    private VoiceHolder playingHolder;

    private void playVoice(NotPostEntity dataBean, VoiceHolder holder) {


        if (player != null) {
            if (playingHolder != null && playingHolder.getAdapterPosition() == holder.getAdapterPosition()) {
                if (playEnd) {
                    player.stop();
                    play(dataBean, holder);
                    return;
                }
                if (player.isPlaying()) {
                    player.pause();
                    holder.cbVoiceTitle.setChecked(true);
                    holder.cbVoiceTime.setChecked(false);
                } else {
                    player.play();
                    holder.cbVoiceTitle.setChecked(false);
                    holder.cbVoiceTime.setChecked(true);
                    seekProgress(dataBean,holder);
                }
            } else {
                player.stop();
                player = null;
                playingHolder.cbVoiceTitle.setChecked(true);
                playingHolder.cbVoiceTime.setChecked(false);
                play(dataBean, holder);
                playingHolder = holder;
            }
        } else {
            play(dataBean, holder);
            playingHolder = holder;
        }
    }

    private void play(NotPostEntity dataBean, VoiceHolder holder) {
        playEnd = false;
        onItemHolderClick(0, holder.getAdapterPosition(), false);
        player = new AudioPlayer(context, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        player.pause();
                        playEnd = true;
                        holder.cbVoiceTitle.setChecked(true);
                        holder.cbVoiceTime.setChecked(false);
                        holder.tvVoiceTime.setText(dataBean.mediaLength);
                        playingHolder = null;
                        player = null;
                        break;

                }
            }
        });
        player.playUrl(dataBean.readFilepath);
        holder.cbVoiceTime.setChecked(true);
        seekProgress(dataBean, holder);
    }


    private void seekProgress(NotPostEntity dataBean, VoiceHolder holder) {
        if (timer != null) {
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (playEnd || player == null) {
                    return;
                }

                long position = player.getCurPosition();
                if (position > 0) {
                    ((BaseActivity)context).runOnUiThread(() -> {
                        int time = (int) (DateConvertUtils.timeToSec(dataBean.mediaLength) - (position/1000));
                        holder.tvVoiceTime.setText(DateConvertUtils.secToTime(time));
                    });
                }
            }
        }, 500, 1000);
    }

    public void pauseVoice() {
        if (player != null) {
            player.pause();
            playingHolder.cbVoiceTitle.setChecked(true);
            playingHolder.cbVoiceTime.setChecked(false);
            if(timer!=null) {
                timer.cancel();
            }
//            player = null;
        }
    }
}
