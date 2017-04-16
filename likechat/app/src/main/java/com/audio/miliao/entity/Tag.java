package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 主播的属性
 */
public class Tag implements Serializable
{
    // 这些属性定义跟服务器一致
    private String identifying;
    private String tagName;
    private String pic;

    public static Tag fromJson(JSONObject jsonObject)
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

    public static Tag fromCursor(Cursor cursor)
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
