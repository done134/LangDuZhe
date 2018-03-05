/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.cctv.langduzhe.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.feature.read.RecordVoiceActivity;


/**
 * 通用操作类
 *
 * @author Lemon
 * @use CommonUtil.xxxMethod(...);
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";

    public CommonUtil() {/* 不能实例化**/}

    //电话<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void call(Activity context, String phone) {
        if (StringUtil.isNotEmpty(phone, true)) {
            Uri uri = Uri.parse("tel:" + phone.trim());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            toActivity(context, intent);
            return;
        }
        ToastUtils.showShort(context, "请先选择号码哦~");
    }

    //电话>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 发送信息，多号码
     *
     * @param context
     * @param phoneList
     */
    public static void toMessageChat(Activity context, List<String> phoneList) {
        if (context == null || phoneList == null || phoneList.size() <= 0) {
            Log.e(TAG, "sendMessage context == null || phoneList == null || phoneList.size() <= 0 " +
                    ">> showShortToast(context, 请先选择号码哦~); return; ");

            ToastUtils.showShort(context, "请先选择号码哦~");
            return;
        }

        String phones = "";
        for (int i = 0; i < phoneList.size(); i++) {
            phones += phoneList.get(i) + ";";
        }
        toMessageChat(context, phones);
    }

    /**
     * 发送信息，单个号码
     *
     * @param context
     * @param phone
     */
    public static void toMessageChat(Activity context, String phone) {
        if (context == null || !StringUtil.isNotEmpty(phone, true)) {
            Log.e(TAG, "sendMessage  context == null || StringUtil.isNotEmpty(phone, true) == false) >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", phone);
        intent.setType("vnd.android-dir/mms-sms");
        toActivity(context, intent);

    }

    //信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 分享信息
     *
     * @param context
     * @param toShare
     */
    public static void shareInfo(Activity context, String toShare) {
        if (context == null || !StringUtil.isNotEmpty(toShare, true)) {
            Log.e(TAG, "shareInfo  context == null || StringUtil.isNotEmpty(toShare, true) == false >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");
        intent.putExtra(Intent.EXTRA_TEXT, toShare.trim());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toActivity(context, intent, -1);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param emailAddress
     */
    public static void sendEmail(Activity context, String emailAddress) {
        if (context == null || !StringUtil.isNotEmpty(emailAddress, true)) {
            Log.e(TAG, "sendEmail  context == null || StringUtil.isNotEmpty(emailAddress, true) == false >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));//缺少"mailto:"前缀导致找不到应用崩溃
        intent.putExtra(Intent.EXTRA_TEXT, "内容");  //最近在MIUI7上无内容导致无法跳到编辑邮箱界面
        toActivity(context, intent, -1);
    }

    /**
     * 打开网站
     *
     * @param context
     * @param webSite
     */
    public static void openWebSite(Activity context, String webSite) {
        if (context == null || !StringUtil.isNotEmpty(webSite, true)) {
            Log.e(TAG, "openWebSite  context == null || StringUtil.isNotEmpty(webSite, true) == false >> return;");
            return;
        }

        toActivity(context, new Intent(Intent.ACTION_VIEW, Uri.parse(StringUtil.getCorrectUrl(webSite))));
    }

    /**
     * 复制文字
     *
     * @param context
     * @param value
     */
    public static void copyText(Context context, String value) {
        if (context == null || !StringUtil.isNotEmpty(value, true)) {
            Log.e(TAG, "copyText  context == null || StringUtil.isNotEmpty(value, true) == false >> return;");
            return;
        }

        ClipData cD = ClipData.newPlainText("simple text", value);
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(cD);
        }
        ToastUtils.showShort(context, "已复制\n" + value);
    }


    //启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     */
    public static void toActivity(final Activity context, final Intent intent) {
        toActivity(context, intent, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param showAnimation
     */
    public static void toActivity(final Activity context, final Intent intent, final boolean showAnimation) {
        toActivity(context, intent, -1, showAnimation);
    }

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     * @param requestCode
     */
    public static void toActivity(final Activity context, final Intent intent, final int requestCode) {
        toActivity(context, intent, requestCode, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public static void toActivity(final Activity context, final Intent intent, final int requestCode, final boolean showAnimation) {
        if (context == null || intent == null) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (requestCode < 0) {
                    context.startActivity(intent);
                } else {
                    context.startActivityForResult(intent, requestCode);
                }
                if (showAnimation) {
                    context.overridePendingTransition(R.anim.right_push_in, R.anim.hold);
                } else {
                    context.overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
                }
            }
        });
    }
    //启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static ProgressDialog progressDialog = null;

    /**
     * 展示加载进度条,无标题
     *
     * @param stringResId
     */
    public static void showProgressDialog(Activity context, int stringResId) {
        try {
            showProgressDialog(context, null, context.getResources().getString(stringResId));
        } catch (Exception e) {
            Log.e(TAG, "showProgressDialog  showProgressDialog(Context context, null, context.getResources().getString(stringResId));");
        }
    }

    /**
     * 展示加载进度条,无标题
     *
     * @param dialogMessage
     */
    public void showProgressDialog(Activity context, String dialogMessage) {
        showProgressDialog(context, null, dialogMessage);
    }

    /**
     * 展示加载进度条
     *
     * @param dialogTitle   标题
     * @param dialogMessage 信息
     */
    public static void showProgressDialog(final Activity context, final String dialogTitle, final String dialogMessage) {
        if (context == null) {
            return;
        }
        context.runOnUiThread(() -> {

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (!TextUtils.isEmpty(dialogTitle)) {
                progressDialog.setTitle(dialogTitle);
            }
            if (!TextUtils.isEmpty(dialogMessage)) {
                progressDialog.setMessage(dialogMessage);
            }
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        });
    }


    /**
     * 隐藏加载进度
     */
    public static void dismissProgressDialog(Activity context) {
        if (context == null || progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        context.runOnUiThread(() -> progressDialog.dismiss());
    }
    //显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    /**
     * 保存照片到SD卡上面
     *
     * @param path
     * @param photoName
     * @param formSuffix
     * @param photoBitmap
     */
    public static String savePhotoToSDCard(String path, String photoName, String formSuffix, Bitmap photoBitmap) {
        if (photoBitmap == null || StringUtil.isNotEmpty(path, true) == false
                || StringUtil.isNotEmpty(StringUtil.getTrimedString(photoName)
                + StringUtil.getTrimedString(formSuffix), true) == false) {
            Log.e(TAG, "savePhotoToSDCard photoBitmap == null || StringUtil.isNotEmpty(path, true) == false" +
                    "|| StringUtil.isNotEmpty(photoName, true) == false) >> return null");
            return null;
        }

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName + "." + formSuffix); // 在指定路径下创建文件
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        fileOutputStream)) {
                    fileOutputStream.flush();
                    Log.i(TAG, "savePhotoToSDCard<<<<<<<<<<<<<<\n" + photoFile.getAbsolutePath() + "\n>>>>>>>>> succeed!");
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "savePhotoToSDCard catch (FileNotFoundException e) {\n " + e.getMessage());
                photoFile.delete();
                //				e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "savePhotoToSDCard catch (IOException e) {\n " + e.getMessage());
                photoFile.delete();
                //				e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "savePhotoToSDCard } catch (IOException e) {\n " + e.getMessage());
                    //					e.printStackTrace();
                }
            }

            return photoFile.getAbsolutePath();
        }

        return null;
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isConnected()  && networkinfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取顶层 Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        return runningTaskInfos == null ? "" : runningTaskInfos.get(0).topActivity.getClassName();
    }


    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     * <p>
     * "13"代表前两位为数字13,
     * "[0-9]"代表第二位可以为0-9中的一个,
     * "[^4]" 代表除了4
     * "\\d{8}"代表后面是可以是0～9的数字, 有8位。
     */
    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        }
        return outMetrics.heightPixels;
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    /**
    * @author 尹振东
    * create at 2018/2/14 下午9:14
    * 方法说明：获取网关地址
    */
    public static String getHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return null;
    }
}