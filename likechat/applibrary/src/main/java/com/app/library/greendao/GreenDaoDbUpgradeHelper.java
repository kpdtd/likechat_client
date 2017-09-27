package com.liu.apidemo.db.greenDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liu.apidemo.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by liujiye-pc on 2017/7/15.
 */

public class DbUpgradeHelper extends DaoMaster.OpenHelper
{
    public DbUpgradeHelper(Context context, String name)
    {
        super(context, name);
    }

    public DbUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory)
    {
        super(context, name, factory);
    }

    //第一次创建新表的时候才会调用
    @Override
    public void onCreate(Database db)
    {
        super.onCreate(db);
        Log.d("qqq_qqq", "onCreate");
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion)
    {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d("qqq_qqq", "onUpgrade oldVersion " + oldVersion + "; newVersion " + newVersion);

        if (oldVersion == newVersion)
        {
            // 不需要升级
            return;
        }

        // 利用switch-case没有break时连续执行的特性，实现数据库从任意旧版本升级到新版本
        switch (oldVersion)
        {
        case 2:
            //切记不能重复调用这个方法来更新表，而是一次性调用传入所需要的全部实体类
            //MigrationHelper.getInstance().migrate(db, ShopDao.class);

            // 如果不知道新添加的字段在数据库中的列名称，可以查看ShopDao
            //db.execSQL("ALTER TABLE SHOP ADD " + "RATIO" + " REAL default 1.0");
            db.execSQL("ALTER TABLE Device ADD " + "_id" + " INTEGER default 1");
        default:
            break;
        }
    }
}
