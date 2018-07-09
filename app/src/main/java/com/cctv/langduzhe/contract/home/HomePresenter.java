package com.cctv.langduzhe.contract.home;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;
import com.cctv.langduzhe.util.Log;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/15.
 * 说明：首页视频列表Presenter
 */
public class HomePresenter implements BasePresenter {

    private final HomeView homeView;
    private Context context;

    private CompositeSubscription subscriptions;

    public HomePresenter(Context context, HomeView view) {
        homeView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频列表
     */
    public void getMediaList(int pageNum, String season) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(pageNum, season));
        Observable<String> observable = ApiClient.apiService.getMediaList(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCode, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午2:48
     * 方法说明：处理返回结果
     */
    private void handleCode(String result) {
        HomeVideoEntity homeVideoEntity = JSON.parseObject(result, HomeVideoEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            homeView.setHomeMedias(homeVideoEntity.getData());
        } else {
            homeView.showToast("没有数据");
        }
    }

    private String handleRequestParams(int pageNum, String seasonId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("seasonId", seasonId);
        return jsonObject.toJSONString();
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
