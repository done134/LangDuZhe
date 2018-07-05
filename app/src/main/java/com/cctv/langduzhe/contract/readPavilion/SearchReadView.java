package com.cctv.langduzhe.contract.readPavilion;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/4/15.
 * 说明：
 */
public interface SearchReadView extends BaseView {
    void setHomeMedias(List<HomeVideoEntity.DataBean> data);

}
