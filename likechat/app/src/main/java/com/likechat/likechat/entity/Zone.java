package com.likechat.likechat.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.likechat.likechat.util.DbFieldUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 主播动态
 */

public class Zone implements Serializable
{
    /** 图片 */
    public final static int MEDIA_PHOTO = 0;
    /** 声音 */
    public final static int MEDIA_VOICE = 1;
    /** 视频 */
    public final static int MEDIA_VIDEO = 2;

    /** 图片最大数量 */
    public final static int PHOTOS_MAX = 9;

    /** 动态id */
    public String id;
    /** 动态内容 */
    public String text;
    /** 时间 */
    public long date;
    /** 观看数量 */
    public int watch;

    /** 媒体类型 */
    public int mediaType;
    /** 图片 jarray 保存的多个 url */
    public String photosUrl;
    /** 声音 单个url */
    public String voiceUrl;
    /** 视频 单个url */
    public String videoUrl;

    /** 觅聊号 */
    public String anchorId;
    /** 头像 */
    public String anchorAvatar;
    public int anchorRes;
    /** 名字 */
    public String anchorName;
    /** 个性签名 */
    public String anchorSign;

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (o instanceof Zone)
        {
            Zone zone = (Zone) o;
            return zone.id.equals(id);
        }

        return false;
    }

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("zoneId", id);
            jsonObject.put("zoneText", text);
            jsonObject.put("zoneDate", date);
            jsonObject.put("zoneWatch", watch);

            jsonObject.put("zoneMediaType", mediaType);
            jsonObject.put("zonePhotosUrl", photosUrl);
            jsonObject.put("zoneVoiceUrl", voiceUrl);
            jsonObject.put("zoneVideoUrl", videoUrl);

            jsonObject.put("anchorId", anchorId);
            jsonObject.put("anchorAvatar", anchorAvatar);
            jsonObject.put("anchorRes", anchorRes);
            jsonObject.put("anchorName", anchorName);
            jsonObject.put("anchorSign", anchorSign);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public ContentValues toContentValues()
    {
        try
        {
            ContentValues values = new ContentValues();

            values.put("zoneId", id);
            values.put("zoneText", text);
            values.put("zoneDate", date);
            values.put("zoneWatch", watch);

            values.put("zoneMediaType", mediaType);
            values.put("zonePhotosUrl", photosUrl);
            values.put("zoneVoiceUrl", voiceUrl);
            values.put("zoneVideoUrl", videoUrl);

            values.put("anchorId", anchorId);
            values.put("anchorAvatar", anchorAvatar);
            values.put("anchorRes", anchorRes);
            values.put("anchorName", anchorName);
            values.put("anchorSign", anchorSign);

            return values;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Zone fromJson(JSONObject jsonObject)
    {
        try
        {
            Zone zone = new Zone();
            zone.id = jsonObject.optString("zoneId");
            zone.text = jsonObject.optString("zoneText");
            zone.date = jsonObject.optLong("zoneDate");
            zone.watch = jsonObject.optInt("zoneWatch");

            zone.mediaType = jsonObject.getInt("zoneMediaType");
            zone.photosUrl = jsonObject.optString("zonePhotosUrl");
            zone.voiceUrl = jsonObject.optString("zoneVoiceUrl");
            zone.videoUrl = jsonObject.optString("zoneVideoUrl");

            zone.anchorId = jsonObject.optString("anchorId");
            zone.anchorAvatar = jsonObject.optString("anchorAvatar");
            zone.anchorRes = jsonObject.getInt("anchorRes");
            zone.anchorName = jsonObject.optString("anchorName");
            zone.anchorSign = jsonObject.optString("anchorSign");

            return zone;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Zone fromCursor(Cursor cursor)
    {
        try
        {
            Zone zone = new Zone();
            zone.id = DbFieldUtil.getString(cursor, "id");
            zone.text = DbFieldUtil.getString(cursor, "text");
            zone.date = DbFieldUtil.getLong(cursor, "date");
            zone.watch = DbFieldUtil.getInt(cursor, "zoneWatch");

            zone.mediaType = DbFieldUtil.getInt(cursor, "mediaType");
            zone.photosUrl = DbFieldUtil.getString(cursor, "photosUrl");
            zone.voiceUrl = DbFieldUtil.getString(cursor, "voiceUrl");
            zone.videoUrl = DbFieldUtil.getString(cursor, "videoUrl");

            zone.anchorId = DbFieldUtil.getString(cursor, "anchorId");
            zone.anchorAvatar = DbFieldUtil.getString(cursor, "anchorAvatar");
            zone.anchorRes = DbFieldUtil.getInt(cursor, "anchorRes");
            zone.anchorName = DbFieldUtil.getString(cursor, "anchorName");
            zone.anchorSign = DbFieldUtil.getString(cursor, "anchorSign");

            return zone;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
