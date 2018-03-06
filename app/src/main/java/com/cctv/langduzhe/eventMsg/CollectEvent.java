package com.cctv.langduzhe.eventMsg;

import com.cctv.langduzhe.data.entites.HomeVideoEntity;

/**
 * Created by gentleyin
 * on 2018/3/1.
 * 说明：
 */
public class CollectEvent {

    public HomeVideoEntity.DataBean collected;
    public String type;
    public CollectEvent(HomeVideoEntity.DataBean collected,String type) {
        this.collected = collected;
        this.type = type;
    }


}
