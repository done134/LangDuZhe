package com.cctv.langduzhe.contract.read;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.db.NotPostDao;
import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.feature.MainActivity;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/26.
 * 说明：
 */
public class RecordVoiceOverPresenter implements BasePresenter {
    private Context context;
    private RecordVoiceOverView mineView;
    private CompositeSubscription subscriptions;
    private NotPostDao postDao;

    public RecordVoiceOverPresenter(Context context, RecordVoiceOverView view) {
        this.mineView = view;
        this.context = context;
        postDao = new NotPostDao(context);
        this.subscriptions = new CompositeSubscription();
    }

    /**
    * @author 尹振东
    * create at 2018/2/27 上午10:13
    * 方法说明：保存到数据库
    */
    public void saveToSDCard(String path,String name) {
        NotPostEntity notPostEntity = new NotPostEntity();
        notPostEntity.type = 0;
        notPostEntity.readFilepath = path;
        notPostEntity.readName = name;
        postDao.add(notPostEntity);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    public void saveVoice(String fileName,String title,int duration,long fileSize) {
        String requestJson = handleRequestParams(fileName, title, duration, fileSize);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestJson);
        Observable<String> observable = ApiClient.apiService.submitMedia(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUpResult, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show());
        subscriptions.add(subscription);
    }

    private String handleRequestParams(String fileName, String title,int duration,long fileSize) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path",fileName);
        jsonObject.put("title",title);
        jsonObject.put("type", "audio");
        jsonObject.put("duration",duration);
        jsonObject.put("fileSize", fileSize);
        return jsonObject.toJSONString();
    }

    private void handleUpResult(String s) {
        JSONObject result = JSONObject.parseObject(s);
        if (result.getString("code").equals(RESULT_OK)) {
            ((BaseActivity)context).toActivity(MainActivity.class);
            mineView.showToast("保存成功");
        }else {
            mineView.showToast(result.getString("message"));
        }
    }
}
