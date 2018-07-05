package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.cctv.langduzhe.view.widget.ClickNotToggleCheckBox;
import com.shuyu.waveview.AudioPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by gentleyin
 * on 2018/4/7.
 * 说明：
 */
public class VoiceAdapter extends BaseRecyclerViewAdapter<VoiceAdapter.VoiceHolder> {


    private List<HomeVideoEntity.DataBean> list;
    private Context context;

    private boolean playEnd;

    private boolean seekBarTouch;

    @Override
    public VoiceAdapter.VoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article_voice, parent, false);
        return new VoiceAdapter.VoiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VoiceAdapter.VoiceHolder holder, int position) {
        HomeVideoEntity.DataBean dataBean = list.get(position);
        //音频
        holder.tvThumbsCount.setText(String.valueOf(dataBean.getLikeSum()));
        holder.tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()));
        holder.tvUploaderName.setText(dataBean.getReaderName());
        holder.tvUploadTime.setText(dataBean.getCreateDate());
        holder.tvThumbsCount.setChecked(dataBean.getIsLike() == 1);
        PicassoUtils.loadImageByurlerr(context, dataBean.getReaderImg(), holder.ivServicePeopleIcon,R.mipmap.head_default_icon);
        holder.tvVoiceTime.setText(DateConvertUtils.secToTime(dataBean.getDuration()));
        holder.tvVoiceTitle.setOnClickListener(v -> playVoice(dataBean, holder));
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

    public int getPlayingHolderPosition() {
        return playingHolder.getAdapterPosition();
    }

    private boolean isPlaying = false;
    public boolean getIsPlaying() {
        return isPlaying;
    }

    public int getPlayPosition() {
        if (playingHolder != null) {
            return playingHolder.getAdapterPosition();
        } else {
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
        CheckBox tvVoiceTitle;
        @BindView(R.id.cb_voice_time)
        CheckBox cbVoiceTime;
        @BindView(R.id.seekbar_voice)
        SeekBar seekBarVoice;
        @BindView(R.id.tv_voice_time)
        TextView tvVoiceTime;

        VoiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvThumbsCount.setOnClickListener(v -> {
                if (CommonUtil.isFastClick()) {
                    onItemHolderClick(1, getLayoutPosition(), !tvThumbsCount.isChecked());
                }
            });
        }
    }


    private AudioPlayer player;
    private Timer timer;
    private VoiceHolder playingHolder;

    private void playVoice(HomeVideoEntity.DataBean dataBean, VoiceHolder holder) {

        if (player != null) {
            if (playingHolder != null && playingHolder.getAdapterPosition() == holder.getAdapterPosition()) {
                if (playEnd) {
                    player.stop();
                    holder.seekBarVoice.setEnabled(true);
                    play(dataBean, holder);
                    return;
                }
                if (player.isPlaying()) {
                    player.pause();
                    isPlaying = false;
                    holder.tvVoiceTitle.setChecked(true);
                    holder.cbVoiceTime.setChecked(false);
                    holder.seekBarVoice.setEnabled(false);
                    timer.cancel();
                } else {
                    player.play();
                    isPlaying = true;
                    holder.tvVoiceTitle.setChecked(false);
                    holder.cbVoiceTime.setChecked(true);
                    holder.seekBarVoice.setEnabled(true);
                    seekProgress(dataBean,holder);
                }
            } else {
                player.stop();
                player = null;
                timer.cancel();
                playingHolder.tvVoiceTitle.setChecked(true);
                playingHolder.cbVoiceTime.setChecked(false);
                playingHolder.seekBarVoice.setEnabled(true);
                playingHolder.seekBarVoice.setProgress(0);
                play(dataBean, holder);
                playingHolder = holder;
            }
        } else {
            play(dataBean, holder);
            playingHolder = holder;
        }
    }

    private void play(HomeVideoEntity.DataBean dataBean, VoiceHolder holder) {
        playEnd = false;
        onItemHolderClick(0, holder.getAdapterPosition(), false);
        holder.tvVideoPlayCount.setText(String.valueOf(dataBean.getWatchSum()+1));
        player = new AudioPlayer(context, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        player.pause();
                        playEnd = true;
                        holder.tvVoiceTitle.setChecked(true);
                        holder.cbVoiceTime.setChecked(false);
                        holder.seekBarVoice.setEnabled(true);
                        holder.tvVoiceTime.setText(DateConvertUtils.secToTime(dataBean.getDuration()));
                        holder.seekBarVoice.setProgress(0);
                        timer.cancel();
                        isPlaying = false;
                        player=null;
                        timer = null;
                        break;

                }
            }
        });
        player.playUrl(dataBean.getPath());
        isPlaying = true;
        holder.cbVoiceTime.setChecked(true);
        holder.seekBarVoice.setMax(dataBean.getDuration() * 1000);
        holder.seekBarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarTouch = false;
                if (!playEnd) {
                    player.seekTo(seekBar.getProgress());
                }
            }
        });

        seekProgress(dataBean,holder);
    }

    private void seekProgress(HomeVideoEntity.DataBean dataBean, VoiceHolder holder) {
        if (timer != null) {
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (playEnd || player == null || !holder.seekBarVoice.isEnabled()) {
                    return;
                }

                long position = player.getCurPosition();
                if (position > 0 && !seekBarTouch) {
                    holder.seekBarVoice.setProgress((int) position);
                    ((BaseActivity)context).runOnUiThread(() -> {
                        int time = (int) (dataBean.getDuration() - (position/1000));
                        holder.tvVoiceTime.setText(DateConvertUtils.secToTime(time));
                    });
                }
            }
        }, 500, 1000);
    }


    public void pauseVideo() {
        if (player != null) {
            player.pause();
            playingHolder.tvVoiceTitle.setChecked(true);
            playingHolder.cbVoiceTime.setChecked(false);
            playingHolder.seekBarVoice.setEnabled(true);
//            player = null;
        }
    }
}
