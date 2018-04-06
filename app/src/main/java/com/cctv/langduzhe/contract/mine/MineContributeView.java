package com.cctv.langduzhe.contract.mine;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.ContributeEntity;

/**
 * Created by gentleyin
 * on 2018/3/17.
 * 说明：我的投稿列表页View层
 */
public interface MineContributeView extends BaseView {
    void setContributeList(ContributeEntity contributeEntity);
}
