package com.cctv.langduzhe.feature.read;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.PostPresenter;
import com.cctv.langduzhe.contract.PostView;
import com.cctv.langduzhe.contract.read.RecordVideoOverPresenter;
import com.cctv.langduzhe.contract.read.RecordVideoOverView;
import com.cctv.langduzhe.feature.MainActivity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.shuyu.waveview.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by gentleyin
 * on 2018/1/22.
 * 说明：视频录制完成，保存或者上传页面
 */
public class ReadOverActivity extends BaseActivity implements RecordVideoOverView,PostView{
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_video_name)
    EditText etVideoName;
    @BindView(R.id.btn_save_the_local)
    Button btnSaveTheLocal;
    @BindView(R.id.btn_post_video)
    Button btnPostVideo;
    @BindView(R.id.video_view)
    JZVideoPlayerStandard videoView;

    private RecordVideoOverPresenter presenter;
    private PostPresenter postPresenter;
    private    String videoPath;
    private String thumbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video_over);
        ButterKnife.bind(this);
        tvTitle.setText("发布");
        initVideo();
    }

    private void initVideo() {
        videoPath = getIntent().getExtras().getString("video_path");
        thumbImage = getIntent().getExtras().getString("thumb_image");
        videoView.setUp(videoPath, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        PicassoUtils.loadImageByurl(this,thumbImage,videoView.thumbImageView);
    }

    @OnClick({R.id.btn_back, R.id.btn_save_the_local, R.id.btn_post_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_save_the_local:String name  = etVideoName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("请输入作品名称！");
                    return;
                }
                presenter.saveToSDCard(videoPath,name,thumbImage);
                toActivity(MainActivity.class);
                showToast("已保存在本地");
                break;
            case R.id.btn_post_video:
                String title  = etVideoName.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    showToast("请输入作品名称！");
                    return;
                }
                postPresenter.postFile(PostPresenter.VIDEO_TYPE, videoPath);
                showProgress();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        FileUtils.deleteFile(videoPath);
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void setPresenter() {
        presenter = new RecordVideoOverPresenter(this,this);
        postPresenter = new PostPresenter(this, this);
    }

    @Override
    public void postSucceed(String fileName, int duration, long fileSize) {
        presenter.saveVideo(fileName,etVideoName.getText().toString().trim(),duration,fileSize);
        dismissProgress();
    }


}
