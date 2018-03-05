package com.cctv.langduzhe.feature.mine;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.view.widget.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by gentleyin
 * on 2018/1/19.
 * 说明：设置页面
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.switch_not_wifi_notice)
    SwitchView switchNotWifiNotice;
    @BindView(R.id.rl_goto_feedback)
    RelativeLayout rlGotoFeedback;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.rl_version_info)
    RelativeLayout rlVersionInfo;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        tvTitle.setText("设置");
    }

    @OnClick({R.id.btn_back, R.id.switch_not_wifi_notice, R.id.rl_goto_feedback, R.id.tv_cache_size, R.id.rl_clear_cache, R.id.rl_version_info, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.switch_not_wifi_notice:
                //开关
                boolean isOpened = switchNotWifiNotice.isOpened();
                break;
            case R.id.rl_goto_feedback:
                //意见反馈
                break;
            case R.id.tv_cache_size:
            case R.id.rl_clear_cache:
                //清理缓存
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("缓存已清理");
                        tvCacheSize.setText("0KB");
                        dismissProgress();
                    }
                },1000);
                break;
            case R.id.rl_version_info:
                //版本信息
                break;
            case R.id.btn_logout:
                //退出登录
                toLogin();
                break;
        }
    }

    @Override
    public void setPresenter() {

    }
}
