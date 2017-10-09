package com.audio.miliao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.library.greendao.GreenDaoDbUpgradeHelper;
import com.app.library.vo.ChatMsgDao;
import com.app.library.vo.DaoMaster;
import com.app.library.vo.DaoSession;
import com.app.library.vo.MessageStateVo;
import com.app.library.vo.MessageStateVoDao;
import com.app.library.vo.MessageVo;
import com.app.library.vo.MessageVoDao;
import com.app.library.vo.ChatMsg;

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
     * 插入单条(id相同也插入)
     * @param messageVo
     */
    public static void insertOrReplace(MessageVo messageVo)
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
     * 插入单条(id相同也插入)
     * @param messageStateVo
     */
    public static void insertOrReplace(MessageStateVo messageStateVo)
    {
        try
        {
            daoSession.getMessageStateVoDao().insertOrReplace(messageStateVo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 插入多条(id相同也插入)
     * @param messageVos
     */
    public static void insertOrReplace(List<MessageVo> messageVos)
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

    public static void insertOrReplace(ChatMsg chatMsg)
    {
        try
        {
            daoSession.getChatMsgDao().insertOrReplace(chatMsg);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static MessageStateVo queryMessageStateVoByMessageId(long messageId)
    {
        try
        {
            return daoSession.getMessageStateVoDao()
                    .queryBuilder()
                    .where(MessageStateVoDao.Properties.MessageId.eq(messageId))
                    .unique();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ChatMsg> queryChatMessageListByActorId(int actorId)
    {
        try
        {
            return daoSession.getChatMsgDao()
                    .queryBuilder()
                    .where(ChatMsgDao.Properties.ActorId.eq(actorId))
                    .list();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
