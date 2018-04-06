package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.mine.AddContributePresenter;
import com.cctv.langduzhe.contract.mine.AddContributeView;
import com.cctv.langduzhe.eventMsg.ContributeEvent;
import com.cctv.langduzhe.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/3/18.
 * 说明：
 */
public class AddContributeActivity extends BaseActivity implements AddContributeView {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_contribute_title)
    EditText etContributeTitle;
    @BindView(R.id.et_contribute_content)
    EditText etContributeContent;
    @BindView(R.id.btn_give_up)
    TextView btnGiveUp;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private AddContributePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contribute);
        ButterKnife.bind(this);
        tvTitle.setText("投稿");
    }

    @Override
    public void setPresenter() {
        presenter = new AddContributePresenter(this, this);
    }

    @OnClick({R.id.btn_back, R.id.btn_give_up, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_give_up:
                onBackPressed();
                break;
            case R.id.btn_submit:
                submitContribute();
                break;
        }
    }

    private void submitContribute() {
        String contributeTitle = etContributeTitle.getText().toString().trim();
        if (TextUtils.isEmpty(contributeTitle)) {
            ToastUtils.showLong(this,"请输入标题");
            return;
        }
        String contributeContent = etContributeContent.getText().toString().trim();
        if (TextUtils.isEmpty(contributeContent)) {
            ToastUtils.showLong(this,"请输入内容");
            return;
        }

        presenter.submitContribute(contributeTitle,contributeContent);
    }

    @Override
    public void submitSuccess() {
        ToastUtils.showLong(this, "提交成功");
        onBackPressed();
        EventBus.getDefault().post(new ContributeEvent());
    }
}
