package com.cctv.langduzhe.feature.message;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.view.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/1/17.
 * 说明：消息详情页面
 */
public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_service_people_icon)
    CircleImageView ivServicePeopleIcon;
    @BindView(R.id.tv_message_title)
    TextView tvMessageTitle;
    @BindView(R.id.tv_message_content)
    TextView tvMessageContent;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        getIntentData();
    }

    private void getIntentData() {
        String title = getIntent().getStringExtra("title");
        String msgContent = getIntent().getStringExtra("msgContent");
        tvTitle.setText(title);
        tvMessageContent.setText(msgContent);

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void setPresenter() {

    }
}
