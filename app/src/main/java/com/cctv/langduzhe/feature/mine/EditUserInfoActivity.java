package com.cctv.langduzhe.feature.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.PostPresenter;
import com.cctv.langduzhe.contract.PostView;
import com.cctv.langduzhe.contract.mine.EditUserInfoPresenter;
import com.cctv.langduzhe.contract.mine.EditUserInfoView;
import com.cctv.langduzhe.feature.MainActivity;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cctv.langduzhe.AppConstants.REQUEST_CODE_CAPTURE;
import static com.cctv.langduzhe.AppConstants.REQUEST_CODE_CHOOSE;

/**
 * Created by gentleyin
 * on 2018/1/18.
 * 说明：设置昵称和头像页面，登录时判断是否设置这些信息，没有则进入该页面，也可从其他页面点击进入
 */
public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener, EditUserInfoView, PostView {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.btn_submit_user_info)
    Button btnSubmitUserInfo;

    private MediaStoreCompat mMediaStoreCompat;
    private PostPresenter postPresenter;
    private EditUserInfoPresenter presenter;
    /**
     * 底部弹出的dialog
     */
    private AlertDialog bottomDialog;


    private String userImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvTitle.setText("昵称~头像");
        boolean isNewUser = getIntent().getBooleanExtra("new_user", false);
        if (!isNewUser) {
            presenter.subscribe();
            tvRight.setVisibility(View.GONE);
        } else {
            tvRight.setText("跳过");
        }
        mMediaStoreCompat = new MediaStoreCompat(this);
        mMediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.cctv.langduzhe.fileprovider"));
    }

    @OnClick({R.id.btn_back, R.id.tv_right, R.id.btn_submit_user_info, R.id.ll_user_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                toActivity(MainActivity.class);
                finish();
                break;
            case R.id.btn_submit_user_info:
                presenter.saveUserInfo(userImgUrl, etUserName.getText().toString());
                break;
            case R.id.ll_user_head:
                //选择图片替换当前头像
                showBottomDialog();
                break;
        }
    }

    /**
     * @author 尹振东
     * create at 2018/1/18 下午9:12
     * 方法说明：弹出底部弹窗
     */
    private void showBottomDialog() {

        if (bottomDialog == null) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_operate, null, false);
            dialogView.findViewById(R.id.btn_camera).setOnClickListener(this);
            dialogView.findViewById(R.id.btn_cancel).setOnClickListener(this);
            dialogView.findViewById(R.id.btn_gallery).setOnClickListener(this);
            bottomDialog = new AlertDialog.Builder(this, R.style.ios_bottom_dialog)
                    .setView(dialogView)
                    .create();
            //获取当前布局的Window
            Window window = bottomDialog.getWindow();                                            //设置无标题栏
            WindowManager.LayoutParams params = null;
            if (window != null) {
                params = window.getAttributes();
                //位置在屏幕处于底部位置
                params.gravity = Gravity.BOTTOM;
                //显示隐藏的动画效果
                params.windowAnimations = R.style.ios_bottom_dialog_anim;
            }
        }
        if (!bottomDialog.isShowing()) {
            bottomDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        switch (v.getId()) {
            case R.id.btn_camera:
                //弹窗消失
                if (bottomDialog != null && bottomDialog.isShowing()) {
                    bottomDialog.dismiss();
                }
                //拍照
                rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new SimpleObserve() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    if (mMediaStoreCompat != null) {
                                        mMediaStoreCompat.dispatchCaptureIntent(EditUserInfoActivity.this, REQUEST_CODE_CAPTURE);
                                    }
                                } else {
                                    ToastUtils.showShort(EditUserInfoActivity.this, "需要相应的权限");
                                }
                            }
                        });
                break;
            case R.id.btn_gallery:
                //弹窗消失
                if (bottomDialog != null && bottomDialog.isShowing()) {
                    bottomDialog.dismiss();
                }
                //从相册选择
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new SimpleObserve() {
                            @Override
                            public void onNext(Boolean value) {
                                if (value) {
                                    Matisse.from(EditUserInfoActivity.this)
                                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(false)
                                            .maxSelectable(1)
                                            .imageEngine(new PicassoEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                } else {
                                    ToastUtils.showShort(EditUserInfoActivity.this, "需要相应的权限");
                                }
                            }
                        });
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<Uri> photoUri = Matisse.obtainResult(data);
                postPresenter.postFile(PostPresenter.IMAGE_TYPE, FileUtil.getRealPathFromUri(this, photoUri.get(0)));

            } else if (requestCode == REQUEST_CODE_CAPTURE) {
                String photoPath = mMediaStoreCompat.getCurrentPhotoPath();
                postPresenter.postFile(PostPresenter.IMAGE_TYPE, photoPath);

            }
            showProgress("正在上传头像");
        }
    }

    @Override
    public void setPresenter() {
        presenter = new EditUserInfoPresenter(this, this);
        postPresenter = new PostPresenter(this, this);
    }

    @Override
    public void setUserInfo(JSONObject dataObj) {
        userImgUrl = dataObj.getString("img");
        String name = dataObj.getString("name");
        if (!TextUtils.isEmpty(userImgUrl)) {
            PicassoUtils.loadImageByurl(this, userImgUrl, ivUserHead);
        }
        etUserName.setText(name);
    }

    @Override
    public void postSucceed(String filepath, int duration, long fileSize) {
        PicassoUtils.loadImageByurl(this,filepath,ivUserHead);
        userImgUrl = filepath;
        dismissProgress();
    }
}
