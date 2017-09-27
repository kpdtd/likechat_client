package com.app.library.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.library.greendao.GreenDaoDbUpgradeHelper;
import com.app.library.vo.DaoMaster;
import com.app.library.vo.DaoSession;
import com.app.library.vo.MessageVo;
import com.app.library.vo.MessageVoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 操作工具类
 */
public class DBUtil
{
    private static DaoSession daoSession;

    /**
     * 配置数据库
     */
    public static void init(Context context)
    {
        if (daoSession == null)
        {
            //创建数据库shop.db"
            DaoMaster.OpenHelper helper = new GreenDaoDbUpgradeHelper(context, "likechat.db", null);
            //获取可写数据库
            SQLiteDatabase db = helper.getWritableDatabase();
            //获取数据库对象
            DaoMaster daoMaster = new DaoMaster(db);
            //获取Dao对象管理者
            daoSession = daoMaster.newSession();
        }
    }

    /**
     * 插入单条
     * @param messageVo
     */
    public static void save(MessageVo messageVo)
    {
        try
        {
            daoSession.getMessageVoDao().insertOrReplace(messageVo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 插入多条
     * @param messageVos
     */
    public static void save(List<MessageVo> messageVos)
    {
        try
        {
            daoSession.getMessageVoDao().insertOrReplaceInTx(messageVos);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static MessageVo queryMessageVo(long id)
    {
        try
        {
            return daoSession.getMessageVoDao().load(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static MessageVo queryMessageVoByActorId(int actorId)
    {
        try
        {
            return daoSession.getMessageVoDao().queryBuilder()
                    .where(MessageVoDao.Properties.ActorId.eq(actorId))
                    .unique();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取所有数据，按date倒序排序
     * @return
     */
    public static List<MessageVo> queryAllMessageVo()
    {
        try
        {
            QueryBuilder<MessageVo> qb = daoSession.getMessageVoDao().queryBuilder();
            return qb.orderDesc(MessageVoDao.Properties.Mdate).list();
            // return daoSession.getMessageVoDao().loadAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
