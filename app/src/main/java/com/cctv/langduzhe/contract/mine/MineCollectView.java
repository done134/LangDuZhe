package com.cctv.langduzhe.contract.mine;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/2/28.
 * 说明：
 */
public interface MineCollectView extends BaseView {
    void setMediaList(List<HomeVideoEntity.DataBean> data);
}
