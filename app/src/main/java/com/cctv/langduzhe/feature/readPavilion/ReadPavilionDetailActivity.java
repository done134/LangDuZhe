package com.cctv.langduzhe.feature.readPavilion;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
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
import com.cctv.langduzhe.data.http.ApiConstants;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.feature.CommentActivity;
import com.cctv.langduzhe.wxapi.WechatShareManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;


/**
 * Created by gentleyin
 * on 2018/1/30.
 * 说明：朗读亭详情页面，包含音频视频两种情况
 */
public class ReadPavilionDetailActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        ReadPavilionDetailView ,CollectView,LikeView,BaseRecyclerViewAdapter.OnItemClickListener,View.OnClickListener{

    private final int COMMENT_REQUEST = 101;
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

    private AlertDialog bottomDialog;

    private Bitmap shareImage;
    private String shareTitle;
    private String shareDesc;
    private String shareWebUrl;
    private boolean isHomeSliceVideo;
    //微信分享
    private IWXAPI wxApi;
    private WechatShareManager mShareManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pavilion_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
            videoEntity = (HomeVideoEntity.DataBean) bundle.getSerializable("video");
            isHomeSliceVideo = bundle.getBoolean("isHome", false);
        }
        tvTitle.setText(videoEntity.getTitle());
        cbCollection.setChecked(videoEntity.getIsCollect()==1);
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        initData();
        cbCollection.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (hasLogin()) {
                if (isChecked) {
                    collectPresenter.collect(videoEntity.getId());
                } else {
                    collectPresenter.deleteCollect(videoEntity.getId());
                }
            }
        });
        initWx();
        presenter.addWatchSum(videoEntity.getId());

    }
    private void initWx() {
        // 微信注册初始化
        wxApi = WXAPIFactory.createWXAPI(this,ApiConstants.WECHAT_APP_ID);
        wxApi.registerApp(ApiConstants.WECHAT_APP_ID);
        mShareManager = WechatShareManager.getInstance(this);
    }

    private void initData() {
        commandAdapter = new CommandAdapter(isHomeSliceVideo);
        rvCommandList.setLinearLayout();
        rvCommandList.setAdapter(commandAdapter);
        rvCommandList.setOnPullLoadMoreListener(this);
        commandAdapter.setOnItemClickListener(this);
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
                shareWebUrl = ApiConstants.SHARE_URL + videoEntity.getId();
                shareTitle = videoEntity.getTitle();
                shareDesc = "一个人，一段文";
                showShareDialog();
                break;
        }
    }

    private void shareWeb(int flag) {
        WechatShareManager.ShareContentWebPage mShareContent = (WechatShareManager.ShareContentWebPage)
                mShareManager.getShareContentWebpag(shareTitle, shareDesc, shareWebUrl, shareImage);
        if (flag == 1) {
            mShareManager.shareByWebchat(mShareContent, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
        } else {
            mShareManager.shareByWebchat(mShareContent, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
        }
        //弹窗消失
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
    }



    /**
     * @author 尹振东
     * create at 2018/1/18 下午9:12
     * 方法说明：弹出底部弹窗
     */
    private void showShareDialog() {
        //设置无标题栏
        WindowManager.LayoutParams params = null;
        if (bottomDialog == null) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_share, null, false);
            dialogView.findViewById(R.id.btn_share_group).setOnClickListener(this);
            dialogView.findViewById(R.id.btn_share_friends).setOnClickListener(this);
            dialogView.findViewById(R.id.btn_cancel).setOnClickListener(this);
            bottomDialog = new AlertDialog.Builder(this, R.style.ios_bottom_dialog)
                    .setView(dialogView)
                    .create();
            //获取当前布局的Window
            Window window = bottomDialog.getWindow();
            if (window != null) {
                params = window.getAttributes();
                //位置在屏幕处于底部位置
                params.gravity = Gravity.BOTTOM;
                //显示隐藏的动画效果
                params.windowAnimations = R.style.ios_bottom_dialog_anim;
            }

            //微信分享内容
            shareImage = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);

        }
        if (!bottomDialog.isShowing()) {
            bottomDialog.show();
        }

        //设置dialog宽度全屏
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        if (params != null) {
            params.width = d.getWidth();    //宽度设置全屏宽度
            bottomDialog.getWindow().setAttributes(params);     //设置生效
        }

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
            if(pageNum!=0) {
                showToast("没有更多数据");
            }
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
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
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
            if (hasLogin()) {
                String mediasId = videoEntity.getId();
                if (yesOrNo) {
                    likePresenter.likeRead(mediasId);
                } else {
                    likePresenter.unlikeRead(mediasId);
                }
            }
        } else {
            if (hasLogin()) {
                Bundle bundle = new Bundle();
                bundle.putString("mediaId",videoEntity.getId());
                toActivityForResult(CommentActivity.class, bundle,COMMENT_REQUEST);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_group:
                shareWeb(0);
                break;
            case R.id.btn_share_friends:
                shareWeb(1);
                break;
            case R.id.btn_cancel:
                //取消，弹窗消失
                if (bottomDialog != null && bottomDialog.isShowing()) {
                    bottomDialog.dismiss();
                }
                break;
        }
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
