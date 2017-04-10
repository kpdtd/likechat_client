package com.audio.miliao.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.audio.miliao.db.DbHelper;
import com.audio.miliao.entity.User;
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
	public static User getAnchor(final String anchorId)
	{
		try
		{
			init();

			String strSQL = "select * from " + DbHelper.TABLE_USER_NAME + " where anchorId = '" + anchorId + "'";
			Cursor cursor = ms_db.rawQuery(strSQL, null);

			User user = null;
			if (cursor.moveToFirst())
			{
				user = User.fromCursor(cursor);
			}
			cursor.close();

			return user;
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
	public static List<User> getAllAnchor()
	{
		List<User> userList = new ArrayList<User>();
		try
		{
			init();

			String strSQL = "select * from " + DbHelper.TABLE_USER_NAME;
			Cursor cursor = ms_db.rawQuery(strSQL, null);

			while (cursor.moveToNext())
			{
				User user = User.fromCursor(cursor);
				if (user != null)
				{
					userList.add(user);
				}
			}
			cursor.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return userList;
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
	public static int saveAnchor(User user)
	{
		try
		{
			if (exist(user.id, "anchorId", DbHelper.TABLE_USER_NAME))
			{
				return updateAnchor(user);
			}
			else
			{
				return insertAnchor(user);
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
	 * @param listUser
	 */
	public static void saveAnchors(List<User> listUser)
	{
		if (UIUtil.isListEmpty(listUser))
		{
			return;
		}

		try
		{
			init();

			ms_db.beginTransaction();

			for (User user : listUser)
			{
				saveAnchor(user);
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
	 * @param users
	 */
	public static void deleteAnchors(List<User> users)
	{
		if (UIUtil.isListEmpty(users))
		{
			return;
		}

		try
		{
			init();

			ms_db.beginTransaction();

			for (User user : users)
			{
				deleteAnchor(user.id);
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
	private static int updateAnchor(User user)
	{
		try
		{
			init();

			synchronized (sqlite_writeLock)
			{
				ContentValues values = user.toContentValues();
				String whereClause = "anchorId = ?";
				String[] whereArgs = new String[]
				{
                        user.id
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
	private static int insertAnchor(User user)
	{
		try
		{
			init();

			synchronized (sqlite_writeLock)
			{
				ContentValues values = user.toContentValues();
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
