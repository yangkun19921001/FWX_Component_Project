package com.yk.component.sdk.db;

import android.app.Application;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.crud.callback.FindCallback;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * Created by yangk on 2018/11/6.
 * 对数据库操作的封装
 */

public class DBManager<T extends LitePalSupport> {

    private static DBManager dbMgr;


    public static synchronized DBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new DBManager();
        }
        return dbMgr;
    }

    /**
     * 初始化数据库操作
     */
    public void initDB(Application application) {
        LitePal.initialize(application);
    }

    /**
     * 异步保存单条数据
     */
    public synchronized void saveAsync(T data, SaveCallback saveCallback) {
        data.saveAsync().listen(saveCallback);
    }

    /**
     * 异步保存多条数据
     */
    public synchronized void saveListAsync(List<T> data, SaveCallback saveCallback) {
        LitePal.saveAllAsync(data).listen(saveCallback);
    }


    /**
     * 同步保存单条数据
     */
    public synchronized void save(T data) {
        data.saveAsync();
    }

    /**
     * 更新数据库中多条数据
     * id
     * content
     */
    public synchronized void upAllAsync(LitePalSupport data, String conditions, String id, String content, UpdateOrDeleteCallback updateOrDeleteCallback) {
        data.updateAllAsync(conditions, id, content).listen(updateOrDeleteCallback);
    }

    /**
     * 更新数据库中多条数据
     * id
     * content
     */
    public synchronized void upAllAsync(T data, String conditions, String id, UpdateOrDeleteCallback updateOrDeleteCallback) {
        data.updateAllAsync(conditions, id).listen(updateOrDeleteCallback);
    }

    /**
     * 更新数据库中单条数据
     */
    public synchronized void upDataAsync(T data, long id, UpdateOrDeleteCallback updateOrDeleteCallback) {
        data.updateAsync(id).listen(updateOrDeleteCallback);
    }


    /**
     * 异步删除单条数据
     */
    public synchronized void delete(Class<?> modelClass, int id, UpdateOrDeleteCallback updateOrDeleteCallback) {
        LitePal.deleteAsync(modelClass, id).listen(updateOrDeleteCallback);
    }

    /**
     * 异步删除多个数据
     */
    public synchronized void deleteAllAsync(Class<?> modelClass, String conditions, String id, String content, UpdateOrDeleteCallback updateOrDeleteCallback) {
        LitePal.deleteAllAsync(modelClass, conditions, id, content).listen(updateOrDeleteCallback);
    }

    /**
     * 异步全部删除数据库中的指定的表
     */
    public synchronized void deleteAllTabAsync(Class<?> modelClass, UpdateOrDeleteCallback updateOrDeleteCallback) {
        LitePal.deleteAllAsync(modelClass).listen(updateOrDeleteCallback);
    }

    /**
     * 简单异步查询
     */
    public synchronized void queryaSsync(Class<T> cls, int id, FindCallback<T> updateOrDeleteCallback) {
        LitePal.findAsync(cls, id).listen(updateOrDeleteCallback);
    }


    /**
     * 获取 当前ID 所有数据
     */
    public synchronized void queryaAllLists(Class<T> cls, int id, FindCallback<T> updateOrDeleteCallback) {
        LitePal.findAsync(cls, id).listen(updateOrDeleteCallback);

    }

    /**
     * 获取 Model 所有数据
     */
    public synchronized void queryaAllLists(Class<T> cls,FindMultiCallback<T> updateOrDeleteCallback) {
        LitePal.findAllAsync(cls).listen(updateOrDeleteCallback);

    }


    /**
     * 获取当前 ID 并且时间倒序的所有数据
     * 参考查询方法：id > ? and name == ?
     */
    public synchronized void queryaAllLists(String commentcount, String id, String content, String time, Class<T> cls, FindMultiCallback<T> findMultiCallback) {
        LitePal.where(commentcount, id, content)
                .order(time + " desc").findAsync(cls).listen(findMultiCallback);
    }

    /**
     * 获取当前 ID 并且时间倒序的所有数据
     * 参考查询方法：id > ? and name == ?
     */
    public synchronized void queryaAllLists(String commentcount, String id, String time, Class<T> cls, FindMultiCallback<T> findMultiCallback) {
        LitePal.where(commentcount, id)
                .order(time + " desc").findAsync(cls).listen(findMultiCallback);
    }
}
