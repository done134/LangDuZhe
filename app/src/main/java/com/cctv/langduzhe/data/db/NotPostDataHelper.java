package com.cctv.langduzhe.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.facebook.stetho.common.LogUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by gentleyin
 * on 2018/2/4.
 * 说明：数据库操作类
 */
public class NotPostDataHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "NotPostDataHelper";
    private static final String DATABASE_NAME = "landDuZhe.db";
    private static final int DATABASE_VERSION = 1;


    private static volatile NotPostDataHelper instance;

    public NotPostDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // 创建表
        try {
            TableUtils.createTable(connectionSource, NotPostEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        onCreate(database, connectionSource);
    }

    /**
     * 单例获取OpenHelper实例
     *
     * @param context application context
     * @return instance
     */
    public static NotPostDataHelper getInstance(Context context) {

        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (NotPostDataHelper.class) {
                if (instance == null) {
                    instance = new NotPostDataHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearCache();
    }

    public <D extends Dao<T, ?>, T> D getReadDao(Class<T> clazz) {
        try {
            return getDao(clazz);
        } catch (SQLException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return null;
    }
}
