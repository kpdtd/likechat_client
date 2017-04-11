package com.audio.miliao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper
{
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "miliao.db3";

	// -----------------------------------------------------------------------------------------------

	// anchor 主表。
	public static final String TABLE_USER_NAME = "User";
	private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER_NAME
			+ " (" + "[_id] INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "[userId] TEXT," //
			+ "[userName] TEXT," //
			+ "[userAvatar] TEXT," //
			+ "[userGender] INTEGER," //
			+ "[userAge] INTEGER," //
			+ "[userIntro] TEXT," //
			+ "[userCity] TEXT," //
			+ "[userFans] INTEGER," //
			+ "[userFollow] INTEGER" //
			+ ")";
	// -----------------------------------------------------------------------------------------------

	// tableName, fieldName
	public static final String[][] DB_Indexs =
	{
		{
				TABLE_USER_NAME, "[userId]"
		}
	};

	public DbHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.execSQL(CREATE_TABLE_USER);

			String[] strCreateIndexs = createIndexString(DB_Indexs);
			if (strCreateIndexs != null)
			{
				for (int i = 0; i < strCreateIndexs.length; i++)
				{
					db.execSQL(strCreateIndexs[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

	/**
	 * 创建索引的字符串<p>
	 * indexs[i][0] tableName, indexs[i][1] fieldName
	 * @param indexs
	 * @return
	 */
	private static String[] createIndexString(String[][] indexs)
	{
		if (indexs != null)
		{
			String[] strIndexs = new String[indexs.length];
			try
			{
				for (int i = 0; i < indexs.length; i++)
				{
					String tableName = indexs[i][0];
					String fieldName = indexs[i][1];
					String indexName = "Index_" + i;

					strIndexs[i] = "CREATE INDEX " + indexName + " ON [" + tableName + "] (" + fieldName + ")";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return strIndexs;
		}
		
		return null;
	}
}