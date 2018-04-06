package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.ContributeEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/3/17.
 * 说明：我的投稿列表Presenter
 */
public class MineContributePresenter implements BasePresenter {
    private Context context;
    private MineContributeView mineView;
    private CompositeSubscription subscriptions;

    public MineContributePresenter(Context context, MineContributeView view) {
        this.mineView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    public void loadMineContribute(int pageNum) {
        Observable<String> observable = ApiClient.apiService.getContributeList(pageNum);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUserInfo, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    /**
     * @author 尹振东
     * create at 2018/2/17 下午5:58
     * 方法说明：获取我的投稿列表
     */
    private void handleUserInfo(String contributeStr) {
        LogUtil.i(contributeStr);
        ContributeEntity contributeEntity = JSON.parseObject(contributeStr, ContributeEntity.class);
        mineView.setContributeList(contributeEntity);

    }



    @Override
    public void subscribe() {
        loadMineContribute(0);
    }

    @Override
    public void unSubscribe() {

    }
}
