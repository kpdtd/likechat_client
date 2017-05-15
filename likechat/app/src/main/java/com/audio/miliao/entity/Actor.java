package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.audio.miliao.util.DbFieldUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 用户
 */

public class Actor implements Serializable
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
    /** 省 */
    public String province;
    /** 城市 */
    public String city = "";
    /** 粉丝数量 */
    public int fans;
    /** 关注数量 */
    public int follow;
    /** 资费价格（整数）转成 --》 1.5币/分 */
    public String price;
    /** 通话时长 */
    public String callTime;
    /** 音频地址 */
    public String videoUrl;
    /** 主播相册地址列表 */
    public List<String> picList;

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (o instanceof Actor)
        {
            Actor actor = (Actor) o;
            return actor.id.equals(id);
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
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("fans", fans);
            jsonObject.put("follow", follow);
            jsonObject.put("price", price);
            jsonObject.put("callTime", callTime);
            jsonObject.put("videoUrl", videoUrl);
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
            values.put("anchorProvince", province);
            values.put("anchorCity", city);
            values.put("anchorFans", fans);
            values.put("anchorFollow", follow);
            values.put("anchorPrice", price);
            values.put("anchorCallTime", callTime);
            values.put("anchorVideoUrl", videoUrl);

            return values;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Actor fromJson(JSONObject jsonObject)
    {
        try
        {
            Actor actor = new Actor();
            actor.id = jsonObject.optString("id");
            actor.name = jsonObject.optString("name");
            actor.avatar = jsonObject.optString("avatar");
            actor.gender = jsonObject.optInt("gender");
            actor.age = jsonObject.optInt("age");
            actor.sign = jsonObject.optString("sign");
            actor.intro = jsonObject.optString("intro");
            actor.province = jsonObject.optString("province");
            actor.city = jsonObject.optString("city");
            actor.fans = jsonObject.optInt("fans");
            actor.follow = jsonObject.optInt("follow");
            actor.price = jsonObject.optString("price");
            actor.callTime = jsonObject.optString("callTime");
            actor.videoUrl = jsonObject.optString("videoUrl");

            return actor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Actor fromCursor(Cursor cursor)
    {
        try
        {
            Actor actor = new Actor();
            actor.id = DbFieldUtil.getString(cursor, "anchorId");
            actor.name = DbFieldUtil.getString(cursor, "anchorName");
            actor.sign = DbFieldUtil.getString(cursor, "anchorSign");
            actor.avatar = DbFieldUtil.getString(cursor, "anchorAvatar");
            actor.gender = DbFieldUtil.getInt(cursor, "anchorGender");
            actor.age = DbFieldUtil.getInt(cursor, "anchorAge");
            actor.intro = DbFieldUtil.getString(cursor, "anchorIntro");
            actor.province = DbFieldUtil.getString(cursor, "anchorProvince");
            actor.city = DbFieldUtil.getString(cursor, "anchorCity");
            actor.fans = DbFieldUtil.getInt(cursor, "anchorFans");
            actor.follow = DbFieldUtil.getInt(cursor, "anchorFollow");
            actor.price = DbFieldUtil.getString(cursor, "anchorPrice");
            actor.callTime = DbFieldUtil.getString(cursor, "anchorCallTime");
            actor.videoUrl = DbFieldUtil.getString(cursor, "anchorVideoUrl");

            return actor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
