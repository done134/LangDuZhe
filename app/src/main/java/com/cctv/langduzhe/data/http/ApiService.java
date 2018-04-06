package com.cctv.langduzhe.data.http;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by gentleyin on 2018/1/13.
 */
public interface ApiService {


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:22
     * 方法说明：发送验证码
     */
    @FormUrlEncoded
    @POST("api/free/sendLoginMsg")
    Observable<String> getPhoneCode(@Field("mobile") String phone,
                                    @Field("captchaId") String captchaId, @Field("captchaCode") String captchaCode);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：登录
     */
    @POST("api/free/login")
    Observable<String> login(@Body RequestBody paramsQ);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：刷新登录token
     */
    @GET("api/reflushToken")
    Observable<String> refreshToken();


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取用户信息
     */
    @GET("api/reader")
    Observable<String> getUserInfo();


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：保存用户信息
     */
    @PUT("api/reader")
    Observable<String> saveUserInfo(@Query("img") String phone, @Query("name") String captchaCode);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取七牛token，用来上传文件
     * @param bucket video/image
     * @param fileName
     * @param screenOrient
     */
    @GET("api/qiniu/")
    Observable<String> getQiNiuToken(@Query("bucket") String bucket, @Query("fileFullName") String fileName, @Query("display")  String screenOrient);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：收藏朗读作品
     */
    @FormUrlEncoded
    @POST("api/collect")
    Observable<String> collectionRead(@Field("mediaId") String mediaId);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：收藏朗读作品
     */
    @DELETE("api/collect")
    Observable<String> deleteCollection(@Query("mediaIds") String mediaId);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取收藏列表
     */
    @GET("api/collections")
    Observable<String> getCollectionList(@Query("pageNum") int pageNum);

    /**
    * @author 尹振东
    * create at 2018/2/14 下午9:23
    * 方法说明：发表评论
     * @param commentRequest
    */
    @POST("api/common")
    Observable<String> submitComment(@Body RequestBody commentRequest);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取评论列表
     */
    @GET("api/free/commons")
    Observable<String> getCommentList(@Query("mediaId") String mediaId,@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：保存媒体信息
     */
    @POST("api/media")
    Observable<String> submitMedia(@Body RequestBody paramsQ);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：查询媒体列表
     */
    @POST("api/optTok/media")
    Observable<String> getMediaList(@Body RequestBody paramsQ);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：查询媒体包列表
     */
    @GET("api/optTok/package")
    Observable<String> getMediaPackageList(@QueryMap Map<String, Object> options);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：增加观看数量
     */
    @PUT("api/watch")
    Observable<String> watchMedia(@Query("mediaId") String mediaId);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取消息列表
     */
    Observable<String> getMessageList(int pageNum);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取我的朗读列表
     */
    @POST("api/media/my")
    Observable<String> getMineReadList(@Body RequestBody paramsQ);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：点赞朗读作品
     */
    @FormUrlEncoded
    @POST("api/like")
    Observable<String> likeRead(@Field("mediaId") String mediaId);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：取消点赞朗读作品
     */
    @DELETE("api/like")
    Observable<String> unlikeRead(@Query("mediaId") String mediaId);



    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：提交意见反馈
     */
    @FormUrlEncoded
    @POST("api/feedback")
    Observable<String> submitFeedBack(@Field("feedbackContent") String feedbackContent);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：我的投稿列表
     */
    @GET("api/articles/my")
    Observable<String> getContributeList(@Query("pageNum") int pageNum);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：提交投稿
     */
    @FormUrlEncoded
    @POST("api/article")
    Observable<String> submitContribute(@Field("title") String title,@Field("content") String content);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取文字主题列表
     */
    @GET("api/free/themes")
    Observable<String> getThemeList(@Query("pageNum") int pageNum);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取文字列表
     */
    @GET("api/free/articles")
    Observable<String> getArticleList(@Query("pageNum") int pageNum,@Query("themeId") String themeId);

    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：获取文字详情
     */
    @GET("api/free/article")
    Observable<String> getArticleDetail(@Query("articleId") String articleId);


    /**
     * @author 尹振东
     * create at 2018/2/14 下午9:23
     * 方法说明：校验授权码
     */
    @GET("api/license")
    Observable<String> checkAuthCode(@Query("licenseCode") String licenseCode);
}

