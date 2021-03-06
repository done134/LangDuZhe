package com.cctv.langduzhe.data.http;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author yzd 2018/2/11
 */
public final class RxSchedulerUtils {

    /**
     * 在RxJava的使用过程中我们会频繁的调用subscribeOn()和observeOn(),通过Transformer结合
     * Observable.compose()我们可以复用这些代码
     *
     * @return Transformer
     */
    public static <T> Observable.Transformer<T, T> normalSchedulersTransformer() {

        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
