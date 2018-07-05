package com.cctv.langduzhe.contract.readPavilion;

import android.content.Context;
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
 * on 2018/4/15.
 * 说明：
 */
public class SearchReadPresenter implements BasePresenter {
    private Context context;
    private SearchReadView readView;
    private CompositeSubscription subscriptions;

    public SearchReadPresenter(Context context, SearchReadView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：搜索朗读亭视频列表
     */
    public void getMediaList(int pageNum, String searchStr) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(pageNum,  searchStr));
        Observable<String> observable = ApiClient.apiService.getMediaList(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCode, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }


    private String handleRequestParams(int pageNum, String searchStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("mediaType", "video");
        jsonObject.put("search", searchStr);
        jsonObject.put("readerType", "ordinary");
        return jsonObject.toJSONString();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午2:48
     * 方法说明：处理请求验证码返回结果
     */
    private void handleCode(String result) {
        HomeVideoEntity homeVideoEntity = JSON.parseObject(result, HomeVideoEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            readView.setHomeMedias(homeVideoEntity.getData());
        } else {
            readView.showToast("没有数据");
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
