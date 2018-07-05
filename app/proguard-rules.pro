# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/baron/develop/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.*
-dontwarn com.squareup.okhttp.**
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
 # Retain generic type information for use by reflection by converters and adapters.
 -keepattributes Signature
 # Retain service method parameters.
 -keepclassmembernames,allowobfuscation interface * {
     @retrofit2.http.* <methods>;
 }
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Exceptions
 # Ignore annotation used for build tooling.
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 -dontwarn okhttp3.**
 -dontwarn okio.**
 -dontwarn javax.annotation.**
 -dontwarn org.conscrypt.**
 -dontwarn rx.internal.util.**
 # A resource is loaded with a relative path so the package of this class must be preserved.
 -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#stetho
-keep class com.facebook.stetho.** { *; }

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
# 泛型与反射
-keepattributes EnclosingMethod
#####butterknife#######
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-dontwarn butterknife.**
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**
-keepclassmembers class * {
  public <init> (org.json.JSONObject);
}
-keep public class com.cctv.langduzhe.R$*{
  public static final int *;
}
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Exceptions

# Gson
-keep class com.cctv.langduzhe.data.entites.**{*;} # 自定义数据模型的bean目录

# OrmLite uses reflection

-keep class com.j256.**

-keepclassmembers class com.j256.** { *; }

-keep enum com.j256.**

-keepclassmembers enum com.j256.** { *; }

-keep interface com.j256.**

-keepclassmembers interface com.j256.** { *; }



-keepclassmembers class * {

@com.j256.ormlite.field.DatabaseField *;

}
-dontwarn com.alibaba.fastjson.**

-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.malinskiy.superrecyclerview.SwipeDismissRecyclerViewTouchListener*
-libraryjars libs/pgyer_sdk_2.8.3.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keepattributes SourceFile,LineNumberTable