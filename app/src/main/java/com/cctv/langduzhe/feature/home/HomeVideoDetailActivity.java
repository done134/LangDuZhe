package com.cctv.langduzhe.feature.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.cctv.langduzhe.contract.home.HomeDetailPresenter;
import com.cctv.langduzhe.contract.home.HomeDetailView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.adapter.DialogCommandAdapter;
import com.cctv.langduzhe.adapter.HomeSliceAdapter;
import com.cctv.langduzhe.feature.CommentActivity;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionDetailActivity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/1/21.
 * 说明：首页视频详情页
 */
public class HomeVideoDetailActivity extends BaseActivity implements HomeSliceAdapter.HomeDetailClick ,HomeDetailView, BaseRecyclerViewAdapter.OnItemClickListener {
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
    private List<HomeVideoEntity> list = new ArrayList<>();
    private HomeSliceAdapter homeSliceAdapter;
    private DialogCommandAdapter commandAdapter;
    private HomeDetailPresenter presenter;

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
                presenter.getCommentList(videoEntity.getId(),commentPage);
            }

            @Override
            public void onLoadMore() {
                commentPage += 1;
                presenter.getCommentList(videoEntity.getId(),commentPage);

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
        homeSliceAdapter = new HomeSliceAdapter(this);
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
                Bundle bundle = new Bundle();
                bundle.putString("mediaId",videoEntity.getId());
                toActivity(CommentActivity.class,bundle);
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
            super.onBackPressed();
        }
    }

    /**
     * 点击评论按钮
     */
    @Override
    public void onCommandClick() {
        showDialogCommand();
    }

    /**
     * 点赞
     */
    @Override
    public void onThumbChecked(boolean isChecked) {
        presenter.collectVideo(videoEntity.getId());
    }

    @Override
    public void setPresenter() {
        presenter = new HomeDetailPresenter(this, this);
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
            showToast("没有更多视频");
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
            showToast("没有更多数据");
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("video",homeSliceAdapter.getItemInPosition(position));
        bundle.putString("type","video");
        toActivity(ReadPavilionDetailActivity.class, bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeSliceAdapter.pauseVideo();
    }
}
