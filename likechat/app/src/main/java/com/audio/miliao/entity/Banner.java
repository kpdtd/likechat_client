package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.library.util.DbFieldUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 主界面banner
 */
public class Banner implements Serializable
{
    // 这些属性定义跟服务器一致
    private String  identifying;
    private String  displayName;
    private String  visitUrl;
    private String  icon;

    public static Banner fromJson(JSONObject jsonObject)
    {
        try
        {
            Banner banner = new Banner();

            banner.identifying = jsonObject.optString("identifying");
            banner.displayName = jsonObject.optString("displayName");
            banner.visitUrl = jsonObject.optString("visitUrl");
            banner.icon = jsonObject.optString("icon");

            return banner;
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
            jsonObject.put("displayName", displayName);
            jsonObject.put("visitUrl", visitUrl);
            jsonObject.put("icon", icon);
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
            Banner banner = new Banner();
            banner.identifying = DbFieldUtil.getString(cursor, "bannerIdentifying");
            banner.displayName = DbFieldUtil.getString(cursor, "bannerDisplayName");
            banner.visitUrl = DbFieldUtil.getString(cursor, "bannerVisitUrl");
            banner.icon = DbFieldUtil.getString(cursor, "bannerIcon");
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
            values.put("bannerIdentifying", identifying);
            values.put("bannerDisplayName", displayName);
            values.put("bannerVisitUrl", visitUrl);
            values.put("bannerIcon", icon);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
