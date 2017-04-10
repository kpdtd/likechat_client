package com.audio.miliao.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.audio.miliao.util.DbFieldUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 聊天的消息<br/>
 * 可以根据发送者来确认消息的类型
 */

public class ChatMessage implements Serializable
{
    /** 消息id */
    public String id = "";
    /** 消息内容 */
    public String text = "";
    /** 消息发送者 */
    public User from;
    /** 消息接收者 */
    public User to;
    /** 时间 */
    public long date;

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", id);
            jsonObject.put("name", text);
            jsonObject.put("from", from.toJson());
            jsonObject.put("to", to.toJson());
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
            values.put("chatId", id);
            values.put("chatText", text);
            values.put("chatFrom", from.toJson().toString());
            values.put("chatTo", to.toJson().toString());

            return values;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static ChatMessage fromJson(JSONObject jsonObject)
    {
        try
        {
            ChatMessage chat = new ChatMessage();
            chat.id = jsonObject.optString("id");
            chat.text = jsonObject.optString("text");
            chat.from = User.fromJson(jsonObject.optJSONObject("from"));
            chat.to = User.fromJson(jsonObject.optJSONObject("to"));

            return chat;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static ChatMessage fromCursor(Cursor cursor)
    {
        try
        {
            ChatMessage chat = new ChatMessage();
            chat.id = DbFieldUtil.getString(cursor, "chatId");
            chat.text = DbFieldUtil.getString(cursor, "chatText");
            String strFrom = DbFieldUtil.getString(cursor, "chatFrom");
            JSONObject jsonFrom = new JSONObject(strFrom);
            chat.from = User.fromJson(jsonFrom);
            String strTo = DbFieldUtil.getString(cursor, "chatTo");
            JSONObject jsonTo = new JSONObject(strTo);
            chat.to = User.fromJson(jsonTo);

            return chat;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
