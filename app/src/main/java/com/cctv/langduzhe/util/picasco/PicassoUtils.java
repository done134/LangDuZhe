package com.cctv.langduzhe.util.picasco;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by gentleyin
 * on 2018/2/18.
 * 说明：
 */
public class PicassoUtils {
    public static void loadImageByurl(Context ctx, String url, ImageView imageView) {
        Picasso.with(ctx).load(url).into(imageView);

    }
    public static void loadImageByRes(int res, Context ctx, ImageView imageView) {
        Picasso.with(ctx).load(res).into(imageView);

    }
    public static void loadImageByFile(Context ctx, ImageView iv, String filePath) {
        File file = new File(filePath);
        loadImageByFile(ctx, iv, file);
    }
    public static void loadImageByFile(Context ctx, ImageView iv, File file) {
        Picasso
                .with(ctx)
                .load(file)
                .into(iv);
    }

    public static void loadImageByURI(Context ctx, ImageView iv, int res) {
        Uri uri = resourceIdToUri(ctx, res);

        Picasso
                .with(ctx)
                .load(uri)
                .into(iv);
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    public static void clearCache(Context context, Uri uri, File file, String path) {

        if (!TextUtils.isEmpty(uri.toString())) {
            Picasso.with(context).invalidate(uri);
            return;
        }
        if (!file.exists()) {
            Picasso.with(context).invalidate(file);
            return;
        }
        if (!TextUtils.isEmpty(path)) {
            Picasso.with(context).invalidate(path);
        }
    }

    public static OkHttpClient getOkClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            client.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

    }

}
