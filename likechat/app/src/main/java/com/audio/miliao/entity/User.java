package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.audio.miliao.util.DbFieldUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 用户
 */

public class User implements Serializable
{
    /** 女 */
    public final static int GENDER_FEMALE = 0;
    /** 男 */
    public final static int GENDER_MALE = 1;

    /** 觅聊号 */
    public String id = "";
    /** 头像 */
    public String avatar = "";
    /** 名字 */
    public String name = "";
    /** 性别 */
    public int gender;
    /** 年龄 */
    public int age;
    /** 个性签名 */
    public String sign = "";
    /** 介绍 */
    public String intro = "";
    /** 在哪个城市 */
    public String city = "";
    /** 粉丝数量 */
    public int fans;
    /** 关注数量 */
    public int follow;

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (o instanceof User)
        {
            User user = (User) o;
            return user.id.equals(id);
        }

        return false;
    }

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("avatar", avatar);
            jsonObject.put("gender", gender);
            jsonObject.put("age", age);
            jsonObject.put("sign", sign);
            jsonObject.put("intro", intro);
            jsonObject.put("city", city);
            jsonObject.put("fans", fans);
            jsonObject.put("follow", follow);
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
            values.put("anchorId", id);
            values.put("anchorName", name);
            values.put("anchorAvatar", avatar);
            values.put("anchorGender", gender);
            values.put("anchorAge", age);
            values.put("anchorSign", sign);
            values.put("anchorIntro", intro);
            values.put("anchorCity", city);
            values.put("anchorFans", fans);
            values.put("anchorFollow", follow);

            return values;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static User fromJson(JSONObject jsonObject)
    {
        try
        {
            User user = new User();
            user.id = jsonObject.optString("id");
            user.name = jsonObject.optString("name");
            user.avatar = jsonObject.optString("avatar");
            user.gender = jsonObject.optInt("gender");
            user.age = jsonObject.optInt("age");
            user.sign = jsonObject.optString("sign");
            user.intro = jsonObject.optString("intro");
            user.city = jsonObject.optString("city");
            user.fans = jsonObject.optInt("fans");
            user.follow = jsonObject.optInt("follow");

            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static User fromCursor(Cursor cursor)
    {
        try
        {
            User user = new User();
            user.id = DbFieldUtil.getString(cursor, "anchorId");
            user.name = DbFieldUtil.getString(cursor, "anchorName");
            user.sign = DbFieldUtil.getString(cursor, "anchorSign");
            user.avatar = DbFieldUtil.getString(cursor, "anchorAvatar");
            user.gender = DbFieldUtil.getInt(cursor, "anchorGender");
            user.age = DbFieldUtil.getInt(cursor, "anchorAge");
            user.intro = DbFieldUtil.getString(cursor, "anchorIntro");
            user.city = DbFieldUtil.getString(cursor, "anchorCity");
            user.fans = DbFieldUtil.getInt(cursor, "anchorFans");
            user.follow = DbFieldUtil.getInt(cursor, "anchorFollow");

            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
