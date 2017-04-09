package com.likechat.likechat.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.likechat.likechat.util.DbFieldUtil;

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
    public int avatar_res;
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
            user.id = jsonObject.optString("anchorId");
            user.name = jsonObject.optString("anchorName");
            user.avatar = jsonObject.optString("anchorAvatar");
            user.gender = jsonObject.optInt("anchorGender");
            user.age = jsonObject.optInt("anchorAge");
            user.sign = jsonObject.optString("anchorSign");
            user.intro = jsonObject.optString("anchorIntro");
            user.city = jsonObject.optString("anchorCity");
            user.fans = jsonObject.optInt("anchorFans");
            user.follow = jsonObject.optInt("anchorFollow");

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

            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
