package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.piterwilson.audio.MP3RadioStreamPlayer;
import com.shuyu.waveview.AudioWaveView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static com.cctv.langduzhe.util.CommonUtil.dip2px;
import static com.cctv.langduzhe.util.CommonUtil.getScreenWidth;

/**
 * Created by gentleyin
 * on 2018/1/30.
 * 说明：评论列表适配器
 */
public class CommandAdapter extends BaseRecyclerViewAdapter {

    /**
     * 音频类型item
     */
    private final int VOICE_TYPE = 0;
    /**
     * 视频类型item
     */
    private final int VIDEO_TYPE = 1;


    private List<CommandEntity.DataBean> list;

    private Context context;

    private CommandTypeEnum adapterType;
    private HomeVideoEntity.DataBean videoInfoEntity;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        if (viewType == VOICE_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_voice_detail, parent, false);
            return new VoiceHolder(itemView, this);

        } else if (viewType == VIDEO_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_video_detail, parent, false);
            return new VideoHolder(itemView, this);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_command, parent, false);
            return new CommandHolder(itemView, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoiceHolder) {

            ((VoiceHolder) holder).tvCommentCount.setText(String.valueOf(videoInfoEntity.getCommentSum()));
            ((VoiceHolder) holder).tvThumbsCount.setText(String.valueOf(videoInfoEntity.getCollectSum()));
            ((VoiceHolder) holder).tvVideoPlayCount.setText(String.valueOf(videoInfoEntity.getWatchSum()));
            ((VoiceHolder) holder).tvCommandName.setText(videoInfoEntity.getReaderName());
            ((VoiceHolder) holder).tvCommandTime.setText(videoInfoEntity.getCreateDate());
            ((VoiceHolder) holder).tvThumbsCount.setChecked(videoInfoEntity.getIsLike() == 1);

//            ((VoiceHolder) holder).setText(videoInfoEntity.getTitle());
        } else if (holder instanceof VideoHolder){
            //普通视频类型展示上传者信息，首页节目片段视频隐藏这块儿布局
            if (getAdapterType() == CommandTypeEnum.VIDEO_COMMAND) {
                ((VideoHolder) holder).llUploaderInfo.setVisibility(View.VISIBLE);
            } else {
                ((VideoHolder) holder).llUploaderInfo.setVisibility(View.GONE);
            }
            //视频
            ((VideoHolder) holder).tvCommentCount.setText(String.valueOf(videoInfoEntity.getCommentSum()));
            ((VideoHolder) holder).tvThumbsCount.setText(String.valueOf(videoInfoEntity.getLikeSum()));
            ((VideoHolder) holder).tvThumbsCount.setChecked(videoInfoEntity.getIsLike()==1);
            ((VideoHolder) holder).tvVideoPlayCount.setText(String.valueOf(videoInfoEntity.getWatchSum()));
            ((VideoHolder) holder).tvCommandName.setText(videoInfoEntity.getReaderName());
            ((VideoHolder) holder).tvCommandTime.setText(videoInfoEntity.getCreateDate());
            PicassoUtils.loadImageByurl(context,videoInfoEntity.getImg(),((VideoHolder) holder).videoView.thumbImageView);
            ((VideoHolder) holder).videoView.setUp(videoInfoEntity.getPath(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
            if (isPause) {
                ((VideoHolder) holder).videoView.release();
            }
//            ((VideoHolder) holder).tv.setText(videoInfoEntity.getTitle());
        } else if (holder instanceof CommandHolder) {
            CommandEntity.DataBean dataBean = list.get(position);
            ((CommandHolder) holder).tvCommandContent.setText(dataBean.getContent());
            ((CommandHolder) holder).tvCommandName.setText(dataBean.getReaderId());
//            ((CommandHolder) holder).tvCommandTime.setText(dataBean.);
        }
    }

    public void setData(List<CommandEntity.DataBean> list) {
        this.list = list;
        list.add(0, null);
        notifyDataSetChanged();
    }

    public void setReadInfo(HomeVideoEntity.DataBean videoInfoEntity) {
        this.list = new ArrayList<>();
        list.add(0, null);
        this.videoInfoEntity = videoInfoEntity;
    }
    public void addData(List<CommandEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }



    private void playVoice(MP3RadioStreamPlayer player,String voiceUrl,AudioWaveView audioWaveView) {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        player = new MP3RadioStreamPlayer();
        //player.setUrlString(this, true, "http://www.stephaniequinn.com/Music/Commercial%20DEMO%20-%2005.mp3");
        player.setUrlString(voiceUrl);

        int size = getScreenWidth(context) / dip2px(context, 1);//控件默认的间隔是1
        player.setDataList(audioWaveView.getRecList(), size);
        audioWaveView.setBaseRecorder(player);
        audioWaveView.startView();
        try {
            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (getAdapterType() == CommandTypeEnum.VOICE_COMMAND) {
                return VOICE_TYPE;
            } else {
                return VIDEO_TYPE;
            }
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return list == null?0:list.size();
    }

    public CommandTypeEnum getAdapterType() {
        return adapterType;
    }

    /**
     * @param adapterType
     */
    public void setAdapterType(CommandTypeEnum adapterType) {
        this.adapterType = adapterType;
    }

    /**
     * 音频类头布局
     */
     class VoiceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_command_head)
        CircleImageView ivCommandHead;
        @BindView(R.id.tv_command_name)
        TextView tvCommandName;
        @BindView(R.id.tv_command_time)
        TextView tvCommandTime;
        @BindView(R.id.ll_uploader_info)
        LinearLayout llUploaderInfo;
        @BindView(R.id.audioWave)
        AudioWaveView audioWave;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;

        VoiceHolder(View itemView, CommandAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvThumbsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(1, getLayoutPosition(),tvThumbsCount.isChecked());
                }
            });
        }
    }

    boolean isPause;
    public void pauseVideo() {
        isPause = true;
        notifyDataSetChanged();
    }
    /**
     * 视频类头布局
     */
     class VideoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_command_head)
        CircleImageView ivCommandHead;
        @BindView(R.id.tv_command_name)
        TextView tvCommandName;
        @BindView(R.id.tv_command_time)
        TextView tvCommandTime;
        @BindView(R.id.ll_uploader_info)
        LinearLayout llUploaderInfo;
        @BindView(R.id.video_view)
        JZVideoPlayerStandard videoView;
        @BindView(R.id.tv_video_play_count)
        TextView tvVideoPlayCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_thumbs_count)
        AppCompatCheckBox tvThumbsCount;


        VideoHolder(View itemView, CommandAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvThumbsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemHolderClick(1, getLayoutPosition(),tvThumbsCount.isChecked());
                }
            });
        }
    }

    /**
     * 评论布局
     */
    static class CommandHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_command_head)
        CircleImageView ivCommandHead;
        @BindView(R.id.tv_command_name)
        TextView tvCommandName;
        @BindView(R.id.tv_command_time)
        TextView tvCommandTime;
        @BindView(R.id.tv_command_content)
        TextView tvCommandContent;


        CommandHolder(View itemView, CommandAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(,this));
        }
    }

    // 定义一个评论列表类型的枚举类
    public enum CommandTypeEnum {

        /**
         * 朗读者视频片段评论
         */
        LANGDUZHE_COMMAND,
        /**
         * 阅读亭视频类型
         */
        VIDEO_COMMAND,
        /**
         * 朗读者音频片段评论
         */
        VOICE_COMMAND;
    }


}
