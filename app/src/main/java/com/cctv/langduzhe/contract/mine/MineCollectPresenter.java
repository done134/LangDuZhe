package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;


import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/28.
 * 说明：
 */
public class MineCollectPresenter implements BasePresenter {
    private Context context;
    private MineCollectView collectView;
    private CompositeSubscription subscriptions;

    public MineCollectPresenter(Context context, MineCollectView view) {
        this.collectView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    public void loadReadInfo(int pageNum) {
        Observable<String> observable = ApiClient.apiService.getCollectionList(pageNum);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUserInfo, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleUserInfo(String s) {
        HomeVideoEntity homeVideoEntity = JSON.parseObject(s, HomeVideoEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            collectView.setMediaList(homeVideoEntity.getData());
        }else {
            collectView.showToast("没有数据");
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
