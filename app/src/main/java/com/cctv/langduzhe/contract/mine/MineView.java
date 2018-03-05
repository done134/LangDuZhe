package com.cctv.langduzhe.contract.mine;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BaseView;

/**
 * Created by gentleyin
 * on 2018/2/17.
 * 说明：
 */
public interface MineView extends BaseView {
    void setUserInfo(JSONObject dataObj);
}
