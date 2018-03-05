package com.cctv.langduzhe.contract;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/3/4.
 * 说明：
 */
public class LikePresenter implements BasePresenter {
    private final LikeView collectView;
    private Context context;

    private CompositeSubscription subscriptions;

    public LikePresenter(Context context, LikeView view) {
        collectView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取验证码
     */
    public void likeRead(String mediaId) {
        Observable<String> observable = ApiClient.apiService.collectionRead(mediaId);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleResult(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            collectView.likeResult(true);
        }
    }
    public void unlikeRead(String mediaIds) {
        Observable<String> observable = ApiClient.apiService.unlikeRead(mediaIds);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleDelResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }


    private void handleDelResult(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            collectView.likeResult(false);
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
