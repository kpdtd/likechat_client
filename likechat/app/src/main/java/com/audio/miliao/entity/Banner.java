package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 主界面banner
 */
public class Banner implements Serializable
{
    // 这些属性定义跟服务器一致
    private String  displayName;
    private String  identifying;
    private String  visitUrl;
    private String  icon;

    public static Banner fromJson(JSONObject jsonObject)
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject toJson()
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Banner fromCursor(Cursor cursor)
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static ContentValues toContentValues()
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
