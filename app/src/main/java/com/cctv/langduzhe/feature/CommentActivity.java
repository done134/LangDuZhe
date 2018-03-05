package com.cctv.langduzhe.feature;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.CommentPresenter;
import com.cctv.langduzhe.contract.CommentView;
import com.cctv.langduzhe.util.Log;
import com.cjt2325.cameralibrary.util.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：
 */
public class CommentActivity extends BaseActivity implements CommentView {

    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_send_comment)
    TextView btnSendComment;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;
    @BindView(R.id.rl_input_view)
    RelativeLayout rlInputView;
    private CommentPresenter presenter;
    private String mediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_input_comment);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mediaId = getIntent().getExtras().getString("mediaId");
    }

    @Override
    public void setPresenter() {
        presenter = new CommentPresenter(this, this);
    }


    @OnClick(R.id.btn_send_comment)
    public void onViewClicked() {
        String contentStr = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(contentStr)) {
            showToast("请输入评论内容");
            return;
        }
        presenter.submitComment(mediaId,contentStr);
    }

    @Override
    public void setSuccess() {
        finish();
    }
}
