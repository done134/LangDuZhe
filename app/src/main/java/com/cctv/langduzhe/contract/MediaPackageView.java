package com.cctv.langduzhe.contract;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.MediaPackageEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/7/5.
 * 说明：查询媒体包列表接口公用View层 type 参数值为 t=期,s=季
 */
public interface MediaPackageView extends BaseView {
    void setPackageInfo(List<MediaPackageEntity.DataBean> data);
}
