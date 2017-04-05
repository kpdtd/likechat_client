package com.likechat.likechat;

import android.app.Application;
import android.content.Context;

import com.likechat.likechat.entity.AppData;
import com.likechat.likechat.entity.User;

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

            User user = new User();
            user.id = "12345678";
            user.name = "我是大管家";
            AppData.saveCurUser(user);
        }
        catch (Exception e)
        {
        }
    }
}
