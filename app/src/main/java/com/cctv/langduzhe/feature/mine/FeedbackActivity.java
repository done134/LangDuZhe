package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.mine.FeedBackPresenter;
import com.cctv.langduzhe.contract.mine.FeedBackView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YinZhendong on 2018/3/7.
 */

public class FeedbackActivity extends BaseActivity implements FeedBackView {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    private FeedBackPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        tvTitle.setText("意见反馈");
        tvRight.setText("提交");
    }

    @Override
    public void setPresenter() {
        presenter = new FeedBackPresenter(this, this);
    }

    @Override
    public void submitSuccess() {
        showToast("反馈已提交");
        finish();
    }

    @OnClick({R.id.btn_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_right:
                String feedbackContent = etFeedbackContent.getText().toString().trim();
                if (TextUtils.isEmpty(feedbackContent)) {
                    showToast("请输入意见反馈内容");
                }else {
                    presenter.feedback(feedbackContent);
                }
                break;
        }
    }
}
