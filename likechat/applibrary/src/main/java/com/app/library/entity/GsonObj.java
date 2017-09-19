package com.app.library.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.Serializable;

public class GsonObj<T> implements Serializable
{
    //public static final Gson gson = new Gson();
    public static final Gson gson = new GsonBuilder()
            //.setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static <T> T parse(String strJson, Class<T> cls)
    {
        try
        {
            return gson.fromJson(strJson, cls);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static <T> T parse(JSONObject jsonObject, Class<T> cls)
    {
        try
        {
            return gson.fromJson(jsonObject.toString(), cls);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String toJsonString()
    {
        return gson.toJson(this);
    }

    public JSONObject toJson()
    {
        try
        {
            JSONObject jsonObject = new JSONObject(toJsonString());
            return jsonObject;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
