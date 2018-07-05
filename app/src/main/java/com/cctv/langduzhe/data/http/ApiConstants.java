package com.cctv.langduzhe.data.http;

/**
 * Created by gentleyin on 2018/1/13.
 */
public final class ApiConstants {

    //测试域名
    static final String TEST_API_HOST = "http://47.92.26.39:8888/";
    //正式域名
    static final String ONLINE_API_HOST = "http://langduzhe.bygmys.com/";
    //测试域名
    public static final int DATA_SOURCE_TYPE_TEST = 0;
    //正式域名
    public static final int DATA_SOURCE_TYPE_ONLINE = 1;

    /**
     * 测试环境七牛图片域名
     */
//    public static final String QI_NIU_IMAGE_DOMAIN = "http://p4v9f6w5q.bkt.clouddn.com/";

    /**
     * 线上环境七牛图片域名
     */
    public static final String QI_NIU_IMAGE_DOMAIN = "http://ldzimage.bygmys.com/";


    //分享地址
    public static final String SHARE_URL = "http://share.bygmys.com/console/share/";

    //微信APP_ID
    public static final String WECHAT_APP_ID = "wx9be29cebb7070b86";

    //微信APP_SECRET
    public static final String WECHAT_APP_SECRET = "12312312313212313213213";

}
