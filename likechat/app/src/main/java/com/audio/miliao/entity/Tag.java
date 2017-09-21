package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.library.util.DbFieldUtil;

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
            Tag tag = new Tag();
            tag.identifying = jsonObject.optString("identifying");
            tag.tagName = jsonObject.optString("tagName");
            tag.pic = jsonObject.optString("pic");

            return tag;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject toJson()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("identifying", identifying);
            jsonObject.put("tagName", tagName);
            jsonObject.put("pic", pic);

            return jsonObject;
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
            Tag tag = new Tag();
            tag.identifying = DbFieldUtil.getString(cursor, "tagIdentifying");
            tag.tagName = DbFieldUtil.getString(cursor, "tagName");
            tag.pic = DbFieldUtil.getString(cursor, "tagPic");

            return tag;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public ContentValues toContentValues()
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("tagIdentifying", identifying);
            values.put("tagName", tagName);
            values.put("tagPic", pic);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
