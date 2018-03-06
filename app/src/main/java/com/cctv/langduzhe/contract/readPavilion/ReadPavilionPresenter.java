package com.cctv.langduzhe.contract.readPavilion;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：
 */
public class ReadPavilionPresenter implements BasePresenter {
    private Context context;
    private ReadPavilionView readView;
    private CompositeSubscription subscriptions;

    public ReadPavilionPresenter(Context context, ReadPavilionView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }
    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频列表
     */
    public void getMediaList(int pageNum,String order) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(pageNum,order));
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
     * 方法说明：处理请求验证码返回结果
     */
    private void handleCode(String result){
        HomeVideoEntity homeVideoEntity = JSON.parseObject(result, HomeVideoEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            readView.setHomeMedias(homeVideoEntity.getData());
        }else {
            readView.showToast("没有数据");
        }
    }

    private String handleRequestParams(int pageNum,String order) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        if(!TextUtils.isEmpty(order)) {
            jsonObject.put("order", order);
        }
        jsonObject.put("readerType", "ordinary");
        return jsonObject.toJSONString();
    }
    @Override

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    /**
    * @author 尹振东
    * create at 2018/2/28 下午8:32
    * 方法说明：获取我的朗读列表
    */
    public void getMineReadList(int pageNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                       jsonObject.toJSONString());
        Observable<String> observable = ApiClient.apiService.getMineReadList(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCode, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }
}
