package com.cctv.langduzhe.feature.readPavilion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.CommandAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.CollectPresenter;
import com.cctv.langduzhe.contract.CollectView;
import com.cctv.langduzhe.contract.LikePresenter;
import com.cctv.langduzhe.contract.LikeView;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionDetailPresenter;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionDetailView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.entites.VideoInfoEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.feature.home.HomeVideoDetailActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/1/30.
 * 说明：朗读亭详情页面，包含音频视频两种情况
 */
public class ReadPavilionDetailActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        ReadPavilionDetailView ,CollectView,LikeView,BaseRecyclerViewAdapter.OnItemClickListener{
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_collection)
    CheckBox cbCollection;
    @BindView(R.id.btn_share)
    ImageView btnShare;
    @BindView(R.id.rv_command_list)
    PullLoadMoreRecyclerView rvCommandList;

    private String type;
    private int pageNum;
    private CommandAdapter commandAdapter;
    private HomeVideoEntity.DataBean videoEntity;
    private ReadPavilionDetailPresenter presenter;
    private CollectPresenter collectPresenter;
    private LikePresenter likePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pavilion_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
            videoEntity = (HomeVideoEntity.DataBean) bundle.getSerializable("video");
        }
        tvTitle.setText(videoEntity.getTitle());
        cbCollection.setChecked(videoEntity.getIsCollect()==1);
        initData();
        cbCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    collectPresenter.collect(videoEntity.getId());
                } else {
                    collectPresenter.deleteCollect(videoEntity.getId());
                }
            }
        });
    }

    private void initData() {
        commandAdapter = new CommandAdapter();
        rvCommandList.setLinearLayout();
        rvCommandList.setAdapter(commandAdapter);
        rvCommandList.setOnPullLoadMoreListener(this);

        commandAdapter.setAdapterType(type .equals("video") ? CommandAdapter.CommandTypeEnum.VIDEO_COMMAND : CommandAdapter.CommandTypeEnum.VOICE_COMMAND);
        commandAdapter.setReadInfo(videoEntity);
        onRefresh();
    }

    @OnClick({R.id.btn_back, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_share:
                shareText();
                break;
        }
    }

    //分享文字
    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoEntity.getPath());
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getCommentList(videoEntity.getId(), pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum +=1;
        presenter.getCommentList(videoEntity.getId(), pageNum);
    }

    @Override
    public void setPresenter() {
        presenter = new ReadPavilionDetailPresenter(this, this);
        collectPresenter = new CollectPresenter(this, this);
        likePresenter = new LikePresenter(this, this);
    }

    @Override
    public void setCommentList(List<CommandEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            if (pageNum == 0) {
                commandAdapter.setData(data);
            } else {
                commandAdapter.addData(data);
            }
        } else {
            showToast("没有更多数据");
        }
        rvCommandList.setPullLoadMoreCompleted();
    }

    @Override
    public void deleteSucceed() {
        cbCollection.setChecked(false);
        showToast("取消收藏");
        videoEntity.setIsCollect(1);
        EventBus.getDefault().post(new CollectEvent(videoEntity,"readPavilion"));
    }

    @Override
    public void collectSucceed() {
        cbCollection.setChecked(true);
        showToast("收藏成功");
        videoEntity.setIsCollect(1);
        EventBus.getDefault().post(new CollectEvent(videoEntity,"readPavilion"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        commandAdapter.pauseVideo();
    }

    @Override
    public void likeResult(boolean isLike) {
        videoEntity.setIsLike(isLike ? 1 : 0);
        if (isLike) {
            videoEntity.setLikeSum(videoEntity.getLikeSum()+1);
        } else {
            videoEntity.setLikeSum(videoEntity.getLikeSum()-1);
        }
        commandAdapter.setReadInfo(videoEntity);
        commandAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new CollectEvent(videoEntity,"readPavilion"));
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        if (optType == 1) {
            String mediasId = videoEntity.getId();
            if (yesOrNo) {
                likePresenter.likeRead(mediasId);
            } else {
                likePresenter.unlikeRead(mediasId);
            }
        }
    }
}
