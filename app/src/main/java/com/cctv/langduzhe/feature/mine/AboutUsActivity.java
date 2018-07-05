package com.cctv.langduzhe.feature.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/4/16.
 * 说明：关于我们页面
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_app_name_version)
    TextView tvAppNameVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(v -> onBackPressed());
        tvTitle.setText("关于我们");
        tvAppNameVersion.setText(String.format("CCTV朗读者(%s)", getVersionName()));
    }

    /**
     * 获取版本名称
     * @return
     */
    public String getVersionName() {
        PackageManager manager = this.getPackageManager();
        String packageName = this.getPackageName();
        try {
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;   //版本名
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void setPresenter() {

    }
}
