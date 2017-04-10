package com.audio.miliao;

import android.app.Application;
import android.content.Context;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.User;

public class theApp extends Application
{
    public static Context CONTEXT = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            CONTEXT = this;

            if (AppData.isLogin())
            {
                saveCurUser();
            }
        }
        catch (Exception e)
        {
        }
    }

    public static void saveCurUser()
    {
        try
        {
            User user = new User();
            user.id = "12345678";
            user.name = "我是大管家";
            user.avatar = "avatar1.jpg";
            AppData.saveCurUser(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
