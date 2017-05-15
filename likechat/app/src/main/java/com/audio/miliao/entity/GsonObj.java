package com.audio.miliao.entity;

import com.google.gson.Gson;

public class GsonObj<T>
{
    public static final Gson gson = new Gson();

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

    public String toJsonString()
    {
        return gson.toJson(this);
    }
}
