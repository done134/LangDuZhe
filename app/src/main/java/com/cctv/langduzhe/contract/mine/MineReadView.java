package com.cctv.langduzhe.contract.mine;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/4/25.
 * 说明：我的朗读已发布页面
 */
public interface MineReadView extends BaseView {
    void setHomeMedias(List<HomeVideoEntity.DataBean> data);

    void setDelSuccess();
}
