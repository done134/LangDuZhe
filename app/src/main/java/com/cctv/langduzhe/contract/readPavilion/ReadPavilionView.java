package com.cctv.langduzhe.contract.readPavilion;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：
 */
public interface ReadPavilionView extends BaseView {
    void setHomeMedias(List<HomeVideoEntity.DataBean> data);
}
