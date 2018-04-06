package com.cctv.langduzhe.contract.articles;

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
 * on 2018/3/19.
 * 说明：文字详情页面Presenter
 */
public class ArticleDetailPresenter implements BasePresenter {
    private Context context;
    private ArticleDetailView readView;
    private CompositeSubscription subscriptions;

    public ArticleDetailPresenter(Context context, ArticleDetailView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }


    /**
    * @author 尹振东
    * create at 2018/3/29 上午2:05
    * 方法说明：获取朗读列表
    */
    public void getMediaList(String mediaId, int pageNum) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(mediaId,pageNum));
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
            readView.setMediaData(homeVideoEntity.getData());
        }else {
            readView.showToast("没有数据");
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
    private String handleRequestParams(String mediaId, int pageNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("articleId", mediaId);
        return jsonObject.toJSONString();
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    public void getArticleDetail(String mediaId) {

        Observable<String> observable = ApiClient.apiService.getArticleDetail(mediaId);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleComments, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    /**
     * @author 尹振东
     * create at 2018/2/16 上午11:26
     * 方法说明：获取评论列表
     */
    private void handleComments(String s) {
        LogUtil.i(s);
        JSONObject jsonObject = JSON.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            String content = jsonObject.getJSONObject("data").getString("content");
                readView.setArticleDetail(content);
        }
    }

}
