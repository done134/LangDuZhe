package com.cctv.langduzhe.feature.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.LikePresenter;
import com.cctv.langduzhe.contract.LikeView;
import com.cctv.langduzhe.contract.home.HomeDetailPresenter;
import com.cctv.langduzhe.contract.home.HomeDetailView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.adapter.DialogCommandAdapter;
import com.cctv.langduzhe.adapter.HomeSliceAdapter;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.feature.CommentActivity;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionDetailActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by gentleyin
 * on 2018/1/21.
 * 说明：首页视频详情页
 */
public class HomeVideoDetailActivity extends BaseActivity implements HomeDetailView, BaseRecyclerViewAdapter.OnItemClickListener,LikeView {

    private final int COMMENT_REQUEST = 101;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_home_slice_list)
    PullLoadMoreRecyclerView rvHomeSliceList;
    @BindView(R.id.view_gray_layer)
    View viewGrayLayer;
    @BindView(R.id.btn_down_dialog)
    ImageView btnDownDialog;
    @BindView(R.id.rv_home_dialog_command)
    PullLoadMoreRecyclerView rvHomeDialogCommand;
    @BindView(R.id.btn_goto_command)
    TextView btnGotoCommand;
    @BindView(R.id.ll_command_dialog)
    LinearLayout llCommandDialog;


    /**
     * 是否弹窗评论在弹出状态
     */
    private boolean isDialogShow;
    private HomeSliceAdapter homeSliceAdapter;
    private DialogCommandAdapter commandAdapter;
    private HomeDetailPresenter presenter;
    private LikePresenter likePresenter;

    private HomeVideoEntity.DataBean videoEntity;
    private int commentPage,videoPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);
        getIntentData();
        initData();
        setListener();
        presenter.addWatchSum(videoEntity.getId());
        rvHomeDialogCommand.setLinearLayout();
    }

    /**
    * @author 尹振东
    * create at 2018/2/27 上午11:37
    * 方法说明：给评论列表和视频列表添加下拉刷新和加载更多回调
    */
    private void setListener() {
        rvHomeDialogCommand.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                commentPage = 0;
                presenter.getCommentList(videoEntity.getId(),commentPage);
            }

            @Override
            public void onLoadMore() {
                commentPage += 1;
                presenter.getCommentList(videoEntity.getId(),commentPage);
            }
        });

        rvHomeSliceList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                videoPage=0;
                presenter.getMediaList(videoEntity,videoPage);
            }

            @Override
            public void onLoadMore() {
                videoPage += 1;
                presenter.getMediaList(videoEntity,videoPage);

            }
        });
        rvHomeSliceList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildPosition(view) != 0) {
                    if (parent.getChildPosition(view) / 2 == 0) {
                        outRect.left = 30;
                    }
                    outRect.right = 30;
                    outRect.bottom = 60;
                    if (parent.getChildPosition(view) == 1 || parent.getChildPosition(view) == 2) {
                        outRect.top = 30;
                    }

                }
            }
        });

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        videoEntity = (HomeVideoEntity.DataBean) bundle.getSerializable("home_video");
        tvTitle.setText(videoEntity.getTitle());
        if(bundle.getBoolean("showComment",false)){
            showDialogCommand();
        }
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }else {
                    return 1;
                }
            }
        });
        rvHomeSliceList.setCustomGridManager(gridLayoutManager);
        homeSliceAdapter = new HomeSliceAdapter();
        homeSliceAdapter.setVideoInfo(videoEntity);
        homeSliceAdapter.setData(null);
        homeSliceAdapter.setOnItemClickListener(this::onItemClick);
        rvHomeSliceList.setAdapter(homeSliceAdapter);
        presenter.getMediaList(videoEntity,0);
    }

    @OnClick({R.id.btn_back,R.id.view_gray_layer, R.id.btn_down_dialog, R.id.btn_goto_command})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_down_dialog:
                dismissDialogCommand();
                break;
            case R.id.btn_goto_command:
                if(hasLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mediaId", videoEntity.getId());
                    toActivityForResult(CommentActivity.class, bundle,COMMENT_REQUEST);
                }
                break;
            case R.id.view_gray_layer:
                //半透明蒙层
                //暂不对点击事件做处理

                    break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDialogShow) {
            commentPage = 0;
            presenter.getCommentList(videoEntity.getId(),commentPage);
        }
    }

    /**
    * @author 尹振东
    * create at 2018/2/1 上午12:15
    * 方法说明：显示弹窗评论
    */
    private void showDialogCommand() {
        initCommandData();
//        presenter.getCommentList();
        viewGrayLayer.setVisibility(View.VISIBLE);
        btnGotoCommand.setVisibility(View.VISIBLE);
        llCommandDialog.setVisibility(View.VISIBLE);
        // 步骤1:创建 需要设置动画的 视图View
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.ios_dialog_enter);
        // 步骤2:创建 动画对象 并传入设置的动画效果xml文件
        llCommandDialog.startAnimation(translateAnimation);
        isDialogShow = true;
    }

    private void initCommandData() {

        presenter.getCommentList(videoEntity.getId(),0);
        if (commandAdapter == null) {
            commandAdapter = new DialogCommandAdapter();
            rvHomeDialogCommand.setAdapter(commandAdapter);
        }
    }

    /**
     * @author 尹振东
     * create at 2018/2/1 上午12:15
     * 方法说明：显示弹窗评论
     */
    private void dismissDialogCommand() {
        // 步骤1:创建 需要设置动画的 视图View
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.ios_dialog_exit);
        // 步骤2:创建 动画对象 并传入设置的动画效果xml文件
        llCommandDialog.startAnimation(translateAnimation);
        viewGrayLayer.setVisibility(View.GONE);
        btnGotoCommand.setVisibility(View.GONE);
        llCommandDialog.setVisibility(View.GONE);
        isDialogShow = false;
    }

    @Override
    public void onBackPressed() {
        if (isDialogShow) {
            dismissDialogCommand();
        }else {
            if (JZVideoPlayer.backPress()) {
                return;
            }
            super.onBackPressed();
        }
    }


    @Override
    public void setPresenter() {
        presenter = new HomeDetailPresenter(this, this);
        likePresenter = new LikePresenter(this, this);
    }

    @Override
    public void setMediaData(List<HomeVideoEntity.DataBean> result) {
        if (result != null && result.size() > 0) {
            if (videoPage == 0) {
                homeSliceAdapter.setData(result);
            }else {
                homeSliceAdapter.addData(result);
            }

        }else {
            if (videoPage != 0) {
                showToast("没有更多视频");
            }
        }
        rvHomeSliceList.setPullLoadMoreCompleted();
    }

    @Override
    public void setCommentList(List<CommandEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            if (commentPage == 0) {
                commandAdapter.setData(data);
            }else {
                commandAdapter.addData(data);
            }
        }else {
            if(commentPage!=0) {
                showToast("没有更多数据");
            }
        }
        rvHomeDialogCommand.setPullLoadMoreCompleted();
        setDataError();

    }

    @Override
    public void setCollectResult() {
//        homeSliceAdapter.setCollect();
    }

    @Override
    public void setDataError() {
        rvHomeSliceList.setPullLoadMoreCompleted();
        rvHomeDialogCommand.setPullLoadMoreCompleted();
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        if (optType == 0) {
            showDialogCommand();
        } else if (optType == 1) {
            if(hasLogin()){
                if (yesOrNo) {
                    likePresenter.likeRead(videoEntity.getId());
                } else {
                    likePresenter.unlikeRead(videoEntity.getId());
                }
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("video", homeSliceAdapter.getItemInPosition(position));
            bundle.putString("type", "video");
            bundle.putBoolean("isHome",true);
            toActivity(ReadPavilionDetailActivity.class, bundle);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
    @Override
    public void likeResult(boolean isLike) {
        videoEntity.setIsLike(isLike ? 1 : 0);
        homeSliceAdapter.setVideoInfo(videoEntity);
        int thumbSum = videoEntity.getLikeSum();
        if (isLike) {
            videoEntity.setLikeSum(thumbSum+1);
        } else {
            videoEntity.setLikeSum(thumbSum-1);
        }
        homeSliceAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new CollectEvent(videoEntity,"home"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==COMMENT_REQUEST&&resultCode == RESULT_OK) {
            CommandEntity.DataBean newComment = new CommandEntity.DataBean();
            newComment.setReaderImg((String) SPUtils.get(this, PreferenceContents.USER_IMG,""));
            newComment.setReaderName((String) SPUtils.get(this, PreferenceContents.USER_NAME,""));
            newComment.setContent(data.getStringExtra("commentStr"));
            commandAdapter.addNewData(newComment);
        }
    }
}
