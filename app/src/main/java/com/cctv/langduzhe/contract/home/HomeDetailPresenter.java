package com.cctv.langduzhe.contract.home;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;
import com.cjt2325.cameralibrary.util.LogUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/15.
 * 说明：
 */
public class HomeDetailPresenter implements BasePresenter {


    private final HomeDetailView homeView;
    private Context context;

    private CompositeSubscription subscriptions;

    public HomeDetailPresenter(Context context, HomeDetailView view) {
        homeView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }
    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频详情
     */
    public void getMediaList(HomeVideoEntity.DataBean videoEntity, int pageNum) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(videoEntity,pageNum));
        Observable<String> observable = ApiClient.apiService.getMediaList(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCode, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    homeView.setDataError();
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
            homeView.setMediaData(homeVideoEntity.getData());
        }else {
            homeView.showToast("没有数据");
        }
    }


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:06
     * 方法说明：组装参数
     *
    "seasonId": "string",
    "termId": "string",
    "title": "string"
     */
    private String handleRequestParams(HomeVideoEntity.DataBean videoEntity, int pageNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("seasonId", videoEntity.getSeasonId());
        jsonObject.put("termId", videoEntity.getTermId());
        jsonObject.put("title", videoEntity.getTitle());
        return jsonObject.toJSONString();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    public void getCommentList(String mediaId, int pageNum) {

        Observable<String> observable = ApiClient.apiService.getCommentList(mediaId,pageNum,10);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleComments, throwable -> {
                    Toast.makeText(context, "暂无评论", Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    /**
    * @author 尹振东
    * create at 2018/2/16 上午11:26
    * 方法说明：获取评论列表
    */
    private void handleComments(String s) {
        CommandEntity commandEntity = JSON.parseObject(s, CommandEntity.class);
        if (commandEntity != null && RESULT_OK.equals(commandEntity.getCode())) {
            homeView.setCommentList(commandEntity.getData());
        }else {
            homeView.showToast("没有数据");
        }
    }

    public void submitComment(String content) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(content));
        Observable<String> observable = ApiClient.apiService.submitComment(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleComments, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:06
     * 方法说明：组装参数
     */
    private String handleRequestParams(String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        jsonObject.put("pageSize", 10);
        return jsonObject.toJSONString();
    }

    public void collectVideo(String mediaId) {
        Observable<String> observable = ApiClient.apiService.collectionRead(mediaId);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCollect, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleCollect(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            homeView.setCollectResult();
        }
    }
}
