package com.cctv.langduzhe.contract;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.MediaPackageEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/7/5.
 * 说明：查询媒体包列表接口公用View层 type 参数值为 t=期,s=季
 */
public class MediaPackagePresenter implements BasePresenter {
    private final MediaPackageView mView;
    private Context context;

    private CompositeSubscription subscriptions;

    public MediaPackagePresenter(Context context, MediaPackageView view) {
        mView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/7/5 下午4:52
     * 方法说明：查询媒体包列表 type 参数值为 t=期,s=季
     */
    public void getPackageList(String type) {

        Observable<String> observable = ApiClient.apiService.getMediaPackageList(type, 0, 100);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handlePackage, throwable -> {
                    Toast.makeText(context, "暂无数据", Toast.LENGTH_LONG).show();
                    mView.setPackageInfo(null);

                });
        subscriptions.add(subscription);
    }

    private void handlePackage(String s) {
//        JSONObject jsonObject = JSONObject.parseObject(s);
        MediaPackageEntity packageEntity = JSON.parseObject(s, MediaPackageEntity.class);
        if (packageEntity != null && RESULT_OK.equals(packageEntity.getCode())) {
            mView.setPackageInfo(packageEntity.getData());
        }else {
            mView.showToast("暂无数据");
            mView.setPackageInfo(null);
        }
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
