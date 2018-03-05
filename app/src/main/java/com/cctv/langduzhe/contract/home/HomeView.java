package com.cctv.langduzhe.contract.home;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/2/15.
 * 说明：
 */
public interface HomeView extends BaseView {
    void setHomeMedias(List<HomeVideoEntity.DataBean> data);
}
