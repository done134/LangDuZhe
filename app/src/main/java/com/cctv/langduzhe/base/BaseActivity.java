package com.cctv.langduzhe.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.cctv.langduzhe.feature.LoginActivity;
import com.cctv.langduzhe.util.StatusBarUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.view.widget.LoadingProgressDialog;

/**
 * Created by gentleyin on 2018/1/13.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView{

    private ProgressDialog progressDialog;
    /*
     * 解决Vector兼容性问题
     * http://www.jianshu.com/p/e3614e7abc03
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter();
        StatusBarUtil.immersiveStatusBar(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
    * @author 尹振东
    * create at 2018/1/16 下午11:32
    * 方法说明：实现简单页面跳转
    */
    public void toActivity(Class clas) {
        toActivity(clas, null);
    }

    /**
    * @author 尹振东
    * create at 2018/1/16 下午11:32
    * 方法说明：普通页面跳转，带参数给下个页面
    */
    public void toActivity(Class clas, Bundle bundle) {
        Intent intent = new Intent(this, clas);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
    * @author 尹振东
    * create at 2018/1/16 下午11:33
    * 方法说明：有返回结果的页面跳转
    */
    public void toActivityForResult(Class clas, int requestCode) {
        toActivityForResult(clas, null, requestCode);
    }

    /**
    * @author 尹振东
    * create at 2018/1/16 下午11:33
    * 方法说明：有返回结果的页面跳转，带参数给下个页面
    */
    public void toActivityForResult(Class clas, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clas);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
    * @author 尹振东
    * create at 2018/2/14 下午12:24
    * 方法说明：展示全屏进度条
    */
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
    * @author 尹振东
    * create at 2018/2/14 下午12:24
    * 方法说明：隐藏全屏进度条
    */
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void showToast(String message){
        if(!TextUtils.isEmpty(message)) {
            ToastUtils.showLong(this, message);
        }
    }

    @Override
    public void toLogin() {
        finish();
        toActivity(LoginActivity.class);
    }
}
