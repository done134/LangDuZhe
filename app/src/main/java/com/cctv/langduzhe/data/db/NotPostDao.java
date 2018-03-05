package com.cctv.langduzhe.data.db;

import android.content.Context;

import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by gentleyin
 * on 2018/2/4.
 * 说明：未发布朗读 数据表操作类
 */
public class NotPostDao {

    private Context context;

    private Dao<NotPostEntity, Integer> notPostOperation;


    public NotPostDao(Context context) {
        this.context = context;
        this.notPostOperation = NotPostDataHelper.getInstance(context).getReadDao(NotPostEntity.class);
    }


    /**
    * @author 尹振东
    * create at 2018/2/5 上午12:23
    * 方法说明：添加一条数据
    */
    public void add(NotPostEntity theme) {
        try {
            notPostOperation.create(theme);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
    * @author 尹振东
    * create at 2018/2/5 上午12:16
    * 方法说明：查询所有
    */
    public List<NotPostEntity> queryAll() {
        try {
            return notPostOperation.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @author 尹振东
    * create at 2018/2/5 上午12:15
    * 方法说明：通过id删除
    */
    public void deleteById(int cityId) {
        try {
            notPostOperation.deleteById(cityId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
