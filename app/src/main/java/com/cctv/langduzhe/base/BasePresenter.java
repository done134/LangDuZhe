package com.cctv.langduzhe.base;

/**
 * presenter interface,所有Presenter必须实现此接口
 *
 */
public interface BasePresenter  {

     final String RESULT_OK = "K-000000";

     void subscribe();

     void unSubscribe();

}
