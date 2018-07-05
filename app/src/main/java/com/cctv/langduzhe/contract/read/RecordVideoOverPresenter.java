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
import com.cctv.langduzhe.util.DateConvertUtils;

import java.util.HashMap;

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
public class RecordVideoOverPresenter implements BasePresenter {
    private Context context;
    private RecordVideoOverView mineView;
    private CompositeSubscription subscriptions;
    private NotPostDao postDao;

    public RecordVideoOverPresenter(Context context, RecordVideoOverView view) {
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
    public void saveToSDCard(String path, String name, String coverPath, boolean isPortrait) {
        NotPostEntity notPostEntity = new NotPostEntity();
        notPostEntity.type = 1;
        notPostEntity.readFilepath = path;
        notPostEntity.readName = name;
        notPostEntity.coverPath = coverPath;
        notPostEntity.display = isPortrait;
        getPlayTime(notPostEntity,path);
        postDao.add(notPostEntity);
    }


    private void  getPlayTime(NotPostEntity notPostEntity, String mUri)
    {
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            if (mUri != null)
            {
                HashMap<String, String> headers = null;
                if (headers == null)
                {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            } else
            {
                //mmr.setDataSource(mFD, mOffset, mLength);
            }
            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
            notPostEntity.mediaLength = duration;
//            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
//            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高

        } catch (Exception ex)
        {
        } finally {
            mmr.release();
        }

    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    public void saveVideo(String fileName,String title,int duration,long fileSize,String authCode) {
        String requestJson = handleRequestParams(fileName, title, duration, fileSize, authCode);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestJson);
        Observable<String> observable = ApiClient.apiService.submitMedia(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUpResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private String handleRequestParams(String fileName, String title,int duration,long fileSize, String authCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path",fileName);
        jsonObject.put("title",title);
        jsonObject.put("type", "video");
        jsonObject.put("duration",duration);
        jsonObject.put("fileSize", fileSize);
        jsonObject.put("licenseCode",authCode);
        return jsonObject.toJSONString();
    }

    private void handleUpResult(String s) {
        JSONObject result = JSONObject.parseObject(s);
        if (result.getString("code").equals(RESULT_OK)) {
            ((BaseActivity)context).toActivity(MainActivity.class);
            mineView.showToast("发布成功");
        }else if(result.getString("code").equals("K-000001")){
            mineView.showToast("标题已存在，请修改后重新上传");
        } else {
            mineView.showToast(result.getString("message"));
        }
    }
}
