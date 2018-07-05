package com.cctv.langduzhe.contract;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.AppConstants;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.ApiConstants;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.Log;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/28.
 * 说明：上传到七牛Presenter
 */
public class PostPresenter implements BasePresenter {

    public static final String VIDEO_TYPE = "video";
    public static final String VOICE_TYPE = "audio";
    public static final String IMAGE_TYPE = "image";
    private final PostView collectView;
    private Context context;

    private String filePath;

    private String fileName;
    private CompositeSubscription subscriptions;
    private UploadManager uploadManager;
    private String bucket;

    public PostPresenter(Context context, PostView view) {
        collectView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取验证码
     */
    public void postFile(String bucket, String filePath, boolean isPortrait) {
        if (uploadManager == null) {
            initUpLoadManager();
        }
        this.filePath = filePath;
        this.bucket = bucket;
        String screenOrient = isPortrait ? "y" : "x";
        if (TextUtils.isEmpty(filePath)) {
            collectView.showToast("文件路径为空");
            return;
        }
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        Observable<String> observable = ApiClient.apiService.getQiNiuToken(bucket,fileName,screenOrient);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleResult, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show());
        subscriptions.add(subscription);
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取验证码
     */
    public void postFile(String bucket, String filePath) {
        if (uploadManager == null) {
            initUpLoadManager();
        }
        this.filePath = filePath;
        this.bucket = bucket;
        if (TextUtils.isEmpty(filePath)) {
            collectView.showToast("文件路径为空");
            return;
        }
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        Observable<String> observable = ApiClient.apiService.getQiNiuToken(bucket,fileName);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleResult, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show());
        subscriptions.add(subscription);
    }

    private void initUpLoadManager() {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
         uploadManager = new UploadManager(config);
    }

    private void handleResult(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            String token = jsonObject.getString("data");
            uploadToQiniu(token, filePath, fileName);
        }
    }

    private void uploadToQiniu(String token, String filePath, String fileName) {
        uploadManager.put(filePath, fileName, token,
                (key, info, response) -> {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if(info.isOK()) {
                        Log.i("qiniu", "Upload Success");
                        if (bucket.equals(IMAGE_TYPE)) {
                            collectView.postSucceed(ApiConstants.QI_NIU_IMAGE_DOMAIN+fileName,0,0);
                        }else {
                            org.json.JSONObject avinfoObj = response.optJSONObject("avinfo");
                            org.json.JSONObject formatObj = avinfoObj.optJSONObject("format");
                            int duration = formatObj.optInt("duration");
                            long size = response.optInt("fsize");
                            collectView.postSucceed(fileName,duration,size);
                        }
                    } else {
                        Log.i("qiniu", "Upload Fail");
                        collectView.dismissProgress();
                        collectView.showToast("上传失败");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    }
                }, null);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
