package com.cctv.langduzhe.feature;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.LoginPresenter;
import com.cctv.langduzhe.contract.LoginView;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.StatusBarUtil;
import com.cctv.langduzhe.util.ToastUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 *
 * @author gentleyin
 */
public class LoginActivity extends BaseActivity implements LoginView{


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.auth_code)
    EditText authCode;
    @BindView(R.id.btn_get_auth_code)
    TextView btnGetAuthCode;
    @BindView(R.id.btn_sign_in)
    TextView btnSignIn;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersiveStatusBar(this,0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        phone.setError(null);
        authCode.setError(null);

        // Store values at the time of the login attempt.
        String email = phone.getText().toString();
        String password = authCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            authCode.setError(getString(R.string.error_invalid_password));
            focusView = authCode;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            phone.setError(getString(R.string.error_field_required));
            focusView = phone;
            cancel = true;
        } else if (!CommonUtil.isMobileNumber(email)) {
            phone.setError(getString(R.string.error_invalid_phone));
            focusView = phone;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            showProgress();
            loginPresenter.login(email,password);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() == 6;
    }
    


    @OnClick({R.id.btn_back, R.id.btn_get_auth_code, R.id.btn_sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                //返回
                onBackPressed();
                break;
            case R.id.btn_get_auth_code:
                //获取手机验证码
                if (TextUtils.isEmpty(phone.getText().toString().trim())) {
                    ToastUtils.showLong(this,"请输入手机号");
                }else {
                    loginPresenter.getPhoneCode(phone.getText().toString());
                }
                break;
            case R.id.btn_sign_in:
                //登录
                attemptLogin();
//                toActivity(MainActivity.class);
//                finish();
                break;
        }
    }

    @Override
    public void setPresenter() {
        loginPresenter = new LoginPresenter(this, this);
    }

    @Override
    public TextView getAuthView() {
        return btnGetAuthCode;
    }
}

