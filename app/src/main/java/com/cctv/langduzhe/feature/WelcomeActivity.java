package com.cctv.langduzhe.feature;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.util.system.StatusBarHelper;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gentleyin on 2018/1/14.
 */
public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.statusBarLightMode(this);
        gotoMainPage();
    }

    private void gotoMainPage() {
        String hasLogin = (String) SPUtils.get(this, PreferenceContents.TOKEN, "");
        if (!TextUtils.isEmpty(hasLogin)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void setPresenter() {

    }
}
