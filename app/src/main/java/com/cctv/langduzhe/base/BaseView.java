package com.cctv.langduzhe.base;

/**
 * view interface,所有View(此项目中的View主要是Fragment和自定义的ViewGroup)必须实现此接口
 *
 */
public interface BaseView {

    void setPresenter();

    void dismissProgress();

    void showToast(String message);

    void toLogin();
}
