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
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.cctv.langduzhe.view.widget.ClickNotToggleCheckBox;
import com.shuyu.waveview.AudioPlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/4/25.
 * 说明：
 */
public class MineReadAdapter  extends BaseRecyclerViewAdapter {

    /**
     * 音频类型item
     */
    private final int VOICE_TYPE = 0;
    /**
     * 视频类型item
     */
    private final int VIDEO_TYPE = 1;


    private List<HomeVideoEntity.DataBean> list;
    private Context context;
    private boolean playEnd;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        if (viewType == VOICE_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mine_voice, parent, false);
            return new MineReadAdapter.VoiceHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mine_video, parent, false);
            return new MineReadAdapter.VideoHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeVideoEntity.DataBean dataBean = list.get(position);
        //音频
        if (holder instanceof MineReadAdapter.VoiceHolder) {
            ((MineReadAdapter.VoiceHolder) holder).tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
            ((MineReadAdapter.VoiceHolder) holder).tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
            ((MineReadAdapter.VoiceHolder) holder).tvUploaderName.setText(dataBean.getReaderName());
            ((MineReadAdapter.VoiceHolder) holder).tvUploadTime.setText(dataBean.getCreateDate());
            ((MineReadAdapter.VoiceHolder) holder).tvVoiceTitle.setText(dataBean.getTitle());
            ((MineReadAdapter.VoiceHolder) holder).tvThumbsCount.setChecked(dataBean.getIsLike() == 1);
            ((MineReadAdapter.VoiceHolder) holder).tvVoiceTime.setText(DateConvertUtils.secToTime(dataBean.getDuration()));
            ((MineReadAdapter.VoiceHolder) holder).cbVoiceTitle.setOnClickListener(v -> playVoice(dataBean, (MineReadAdapter.VoiceHolder) holder));
            PicassoUtils.loadImageByurlerr(context, dataBean.getReaderImg(), ((MineReadAdapter.VoiceHolder) holder).ivServicePeopleIcon,R.mipmap.head_default_icon);
        } else {
            //视频
            ((MineReadAdapter.VideoHolder) holder).tvCommentCount.setText(String.valueOf(dataBean.getCommentSum()));
            ((MineReadAdapter.VideoHolder) holder).tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
            ((MineReadAdapter.VideoHolder) holder).tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
            ((MineReadAdapter.VideoHolder) holder).tvUploaderName.setText(dataBean.getReaderName());
            ((MineReadAdapter.VideoHolder) holder).tvUploadTime.setText(dataBean.getCreateDate());
            ((MineReadAdapter.VideoHolder) holder).tvVideoTitle.setText(dataBean.getTitle());
            ((MineReadAdapter.VideoHolder) holder).tvThumbsCount.setChecked(dataBean.getIsLike() == 1);
            PicassoUtils.loadImageByurlerr(context, dataBean.getReaderImg(), ((MineReadAdapter.VideoHolder) holder).ivServicePeopleIcon,R.mipmap.head_default_icon);
//            PicassoUtils.loadImageByurl(context, dataBean.getImg(), ((VideoHolder) holder).ivCover);
            Picasso.with(context).load(dataBean.getImg()).into(((MineReadAdapter.VideoHolder) holder).ivCover);
            ((MineReadAdapter.VideoHolder) holder).tvMediaLength.setText(DateConvertUtils.secToTime(dataBean.getDuration()));
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
        if (list != null && !list.get(position).getType().equals("video")) {
            return VOICE_TYPE;
        } else {
            return VIDEO_TYPE;
        }
    }

    public int getPlayPosition() {
        if (playingHolder != null) {
            return playingHolder.getAdapterPosition();
        }else {
            return -1;
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
        @BindView(R.id.tv_thumbs_count)
        ClickNotToggleCheckBox tvThumbsCount;
        @BindView(R.id.tv_voice_title)
        TextView tvVoiceTitle;
        @BindView(R.id.cb_voice_title)
        CheckBox cbVoiceTitle;
        @BindView(R.id.cb_voice_time)
        CheckBox cbVoiceTime;
        @BindView(R.id.tv_voice_time)
        TextView tvVoiceTime;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        VoiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(null);
            btnDelete.setOnClickListener(v -> onItemHolderClick(1, getLayoutPosition(), false));
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
        ClickNotToggleCheckBox tvThumbsCount;
        @BindView(R.id.tv_media_length)
        TextView tvMediaLength;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onItemHolderClick(2, getLayoutPosition(), false));
            btnDelete.setOnClickListener(v -> onItemHolderClick(1, getLayoutPosition(), false));
        }
    }


    private AudioPlayer player;
    private Timer timer;
    private MineReadAdapter.VoiceHolder playingHolder;

    private void playVoice(HomeVideoEntity.DataBean dataBean, MineReadAdapter.VoiceHolder holder) {

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
                    if(timer!=null) {
                        timer.cancel();
                    }
                } else {
                    player.play();
                    holder.cbVoiceTitle.setChecked(false);
                    holder.cbVoiceTime.setChecked(true);
                    seekProgress(dataBean,holder);
                }
            } else {
                player.stop();
                player = null;
                if(timer!=null) {
                    timer.cancel();
                }
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

    private void play(HomeVideoEntity.DataBean dataBean, MineReadAdapter.VoiceHolder holder) {
//        onItemHolderClick(2, holder.getAdapterPosition(), false);
        holder.tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum() + 1));
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
                        holder.tvVoiceTime.setText(DateConvertUtils.secToTime(dataBean.getDuration()));
                        if(timer!=null) {
                            timer.cancel();
                        }
                        playingHolder = null;
                        player = null;
                        timer = null;
                        break;

                }
            }
        });
        player.playUrl(dataBean.getPath());
        holder.cbVoiceTime.setChecked(true);
        seekProgress(dataBean,holder);
    }

    private void seekProgress(HomeVideoEntity.DataBean dataBean, MineReadAdapter.VoiceHolder holder) {
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
                    ((BaseActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int time = (int) (dataBean.getDuration() - (position/1000));
                            holder.tvVoiceTime.setText(DateConvertUtils.secToTime(time));
                        }
                    });
                }
            }
        }, 500, 1000);
    }

    public void pauseVideo() {
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