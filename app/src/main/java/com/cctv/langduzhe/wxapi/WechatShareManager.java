package com.cctv.langduzhe.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.data.http.ApiConstants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 业务名:
 * 功能说明: 实现微信分享功能的核心类
 * 编写日期:	2017/1/3
 * 作者:	尹振东
 * <p/>
 * 历史记录
 * 1、修改日期：
 * 修改人：
 * 修改内容：
 */
public class WechatShareManager {

    private static final int THUMB_SIZE = 150;

    public static final int WECHAT_SHARE_WAY_TEXT = 1;   //文字
    public static final int WECHAT_SHARE_WAY_PICTURE = 2; //图片
    public static final int WECHAT_SHARE_WAY_WEBPAGE = 3;  //链接
    public static final int WECHAT_SHARE_WAY_VIDEO = 4; //视频
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话
    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    private static WechatShareManager mInstance;
    private ShareContent mShareContentText, mShareContentPicture, mShareContentWebpag, mShareContentVideo;
    private IWXAPI mWXApi;
    private Context mContext;

    private WechatShareManager(Context context) {
        this.mContext = context;
        //初始化数据
        //初始化微信分享代码
        initWechatShare(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static WechatShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

    private void initWechatShare(Context context) {
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(context, ApiConstants.WECHAT_APP_ID, true);
        }
        mWXApi.registerApp(ApiConstants.WECHAT_APP_ID);
    }

    /**
     * 通过微信分享
     *
     * @param shareType 分享的类型（朋友圈，会话）
     */
    public void shareByWebchat(ShareContent shareContent, int shareType) {

        switch (shareContent.getShareWay()) {
            case WECHAT_SHARE_WAY_TEXT:
                shareText(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_PICTURE:
                sharePicture(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_WEBPAGE:
                shareWebPage(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_VIDEO:
                shareVideo(shareContent, shareType);
                break;
        }


    }

    private abstract class ShareContent {
        protected abstract int getShareWay();

        protected abstract String getContent();

        protected abstract String getTitle();

        protected abstract String getURL();

        protected abstract Bitmap getPicture();
    }

    /**
     * 设置分享文字的内容
     *
     * @author chengcj1
     */
    public class ShareContentText extends ShareContent {
        private String content;

        /**
         * 构造分享文字类
         */
        public ShareContentText(String content) {
            this.content = content;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_TEXT;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }

        @Override
        protected Bitmap getPicture() {
            return null;
        }
    }

    /*
     * 获取文本分享对象
     */
    public ShareContent getShareContentText(String content) {
        if (mShareContentText != null) {
            mShareContentText = null;
        }
        mShareContentText = new ShareContentText(content);
        return (ShareContentText) mShareContentText;
    }

    /**
     * 设置分享图片的内容
     *
     * @author chengcj1
     */
    public class ShareContentPicture extends ShareContent {
        private Bitmap pictureResource;

        public ShareContentPicture(Bitmap pictureResource) {
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_PICTURE;
        }

        @Override
        protected Bitmap getPicture() {
            return pictureResource;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }
    }

    /*
     * 获取图片分享对象
     */
    public ShareContent getShareContentPicture(Bitmap pictureResource) {
        if (mShareContentPicture != null) {
            mShareContentPicture = null;
        }
        mShareContentPicture = new ShareContentPicture(pictureResource);
        return mShareContentPicture;
    }

    /**
     * 设置分享链接的内容
     *
     * @author chengcj1
     */
    public class ShareContentWebPage extends ShareContent {
        private String title;
        private String content;
        private String url;
        private Bitmap pictureResource;

        public ShareContentWebPage(String title, String content, String url, Bitmap pictureResource) {
            this.title = title;
            this.content = content;
            this.url = url;
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_WEBPAGE;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return title;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected Bitmap getPicture() {
            return pictureResource;
        }
    }

    /*
     * 获取网页分享对象
     */
    public ShareContent getShareContentWebpag(String title, String content, String url, Bitmap pictureResource) {
        if (mShareContentWebpag != null) {
            mShareContentWebpag = null;
        }
        mShareContentWebpag = new ShareContentWebPage(title, content, url, pictureResource);
        return mShareContentWebpag;
    }

    /**
     * 设置分享视频的内容
     *
     * @author chengcj1
     */
    public class ShareContentVideo extends ShareContent {
        private String url;

        public ShareContentVideo(String url) {
            this.url = url;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_VIDEO;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected Bitmap getPicture() {
            return null;
        }
    }

    /*
     * 获取视频分享内容
     */
    public ShareContent getShareContentVideo(String url) {
        if (mShareContentVideo != null) {
            mShareContentVideo = null;
        }
        mShareContentVideo = new ShareContentVideo(url);
        return mShareContentVideo;
    }

    /*
     * 分享文字
     */
    private void shareText(ShareContent shareContent, int shareType) {
        String text = shareContent.getContent();
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /*
     * 分享图片
     */
    private void sharePicture(ShareContent shareContent, int shareType) {
        Bitmap bitmap = shareContent.getPicture();
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//        bitmap.recycle();
        msg.setThumbImage(thumbBitmap);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("imgae");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /*
     * 分享链接
     */
    private void shareWebPage(ShareContent shareContent, int shareType) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getURL();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();
        Bitmap thumb = Bitmap.createScaledBitmap(shareContent.getPicture(), THUMB_SIZE, THUMB_SIZE, true);
        if (thumb == null) {
            Toast.makeText(mContext, "图片不能为空", Toast.LENGTH_SHORT).show();
        } else {
            msg.setThumbImage(thumb);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /*
     * 分享视频
     */
    private void shareVideo(ShareContent shareContent, int shareType) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = shareContent.getURL();

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
//		BitmapFactory.decodeStream(new URL(video.videoUrl).openStream());
        /**
         * 测试过程中会出现这种情况，会有个别手机会出现调不起微信客户端的情况。造成这种情况的原因是微信对缩略图的大小、title、description等参数的大小做了限制，所以有可能是大小超过了默认的范围。
         * 一般情况下缩略图超出比较常见。Title、description都是文本，一般不会超过。
         */
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        thumb.recycle();
        msg.setThumbImage(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}