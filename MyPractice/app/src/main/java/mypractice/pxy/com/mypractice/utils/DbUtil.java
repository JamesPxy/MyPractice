package mypractice.pxy.com.mypractice.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pxy.greendao.gen.DaoMaster;
import com.pxy.greendao.gen.DaoSession;
import com.pxy.greendao.gen.DownInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import mypractice.pxy.com.mypractice.MyApplication;
import mypractice.pxy.com.mypractice.internet.download.DownInfo;
import mypractice.pxy.com.mypractice.internet.download.HttpDownManager;


/**
 * 断点续传
 * 数据库工具类-geendao运用
 * Created by WZG on 2016/10/25.
 */

public class DbUtil {
    private static DbUtil db;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;


    public DbUtil() {
        context= MyApplication.app;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    /**
     * 获取单例
     * @return
     */
    public static DbUtil getInstance() {
        if (db == null) {
            synchronized (HttpDownManager.class) {
                if (db == null) {
                    db = new DbUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void save(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.insert(info);
    }

    public void update(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.update(info);
    }

    public void deleteDowninfo(DownInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.delete(info);
    }


    public DownInfo queryDownBy(long Id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.Id.eq(Id));
        List<DownInfo> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<DownInfo> queryDownAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
