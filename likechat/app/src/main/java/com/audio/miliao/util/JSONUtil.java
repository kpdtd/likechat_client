package com.audio.miliao.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class JSONUtil
{
	public static final long LONG_ERROR = -1000;
	public static final int INT_ERROR = -1001;
	/** 精确到秒 */
	private static final SimpleDateFormat jsonSimpleDateFormatZ = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	/** 精确到秒 */
	public static final SimpleDateFormat jsonSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	// private static final SimpleDateFormat jsonSimpleDateFormat = new
	// SimpleDateFormat(
	// "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"); // 精确到毫秒

	/**
	 * 从 JSONObject 中获取字符串，如果没有该项或者该项值为null(不是"null")则返回""
	 */
	public static String getString(JSONObject jsonObject, String name)
	{
		if (jsonObject != null)
		{
			if (jsonObject.isNull(name))
			{
				return "";
			}

			return jsonObject.optString(name);
		}

		return "";
	}

	public static int getInt(JSONObject jsonObject, String name)
	{
		if (jsonObject != null)
		{
			if (jsonObject.has(name))
			{
				return jsonObject.optInt(name);
			}

			return INT_ERROR;
		}

		return INT_ERROR;
	}

	/**
	 * 从 JSONObject 中获取长整形，如果没有该项或者该项值为null则返回LONG_ERROR
	 * */
	public static long getLong(JSONObject jsonObject, String name)
	{
		if (jsonObject != null)
		{
			if (jsonObject.has(name))
			{
				return jsonObject.optLong(name);
			}

			return LONG_ERROR;
		}

		return LONG_ERROR;
	}

	public static boolean getBoolean(JSONObject jsonObject, String name, boolean defaultValue)
	{
		if (jsonObject != null)
		{
			if (jsonObject.has(name))
			{
				return jsonObject.optBoolean(name);
			}
		}

		return defaultValue;
	}

	/**
	 * 从 JSONObject 中获取JSONArray，如果没有该项或者该项值为null则返回null
	 * */
	public static JSONArray getJsonArray(JSONObject jsonObject, String name)
	{
		if (jsonObject != null)
		{
			if (jsonObject.has(name))
			{
				return jsonObject.optJSONArray(name);
			}
		}

		return null;
	}

	public static Date getDate(SimpleDateFormat dateFormat, JSONObject jsonObject, String name)
	{
		if (jsonObject != null)
		{
			if (jsonObject.has(name))
			{
				try
				{
					String strDate = jsonObject.optString(name);
					dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					// 国内时区
					// dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
					// 手机设置的时区
					// dateFormat.setTimeZone(TimeZone.getDefault());
					Date date = dateFormat.parse(strDate);

					return date;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static Date getDate(JSONObject jsonObject, String name)
	{
		return getDate(jsonSimpleDateFormatZ, jsonObject, name);
	}

	public static List<String> jsonArray2StringList(JSONArray jsonArray)
	{
		if ((jsonArray == null) || (jsonArray.length() == 0))
			return null;

		List<String> result = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++)
		{
			result.add(jsonArray.optString(i));
		}

		return result;
	}

	public static String jsonArray2String(JSONArray jsonArray)
	{
		if (jsonArray == null)
			return "";

		return jsonArray.toString();
	}

	public static List<String> jsonArrayString2StringList(String strJsonArrayString)
	{
		List<String> result = null;

		try
		{
			JSONArray jsonArray = new JSONArray(strJsonArrayString);
			result = jsonArray2StringList(jsonArray);
		}
		catch (JSONException e)
		{
			//
		}

		return result;
	}

	public static JSONArray stringListToJsonArray(List<String> list)
	{
		JSONArray jsonArray = new JSONArray();
		try
		{
			if (list != null && list.size() > 0)
			{
				for (String str : list)
				{
					jsonArray.put(str);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jsonArray;
	}

	/**
	 * 判断字符串str是否是strJsonArrayString 构成的json数组中的某一项
	 */
	public static boolean strInJSONArray(String str, String strJsonArrayString)
	{
		List<String> jsonArray = jsonArrayString2StringList(strJsonArrayString);
		for (String json : jsonArray)
		{
			if (json.equals(str))
			{
				return true;
			}
		}

		return false;
	}
}
