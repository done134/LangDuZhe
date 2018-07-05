package com.cctv.langduzhe.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.feature.LoginActivity;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.view.widget.LoadingProgressDialog;

/**
 * Fragment 共有父类，实现懒加载
 * Created by gentleyin on 2018/1/13.
 */
public abstract class BaseFragment extends Fragment implements BaseView{

//    private PublishSubject<FragmentLifecycleEvent> fragmentLifecycleSubject = PublishSubject.create();

    protected boolean isViewInitiated;
    protected boolean isDataLoaded;
    protected Context context;
//    private BasePresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        setPresenter();
        prepareRequestData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
//        fragmentLifecycleSubject.onNext(FragmentLifecycleEvent.DESTROY_VIEW);

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public abstract void requestData();
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
        Intent intent = new Intent(getActivity(), clas);
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
        Intent intent = new Intent(getActivity(), clas);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData();
    }
    public boolean prepareRequestData() {
        return prepareRequestData(false);
    }
    public boolean prepareRequestData(boolean forceUpdate) {
        if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            requestData();
            isDataLoaded = true;
            return true;
        }
        return false;
    }

    /**
     * @author 尹振东
     * create at 2018/2/14 下午12:24
     * 方法说明：展示全屏进度条
     */
    public void showProgress() {
        ((BaseActivity)getActivity()).showProgress();
    }

    /**
     * @author 尹振东
     * create at 2018/2/14 下午12:24
     * 方法说明：隐藏全屏进度条
     */
    public void dismissProgress() {
        ((BaseActivity)getActivity()).dismissProgress();
    }

    public void showToast(String message){
        if(!TextUtils.isEmpty(message)) {
            ToastUtils.showLong(getActivity(), message);
        }
    }

    @Override
    public void toLogin() {
        toActivity(LoginActivity.class);
    }

    /**
     * @author 尹振东
     * create at 2018/4/14 下午4:40
     * 方法说明：在某些需要用户登录才能操作的地方用到
     * 已登录则返回true，否则跳转到登录页面
     */
    protected boolean hasLogin() {
        String hasLogin = (String) SPUtils.get(getActivity(), PreferenceContents.TOKEN, "");
        if (!TextUtils.isEmpty(hasLogin)) {
            return true;
        } else {
            toActivity(LoginActivity.class);
            return false;
        }
    }
}
