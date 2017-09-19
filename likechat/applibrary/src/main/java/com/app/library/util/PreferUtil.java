package com.app.library.util;

import android.content.Context;
import android.content.SharedPreferences;



public class PreferUtil
{
	private static final String      ms_perferenceName = "likechat_preference";
	private static SharedPreferences ms_preferences    = null;
	private static Context ms_context = null;

	public static synchronized void init(Context context)
	{
		ms_context = context;
		//init();
	}

	private static synchronized void init()
	{
		try
		{
			if (ms_preferences != null)
				return;

			ms_preferences = ms_context.getSharedPreferences(ms_perferenceName, Context.MODE_PRIVATE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 清除所有数据
	 */
	public static void clearAll()
	{
		try
		{
			init();

			ms_preferences.edit().clear().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 移除项
	 * @param name 键值
	 */
	public static void remove(final String name)
	{
		try
		{
			init();

			ms_preferences.edit().remove(name).commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 返回 bool 值
	 * @param key 键值
	 * @param defValue 默认值
	 */
	public static boolean getBooleanPreference(final String key, final boolean defValue)
	{
		try
		{
			init();

			return ms_preferences.getBoolean(key, defValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 设置 bool 值
	 * @param key 键值
	 * @param value 值
	 * 
	 */
	public static boolean setBooleanPreference(final String key, final boolean value)
	{
		try
		{
			return ms_preferences.edit().putBoolean(key, value).commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 返回字符串
	 * @param key 键值
	 */
	public static String getStringPreference(final String key)
	{
		try
		{
			init();

			return ms_preferences.getString(key, "");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}


	/**
	 * 设置字符串
	 * @param key 键值
	 * @param value 值
	 */
	public static boolean setStringPreference(final String key, final String value)
	{
		try
		{
			init();

			return ms_preferences.edit().putString(key, value).commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}


	/**
	 * 返回整数类型
	 * @param key 键值
	 * @param defValue 默认值
	 */
	public static int getIntPreference(final String key, final int defValue)
	{
		try
		{
			init();

			return ms_preferences.getInt(key, defValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 设置整数类型
	 * @param key 键值
	 * @param value 值
	 */
	public static void setIntPreference(final String key, final int value)
	{
		try
		{
			init();

			ms_preferences.edit().putInt(key, value).commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 返回长整形
	 * @param key 键值
	 * @param defValue 默认值
	 */
	public static long getLongPreference(final String key, final long defValue)
	{
		try
		{
			init();

			return ms_preferences.getLong(key, defValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 设置长整形
	 * @param key 键值
	 * @param value 值
	 */
	public static void setLongPreference(final String key, final long value)
	{
		try
		{
			init();

			ms_preferences.edit().putLong(key, value).commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
