package com.cctv.langduzhe.feature;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.contract.CommentPresenter;
import com.cctv.langduzhe.contract.CommentView;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.SoftKeyBoardListener;
import com.cctv.langduzhe.util.StatusBarUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：提交评论页面
 */
public class CommentActivity extends Activity implements CommentView {

    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_send_comment)
    TextView btnSendComment;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;
    @BindView(R.id.rl_input_view)
    RelativeLayout rlInputView;
    @BindView(R.id.rl_cancel)
    FrameLayout rlCancel;
    private CommentPresenter presenter;
    private String mediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter();
        StatusBarUtil.immersiveStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_input_comment);
        ButterKnife.bind(this);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
        mediaId = getIntent().getExtras().getString("mediaId");
//        controlKeyboardLayout(rlInput, rlInputView);
        setKeyboardListener();
        showKeyboard();
    }

    private void setKeyboardListener() {
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlInputView.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, height);
                        rlInputView.setLayoutParams(layoutParams);
                    }
                }, 200);
            }

            @Override
            public void keyBoardHide(int height) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlInputView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                rlInputView.setLayoutParams(layoutParams);
            }
        });

    }

    /**
     * @param root         最外层布局,需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView,滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            //获取root在窗体的可视区域
            root.getWindowVisibleDisplayFrame(rect);
            //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
            int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
            //若不可视区域高度大于100，则键盘显示
            if (rootInvisibleHeight > 100) {
                int[] location = new int[2];
                //获取scrollToView在窗体的坐标
                scrollToView.getLocationInWindow(location);
                //计算root滚动高度，使scrollToView在可见区域
                int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                root.scrollTo(0, srollHeight);
            } else {
                //键盘隐藏
                root.scrollTo(0, 0);
            }
        });
    }

    private void showKeyboard() {
        new Handler().postDelayed(() -> {
            etComment.setFocusable(true);

            etComment.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(etComment, 0);
            }
        }, 500);
    }

    @Override
    public void setPresenter() {
        presenter = new CommentPresenter(this, this);
    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void toLogin() {

    }


    @OnClick({R.id.btn_send_comment, R.id.rl_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_comment:
                if (CommonUtil.isFastClick()) {
                    String contentStr = etComment.getText().toString().trim();
                    if (TextUtils.isEmpty(contentStr)) {
                        showToast("请输入评论内容");
                        return;
                    }
                    showProgress();
                    presenter.submitComment(mediaId, contentStr);

                }
                break;
            case R.id.rl_cancel:
                finish();
                break;
        }
    }

    @Override
    public void setSuccess() {
        dismissProgress();
        Intent intent = new Intent();
        intent.putExtra("commentStr", etComment.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

}
