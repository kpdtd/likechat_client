package com.audio.miliao.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.audio.miliao.db.DbHelper;
import com.audio.miliao.entity.Actor;
import com.audio.miliao.theApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类
 */
public class DbUtil
{
	/** 数据库类 */
	public static SQLiteDatabase ms_db;

	/**
	 * 数据库写锁
	 */
	private final static byte[] sqlite_writeLock = new byte[0];

	/**
	 * 初始化
	 */
	private static void init()
	{
		try
		{
			if (ms_db == null)
			{
				DbHelper helper = new DbHelper(theApp.CONTEXT);
				ms_db = helper.getWritableDatabase();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 查找主播
	 */
	public static Actor getAnchor(final String anchorId)
	{
		try
		{
			init();

			String strSQL = "select * from " + DbHelper.TABLE_USER_NAME + " where anchorId = '" + anchorId + "'";
			Cursor cursor = ms_db.rawQuery(strSQL, null);

			Actor actor = null;
			if (cursor.moveToFirst())
			{
				actor = Actor.fromCursor(cursor);
			}
			cursor.close();

			return actor;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 查找所有的主播
	 * 
	 * @return
	 */
	public static List<Actor> getAllAnchor()
	{
		List<Actor> actorList = new ArrayList<Actor>();
		try
		{
			init();

			String strSQL = "select * from " + DbHelper.TABLE_USER_NAME;
			Cursor cursor = ms_db.rawQuery(strSQL, null);

			while (cursor.moveToNext())
			{
				Actor actor = Actor.fromCursor(cursor);
				if (actor != null)
				{
					actorList.add(actor);
				}
			}
			cursor.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return actorList;
	}

	/**
	 * 统计主播数量
	 * 
	 * @return
	 */
	public static int getAnchorCount()
	{
		int nCount = count(null, "anchorId", DbHelper.TABLE_USER_NAME);
		return nCount;
	}

	/**
	 * 保存主播信息
	 */
	public static int saveAnchor(Actor actor)
	{
		try
		{
			if (exist(actor.id, "anchorId", DbHelper.TABLE_USER_NAME))
			{
				return updateAnchor(actor);
			}
			else
			{
				return insertAnchor(actor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 批量保存主播数据
	 * 
	 * @param listActor
	 */
	public static void saveAnchors(List<Actor> listActor)
	{
		if (UIUtil.isListEmpty(listActor))
		{
			return;
		}

		try
		{
			init();

			ms_db.beginTransaction();

			for (Actor actor : listActor)
			{
				saveAnchor(actor);
			}

			ms_db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ms_db.endTransaction();
		}
	}

	/**
	 * 删除主播
	 * 
	 * @param id
	 * @return
	 */
	public static boolean deleteAnchor(String id)
	{
		try
		{
			String whereClause = "anchorSessionId = ?";
			String[] whereArgs = new String[]
			{
				id
			};
			int result = ms_db.delete(DbHelper.TABLE_USER_NAME, whereClause, whereArgs);
			if (result > 0)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 批量删除主播
	 * @param actors
	 */
	public static void deleteAnchors(List<Actor> actors)
	{
		if (UIUtil.isListEmpty(actors))
		{
			return;
		}

		try
		{
			init();

			ms_db.beginTransaction();

			for (Actor actor : actors)
			{
				deleteAnchor(actor.id);
			}

			ms_db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ms_db.endTransaction();
		}
	}

	/**
	 * 统计指定表中的指定项的数量
	 * 
	 * @param strValue null 统计有多少个key，非null统计有多少个key == value
	 * @param strKey
	 * @param strTable
	 * @return
	 */
	private static int count(final String strValue, final String strKey, final String strTable)
	{
		int nCount = 0;
		try
		{
			init();
			String whereCause = "";
			if (strValue != null)
			{
				whereCause = " where " + strKey + " = '" + strValue + "'";
			}

			String strSQL = "select count('" + strKey + "') as Count from " + strTable + whereCause;
			Cursor cursor = ms_db.rawQuery(strSQL, null);

			if (cursor.moveToFirst())
			{
				nCount = DbFieldUtil.getInt(cursor, "Count");
			}
			cursor.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return nCount;
	}

	/**
	 * 是否存在指定数据
	 * 
	 * @return
	 */
	private static boolean exist(final String strValue, final String strKey, final String strTable)
	{
		int nCount = count(strValue, strKey, strTable);
		return nCount > 0;
	}

	/**
	 * 更新主播信息
	 * 
	 * @return 影响的行数
	 */
	private static int updateAnchor(Actor actor)
	{
		try
		{
			init();

			synchronized (sqlite_writeLock)
			{
				ContentValues values = actor.toContentValues();
				String whereClause = "anchorId = ?";
				String[] whereArgs = new String[]
				{
                        actor.id
				};
				int nRow = ms_db.update(DbHelper.TABLE_USER_NAME, values, whereClause, whereArgs);
				return nRow;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 插入主播
	 * 
	 * @return 影响的行数
	 */
	private static int insertAnchor(Actor actor)
	{
		try
		{
			init();

			synchronized (sqlite_writeLock)
			{
				ContentValues values = actor.toContentValues();
				int nRow = (int) ms_db.insert(DbHelper.TABLE_USER_NAME, null, values);

				return nRow;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}
}
